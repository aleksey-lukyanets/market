package market.dto;

import market.domain.CartItem;
import org.springframework.hateoas.ResourceSupport;

/**
 * Адаптер элемента корзины.
 */
public class CartItemDTO extends ResourceSupport {

    private Long productId;
    private Short quantity;

    public CartItemDTO() {
    }
    
    public CartItemDTO(long productId, short quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    public CartItemDTO(CartItem item) {
        this.productId = item.getProduct().getId();
        this.quantity = (short)item.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Short getQuantity() {
        return quantity;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }
}
