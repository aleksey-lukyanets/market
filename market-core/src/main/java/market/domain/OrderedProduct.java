package market.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * {@link Product} of the {@link Order}.
 */
@Entity
@Table(name = "ordered_product")
public class OrderedProduct implements Serializable {
	private static final long serialVersionUID = -2055528467252485472L;

	@EmbeddedId
	private OrderedProductId pk = new OrderedProductId();

	@MapsId("orderId")
	@JoinColumn(name = "customer_order_id", referencedColumnName = "id")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Order order;

	@MapsId("productId")
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Product product;

	@Column(name = "quantity")
	private int quantity;

	public OrderedProductId getPk() {
		return pk;
	}

	public void setPk(OrderedProductId pk) {
		this.pk = pk;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
		getPk().setCustomerOrder(order.getId());
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		getPk().setProduct(product.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderedProduct that = (OrderedProduct) o;
		return quantity == that.quantity &&
			Objects.equals(order.getId(), that.order.getId()) &&
			Objects.equals(product.getId(), that.product.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(order.getId(), product.getId(), quantity);
	}

	public static class Builder {
		private Order order;
		private Product product;
		private int quantity;

		public OrderedProduct build() {
			OrderedProduct orderedProduct = new OrderedProduct();
			orderedProduct.setOrder(order);
			orderedProduct.setProduct(product);
			orderedProduct.setQuantity(quantity);
			return orderedProduct;
		}

		public Builder setOrder(Order order) {
			this.order = order;
			return this;
		}

		public Builder setProduct(Product product) {
			this.product = product;
			return this;
		}

		public Builder setQuantity(int quantity) {
			this.quantity = quantity;
			return this;
		}
	}
}
