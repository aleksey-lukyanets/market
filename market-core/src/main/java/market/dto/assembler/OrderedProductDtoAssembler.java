package market.dto.assembler;

import market.domain.OrderedProduct;
import market.dto.OrderedProductDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class OrderedProductDtoAssembler implements RepresentationModelAssembler<OrderedProduct, OrderedProductDTO> {

	@Override
	public OrderedProductDTO toModel(OrderedProduct orderedProduct) {
		OrderedProductDTO dto = new OrderedProductDTO();
		dto.setOrderId(orderedProduct.getOrder().getId());
		dto.setQuantity(orderedProduct.getQuantity());
		dto.setProductId(orderedProduct.getProduct().getId());
		return dto;
	}
}
