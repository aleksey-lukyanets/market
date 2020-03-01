package market.domain;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.*;

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

	public Contacts(String phone, String address/*, String cityAndRegion*/) {
		this(null, phone, address);
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

}
