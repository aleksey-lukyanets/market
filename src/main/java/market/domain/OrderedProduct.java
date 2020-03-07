package market.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
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
			Objects.equals(pk, that.pk) &&
			Objects.equals(order, that.order) &&
			Objects.equals(product, that.product);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk, order, product, quantity);
	}
}
