package market.controller.frontend;

import market.domain.Cart;
import market.domain.CartItem;
import market.domain.Product;
import market.dto.CartDTO;
import market.dto.CartItemDTO;
import market.dto.ProductDTO;
import market.dto.assembler.CartDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.exception.UnknownEntityException;
import market.properties.MarketProperties;
import market.service.CartService;
import market.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Controller
@RequestMapping("/cart")
@SessionAttributes({"cart"})
public class CartController {
	private static final Logger log = LoggerFactory.getLogger(CartController.class);

	private static final String CART_BASE = "cart";

	private final CartService cartService;
	private final ProductService productService;
	private final CartDtoAssembler cartDtoAssembler = new CartDtoAssembler();
	private final ProductDtoAssembler productDtoAssembler = new ProductDtoAssembler();
	private final MarketProperties marketProperties;

	public CartController(CartService cartService, ProductService productService, MarketProperties marketProperties) {
		this.cartService = cartService;
		this.productService = productService;
		this.marketProperties = marketProperties;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getCart(
		Principal principal, HttpServletRequest request, Model model,
		@ModelAttribute(value = "cart") CartDTO cartDto)
	{
		if (isAuthorized(principal)) {
			Cart cart = cartService.getCartOrCreate(principal.getName());
			request.getSession().setAttribute("cart", cartDtoAssembler.toModel(cart));
			model.addAttribute("productsById", collectProductsMap(cart));
		} else {
			Map<Long, ProductDTO> productsById = cartDto.getCartItems().stream()
				.map(CartItemDTO::getProductId)
				.map(productService::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(productDtoAssembler::toModel)
				.collect(toMap(ProductDTO::getProductId, p -> p));
			model.addAttribute("productsById", productsById);
		}
		model.addAttribute("deliveryCost", marketProperties.getDeliveryCost());
		return CART_BASE;
	}

	private Map<Long, ProductDTO> collectProductsMap(Cart cart) {
		return cart.getCartItems().stream()
			.map(CartItem::getProduct)
			.map(productDtoAssembler::toModel)
			.collect(toMap(ProductDTO::getProductId, p -> p));
	}

	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public String clearCart(
		Principal principal, Model model,
		@ModelAttribute(value = "cart") CartDTO cartDto)
	{
		if (isAuthorized(principal)) {
			Cart clearedCart = cartService.clearCart(principal.getName());
			model.addAttribute("cart", cartDtoAssembler.toModel(clearedCart));
		} else {
			Cart cart = cartDtoAssembler.toDomain(cartDto, productService);
			cart.clear();
			model.addAttribute("cart", cartDtoAssembler.toAnonymousResource(cart));
		}
		return "redirect:/" + CART_BASE;
	}

	//--------------------------------------------- Adding item to cart

	@RequestMapping(method = RequestMethod.POST)
	public String updateCartByForm(
		Principal principal,
		@Valid @ModelAttribute("cartItem") CartItemDTO cartItemDto,
		BindingResult bindingResult, Model model,
		@ModelAttribute(value = "cart") CartDTO cartDto
	) {
		if (bindingResult.hasErrors())
			return CART_BASE;

		if (!isAuthorized(principal)) {
			CartDTO handledCartDto = updateGuestCart(cartDto, cartItemDto);
			model.addAttribute("cart", handledCartDto);
			model.addAttribute("deliveryCost", marketProperties.getDeliveryCost());
			return CART_BASE;
		}

		try {
			Cart updatedCart = updateCart(principal, cartItemDto);
			model.addAttribute("cart", cartDtoAssembler.toModel(updatedCart));
		} catch (UnknownEntityException ex) {
			bindingResult.addError(ex.getFieldError());
			return CART_BASE;
		}
		return "redirect:/" + CART_BASE;
	}

	private CartDTO updateGuestCart(CartDTO cartDto, CartItemDTO newCartItem) {
		Optional<Product> productOptional = productService.findById(newCartItem.getProductId());
		if (productOptional.isPresent()) {
			Product product = productOptional.get();
			if (product.isAvailable()) {
				Cart cart = cartDtoAssembler.toDomain(cartDto, productService);
				cart.update(product, newCartItem.getQuantity());
				return cartDtoAssembler.toModel(cart);
			}
		}
		return cartDto;
	}

	/**
	 * Adding via AJAX
	 * @return updated cart
	 */
	@RequestMapping(method = RequestMethod.PUT,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public CartDTO updateCartByAjax(
		Principal principal,
		@Valid @RequestBody CartItemDTO cartItemDto,
		BindingResult bindingResult, Model model,
		@ModelAttribute(value = "cart") CartDTO cartDto
	) {
		if (bindingResult.hasErrors())
			return cartDto;

		if (!isAuthorized(principal)) {
			CartDTO handledCartDto = updateGuestCart(cartDto, cartItemDto);
			model.addAttribute("cart", handledCartDto);
			return handledCartDto;
		}

		try {
			Cart updatedCart = updateCart(principal, cartItemDto);
			return cartDtoAssembler.toModel(updatedCart);
		} catch (UnknownEntityException e) {
			log.error("Can not add item to cart", e);
			return cartDto;
		}
	}

	private Cart updateCart(Principal principal, CartItemDTO cartItem) throws UnknownEntityException {
		String login = principal.getName();
		return cartService.addToCart(login, cartItem.getProductId(), cartItem.getQuantity());
	}

	//---------------------------------------------- Setting delivery option

	/**
	 * Setting delivery option via AJAX.
	 * @return updated cart
	 */
	@RequestMapping(value = "/delivery/{delivery}",
		method = RequestMethod.PUT,
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public CartDTO setDelivery(
		Principal principal,
		@PathVariable String delivery,
		@ModelAttribute(value = "cart") CartDTO cartDto)
	{
		boolean included = Boolean.parseBoolean(delivery);
		if (isAuthorized(principal)) {
			String login = principal.getName();
			Cart updatedCart = cartService.setDelivery(login, included);
			return cartDtoAssembler.toModel(updatedCart);
		} else {
			cartDto.setDeliveryIncluded(included);
			return cartDto;
		}
	}

	private boolean isAuthorized(Principal principal) {
		return principal != null;
	}
}
