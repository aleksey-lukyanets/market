package market.dto;

import org.springframework.hateoas.RepresentationModel;

/**
 * Адаптер товара.
 */
public class ProductPreviewDTO extends RepresentationModel<ProductPreviewDTO> {

	private long productId;
	private String region;
	private String distillery;
	private String name;
	private Double price;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductPreviewDTO{" +
			"productId=" + productId +
			", region='" + region + '\'' +
			", distillery='" + distillery + '\'' +
			", name='" + name + '\'' +
			", price=" + price +
			'}';
	}
}
