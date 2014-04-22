package market.rest;

import java.security.Principal;
import java.util.List;
import market.dto.OrderDTO;
import market.exception.OrderNotFoundException;
import market.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST-контроллер истории заказов покупателя.
 */
@Controller
@RequestMapping(value = "/rest/customer/orders")
@ExposesResourceFor(OrderDTO.class)
public class OrdersRestController {
    
    @Autowired
    private OrderService orderService;

    /**
     * Просмотр всех заказов покупателя.
     * 
     * @return список заказов
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<OrderDTO> getOrders(Principal principal) {
        String login = principal.getName();
        return orderService.getUserOrders(login);
    }

    /**
     * Просмотр одного заказа покупателя.
     * 
     * @param id идентификатор заказа
     * @param principal
     * @return запрошенный заказ покупателя
     * @throws OrderNotFoundException если заказ не существует у текущего покупателя
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public OrderDTO getOrder(Principal principal, @PathVariable long id) throws OrderNotFoundException {
        String login = principal.getName();
        return orderService.getUserOrder(login, id);
    }
}
