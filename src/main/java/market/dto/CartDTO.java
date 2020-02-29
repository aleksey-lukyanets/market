package market.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Collections;
import java.util.List;

/**
 * Адаптер корзины.
 */
public class CartDTO extends RepresentationModel<CartDTO> {

	private String user;
	private List<CartItemDTO> cartItems = Collections.emptyList();
	private int itemsCount;
	private double productsCost;
	private int deliveryCost;
	private boolean deliveryIncluded;
	private double totalCost;

	public CartDTO() {
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<CartItemDTO> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItemDTO> cartItems) {
		this.cartItems = cartItems;
	}

	public boolean isDeliveryIncluded() {
		return deliveryIncluded;
	}

	public void setDeliveryIncluded(boolean deliveryIncluded) {
		this.deliveryIncluded = deliveryIncluded;
	}

	public double getProductsCost() {
		return productsCost;
	}

	public void setProductsCost(double productsCost) {
		this.productsCost = productsCost;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public int getDeliveryCost() {
		return deliveryCost;
	}

	public void setDeliveryCost(int deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public int getItemsCount() {
		return itemsCount;
	}

	public void setItemsCount(int itemsCount) {
		this.itemsCount = itemsCount;
	}
}
