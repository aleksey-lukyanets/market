package market.dto.assembler;

import market.domain.Order;
import market.dto.OrderDTO;
import market.rest.OrdersRestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.List;

public class OrderDtoAssembler extends RepresentationModelAssemblerSupport<Order, OrderDTO> {

	public OrderDtoAssembler() {
		super(OrdersRestController.class, OrderDTO.class);
	}

	@Override
	public OrderDTO toModel(Order order) {
		OrderDTO dto = createModelWithId(order.getId(), order);
		dto.setId(order.getId());
		dto.setUserAccount(order.getUserAccount().getEmail());
		dto.setBillNumber(order.getBill().getNumber());
		dto.setProductsCost(order.getProductsCost());
		dto.setDateCreated(order.getDateCreated());
		dto.setDeliveryCost(order.getDeliveryCost());
		dto.setTotalCost(order.isDeliveryIncluded() ? (order.getProductsCost() + order.getDeliveryCost()) : order.getProductsCost());
		dto.setDeliveryIncluded(order.isDeliveryIncluded());
		dto.setPayed(order.getBill().isPayed());
		dto.setExecuted(order.isExecuted());
		return dto;
	}

	public PageImpl<OrderDTO> toModel(Page<Order> page) {
		List<OrderDTO> dtoList = page.map(this::toModel).toList();
		return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
	}

	public OrderDTO[] toDtoArray(List<Order> items) {
		return toCollectionModel(items).getContent().toArray(new OrderDTO[items.size()]);
	}
}
