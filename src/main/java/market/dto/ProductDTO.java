package market.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Адаптер товара.
 */
public class ProductDTO extends RepresentationModel<ProductDTO> {

	private long productId;

	private String distillery;

	@NotEmpty
	@Pattern(regexp = "^[^#$%^&*()']*$")
	private String name;

	@NotNull
	private Double price;

	@Max(value = 2000)
	private Integer age;

	@NotNull
	private Integer volume;

	@NotNull
	@Min(value = 1)
	@Max(value = 96)
	private Float alcohol;

	private String description;
	private boolean available;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getDistillery() {
		return distillery;
	}

	public void setDistillery(String distillery) {
		this.distillery = distillery;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(@NotNull Double price) {
		this.price = price;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
