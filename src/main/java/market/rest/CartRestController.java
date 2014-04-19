package market.rest;

import java.net.URI;
import java.security.Principal;
import javax.validation.Valid;
import market.domain.Cart;
import market.domain.Order;
import market.domain.dto.CartDTO;
import market.domain.dto.CartItemDTO;
import market.domain.dto.CreditCardDTO;
import market.domain.dto.OrderDTO;
import market.exception.EmptyCartException;
import market.exception.UnknownProductException;
import market.service.CartService;
import market.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST-контроллер корзины.
 */
@Controller
@RequestMapping(value = "/rest/cart")
public class CartRestController {

    @Value("${deliveryCost}")
    private int deliveryCost;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    EntityLinks entityLinks;

    /**
     * Просмотр корзины.
     * 
     * @return корзина
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
     * @param item добавляемый элемент корзины
     * @param principal
     * @return обновлённая корзина
     * @throws UnknownProductException при добавлении неизвестного товара
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
     * @return пустая корзина
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
     * @param delivery значение опции доставки
     * @param principal
     * @return изменённая корзина
     */
    @RequestMapping(value = "/delivery/{delivery}",
            method = RequestMethod.PUT,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CartDTO setDelivery(Principal principal, @PathVariable String delivery) {
        String login = principal.getName();
        Boolean included = Boolean.valueOf(delivery);
        Cart cart = cartService.setUserCartDelivery(login, included);
        return cart.createDTO(deliveryCost);
    }

    /**
     * Оформление заказа.
     *
     * @param card данные банковской карты
     * @param principal
     * @return созданный заказ
     * @throws EmptyCartException при оплате пустой корзины
     */
    @RequestMapping(value = "/payment",
            method = RequestMethod.POST,
            consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<OrderDTO> payByCard(Principal principal, @Valid @RequestBody CreditCardDTO card) throws EmptyCartException {
        String login = principal.getName();
        Order order = orderService.createUserOrder(card, login, deliveryCost);
        OrderDTO dto = order.createDTO();
        
        HttpHeaders headers = new HttpHeaders();
        Link link = entityLinks.linkToSingleResource(OrderDTO.class, dto.getId());
        headers.setLocation(URI.create(link.getHref()));
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }
}
