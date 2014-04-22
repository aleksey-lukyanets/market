package market.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;
import market.dto.OrderDTO;

/**
 * Заказ.
 */
@Entity
@Table(name = "customer_order")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
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
    
    @OneToOne(mappedBy="order", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private Bill bill;
    
    @Column(name = "products_cost", nullable = false)
    private int productsCost;
    
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

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the amount
     */
    public int getProductsCost() {
        return productsCost;
    }

    /**
     * @param amount the amount to set
     */
    public void setProductsCost(int amount) {
        this.productsCost = amount;
    }

    /**
     * @return the name
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param name the name to set
     */
    public void setDateCreated(Date name) {
        this.dateCreated = name;
    }

    /**
     * @return the userAccount
     */
    public UserAccount getUserAccount() {
        return userAccount;
    }

    /**
     * @param userAccount the userAccount to set
     */
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * @return the products
     */
    public Set<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    /**
     * @param products the products to set
     */
    public void setOrderedProducts(Set<OrderedProduct> products) {
        this.orderedProducts = products;
    }

    /**
     * @return the deliveryСost
     */
    public int getDeliveryСost() {
        return deliveryСost;
    }

    /**
     * @param deliveryСost the deliveryСost to set
     */
    public void setDeliveryСost(int deliveryСost) {
        this.deliveryСost = deliveryСost;
    }

    /**
     * @return the deliveryIncluded
     */
    public boolean isDeliveryIncluded() {
        return deliveryIncluded;
    }

    /**
     * @param deliveryIncluded the deliveryIncluded to set
     */
    public void setDeliveryIncluded(boolean deliveryIncluded) {
        this.deliveryIncluded = deliveryIncluded;
    }

    /**
     * @return the bill
     */
    public Bill getBill() {
        return bill;
    }

    /**
     * @param bill the bill to set
     */
    public void setBill(Bill bill) {
        this.bill = bill;
    }

    /**
     * @return the executed
     */
    public boolean isExecuted() {
        return executed;
    }

    /**
     * @param executed the executed to set
     */
    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

}
