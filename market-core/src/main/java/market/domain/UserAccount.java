package market.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_account")
public class UserAccount implements Serializable {
	private static final long serialVersionUID = -8278943418573848966L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "active", nullable = false)
	private boolean active;

	@OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
	private Contacts contacts;

	@OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
	private Cart cart;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinTable(name = "user_role",
		joinColumns = {
			@JoinColumn(name = "user_id")},
		inverseJoinColumns = {
			@JoinColumn(name = "role_id")})
	private Set<Role> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Contacts getContacts() {
		return contacts;
	}

	public void setContacts(Contacts contacts) {
		this.contacts = contacts;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserAccount account = (UserAccount) o;
		return active == account.active &&
			Objects.equals(id, account.id) &&
			Objects.equals(email, account.email) &&
			Objects.equals(password, account.password) &&
			Objects.equals(name, account.name) &&
			Objects.equals(contacts, account.contacts) &&
			Objects.equals(cart, account.cart) &&
			Objects.equals(roles, account.roles);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, password, name, active, contacts, cart, roles);
	}

	public static class Builder {
		private Long id;
		private String email;
		private String password;
		private String name;
		private boolean active;
		private Contacts contacts;
		private Cart cart;
		private Set<Role> roles = new HashSet<>();

		public Builder() {
		}

		public Builder(UserAccount account) {
			this.id = account.id;
			this.email = account.email;
			this.password = account.password;
			this.name = account.name;
			this.active = account.active;
			this.contacts = account.contacts;
			this.cart = account.cart;
			this.roles = account.roles;
		}

		public UserAccount build() {
			UserAccount account = new UserAccount();
			account.id = id;
			account.email = email;
			account.password = password;
			account.name = name;
			account.active = active;
			account.contacts = contacts;
			account.cart = cart;
			account.roles = roles;
			return account;
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setActive(boolean active) {
			this.active = active;
			return this;
		}

		public Builder setContacts(Contacts contacts) {
			this.contacts = contacts;
			return this;
		}

		public Builder setCart(Cart cart) {
			this.cart = cart;
			return this;
		}

		public Builder setRoles(Set<Role> roles) {
			this.roles = roles;
			return this;
		}

		public Long getId() {
			return id;
		}
	}
}
