package market.dto.assembler;

import market.domain.Order;
import market.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.List;

public class OrderDtoAssembler implements RepresentationModelAssembler<Order, OrderDTO> {

	@Override
	public OrderDTO toModel(Order order) {
		OrderDTO dto = new OrderDTO();
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
