package market.dto;

import org.springframework.hateoas.RepresentationModel;

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
}
