package market.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * Элемент корзины.
 */
@Entity
@Table(name = "cart_item")
public class CartItem implements Serializable {

    @EmbeddedId
    private CartItemId pk = new CartItemId();

    @MapsId("cartId")
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cart cart;

    @MapsId("productId")
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    public CartItem() {
    }

    public CartItem(Cart cart, Product product, int quantity) {
        this.pk = new CartItemId(cart.getId(), product.getId());
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem that = (CartItem) o;
        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }

    public CartItemId getPk() {
        return pk;
    }

    public void setPk(CartItemId pk) {
        this.pk = pk;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the order
     */
    public Cart getOrder() {
        return cart;
    }

    /**
     * @param cart the order to set
     */
    public void setOrder(Cart cart) {
        this.cart = cart;
        getPk().setCart(cart.getId());
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
        getPk().setProduct(product.getId());
    }

}
