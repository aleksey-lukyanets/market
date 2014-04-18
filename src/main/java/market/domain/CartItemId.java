package market.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 * Первичный ключ элемента корзины.
 */
@Embeddable
public class CartItemId implements Serializable {
    
    private Long cartId;
    private Long productId;
    
    public CartItemId() {
    }

    public CartItemId(Long cartId, Long productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    public Long getCart() {
        return cartId;
    }

    public void setCart(Long orderId) {
        this.cartId = orderId;
    }

    public Long getProduct() {
        return productId;
    }

    public void setProduct(Long productId) {
        this.productId = productId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        CartItemId that = (CartItemId) o;
 
        if (cartId != null ? !cartId.equals(that.cartId) : that.cartId != null) return false;
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        int result;
        result = (cartId != null ? cartId.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }
    
}
