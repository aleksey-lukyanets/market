package market.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Contacts of the {@link UserAccount}.
 */
@Entity
@Table(name = "contacts")
public class Contacts implements Serializable {
	private static final long serialVersionUID = 582080671801480110L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "userAccount"))
	private Long id;

	@OneToOne
	@PrimaryKeyJoinColumn
	private UserAccount userAccount; // todo: change to 'String userLogin'

	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "city_region", nullable = false)
	private String cityAndRegion;

	public Contacts() {
	}

	public Contacts(UserAccount userAccount, String phone, String address/*, String cityAndRegion*/) {
		this.userAccount = userAccount;
		this.phone = phone;
		this.address = address;
		this.cityAndRegion = "13";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
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
		Contacts contacts = (Contacts) o;
		return Objects.equals(id, contacts.id) &&
			Objects.equals(phone, contacts.phone) &&
			Objects.equals(address, contacts.address) &&
			Objects.equals(cityAndRegion, contacts.cityAndRegion);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, phone, address, cityAndRegion);
	}

	public static class Builder {
		private Long id;
		private UserAccount userAccount;
		private String phone;
		private String address;
		private String cityAndRegion;

		public Builder() {
		}

		public Builder(Contacts contacts) {
			id = contacts.id;
			userAccount = contacts.userAccount;
			phone = contacts.phone;
			address = contacts.address;
			cityAndRegion = contacts.cityAndRegion;
		}

		public Contacts build() {
			Contacts contacts = new Contacts();
			contacts.id = id;
			contacts.userAccount = userAccount;
			contacts.phone = phone;
			contacts.address = address;
			contacts.cityAndRegion = cityAndRegion;
			return contacts;
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setUserAccount(UserAccount userAccount) {
			this.userAccount = userAccount;
			return this;
		}

		public Builder setPhone(String phone) {
			this.phone = phone;
			return this;
		}

		public Builder setAddress(String address) {
			this.address = address;
			return this;
		}

		public Builder setCityAndRegion(String cityAndRegion) {
			this.cityAndRegion = cityAndRegion;
			return this;
		}
	}
}
