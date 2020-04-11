package market.controller.frontend;

import market.domain.Cart;
import market.domain.Order;
import market.domain.OrderedProduct;
import market.domain.UserAccount;
import market.dto.*;
import market.dto.assembler.*;
import market.exception.EmailExistsException;
import market.security.AuthenticationService;
import market.service.CartService;
import market.service.OrderService;
import market.service.ProductService;
import market.service.UserAccountService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/customer")
@SessionAttributes({"cart"})
public class CustomerController {
	private static final String CUSTOMER_ORDERS = "customer/orders";
	private static final String CUSTOMER_NEW = "customer/new";
	private static final String ROOT = "/";

	private final UserAccountService userAccountService;
	private final CartService cartService;
	private final OrderService orderService;
	private final ProductService productService;
	private final AuthenticationService authenticationService;

	private final UserAccountDtoAssembler userAccountDtoAssembler = new UserAccountDtoAssembler();
	private final OrderDtoAssembler orderDtoAssembler = new OrderDtoAssembler();
	private final OrderedProductDtoAssembler orderedProductDtoAssembler = new OrderedProductDtoAssembler();
	private final ProductDtoAssembler productDtoAssembler = new ProductDtoAssembler();
	private final CartDtoAssembler cartDtoAssembler = new CartDtoAssembler();

	public CustomerController(UserAccountService userAccountService, OrderService orderService,
		AuthenticationService authenticationService, CartService cartService, ProductService productService)
	{
		this.userAccountService = userAccountService;
		this.orderService = orderService;
		this.authenticationService = authenticationService;
		this.cartService = cartService;
		this.productService = productService;
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String orders(Principal principal, Model model) {
		if (!isAuthorized(principal))
			return "redirect:" + ROOT;

		List<Order> orders = orderService.getUserOrders(principal.getName());
		List<OrderDTO> ordersDto = orders.stream()
			.map(orderDtoAssembler::toModel)
			.collect(toList());
		model.addAttribute("userOrders", ordersDto);

		Map<Long, List<OrderedProductDTO>> orderedProductsByOrderId = new HashMap<>();
		for (Order order : orders) {
			List<OrderedProductDTO> productsDto = order.getOrderedProducts().stream()
				.map(orderedProductDtoAssembler::toModel)
				.collect(toList());
			orderedProductsByOrderId.put(order.getId(), productsDto);
		}
		model.addAttribute("orderedProductsByOrderId", orderedProductsByOrderId);

		Map<Long, List<ProductDTO>> productsByOrderId = new HashMap<>();
		for (Order order : orders) {
			List<ProductDTO> productsDto = order.getOrderedProducts().stream()
				.map(OrderedProduct::getProduct)
				.distinct()
				.map(productDtoAssembler::toModel)
				.collect(toList());
			productsByOrderId.put(order.getId(), productsDto);
		}
		model.addAttribute("productsByOrderId", productsByOrderId);

		return CUSTOMER_ORDERS;
	}

	private boolean isAuthorized(Principal principal) {
		return principal != null;
	}

	//----------------------------------------- Registering new account

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String getRegistrationPage(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return CUSTOMER_NEW;
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String postRegistrationForm(
		Model model,
		@Valid UserDTO user,
		@ModelAttribute(value = "cart") CartDTO cartDto,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return CUSTOMER_NEW;

		UserAccount account = userAccountDtoAssembler.toDomain(user);
		UserAccount newAccount;
		try {
			newAccount = userAccountService.create(account);
		} catch (EmailExistsException e) {
			bindingResult.addError(e.getFieldError());
			return CUSTOMER_NEW;
		}
		boolean authenticated = authenticationService.authenticate(newAccount);
		if (!authenticated)
			return CUSTOMER_NEW;

		model.addAttribute("userAccount", userAccountDtoAssembler.toModel(newAccount));

		Cart unauthorisedCart = cartDtoAssembler.toDomain(cartDto, productService);
		Cart updatedCart = cartService.addAllToCart(newAccount.getEmail(), unauthorisedCart.getCartItems());
		model.addAttribute("cart", cartDtoAssembler.toModel(updatedCart));

		return "redirect:" + ROOT;
	}
}
