package market.rest;

import market.domain.Cart;
import market.domain.Order;
import market.dto.CartDTO;
import market.dto.CartItemDTO;
import market.dto.CreditCardDTO;
import market.dto.OrderDTO;
import market.dto.assembler.CartDtoAssembler;
import market.dto.assembler.OrderDtoAssembler;
import market.exception.EmptyCartException;
import market.exception.UnknownEntityException;
import market.properties.MarketProperties;
import market.service.CartService;
import market.service.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@Controller
@RequestMapping(value = "/rest/cart")
@Secured({"ROLE_USER"})
public class CartRestController {
	private final CartService cartService;
	private final OrderService orderService;
	private final CartDtoAssembler cartDtoAssembler = new CartDtoAssembler();
	private final OrderDtoAssembler orderDtoAssembler = new OrderDtoAssembler();
	private final MarketProperties marketProperties;

	public CartRestController(CartService cartService, OrderService orderService, MarketProperties marketProperties) {
		this.cartService = cartService;
		this.orderService = orderService;
		this.marketProperties = marketProperties;
	}

	/**
	 * Viewing the cart.
	 */
	@RequestMapping(
		method = RequestMethod.GET,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public CartDTO getCart(Principal principal) {
		Cart cart = cartService.getCartOrCreate(principal.getName());
		return cartDtoAssembler.toAnonymousResource(cart);
	}

	/**
	 * Adding a product.
	 *
	 * @return updated cart
	 * @throws UnknownEntityException if the specified product does not exist
	 */
	@RequestMapping(
		method = RequestMethod.PUT,
		consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public CartDTO addItem(Principal principal, @RequestBody CartItemDTO item) throws UnknownEntityException {
		String login = principal.getName();
		Cart cart = cartService.addToCart(login, item.getProductId(), item.getQuantity());
		return cartDtoAssembler.toAnonymousResource(cart);
	}

	/**
	 * Clearing the cart.
	 *
	 * @return cleared cart
	 */
	@RequestMapping(
		method = RequestMethod.DELETE,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public CartDTO clearCart(Principal principal) {
		Cart cart = cartService.clearCart(principal.getName());
		return cartDtoAssembler.toAnonymousResource(cart);
	}

	/**
	 * Setting delivery option.
	 *
	 * @return updated cart
	 */
	@RequestMapping(value = "/delivery/{delivery}",
		method = RequestMethod.PUT,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public CartDTO setDelivery(Principal principal, @PathVariable String delivery) {
		String login = principal.getName();
		boolean included = Boolean.parseBoolean(delivery);
		Cart cart = cartService.setDelivery(login, included);
		return cartDtoAssembler.toAnonymousResource(cart);
	}

	/**
	 * Order registration.
	 *
	 * @return created order
	 * @throws EmptyCartException if the cart is empty
	 */
	@RequestMapping(value = "/payment",
		method = RequestMethod.POST,
		consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<OrderDTO> payByCard(Principal principal, @Valid @RequestBody CreditCardDTO card) throws EmptyCartException {
		String login = principal.getName();
		Order order = orderService.createUserOrder(login, marketProperties.getDeliveryCost(), card.getNumber());
		OrderDTO dto = orderDtoAssembler.toModel(order);

		HttpHeaders headers = new HttpHeaders();
		dto.getLink("self").ifPresent(link -> headers.setLocation(URI.create(link.getHref())));
		return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
	}
}
