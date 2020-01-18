package market.controller.frontend;

import market.domain.Cart;
import market.domain.Order;
import market.domain.OrderedProduct;
import market.domain.UserAccount;
import market.dto.UserDTO;
import market.dto.assembler.UserAccountDtoAssembler;
import market.exception.EmailExistsException;
import market.security.AuthenticationService;
import market.service.CartService;
import market.service.OrderService;
import market.service.UserAccountService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер аккаунта покупателя.
 */
@Controller
@RequestMapping("/customer")
@SessionAttributes({"cart"})
public class CustomerController {
	private final UserAccountService userAccountService;
	private final CartService cartService;
	private final OrderService orderService;
	private final AuthenticationService authenticationService;
	private final UserAccountDtoAssembler userAccountDtoAssembler;

	public CustomerController(UserAccountService userAccountService, CartService cartService, OrderService orderService,
		AuthenticationService authenticationService, UserAccountDtoAssembler userAccountDtoAssembler) {
		this.userAccountService = userAccountService;
		this.cartService = cartService;
		this.orderService = orderService;
		this.authenticationService = authenticationService;
		this.userAccountDtoAssembler = userAccountDtoAssembler;
	}

	/**
	 * Страница истории заказов.
	 */
	@Secured({"ROLE_USER"})
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String orders(Model model) {
		String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
		UserAccount account = userAccountService.findByEmail(userLogin);
		List<Order> userOrders = orderService.findByUserAccount(account);

		Map<Long, List<OrderedProduct>> orderedProductsMap = new HashMap<>();
		for (Order order : userOrders) {
			orderedProductsMap.put(order.getId(), new ArrayList<>(order.getOrderedProducts()));
		}
		model.addAttribute("userOrders", userOrders);
		model.addAttribute("orderedProductsMap", orderedProductsMap);
		return "customer/orders";
	}

	//----------------------------------------- Регистрация нового пользователя

	/**
	 * Страница регистрации.
	 */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String getSignup(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return "customer/new";
	}

	/**
	 * Обработка формы регистрации.
	 *
	 * @param user          данные нового пользователя
	 * @param bindingResult ошибки валидации данных пользователя
	 * @param sessionCart   сеансовая корзина
	 * @return
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String postSignup(
		Model model,
		@Valid UserDTO user,
		BindingResult bindingResult,
		@ModelAttribute(value = "cart") Cart sessionCart
	) {
		String view = "customer/new";
		if (bindingResult.hasErrors())
			return view;

		try {
			UserAccount userData = userAccountDtoAssembler.toDomain(user);
			UserAccount newAccount = userAccountService.createUser(userData);
			authenticationService.authenticate(newAccount);
			model.addAttribute("userDTO", userAccountDtoAssembler.toResource(newAccount));
		} catch (EmailExistsException ex) {
			bindingResult.addError(ex.getFieldError());
			return view;
		}
		if (!sessionCart.isEmpty())
			cartService.fillUserCart(user.getEmail(), sessionCart.getCartItems());

		return "redirect:/";
	}
}
