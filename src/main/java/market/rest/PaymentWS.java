package market.rest;

import java.net.URI;
import java.security.Principal;
import javax.validation.Valid;
import market.domain.Order;
import market.domain.dto.OrderDTO;
import market.domain.dto.CreditCardDTO;
import market.exception.EmptyCartException;
import market.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 */
@Controller
@RequestMapping(value = "/rest/payment")
public class PaymentWS {

    @Value("${deliveryCost}")
    private int deliveryCost;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    EntityLinks entityLinks;

    /**
     * Оформление заказа.
     *
     * @param principal
     * @param card
     * @param builder
     * @return
     * @throws market.exception.EmptyCartException
     */
    @RequestMapping(value = "/card",
            method = RequestMethod.POST,
            consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<OrderDTO> payByCard(Principal principal, @Valid @RequestBody CreditCardDTO card, UriComponentsBuilder builder) throws EmptyCartException {
        String login = principal.getName();
        Order order = orderService.createUserOrder(card, login, deliveryCost);
        OrderDTO dto = order.createDTO();
        
        HttpHeaders headers = new HttpHeaders();
        Link link = entityLinks.linkToSingleResource(OrderDTO.class, dto.getId());
        headers.setLocation(URI.create(link.getHref()));
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }
}
