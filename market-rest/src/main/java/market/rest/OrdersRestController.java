package market.rest;

import market.dto.OrderDTO;
import market.dto.assembler.OrderDtoAssembler;
import market.exception.UnknownEntityException;
import market.service.OrderService;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Customer orders history.
 */
@RestController
@RequestMapping(value = "customer/orders")
@ExposesResourceFor(OrderDTO.class)
@Secured({"ROLE_USER"})
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
	@GetMapping
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
	@GetMapping(value = "/{orderId}")
	public OrderDTO getOrder(Principal principal, @PathVariable long orderId) {
		String login = principal.getName();
		return orderService.getUserOrder(login, orderId)
			.map(orderDtoAssembler::toModel)
			.orElseThrow(() -> new UnknownEntityException(OrderDTO.class, orderId));
	}
}
