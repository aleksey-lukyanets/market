package market.dto;

import java.util.List;
import org.springframework.hateoas.ResourceSupport;

/**
 * Адаптер корзины.
 */
public class CartDTO extends ResourceSupport {
    
    private String user;
    private List<CartItemDTO> items;
    private int totalItems;
    private int productsCost;
    private int deliveryCost;
    private boolean deliveryIncluded;
    private int totalCost;

    public CartDTO() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public boolean isDeliveryIncluded() {
        return deliveryIncluded;
    }

    public void setDeliveryIncluded(boolean deliveryIncluded) {
        this.deliveryIncluded = deliveryIncluded;
    }

    public int getProductsCost() {
        return productsCost;
    }

    public void setProductsCost(int productsCost) {
        this.productsCost = productsCost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
