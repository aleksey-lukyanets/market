package market.dto.assembler;

import market.domain.Cart;
import market.domain.CartItem;
import market.dto.CartDTO;
import market.dto.CartItemDTO;
import market.rest.CartRestController;
import market.rest.ContactsRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 *
 */
@Component
public class CartDtoAssembler extends RepresentationModelAssemblerSupport<Cart, CartDTO> {

	public CartDtoAssembler() {
		super(CartRestController.class, CartDTO.class);
	}

	@Override
	public CartDTO toModel(Cart cart) {
		return instantiateModel(cart);
	}

	public CartDTO toUserResource(Cart cart, int deliveryСost) {
		CartDTO dto = toAnonymousResource(cart, deliveryСost);
		dto.setUser(cart.getUserAccount().getEmail());
		dto.add(linkTo(ContactsRestController.class).withRel("Customer contacts"));
		dto.add(linkTo(CartRestController.class).slash("payment").withRel("Payment"));
		return dto;
	}

	public CartDTO toAnonymousResource(Cart cart, int deliveryСost) {
		int currentDeliveryCost = cart.isDeliveryIncluded() ? deliveryСost : 0;
		int totalCost = cart.isEmpty() ? 0 : (cart.getProductsCost() + currentDeliveryCost);

		CartDTO dto = toModel(cart);
		dto.setDeliveryIncluded(cart.isDeliveryIncluded());
		dto.setDeliveryCost(deliveryСost);
		dto.setProductsCost(cart.getProductsCost());
		dto.setTotalCost(totalCost);
		dto.setTotalItems(cart.getTotalItems());

		List<CartItem> cartItems = cart.getCartItems();
		List<CartItemDTO> cartItemsDto = new ArrayList<>(new CartItemDtoAssembler().toCollectionModel(cartItems).getContent());
		dto.setItems(cartItemsDto);

		return dto;
	}
}
