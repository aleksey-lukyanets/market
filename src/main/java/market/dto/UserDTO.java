package market.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Адаптер данных пользователя.
 */
public class UserDTO extends RepresentationModel<UserDTO> {

	@NotEmpty
	@Size(max = 50)
	@Pattern(regexp = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]+$")
	private String email;

	@Size(min = 6, max = 50)
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	private String password;

	@Size(max = 50)
	@NotEmpty
	@Pattern(regexp = "^[\\pL '-]+$")
	private String name;

	@NotEmpty
	@Size(max = 20)
	@Pattern(regexp = "^\\+[1-9][0-9]?[\\s]*\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$")
	private String phone;

	@NotEmpty
	@Size(max = 100)
	@Pattern(regexp = "^[^#$%^*()']*$")
	private String address;

	public UserDTO() {
	}

	public UserDTO(String email, String password, String name, String phone, String address) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserDTO userDTO = (UserDTO) o;
		return Objects.equals(email, userDTO.email) &&
			Objects.equals(password, userDTO.password) &&
			Objects.equals(name, userDTO.name) &&
			Objects.equals(phone, userDTO.phone) &&
			Objects.equals(address, userDTO.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, password, name, phone, address);
	}
}
