package market.controller.frontend;

import market.controller.CartModelHelper;
import market.domain.Cart;
import market.domain.Order;
import market.domain.OrderedProduct;
import market.domain.UserAccount;
import market.dto.CartDTO;
import market.dto.OrderDTO;
import market.dto.OrderedProductDTO;
import market.dto.ProductDTO;
import market.dto.UserDTO;
import market.dto.assembler.CartDtoAssembler;
import market.dto.assembler.OrderDtoAssembler;
import market.dto.assembler.OrderedProductDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.dto.assembler.UserAccountDtoAssembler;
import market.exception.EmailExistsException;
import market.properties.MarketProperties;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
	private final CartDtoAssembler cartDtoAssembler;
	private final CartModelHelper cartModelHelper;

	public CustomerController(UserAccountService userAccountService, OrderService orderService,
	    AuthenticationService authenticationService, CartService cartService, ProductService productService,
	    MarketProperties marketProperties)
	{
		this.userAccountService = userAccountService;
		this.orderService = orderService;
		this.authenticationService = authenticationService;
		this.cartService = cartService;
		this.productService = productService;
		cartDtoAssembler = new CartDtoAssembler(marketProperties);
		cartModelHelper = new CartModelHelper(cartDtoAssembler);
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

		Map<Long, ProductDTO> productsById = orders.stream()
			.map(Order::getOrderedProducts)
			.flatMap(Collection::stream)
			.map(OrderedProduct::getProduct)
			.distinct()
			.map(productDtoAssembler::toModel)
			.collect(toMap(ProductDTO::getProductId, p -> p));
		model.addAttribute("productsById", productsById);

		return CUSTOMER_ORDERS;
	}

	private boolean isAuthorized(Principal principal) {
		return principal != null;
	}

	//----------------------------------------- Registering new account

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String getRegistrationPage(Model model) {
		model.addAttribute("userAccount", new UserDTO());
		return CUSTOMER_NEW;
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String postRegistrationForm(
		Model model, HttpServletRequest request,
		@Valid UserDTO user, BindingResult bindingResult,
		@ModelAttribute(value = "cart") CartDTO cartDto
	) {
		model.addAttribute("userAccount", user); // place user data back to redirect him back to pre-filled registration form
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
		boolean authenticated = authenticationService.authenticate(account.getEmail(), user.getPassword());
		if (!authenticated)
			return CUSTOMER_NEW;

		model.addAttribute("userAccount", userAccountDtoAssembler.toModel(newAccount)); // now add the authorized data

		Cart unauthorisedCart = cartDtoAssembler.toDomain(cartDto, productService);
		Cart updatedCart = cartService.addAllToCart(newAccount.getEmail(), unauthorisedCart.getCartItems());
		cartModelHelper.convertAndUpdateAttributes(updatedCart, model, request);

		return "redirect:" + ROOT;
	}
}
