package market.controller.frontend;

import market.controller.CartModelHelper;
import market.domain.Cart;
import market.domain.CartItem;
import market.domain.Contacts;
import market.domain.Order;
import market.domain.UserAccount;
import market.dto.ContactsDTO;
import market.dto.CreditCardDTO;
import market.dto.ProductDTO;
import market.dto.assembler.CartDtoAssembler;
import market.dto.assembler.ContactsDtoAssembler;
import market.dto.assembler.OrderDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.dto.assembler.UserAccountDtoAssembler;
import market.exception.EmptyCartException;
import market.properties.MarketProperties;
import market.service.CartService;
import market.service.ContactsService;
import market.service.OrderService;
import market.service.UserAccountService;
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
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Checkout steps controller.
 */
@Controller
@RequestMapping("/checkout")
@Secured({"ROLE_USER"})
@SessionAttributes({"createdOrder"})
public class CheckoutController {
	public static final String CHECKOUT_BASE = "checkout";
	public static final String CHECKOUT_DETAILS = CHECKOUT_BASE + "/details";
	public static final String CHECKOUT_PAYMENT = CHECKOUT_BASE + "/payment";
	public static final String CHECKOUT_CONFIRMATION = CHECKOUT_BASE + "/confirmation";

	private final UserAccountService userAccountService;
	private final ContactsService contactsService;
	private final OrderService orderService;
	private final CartService cartService;
	private final OrderDtoAssembler orderDtoAssembler = new OrderDtoAssembler();
	private final ContactsDtoAssembler contactsDtoAssembler = new ContactsDtoAssembler();
	private final UserAccountDtoAssembler accountDtoAssembler = new UserAccountDtoAssembler();
	private final ProductDtoAssembler productDtoAssembler = new ProductDtoAssembler();
	private final MarketProperties marketProperties;
	private final CartModelHelper cartModelHelper;

	public CheckoutController(UserAccountService userAccountService, ContactsService contactsService,
		OrderService orderService, CartService cartService, MarketProperties marketProperties)
	{
		this.userAccountService = userAccountService;
		this.contactsService = contactsService;
		this.orderService = orderService;
		this.cartService = cartService;
		this.marketProperties = marketProperties;
		cartModelHelper = new CartModelHelper(new CartDtoAssembler(marketProperties));
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
	public String changeContacts(
		Model model, Principal principal,
		@RequestParam(value = "changeContacts") String changeContacts,
		@Valid ContactsDTO contactsDto, BindingResult bindingResult
	) {
		String login = principal.getName();
		if (!"changeRequested".equals(changeContacts)) {
			model.addAttribute("userContacts", contactsDtoAssembler.toModel(contactsService.getContacts(login)));
			return "redirect:/" + CHECKOUT_PAYMENT;
		}

		if (bindingResult.hasErrors())
			return CHECKOUT_DETAILS;

		Contacts changedContacts = contactsDtoAssembler.toDomain(contactsDto);
		contactsService.updateUserContacts(changedContacts, login);
		model.addAttribute("userContacts", contactsDtoAssembler.toModel(changedContacts));
		return "redirect:/" + CHECKOUT_PAYMENT;
	}

	//------------------------------------------------------- Payment

	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String getPayment(Principal principal, Model model) {
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
		model.addAttribute("userContacts", contactsDtoAssembler.toModel(account.getContacts()));
		model.addAttribute("deliveryCost", marketProperties.getDeliveryCost());
		model.addAttribute("creditCard", new CreditCardDTO());
		return CHECKOUT_PAYMENT;
	}

	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String postPayment(
		Principal principal, Model model, HttpServletRequest request,
		@Valid CreditCardDTO creditCard, BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return CHECKOUT_PAYMENT;

		String login = principal.getName();
		try {
			Order order = orderService.createUserOrder(login, marketProperties.getDeliveryCost(), creditCard.getCcNumber());
			model.addAttribute("createdOrder", orderDtoAssembler.toModel(order));

			Cart cart = cartService.getCartOrCreate(login);
			cartModelHelper.convertAndUpdateAttributes(cart, model, request);

			return "redirect:/" + CHECKOUT_CONFIRMATION;
		} catch (EmptyCartException ex) {
			bindingResult.addError(ex.getFieldError());
			return CHECKOUT_PAYMENT;
		}
	}

	//---------------------------------- Gratitude

	@RequestMapping(value = "/confirmation", method = RequestMethod.GET)
	public String getGratitude(Principal principal, Model model, HttpServletRequest request) {
		UserAccount account = userAccountService.findByEmail(principal.getName());
		if (account == null)
			return CHECKOUT_DETAILS;

		model.addAttribute("userAccount", accountDtoAssembler.toModel(account));
		model.addAttribute("userContacts", contactsDtoAssembler.toModel(account.getContacts()));
		return CHECKOUT_CONFIRMATION;
	}
}
