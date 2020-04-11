package market.dto.assembler;

import market.controller.backend.OrdersController;
import market.domain.OrderedProduct;
import market.dto.OrderedProductDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class OrderedProductDtoAssembler extends RepresentationModelAssemblerSupport<OrderedProduct, OrderedProductDTO> {

	public OrderedProductDtoAssembler() {
		super(OrdersController.class, OrderedProductDTO.class);
	}

	@Override
	public OrderedProductDTO toModel(OrderedProduct orderedProduct) {
		OrderedProductDTO dto = instantiateModel(orderedProduct);
		dto.setOrderId(orderedProduct.getOrder().getId());
		dto.setQuantity(orderedProduct.getQuantity());
		dto.setProductId(orderedProduct.getProduct().getId());
		return dto;
	}
}
