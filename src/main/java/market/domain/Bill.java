package market.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
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

	public Bill() {
	}

	public Bill(int totalCost, String ccNumber) {
		this.totalCost = totalCost;
		this.ccNumber = ccNumber;
	}

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


}
