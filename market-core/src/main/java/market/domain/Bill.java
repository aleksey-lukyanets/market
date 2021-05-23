package market.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Bill of the {@link Order}.
 */
@Entity
@Table(name = "bill")
public class Bill implements Serializable {
	private static final long serialVersionUID = 3689283961628876802L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Order order;

	@Column(name = "number", nullable = false)
	private int number;

	@Column(name = "total_cost", nullable = false)
	private double totalCost;

	@Column(name = "payed", nullable = false)
	private boolean payed = false;

	@Column(name = "cc_number", nullable = false)
	@NotEmpty
	@Pattern(regexp = "\\b(?:\\d[ -]*?){13,16}\\b")
	private String ccNumber;

	@Column(name = "date_created", nullable = false)
	@Temporal(TIMESTAMP)
	private Date dateCreated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public static class Builder {
		private Long id;
		private Order order;
		private int number;
		private double totalCost;
		private boolean payed = false;
		private String ccNumber;
		private Date dateCreated;

		public Bill build() {
			Bill bill = new Bill();
			bill.id = id;
			bill.order = order;
			bill.number = number;
			bill.totalCost = totalCost;
			bill.payed = payed;
			bill.ccNumber = ccNumber;
			bill.dateCreated = dateCreated;
			return bill;
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setOrder(Order order) {
			this.order = order;
			return this;
		}

		public Builder setNumber(int number) {
			this.number = number;
			return this;
		}

		public Builder setTotalCost(double totalCost) {
			this.totalCost = totalCost;
			return this;
		}

		public Builder setPayed(boolean payed) {
			this.payed = payed;
			return this;
		}

		public Builder setCcNumber(String ccNumber) {
			this.ccNumber = ccNumber;
			return this;
		}

		public Builder setDateCreated(Date dateCreated) {
			this.dateCreated = dateCreated;
			return this;
		}
	}
}
