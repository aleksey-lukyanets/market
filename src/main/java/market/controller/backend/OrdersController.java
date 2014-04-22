package market.controller.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import market.domain.Order;
import market.domain.OrderedProduct;
import market.service.OrderService;
import market.sorting.ISorter;
import market.sorting.SortingValuesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер управления заказами.
 */
@Controller
@RequestMapping("/admin/orders")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class OrdersController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ISorter<Order> orderSorting;

    /**
     * Перечень заказов.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getOrders(
            SortingValuesDTO sortingValues,
            @RequestParam(value = "exec", required = false, defaultValue = "all") String executed,
            @RequestParam(value = "created", required = false, defaultValue = "all") String created,
            Model model
    ) {
        PageRequest request = orderSorting.updateSorting(sortingValues);
        Page<Order> pagedList = orderService.fetchFilteredAndPaged(executed, created, request);
        orderSorting.prepareModel(model, pagedList);

        Map<Long, List<OrderedProduct>> orderedProductsMap = new HashMap<>();
        for (Order order : pagedList.getContent()) {
            orderedProductsMap.put(order.getId(), new ArrayList<>(order.getOrderedProducts()));
        }
        model.addAttribute("orderedProductsMap", orderedProductsMap);

        model.addAttribute("currentExecuted", executed);
        model.addAttribute("currentCreated", created);
        return "admin/orders";
    }

    /**
     * Изменение статуса исполнения заказа.
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{orderId}")
    public String putOrderExecutionStatus(
            @PathVariable long orderId,
            @RequestParam(value = "executed") Boolean executed
    ) {
        if (executed != null) {
            Order order = orderService.findOne(orderId);
            order.setExecuted(executed);
            orderService.save(order);
        }
        return "redirect:/admin/orders";
    }
}
