package market.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Cart of the {@link UserAccount}.
 */
@Entity
@Table(name = "cart")
public class Cart implements Serializable {
	private static final long serialVersionUID = -6884843696895527904L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "userAccount"))
	private long id;

	@OneToOne
	@PrimaryKeyJoinColumn
	private UserAccount userAccount;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true,
		targetEntity = CartItem.class, mappedBy = "cart")
	private List<CartItem> cartItems = new ArrayList<>(0);

	@Column(name = "delivery_included", nullable = false)
	private boolean deliveryIncluded = true;

	@Transient
	private double itemsCost;

	public Cart() {
		this(null);
	}

	public Cart(UserAccount userAccount) {
		this.userAccount = userAccount;
		itemsCost = calculateItemsCost();
	}

	public boolean isEmpty() {
		return cartItems.isEmpty();
	}

	public void update(Product product, int newQuantity) {
		if (product == null)
			return;

		if (newQuantity > 0) {
			CartItem existingItem = findItem(product.getId());
			if (existingItem == null) {
				cartItems.add(new CartItem(this, product, newQuantity));
			} else {
				existingItem.setQuantity(newQuantity);
			}
		} else {
			removeItem(product.getId());
		}
		itemsCost = calculateItemsCost();
	}

	private void removeItem(long productId) {
		Iterator<CartItem> iterator = cartItems.iterator();
		while (iterator.hasNext()) {
			CartItem item = iterator.next();
			if (item.getProduct().getId() == productId)
				iterator.remove();
		}
	}

	private CartItem findItem(long productId) {
		for (CartItem existingItem : cartItems) {
			if (existingItem.getProduct().getId() == productId)
				return existingItem;
		}
		return null;
	}

	private double calculateItemsCost() {
		return cartItems.stream()
			.mapToDouble(CartItem::calculateCost)
			.sum();
	}

	public void clear() {
		cartItems.clear();
		itemsCost = 0;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
		itemsCost = calculateItemsCost();
	}

	public boolean isDeliveryIncluded() {
		return deliveryIncluded;
	}

	public void setDeliveryIncluded(boolean deliveryIncluded) {
		this.deliveryIncluded = deliveryIncluded;
	}

	public int getItemsCount() {
		return cartItems.size();
	}

	public double getItemsCost() {
		return itemsCost;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cart cart = (Cart) o;
		return deliveryIncluded == cart.deliveryIncluded &&
			Objects.equals(id, cart.id) &&
			Objects.equals(userAccount, cart.userAccount) &&
			Objects.equals(cartItems, cart.cartItems);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userAccount, cartItems, deliveryIncluded);
	}
}
