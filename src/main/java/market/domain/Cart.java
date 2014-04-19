package market.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import market.domain.dto.CartDTO;
import market.domain.dto.CartItemDTO;
import market.exception.ProductNotFoundException;
import market.rest.ContactsWS;
import market.rest.PaymentWS;
import market.rest.ProductsWS;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Корзина.
 */
@Entity
@Table(name = "cart")
public class Cart implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "userAccount"))
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private UserAccount userAccount;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true,
            targetEntity = CartItem.class, mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>(0);

    @Column(name = "delivery_included", nullable = false)
    private boolean deliveryIncluded;

    @Column(name = "total_items")
    private int totalItems;

    @Column(name = "products_cost")
    private int productsCost;

    public Cart() {
        this.deliveryIncluded = true;
        this.totalItems = 0;
        this.productsCost = 0;
    }

    public Cart(UserAccount userAccount) {
        this.userAccount = userAccount;
        this.deliveryIncluded = true;
        this.totalItems = 0;
        this.productsCost = 0;
    }
    
    public boolean isEmpty() {
        return (totalItems == 0);
    }

    public void update(Product product, int quantity) {
        CartItem newItem = new CartItem(this, product, quantity);
        List<CartItem> items = getCartItems();
        if (items.contains(newItem)) {
            int index = items.indexOf(newItem);
            if (quantity > 0) {
                items.get(index).setQuantity(quantity);
            } else {
                items.remove(newItem);
            }
        } else {
            items.add(newItem);
        }
        revalidateCartMetrics();
    }

    public void clear() {
        getCartItems().clear();
        revalidateCartMetrics();
    }

    public void revalidateCartMetrics() {
        int total = 0;
        int cost = 0;
        for (CartItem item : getCartItems()) {
            total += item.getQuantity();
            cost += item.getQuantity() * item.getProduct().getPrice();
        }
        setProductsCost(cost);
        setTotalItems(total);
    }
    
    public CartDTO createDTO(int deliveryСost) {
        CartDTO dto = createAnonymousDTO(deliveryСost);
        dto.setUser(userAccount.getEmail());
        dto.add(linkTo(ContactsWS.class).withRel("Customer contacts"));
        dto.add(linkTo(PaymentWS.class).slash("card").withRel("Payment"));
        return dto;
    }
    
    public CartDTO createAnonymousDTO(int deliveryСost) {
        int currentDeliveryCost = deliveryIncluded ? deliveryСost : 0;
        int totalCost = isEmpty() ? 0 : (productsCost + currentDeliveryCost);
        
        CartDTO dto = new CartDTO();
        dto.setDeliveryIncluded(deliveryIncluded);
        dto.setDeliveryCost(deliveryСost);
        dto.setProductsCost(productsCost);
        dto.setTotalCost(totalCost);
        dto.setItems(createCartItemsDtoList(cartItems));
        dto.setTotalItems(totalItems);
        
        return dto;
    }
    
    private List<CartItemDTO> createCartItemsDtoList(List<CartItem> items) {
        List<CartItemDTO> dtos = new ArrayList<>();
        for (CartItem item : items) {
            CartItemDTO dto = new CartItemDTO(item);
            try {
                dto.add(linkTo(methodOn(ProductsWS.class).getProduct(dto.getProductId())).withSelfRel());
                dtos.add(dto);
            } catch (ProductNotFoundException ex) {}
        }
        return dtos;
    }

    //---------------------------------------------------- Аксессоры и мутаторы
    
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

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public boolean isDeliveryIncluded() {
        return deliveryIncluded;
    }

    public void setDeliveryIncluded(boolean deliveryIncluded) {
        this.deliveryIncluded = deliveryIncluded;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getProductsCost() {
        return productsCost;
    }

    public void setProductsCost(int productsCost) {
        this.productsCost = productsCost;
    }
}
