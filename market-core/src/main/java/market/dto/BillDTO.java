package market.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class BillDTO extends RepresentationModel<BillDTO> {
	private Long id;
	private int number;
	private double totalCost;
	private boolean payed;
	private String ccNumber;
	private String dateCreated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BillDTO billDTO = (BillDTO) o;
		return number == billDTO.number &&
			Double.compare(billDTO.totalCost, totalCost) == 0 &&
			payed == billDTO.payed &&
			Objects.equals(id, billDTO.id) &&
			Objects.equals(ccNumber, billDTO.ccNumber) &&
			Objects.equals(dateCreated, billDTO.dateCreated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, number, totalCost, payed, ccNumber, dateCreated);
	}
}
