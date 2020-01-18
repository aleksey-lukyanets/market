package market.rest;

import market.domain.Order;
import market.dto.OrderDTO;
import market.dto.assembler.OrderDtoAssembler;
import market.exception.OrderNotFoundException;
import market.service.OrderService;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

/**
 * REST-контроллер истории заказов покупателя.
 */
@Controller
@RequestMapping(value = "/rest/customer/orders")
@ExposesResourceFor(OrderDTO.class)
public class OrdersRestController {
	private final OrderService orderService;
	private final OrderDtoAssembler orderDtoAssembler;

	public OrdersRestController(OrderService orderService, OrderDtoAssembler orderDtoAssembler) {
		this.orderService = orderService;
		this.orderDtoAssembler = orderDtoAssembler;
	}

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
		List<Order> orders = orderService.getUserOrders(login);
		return orderDtoAssembler.toResources(orders);
	}

	/**
	 * Просмотр одного заказа покупателя.
	 *
	 * @param id        идентификатор заказа
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
		Order order = orderService.getUserOrder(login, id);
		return orderDtoAssembler.toResource(order);
	}
}
