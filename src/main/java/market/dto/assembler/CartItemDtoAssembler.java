package market.dto.assembler;

import market.domain.CartItem;
import market.dto.CartItemDTO;
import market.rest.ProductsRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

/**
 *
 */
public class CartItemDtoAssembler extends RepresentationModelAssemblerSupport<CartItem, CartItemDTO> {

	public CartItemDtoAssembler() {
		super(ProductsRestController.class, CartItemDTO.class);
	}

	@Override
	public CartItemDTO toModel(CartItem cartItem) {
		Long productId = cartItem.getProduct().getId();
		CartItemDTO dto = createModelWithId(productId, cartItem);
		dto.setProductId(productId);
		dto.setQuantity((short) cartItem.getQuantity());
		return dto;
	}
}
