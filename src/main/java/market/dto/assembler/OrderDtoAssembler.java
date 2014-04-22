package market.dto.assembler;

import market.domain.Order;
import market.dto.OrderDTO;
import market.rest.OrdersRestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class OrderDtoAssembler extends ResourceAssemblerSupport<Order, OrderDTO> {

    public OrderDtoAssembler() {
        super(OrdersRestController.class, OrderDTO.class);
    }

    @Override
    public OrderDTO toResource(Order order) {
        OrderDTO dto = createResourceWithId(order.getId(), order);
        dto.setOrderId(order.getId());
        dto.setUser(order.getUserAccount().getEmail());
        dto.setBillNumber(order.getBill().getNumber());
        dto.setProductsCost(order.getProductsCost());
        dto.setDateCreated(order.getDateCreated());
        dto.setDeliveryCost(order.getDeliveryСost());
        dto.setTotalCost(order.isDeliveryIncluded() ? (order.getProductsCost() + order.getDeliveryСost()) : order.getProductsCost());
        dto.setDeliveryIncluded(order.isDeliveryIncluded());
        dto.setPayed(order.getBill().isPayed());
        dto.setExecuted(order.isExecuted());
        return dto;
    }
}
