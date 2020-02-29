package market.controller.backend;

import market.domain.Order;
import market.domain.OrderedProduct;
import market.dto.OrderedProductDTO;
import market.dto.ProductDTO;
import market.dto.assembler.OrderedProductDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.service.OrderService;
import market.sorting.ISorter;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/admin/orders")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class OrdersController {
	private static final String ORDERS_BASE = "admin/orders";

	private final OrderService orderService;
	private final ISorter<Order> orderSorting;
	private final OrderedProductDtoAssembler orderedProductDTOAssembler;
	private final ProductDtoAssembler productDTOAssembler;

	public OrdersController(OrderService orderService, ISorter<Order> orderSorting,
		OrderedProductDtoAssembler orderedProductDTOAssembler, ProductDtoAssembler productDTOAssembler)
	{
		this.orderService = orderService;
		this.orderSorting = orderSorting;
		this.orderedProductDTOAssembler = orderedProductDTOAssembler;
		this.productDTOAssembler = productDTOAssembler;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getOrders(
		SortingValuesDTO sortingValues,
		@RequestParam(value = "executed", required = false, defaultValue = "all") String executed,
		@RequestParam(value = "created", required = false, defaultValue = "all") String created,
		Model model
	) {
		PageRequest request = orderSorting.updateSorting(sortingValues);
		Page<Order> pagedList = orderService.fetchFiltered(executed, created, request);
		orderSorting.prepareModel(model, pagedList);
		List<Order> orders = pagedList.getContent();

		Map<Long, List<OrderedProductDTO>> orderedProductsMap = new HashMap<>();
		for (Order order : orders) {
			List<OrderedProductDTO> productsDto = order.getOrderedProducts().stream()
				.map(orderedProductDTOAssembler::toModel)
				.collect(toList());
			orderedProductsMap.put(order.getId(), productsDto);
		}
		model.addAttribute("orderedProductsByOrderId", orderedProductsMap);

		Map<Long, List<ProductDTO>> productsByOrderId = new HashMap<>();
		for (Order order : orders) {
			List<ProductDTO> productsDto = order.getOrderedProducts().stream()
				.map(OrderedProduct::getProduct)
				.distinct()
				.map(productDTOAssembler::toModel)
				.collect(toList());
			productsByOrderId.put(order.getId(), productsDto);
		}
		model.addAttribute("productsByOrderId", productsByOrderId);

		model.addAttribute("currentExecuted", executed);
		model.addAttribute("currentCreated", created);
		return ORDERS_BASE;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{orderId}")
	public String putOrderExecutionStatus(
		@PathVariable long orderId,
		@RequestParam(value = "executed") boolean executed
	) {
		orderService.updateStatus(orderId, executed);
		return "redirect:/" + ORDERS_BASE;
	}
}
