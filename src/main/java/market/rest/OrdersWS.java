package market.rest;

import java.security.Principal;
import java.util.List;
import market.domain.dto.OrderDTO;
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
 * 
 */
@Controller
@ExposesResourceFor(OrderDTO.class)
@RequestMapping(value = "/rest/customer/orders")
public class OrdersWS {
    
    @Autowired
    private OrderService orderService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<OrderDTO> getOrders(Principal principal) {
        String login = principal.getName();
        return orderService.getUserOrders(login);
    }
    
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public OrderDTO getOrder(Principal principal, @PathVariable long id) throws OrderNotFoundException {
        String login = principal.getName();
        return orderService.getUserOrder(login, id);
    }
}
