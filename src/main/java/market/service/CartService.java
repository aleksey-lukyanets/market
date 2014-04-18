package market.service;

import java.util.List;
import market.domain.Cart;
import market.domain.CartItem;
import market.domain.dto.CartItemDTO;
import market.exception.UnknownProductException;

/**
 * Сервис корзины.
 */
public interface CartService {
    
    Cart save(Cart cart);
    
    void delete(Cart cart);

    Cart findOne(long cartId);

    List<Cart> findAll();
    
    Cart updateCartObject(Cart cart, CartItemDTO item) throws UnknownProductException;
    
    //---------------------------------------- Операции с корзиной пользователя
    
    Cart getUserCart(String userLogin);
    
    Cart clearUserCart(String userLogin);
    
    Cart updateUserCart(String userLogin, CartItemDTO item) throws UnknownProductException;

    Cart setUserCartDelivery(String userLogin, boolean deliveryIncluded);
    
    Cart fillUserCart(String userLogin, List<CartItem> itemsToCopy);
}
