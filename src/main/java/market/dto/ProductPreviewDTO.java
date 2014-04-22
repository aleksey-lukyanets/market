package market.dto;

import org.springframework.hateoas.ResourceSupport;

/**
 * Адаптер товара.
 */
public class ProductPreviewDTO extends ResourceSupport {
    
    private long productId;
    private String distillery;
    private String name;
    private Integer price;

    public ProductPreviewDTO() {
    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
