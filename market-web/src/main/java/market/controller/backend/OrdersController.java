package market.controller.backend;

import market.domain.Order;
import market.domain.OrderedProduct;
import market.domain.UserAccount;
import market.dto.*;
import market.dto.assembler.*;
import market.properties.PaginationProperties;
import market.service.OrderService;
import market.sorting.ISorter;
import market.sorting.OrderSorting;
import market.sorting.SortingValuesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Controller
@RequestMapping("/admin/orders")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class OrdersController {
	private static final String ORDERS_BASE = "admin/orders";

	private final OrderService orderService;
	private final ISorter<OrderDTO> orderSorting;
	private final OrderDtoAssembler orderDtoAssembler = new OrderDtoAssembler();
	private final OrderedProductDtoAssembler orderedProductDTOAssembler = new OrderedProductDtoAssembler();
	private final ProductDtoAssembler productDTOAssembler = new ProductDtoAssembler();
	private final ContactsDtoAssembler contactsDTOAssembler = new ContactsDtoAssembler();
	private final BillDtoAssembler billDTOAssembler = new BillDtoAssembler();

	public OrdersController(OrderService orderService, PaginationProperties paginationProperties) {
		this.orderService = orderService;
		orderSorting = new OrderSorting(paginationProperties.getBackendOrder());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getOrders(
		SortingValuesDTO sortingValues,
		@RequestParam(value = "executed", required = false, defaultValue = "all") String executed,
		@RequestParam(value = "created", required = false, defaultValue = "all") String created,
		Model model
	) {
		PageRequest request = orderSorting.updateSorting(sortingValues);
		Page<Order> page = orderService.fetchFiltered(executed, created, request);
		orderSorting.prepareModel(model, page.map(orderDtoAssembler::toModel));
		List<Order> orders = page.getContent();

		Map<Long, List<OrderedProductDTO>> orderedProductsMap = new HashMap<>();
		for (Order order : orders) {
			List<OrderedProductDTO> productsDto = order.getOrderedProducts().stream()
				.map(orderedProductDTOAssembler::toModel)
				.collect(toList());
			orderedProductsMap.put(order.getId(), productsDto);
		}
		model.addAttribute("orderedProductsByOrderId", orderedProductsMap);

		Map<Long, ProductDTO> productsById = orders.stream()
			.map(Order::getOrderedProducts)
			.flatMap(Collection::stream)
			.map(OrderedProduct::getProduct)
			.distinct()
			.map(productDTOAssembler::toModel)
			.collect(toMap(ProductDTO::getProductId, p -> p));
		model.addAttribute("productsById", productsById);

		Map<String, ContactsDTO> contactsByAccount = orders.stream()
			.map(Order::getUserAccount)
			.collect(toMap(UserAccount::getEmail, a -> contactsDTOAssembler.toModel(a.getContacts())));
		model.addAttribute("contactsByAccount", contactsByAccount);

		Map<Long, BillDTO> billsByOrderId = orders.stream()
			.collect(toMap(Order::getId, o -> billDTOAssembler.toModel(o.getBill())));
		model.addAttribute("billsByOrderId", billsByOrderId);

		model.addAttribute("currentExecuted", executed);
		model.addAttribute("currentCreated", created);
		return ORDERS_BASE;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{orderId}")
	public String setExecutionStatus(
		@PathVariable long orderId,
		@RequestParam(value = "executed") boolean executed
	) {
		orderService.updateStatus(orderId, executed);
		return "redirect:/" + ORDERS_BASE;
	}
}
