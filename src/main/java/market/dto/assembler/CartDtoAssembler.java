package market.dto.assembler;

import java.util.List;
import market.domain.Cart;
import market.dto.CartDTO;
import market.dto.CartItemDTO;
import market.rest.CartRestController;
import market.rest.ContactsRestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class CartDtoAssembler extends ResourceAssemblerSupport<Cart, CartDTO> {

    public CartDtoAssembler() {
        super(CartRestController.class, CartDTO.class);
    }

    @Override
    public CartDTO toResource(Cart cart) {
        return instantiateResource(cart);
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
        
        CartDTO dto = toResource(cart);
        dto.setDeliveryIncluded(cart.isDeliveryIncluded());
        dto.setDeliveryCost(deliveryСost);
        dto.setProductsCost(cart.getProductsCost());
        dto.setTotalCost(totalCost);
        dto.setTotalItems(cart.getTotalItems());
        
        List<CartItemDTO> items = new CartItemDtoAssembler().toResources(cart.getCartItems());
        dto.setItems(items);
        
        return dto;
    }
}
