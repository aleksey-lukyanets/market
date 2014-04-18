package market.rest;

import java.security.Principal;
import market.domain.Cart;
import market.domain.dto.CartDTO;
import market.domain.dto.CartItemDTO;
import market.exception.UnknownProductException;
import market.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Контроллер корзины.
 */
@Controller
@RequestMapping(value = "/rest/cart")
public class CartWS {

    @Value("${deliveryCost}")
    private int deliveryCost;
    
    @Autowired
    private CartService cartService;

    /**
     * Просмотр корзины.
     * 
     * @param principal
     * @return 
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CartDTO getCart(Principal principal) {
        Cart cart = cartService.getUserCart(principal.getName());
        return cart.createDTO(deliveryCost);
    }

    /**
     * Добавление товара.
     * 
     * @param principal
     * @param item
     * @return 
     * @throws UnknownProductException 
     */
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CartDTO addItem(Principal principal, @RequestBody CartItemDTO item) throws UnknownProductException {
        String login = principal.getName();
        Cart cart = cartService.updateUserCart(login, item);
        return cart.createDTO(deliveryCost);
    }

    /**
     * Очистка корзины.
     * 
     * @param principal
     * @return 
     */
    @RequestMapping(
            method = RequestMethod.DELETE,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CartDTO clearCart(Principal principal) {
        Cart cart = cartService.clearUserCart(principal.getName());
        return cart.createDTO(deliveryCost);
    }
    
    /**
     * Установка способа доставки.
     * 
     * @param principal
     * @param delivery
     * @return 
     */
    @RequestMapping(value = "/delivery/{delivery}",
            method = RequestMethod.POST,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CartDTO setDelivery(Principal principal, @PathVariable String delivery) {
        String login = principal.getName();
        Boolean included = Boolean.valueOf(delivery);
        Cart cart = cartService.setUserCartDelivery(login, included);
        return cart.createDTO(deliveryCost);
    }
}
