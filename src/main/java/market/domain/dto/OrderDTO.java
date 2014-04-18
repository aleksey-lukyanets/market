package market.domain.dto;

import java.util.Date;

/**
 * 
 */
public class OrderDTO {
    
    private String user;
    private long id;
    private int billNumber;
    private Date dateCreated;
    private int productsCost;
    private int deliveryCost;
    private boolean deliveryIncluded;
    private int totalCost;
    private boolean payed;
    private boolean executed;
    
    public OrderDTO() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public int getProductsCost() {
        return productsCost;
    }

    public void setProductsCost(int productsCost) {
        this.productsCost = productsCost;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isDeliveryIncluded() {
        return deliveryIncluded;
    }

    public void setDeliveryIncluded(boolean deliveryIncluded) {
        this.deliveryIncluded = deliveryIncluded;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }
}
