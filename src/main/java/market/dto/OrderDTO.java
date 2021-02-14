package market.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;

public class OrderDTO extends RepresentationModel<OrderDTO> {

	private String userAccount;
	private long id;
	private int billNumber;
	private Date dateCreated;
	private double productsCost;
	private int deliveryCost;
	private boolean deliveryIncluded;
	private double totalCost;
	private boolean payed;
	private boolean executed;

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public int getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(int billNumber) {
		this.billNumber = billNumber;
	}

	public double getProductsCost() {
		return productsCost;
	}

	public void setProductsCost(double productsCost) {
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

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderDTO orderDTO = (OrderDTO) o;
		return id == orderDTO.id &&
			billNumber == orderDTO.billNumber &&
			Double.compare(orderDTO.productsCost, productsCost) == 0 &&
			deliveryCost == orderDTO.deliveryCost &&
			deliveryIncluded == orderDTO.deliveryIncluded &&
			Double.compare(orderDTO.totalCost, totalCost) == 0 &&
			payed == orderDTO.payed &&
			executed == orderDTO.executed &&
			Objects.equals(userAccount, orderDTO.userAccount) &&
			Objects.equals(dateCreated, orderDTO.dateCreated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userAccount, id, billNumber, dateCreated, productsCost, deliveryCost, deliveryIncluded, totalCost, payed, executed);
	}

	@Override
	public String toString() {
		return "OrderDTO{" +
			"userAccount='" + userAccount + '\'' +
			", id=" + id +
			'}';
	}
}
