package market.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;

/**
 *
 */
public class OrderDTO extends RepresentationModel<OrderDTO> {

	private String user;
	private long orderId;
	private int billNumber;
	private Date dateCreated;
	private double productsCost;
	private int deliveryCost;
	private boolean deliveryIncluded;
	private double totalCost;
	private boolean payed;
	private boolean executed;

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

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
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
		return orderId == orderDTO.orderId &&
			billNumber == orderDTO.billNumber &&
			Double.compare(orderDTO.productsCost, productsCost) == 0 &&
			deliveryCost == orderDTO.deliveryCost &&
			deliveryIncluded == orderDTO.deliveryIncluded &&
			Double.compare(orderDTO.totalCost, totalCost) == 0 &&
			payed == orderDTO.payed &&
			executed == orderDTO.executed &&
			Objects.equals(user, orderDTO.user) &&
			Objects.equals(dateCreated, orderDTO.dateCreated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, orderId, billNumber, dateCreated, productsCost, deliveryCost, deliveryIncluded, totalCost, payed, executed);
	}
}
