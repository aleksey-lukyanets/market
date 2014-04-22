package market.controller.frontend;

import market.dto.UserDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import market.domain.Cart;
import market.domain.Order;
import market.domain.OrderedProduct;
import market.domain.UserAccount;
import market.exception.EmailExistsException;
import market.service.CartService;
import market.service.OrderService;
import market.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Контроллер аккаунта покупателя.
 */
@Controller
@RequestMapping("/customer")
@SessionAttributes({"cart"})
public class CustomerController {

    @Autowired
    private UserAccountService userAccountService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderService orderService;

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
     * @param user данные нового пользователя
     * @param bindingResult ошибки валидации данных пользователя
     * @param sessionCart сеансовая корзина
     * @return
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String postSignup(
            @Valid UserDTO user,
            BindingResult bindingResult,
            @ModelAttribute(value = "cart") Cart sessionCart
    ) {
        String view = "customer/new";
        if (bindingResult.hasErrors()) {
            return view;
        }
        try {
            userAccountService.createUserThenAuthenticate(user);
        } catch (EmailExistsException ex) {
            bindingResult.addError(ex.getFieldError());
            return view;
        }
        if (!sessionCart.isEmpty()) {
            cartService.fillUserCart(user.getEmail(), sessionCart.getCartItems());
        }
        return "redirect:/";
    }
}
