package market.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Order of the {@link UserAccount}.
 */
@Entity
@Table(name = "customer_order")
public class Order implements Serializable {
	private static final long serialVersionUID = -8328584058042877489L;

	@Id
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "bill"))
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_account_id")
	private UserAccount userAccount;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
		targetEntity = OrderedProduct.class, mappedBy = "order")
//    @OneToMany(fetch = FetchType.LAZY, mappedBy ="pk.order", cascade =
//            {CascadeType.PERSIST, CascadeType.MERGE})
//    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
	private Set<OrderedProduct> orderedProducts = new HashSet<>(0);

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Bill bill;

	@Column(name = "products_cost", nullable = false)
	private double productsCost;

	@Column(name = "date_created", nullable = false)
	@Temporal(TIMESTAMP)
	private Date dateCreated;

	@Column(name = "delivery_cost")
	private int deliveryCost;

	@Column(name = "delivery_included", nullable = false)
	private boolean deliveryIncluded;

	@Column(name = "executed", nullable = false)
	private boolean executed;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getProductsCost() {
		return productsCost;
	}

	public void setProductsCost(double amount) {
		this.productsCost = amount;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date name) {
		this.dateCreated = name;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Set<OrderedProduct> getOrderedProducts() {
		return orderedProducts;
	}

	public void setOrderedProducts(Set<OrderedProduct> products) {
		this.orderedProducts = products;
	}

	public int getDeliveryCost() {
		return deliveryCost;
	}

	public void setDeliveryCost(int deliveryСost) {
		this.deliveryCost = deliveryСost;
	}

	public boolean isDeliveryIncluded() {
		return deliveryIncluded;
	}

	public void setDeliveryIncluded(boolean deliveryIncluded) {
		this.deliveryIncluded = deliveryIncluded;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return Double.compare(order.productsCost, productsCost) == 0 &&
			deliveryCost == order.deliveryCost &&
			deliveryIncluded == order.deliveryIncluded &&
			executed == order.executed &&
			Objects.equals(id, order.id) &&
			Objects.equals(userAccount, order.userAccount) &&
			Objects.equals(orderedProducts, order.orderedProducts) &&
			Objects.equals(bill, order.bill) &&
			Objects.equals(dateCreated, order.dateCreated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userAccount, orderedProducts, bill, productsCost, dateCreated, deliveryCost, deliveryIncluded, executed);
	}

	public static class Builder {
		private Long id;
		private UserAccount userAccount;
		private Set<OrderedProduct> orderedProducts = new HashSet<>();
		private Bill bill;
		private double productsCost;
		private Date dateCreated;
		private int deliveryCost;
		private boolean deliveryIncluded;
		private boolean executed;

		public Order build() {
			Order order = new Order();
			order.id = id;
			order.userAccount = userAccount;
			order.orderedProducts = orderedProducts;
			order.bill = bill;
			order.productsCost = productsCost;
			order.dateCreated = dateCreated;
			order.deliveryCost = deliveryCost;
			order.deliveryIncluded = deliveryIncluded;
			order.executed = executed;
			return order;
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setUserAccount(UserAccount userAccount) {
			this.userAccount = userAccount;
			return this;
		}

		public Builder setOrderedProducts(Set<OrderedProduct> orderedProducts) {
			this.orderedProducts = orderedProducts;
			return this;
		}

		public Builder setBill(Bill bill) {
			this.bill = bill;
			return this;
		}

		public Builder setProductsCost(double productsCost) {
			this.productsCost = productsCost;
			return this;
		}

		public Builder setDateCreated(Date dateCreated) {
			this.dateCreated = dateCreated;
			return this;
		}

		public Builder setDeliveryCost(int deliveryCost) {
			this.deliveryCost = deliveryCost;
			return this;
		}

		public Builder setDeliveryIncluded(boolean deliveryIncluded) {
			this.deliveryIncluded = deliveryIncluded;
			return this;
		}

		public Builder setExecuted(boolean executed) {
			this.executed = executed;
			return this;
		}
	}
}
