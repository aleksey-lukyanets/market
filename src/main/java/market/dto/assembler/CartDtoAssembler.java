package market.dto.assembler;

import market.domain.Cart;
import market.domain.CartItem;
import market.domain.Product;
import market.dto.CartDTO;
import market.dto.CartItemDTO;
import market.properties.MarketProperties;
import market.rest.CartRestController;
import market.rest.ContactsRestController;
import market.rest.ProductsRestController;
import market.service.ProductService;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class CartDtoAssembler extends RepresentationModelAssemblerSupport<Cart, CartDTO> {

	private final MarketProperties marketProperties;

	public CartDtoAssembler(MarketProperties marketProperties) {
		super(CartRestController.class, CartDTO.class);
		this.marketProperties = marketProperties;
	}

	@Override
	public CartDTO toModel(Cart cart) {
		CartDTO dto = toAnonymousResource(cart);
		dto.setUser(cart.getUserAccount().getEmail());
		dto.add(linkTo(ContactsRestController.class).withRel("Customer contacts"));
		dto.add(linkTo(CartRestController.class).slash(CartRestController.PAY).withRel("Proceed to payment"));
		return dto;
	}

	public CartDTO convertToModelAndUpdateAttributes(Cart cart, String attribute, Model model, HttpServletRequest request) {
		CartDTO cartDTO = toModel(cart);
		model.addAttribute(attribute, cartDTO);
		request.getSession().setAttribute(attribute, cartDTO);
		return cartDTO;
	}

	public CartDTO toAnonymousResource(Cart cart) {
		int deliveryCost = marketProperties.getDeliveryCost();

		CartDTO dto = instantiateModel(cart);
		dto.setDeliveryIncluded(cart.isDeliveryIncluded());
		dto.setProductsCost(cart.getItemsCost());
		dto.setDeliveryCost(deliveryCost);
		dto.setTotalCost(cart.getItemsCost() + deliveryCost);
		dto.setTotalItems(cart.getItemsCount());

		List<CartItemDTO> cartItemsDto = cart.getCartItems().stream()
			.map(this::toCartItemDto)
			.collect(Collectors.toList());
		dto.setCartItems(cartItemsDto);

		return dto;
	}

	public CartItemDTO toCartItemDto(CartItem cartItem) {
		Long productId = cartItem.getProduct().getId();

		CartItemDTO dto = new CartItemDTO();
		dto.setProductId(productId);
		dto.setQuantity(cartItem.getQuantity());
		dto.add(linkTo(ProductsRestController.class).slash(productId).withRel("View product"));
		return dto;
	}

	/**
	 * @return domain cart created from DTO
	 */
	public Cart toDomain(CartDTO cartDTO, ProductService productService) { // todo: avoid passing service here, pass a map
		Cart cart = new Cart();
		cart.setDeliveryIncluded(cartDTO.isDeliveryIncluded());
		for (CartItemDTO cartItemDto : cartDTO.getCartItems()) {
			Optional<Product> productOptional = productService.findById(cartItemDto.getProductId());
			if (productOptional.isPresent()) {
				Product product = productOptional.get();
				if (product.isAvailable())
					cart.update(product, cartItemDto.getQuantity());
			}
		}
		return cart;
	}
}
