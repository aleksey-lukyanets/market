package market.service;

import java.util.List;
import market.domain.Cart;
import market.domain.CartItem;
import market.dto.CartItemDTO;
import market.exception.UnknownProductException;

/**
 * Сервис корзины.
 */
public interface CartService {
    
    Cart save(Cart cart);
    
    void delete(Cart cart);

    Cart findOne(long cartId);

    List<Cart> findAll();
    
    /**
     * Добавление новой позиции в объект корзины.
     * 
     * @param cart корзина
     * @param item добавляемая позиция
     * @return обновлённая корзина
     * @throws UnknownProductException если запрошенный товар не существует
     */
    Cart updateCartObject(Cart cart, CartItemDTO item) throws UnknownProductException;
    
    //---------------------------------------- Операции с корзиной пользователя
    
    /**
     * Получение корзины покупателя.
     * 
     * @param userLogin логин покупателя
     * @return обновлённая корзина
     */
    Cart getUserCart(String userLogin);
    
    /**
     * Очистка корзины покупателя.
     * 
     * @param userLogin логин покупателя
     * @return обновлённая корзина
     */
    Cart clearUserCart(String userLogin);
    
    /**
     * Добавление новой позиции в корзину покупателя.
     * 
     * @param userLogin логин покупателя
     * @param item добавляемая позиция
     * @return обновлённая корзина
     * @throws UnknownProductException если запрошенный товар не существует
     */
    Cart updateUserCart(String userLogin, CartItemDTO item) throws UnknownProductException;

    /**
     * Изменение опции доставки в корзине покупателя.
     *
     * @param userLogin логин покупателя
     * @param deliveryIncluded
     * @return обновлённая корзина
     */
    Cart setUserCartDelivery(String userLogin, boolean deliveryIncluded);
    
    /**
     * Добавление в корзину покупателя списка позиций.
     *
     * @param userLogin логин покупателя
     * @param itemsToCopy
     * @return обновлённая корзина
     */
    Cart fillUserCart(String userLogin, List<CartItem> itemsToCopy);
}
