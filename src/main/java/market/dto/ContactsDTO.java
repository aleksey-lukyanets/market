package market.dto;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Контактные данные пользователя.
 */
public class ContactsDTO extends RepresentationModel<ContactsDTO> {

	@NotEmpty
	@Size(max = 20)
	@Pattern(regexp = UserDTO.USER_PHONE_REGEX)
	private String phone;

	@NotEmpty
	@Size(max = 100)
	@Pattern(regexp = UserDTO.USER_ADDRESS_REGEX)
	private String address;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ContactsDTO that = (ContactsDTO) o;
		return Objects.equals(phone, that.phone) &&
			Objects.equals(address, that.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(phone, address);
	}

	@Override
	public String toString() {
		return "ContactsDTO{" +
			"phone='" + phone + '\'' +
			", address='" + address + '\'' +
			'}';
	}
}
