package market.dto;

import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class CreditCardDTO {

	@NotEmpty
	@CreditCardNumber
	@Pattern(regexp = "\\b(?:\\d[ -]*?){13,16}\\b")
	private String ccNumber;

	public CreditCardDTO() {
	}

	public CreditCardDTO(String number) {
		this.ccNumber = number;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	@Override
	public String toString() {
		return "CreditCardDTO{" +
			"ccNumber='" + ccNumber + '\'' +
			'}';
	}
}
