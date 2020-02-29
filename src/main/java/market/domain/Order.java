package market.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Order of the {@link UserAccount}.
 */
@Entity
@Table(name = "customer_order")
public class Order implements Serializable {

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
	private int deliveryСost;

	@Column(name = "delivery_included", nullable = false)
	private boolean deliveryIncluded;

	@Column(name = "executed", nullable = false)
	private boolean executed;

	public Order() {
	}

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

	public int getDeliveryСost() {
		return deliveryСost;
	}

	public void setDeliveryСost(int deliveryСost) {
		this.deliveryСost = deliveryСost;
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

}
