package market.dto;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserDTO extends RepresentationModel<UserDTO> {
	public static final String ALPHANUMERIC = "^[a-zA-Z0-9]+$";
	public static final String EMAIL_REGEX = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]+$";
	public static final String USER_PHONE_REGEX = "^\\+[1-9][0-9]?[\\s]*\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$";
	public static final String USER_ADDRESS_REGEX = "^[^#$%^*()']*$";

	public static final String HIDDEN_PASSWORD = "hidden";

	@NotEmpty
	@Size(max = 50)
	@Pattern(regexp = EMAIL_REGEX)
	private String email;

	@Size(min = 6, max = 50)
	@Pattern(regexp = ALPHANUMERIC)
	private String password;

	@NotEmpty
	@Size(max = 50)
	@Pattern(regexp = "^[\\pL '-]+$")
	private String name;

	@NotEmpty
	@Size(max = 20)
	@Pattern(regexp = USER_PHONE_REGEX)
	private String phone;

	@NotEmpty
	@Size(max = 100)
	@Pattern(regexp = USER_ADDRESS_REGEX)
	private String address;

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

	@Override
	public String toString() {
		return "UserDTO{" +
			"email='" + email + '\'' +
			", password='" + HIDDEN_PASSWORD + '\'' +
			", name='" + name + '\'' +
			", phone='" + phone + '\'' +
			", address='" + address + '\'' +
			'}';
	}
}
