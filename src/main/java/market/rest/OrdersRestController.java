package market.rest;

import market.domain.Order;
import market.dto.OrderDTO;
import market.dto.assembler.OrderDtoAssembler;
import market.exception.UnknownEntityException;
import market.service.OrderService;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Customer orders history.
 */
@Controller
@RequestMapping(value = "/rest/customer/orders")
@Secured({"ROLE_USER"})
@ExposesResourceFor(OrderDTO.class)
public class OrdersRestController {
	private final OrderService orderService;
	private final OrderDtoAssembler orderDtoAssembler = new OrderDtoAssembler();

	public OrdersRestController(OrderService orderService) {
		this.orderService = orderService;
	}

	/**
	 * View orders.
	 *
	 * @return orders list of the specified customer
	 */
	@RequestMapping(
		method = RequestMethod.GET,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<OrderDTO> getOrders(Principal principal) {
		return orderService.getUserOrders(principal.getName()).stream()
			.map(orderDtoAssembler::toModel)
			.collect(toList());
	}

	/**
	 * View a single order.
	 *
	 * @return order of the specified customer
	 * @throws UnknownEntityException if the order with the specified id doesn't exist
	 */
	@RequestMapping(value = "/{id}",
		method = RequestMethod.GET,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public OrderDTO getOrder(Principal principal, @PathVariable long id) throws UnknownEntityException {
		String login = principal.getName();
		Order order = orderService.getUserOrder(login, id);
		return orderDtoAssembler.toModel(order);
	}
}
