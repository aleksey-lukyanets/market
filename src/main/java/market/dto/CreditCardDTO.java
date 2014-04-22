package market.dto;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 */
public class CreditCardDTO {

    @NotEmpty
    @Pattern(regexp="\\b(?:\\d[ -]*?){13,16}\\b")
    private String number;
    
    public CreditCardDTO() {
    }

    public CreditCardDTO(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
