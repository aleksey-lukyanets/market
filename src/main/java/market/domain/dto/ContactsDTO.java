package market.domain.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Контактные данные пользователя.
 */
public class ContactsDTO {

    @Size(max = 20)
    @Pattern(regexp = "^\\+[1-9][0-9]?[\\s]*\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$")
    private String phone;

    @NotEmpty
    @Size(max = 100)
    @Pattern(regexp="^[^#$%^*()']*$")
    private String address;

    public ContactsDTO() {
    }

    public ContactsDTO(String phone, String address) {
        this.phone = phone;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
