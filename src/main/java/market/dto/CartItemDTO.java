package market.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

/**
 * Адаптер элемента корзины.
 */
public class CartItemDTO extends RepresentationModel<CartItemDTO> {

	private long productId;
	private int quantity;

	public CartItemDTO() {
	}

	public CartItemDTO(long productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CartItemDTO that = (CartItemDTO) o;
		return productId == that.productId &&
			quantity == that.quantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, quantity);
	}
}
