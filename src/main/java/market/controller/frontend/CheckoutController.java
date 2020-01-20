package market.controller.frontend;

import market.domain.*;
import market.dto.ContactsDTO;
import market.dto.CreditCardDTO;
import market.dto.assembler.ContactsDtoAssembler;
import market.dto.assembler.OrderDtoAssembler;
import market.exception.EmptyCartException;
import market.service.CartService;
import market.service.ContactsService;
import market.service.OrderService;
import market.service.UserAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер оформления заказа.
 */
@Controller
@RequestMapping("/checkout")
@Secured({"ROLE_USER"})
@SessionAttributes({"createdOrder"})
public class CheckoutController {
	private final UserAccountService userAccountService;
	private final ContactsService contactsService;
	private final OrderService orderService;
	private final CartService cartService;
	private final OrderDtoAssembler orderDtoAssembler;
	private final ContactsDtoAssembler contactsDtoAssembler;

	@Value("${deliveryCost}")
	private int deliveryCost;

	public CheckoutController(UserAccountService userAccountService, ContactsService contactsService,
		OrderService orderService, CartService cartService, OrderDtoAssembler orderDtoAssembler,
		ContactsDtoAssembler contactsDtoAssembler) {
		this.userAccountService = userAccountService;
		this.contactsService = contactsService;
		this.orderService = orderService;
		this.cartService = cartService;
		this.orderDtoAssembler = orderDtoAssembler;
		this.contactsDtoAssembler = contactsDtoAssembler;
	}

	//--------------------------------------------- Изменение контактных данных

	/**
	 * Страница изменения контактных данных.
	 */
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String details(Principal principal, Model model) {
		String login = principal.getName();
		Cart cart = cartService.getUserCart(login);
		if (cart.isDeliveryIncluded() == false) {
			return "redirect:/checkout/payment";
		}
		model.addAttribute("userContacts", contactsService.getUserContacts(login));
		return "checkout/details";
	}

	/**
	 * Внесение изменений в контактные данные.
	 *
	 * @param contactsDto   новые контакты
	 * @param bindingResult ошибки валидации контактов
	 * @param infoOption    подтверждение изменения
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/details", method = RequestMethod.PUT)
	public String putDetails(
		Model model,
		Principal principal,
		@Valid ContactsDTO contactsDto,
		BindingResult bindingResult,
		@RequestParam(value = "infoOption") String infoOption
	) {
		if (infoOption.equals("useNew")) {
			if (bindingResult.hasErrors())
				return "checkout/details";

			String login = principal.getName();
			String newPhone = contactsDto.getPhone();
			String newAddress = contactsDto.getAddress();
			Contacts updatedContacts = contactsService.updateUserContacts(login, newPhone, newAddress);
			model.addAttribute("userDetails", contactsDtoAssembler.toModel(updatedContacts));
		}
		return "redirect:/checkout/payment";
	}

	//------------------------------------------------------- Проверка и оплата

	/**
	 * Страница оплаты.
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String payment(Principal principal, Model model) {
		String login = principal.getName();
		Cart cart = cartService.getUserCart(login);
		List<Product> products = new ArrayList<>();
		for (CartItem item : cart.getCartItems()) {
			products.add(item.getProduct());
		}
		model.addAttribute("products", products);
		model.addAttribute("userAccount", userAccountService.getUserAccount(login));
		model.addAttribute("contacts", contactsService.getUserContacts(login));
		model.addAttribute("deliveryCost", deliveryCost);
		model.addAttribute("creditCard", new CreditCardDTO());
		return "checkout/payment";
	}

	/**
	 * Обработка формы оплаты.
	 *
	 * @param creditCard    данные карты
	 * @param bindingResult ошибки валидации данных карты
	 * @param request
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String paymentPost(
		Principal principal,
		@Valid CreditCardDTO creditCard,
		BindingResult bindingResult,
		HttpServletRequest request
	) {
		String view = "checkout/payment";
		if (bindingResult.hasErrors()) {
			return view;
		}
		String login = principal.getName();
		try {
			Order order = orderService.createUserOrder(login, deliveryCost, creditCard.getNumber());
			request.getSession().setAttribute("createdOrder", orderDtoAssembler.toModel(order));
			return "redirect:/checkout/confirmation";
		} catch (EmptyCartException ex) {
			bindingResult.addError(ex.getFieldError());
			return view;
		}
	}

	//---------------------------------- Страница подтверждения и благодарности

	/**
	 * Подтверждение и благодарность.
	 *
	 * @param createdOrder созданный заказ
	 * @param principal
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/confirmation", method = RequestMethod.GET)
	public String purchase(
		Principal principal,
		Model model
	) {
		String login = principal.getName();
		Contacts contacts = contactsService.getUserContacts(login);
		model.addAttribute("userAccount", userAccountService.getUserAccount(login));
		model.addAttribute("userDetails", contactsDtoAssembler.toModel(contacts));
		return "checkout/confirmation";
	}
}
