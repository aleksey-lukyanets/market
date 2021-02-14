package market.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class OrderedProductDTO extends RepresentationModel<OrderedProductDTO> {
	private long orderId;
	private int quantity;
	private long productId;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderedProductDTO that = (OrderedProductDTO) o;
		return orderId == that.orderId &&
			quantity == that.quantity &&
			productId == that.productId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, quantity, productId);
	}

	@Override
	public String toString() {
		return "OrderedProductDTO{" +
			"orderId=" + orderId +
			", quantity=" + quantity +
			", productId=" + productId +
			'}';
	}
}
