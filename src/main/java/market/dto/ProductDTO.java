package market.dto;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class ProductDTO extends RepresentationModel<ProductDTO> {

	private Long productId;

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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductDTO that = (ProductDTO) o;
		return available == that.available &&
			Objects.equals(productId, that.productId) &&
			Objects.equals(distillery, that.distillery) &&
			Objects.equals(name, that.name) &&
			Objects.equals(price, that.price) &&
			Objects.equals(age, that.age) &&
			Objects.equals(volume, that.volume) &&
			Objects.equals(alcohol, that.alcohol) &&
			Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, distillery, name, price, age, volume, alcohol, description, available);
	}

	@Override
	public String toString() {
		return "ProductDTO{" +
			"productId=" + productId +
			", distillery='" + distillery + '\'' +
			", name='" + name + '\'' +
			", price=" + price +
			", age=" + age +
			", volume=" + volume +
			", alcohol=" + alcohol +
			", description='" + description + '\'' +
			", available=" + available +
			'}';
	}
}
