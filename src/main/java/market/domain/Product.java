package market.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name = "product")
public class Product implements Serializable {
	private static final long serialVersionUID = -5637368176838137416L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distillery_id", nullable = false)
	private Distillery distillery;

	@Column(name = "name", nullable = false)
	@NotEmpty
	@Pattern(regexp = "^[^#$%^&*()']*$")
	private String name;

	@Column(name = "price", nullable = false)
	@NotNull
	private Double price;

	@Column(name = "description")
	private String description;

	@Column(name = "volume")
	@NotNull
	private Integer volume;

	@Column(name = "alcohol")
	@NotNull
	@Min(value = 1)
	@Max(value = 96)
	private Float alcohol;

	@Column(name = "age")
	@Max(value = 2000)
	private Integer age;

	@Column(name = "available", nullable = false)
	private boolean available = true;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public @NotNull Double getPrice() {
		return price;
	}

	public void setPrice(@NotNull Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public Float getAlcohol() {
		return alcohol;
	}

	public void setAlcohol(Float alcohol) {
		this.alcohol = alcohol;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Distillery getDistillery() {
		return distillery;
	}

	public void setDistillery(Distillery distillery) {
		this.distillery = distillery;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public static class Builder {
		private long id;
		private Distillery distillery;
		private String name;
		private Double price;
		private String description;
		private Integer volume;
		private Float alcohol;
		private Integer age;
		private boolean available = true;

		public Product build() {
			Product product = new Product();
			product.id = id;
			product.distillery = distillery;
			product.name = name;
			product.price = price;
			product.description = description;
			product.volume = volume;
			product.alcohol = alcohol;
			product.age = age;
			product.available = available;
			return product;
		}

		public Builder setId(long id) {
			this.id = id;
			return this;
		}

		public Builder setDistillery(Distillery distillery) {
			this.distillery = distillery;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setPrice(@NotNull Double price) {
			this.price = price;
			return this;
		}

		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder setVolume(Integer volume) {
			this.volume = volume;
			return this;
		}

		public Builder setAlcohol(Float alcohol) {
			this.alcohol = alcohol;
			return this;
		}

		public Builder setAge(Integer age) {
			this.age = age;
			return this;
		}

		public Builder setAvailable(boolean available) {
			this.available = available;
			return this;
		}
	}
}
