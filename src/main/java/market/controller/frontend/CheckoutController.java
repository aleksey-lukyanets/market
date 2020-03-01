package market.controller.frontend;

import market.domain.*;
import market.dto.*;
import market.dto.assembler.*;
import market.exception.*;
import market.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.security.*;
import java.util.*;
import java.util.function.*;

import static java.util.stream.Collectors.*;

/**
 * Checkout steps controller.
 */
@Controller
@RequestMapping("/checkout")
@Secured({"ROLE_USER"})
@SessionAttributes({"createdOrder"})
public class CheckoutController {
	private static final String CHECKOUT_BASE = "checkout";
	private static final String CHECKOUT_DETAILS = CHECKOUT_BASE + "/details";
	private static final String CHECKOUT_PAYMENT = CHECKOUT_BASE + "/payment";
	private static final String CHECKOUT_CONFIRMATION = CHECKOUT_BASE + "/confirmation";

	private final UserAccountService userAccountService;
	private final ContactsService contactsService;
	private final OrderService orderService;
	private final CartService cartService;
	private final OrderDtoAssembler orderDtoAssembler;
	private final ContactsDtoAssembler contactsDtoAssembler;
	private final UserAccountDtoAssembler userDtoAssembler;
	private final ProductDtoAssembler productDtoAssembler;

	@Value("${deliveryCost}")
	private int deliveryCost;

	public CheckoutController(UserAccountService userAccountService, ContactsService contactsService,
		OrderService orderService, CartService cartService, OrderDtoAssembler orderDtoAssembler,
		ContactsDtoAssembler contactsDtoAssembler, UserAccountDtoAssembler userDtoAssembler,
		ProductDtoAssembler productDtoAssembler)
	{
		this.userAccountService = userAccountService;
		this.contactsService = contactsService;
		this.orderService = orderService;
		this.cartService = cartService;
		this.orderDtoAssembler = orderDtoAssembler;
		this.contactsDtoAssembler = contactsDtoAssembler;
		this.userDtoAssembler = userDtoAssembler;
		this.productDtoAssembler = productDtoAssembler;
	}

	//--------------------------------------------- Changing contacts

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String details(Principal principal, Model model) {
		String login = principal.getName();
		Cart cart = cartService.getCartOrCreate(login);
		if (!cart.isDeliveryIncluded())
			return "redirect:/" + CHECKOUT_PAYMENT;

		model.addAttribute("userContacts", contactsDtoAssembler.toModel(contactsService.getContacts(login)));
		return CHECKOUT_DETAILS;
	}

	@RequestMapping(value = "/details", method = RequestMethod.POST)
	public String putDetails(
		Model model,
		Principal principal,
		@Valid ContactsDTO contactsDto,
		BindingResult bindingResult,
		@RequestParam(value = "infoOption") String infoOption
	) {
		if (bindingResult.hasErrors())
			return CHECKOUT_DETAILS;

		if ("useNew".equals(infoOption)) {
			String login = principal.getName();
			Contacts changedContacts = contactsDtoAssembler.toDomain(contactsDto);
			contactsService.updateUserContacts(changedContacts, login);
			model.addAttribute("userContacts", contactsDtoAssembler.toModel(changedContacts));
		}
		return "redirect:/" + CHECKOUT_PAYMENT;
	}

	//------------------------------------------------------- Payment

	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String payment(Principal principal, Model model) {
		String login = principal.getName();
		UserAccount account = userAccountService.findByEmail(login);
		if (account == null)
			return CHECKOUT_DETAILS;

		Cart cart = cartService.getCartOrCreate(login);
		Map<Long, ProductDTO> productsById = cart.getCartItems().stream()
			.map(CartItem::getProduct)
			.map(productDtoAssembler::toModel)
			.collect(toMap(ProductDTO::getProductId, Function.identity()));
		model.addAttribute("productsById", productsById);
		model.addAttribute("userName", account.getName());
		model.addAttribute("contacts", contactsDtoAssembler.toModel(contactsService.getContacts(login)));
		model.addAttribute("deliveryCost", deliveryCost);
		model.addAttribute("creditCard", new CreditCardDTO());
		return CHECKOUT_PAYMENT;
	}

	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String paymentPost(
		Principal principal,
		@Valid CreditCardDTO creditCard,
		BindingResult bindingResult,
		HttpServletRequest request
	) {
		if (bindingResult.hasErrors())
			return CHECKOUT_PAYMENT;

		String login = principal.getName();
		try {
			Order order = orderService.createUserOrder(login, deliveryCost, creditCard.getNumber());
			request.getSession().setAttribute("createdOrder", orderDtoAssembler.toModel(order));
			return "redirect:/" + CHECKOUT_CONFIRMATION;
		} catch (EmptyCartException ex) {
			bindingResult.addError(ex.getFieldError());
			return CHECKOUT_PAYMENT;
		}
	}

	//---------------------------------- Confirmation and gratitude

	@RequestMapping(value = "/confirmation", method = RequestMethod.GET)
	public String purchase(Principal principal, Model model ) {
		UserAccount account = userAccountService.findByEmail(principal.getName());
		if (account == null)
			return CHECKOUT_DETAILS;

		model.addAttribute("userAccount", userDtoAssembler.toModel(account));
		model.addAttribute("userDetails", contactsDtoAssembler.toModel(account.getContacts()));
		return CHECKOUT_CONFIRMATION;
	}
}
