package market.dto;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Positive;
import java.util.Objects;

/**
 * Адаптер элемента корзины.
 */
public class CartItemDTO extends RepresentationModel<CartItemDTO> {

	@Positive
	@NumberFormat
	private long productId;

	@Positive
	@NumberFormat
	private int quantity;

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

	@Override
	public String toString() {
		return "CartItemDTO{" +
			"productId=" + productId +
			", quantity=" + quantity +
			'}';
	}
}
