package market.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Primary key of the {@link CartItem}.
 */
@Embeddable
public class CartItemId implements Serializable {
	private static final long serialVersionUID = -1255025293895062037L;

	private long cartId;
	private long productId;

	public CartItemId() {
	}

	public CartItemId(long cartId, long productId) {
		this.cartId = cartId;
		this.productId = productId;
	}

	public long getCart() {
		return cartId;
	}

	public void setCart(long orderId) {
		this.cartId = orderId;
	}

	public long getProduct() {
		return productId;
	}

	public void setProduct(long productId) {
		this.productId = productId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CartItemId that = (CartItemId) o;
		return cartId == that.cartId &&
			productId == that.productId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cartId, productId);
	}
}
