package market.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Контактные данные пользователя.
 */
public class ContactsDTO extends RepresentationModel<ContactsDTO> {

	@Size(max = 20)
	@Pattern(regexp = "^\\+[1-9][0-9]?[\\s]*\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$")
	private String phone;

	@NotEmpty
	@Size(max = 100)
	@Pattern(regexp = "^[^#$%^*()']*$")
	private String address;

	private String cityAndRegion;

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

	public String getCityAndRegion() {
		return cityAndRegion;
	}

	public void setCityAndRegion(String cityAndRegion) {
		this.cityAndRegion = cityAndRegion;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ContactsDTO that = (ContactsDTO) o;
		return Objects.equals(phone, that.phone) &&
			Objects.equals(address, that.address) &&
			Objects.equals(cityAndRegion, that.cityAndRegion);
	}

	@Override
	public int hashCode() {
		return Objects.hash(phone, address, cityAndRegion);
	}
}
