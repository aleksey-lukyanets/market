package market.dto;

import org.springframework.hateoas.RepresentationModel;

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
}
