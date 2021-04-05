package market.service;

import market.domain.Cart;
import market.domain.CartItem;

import java.util.List;

public interface CartService {

	/**
	 * Returns existing or creates new cart of the specified user.
	 */
	Cart getCartOrCreate(String userEmail);

	/**
	 * Adds new item into the specified user cart and saves cart.
	 * @return updated cart
	 */
	Cart addToCart(String userEmail, long productId, int quantity);

	/**
	 * Adds all the listed items into the specified user cart and saves cart.
	 * @return updated cart
	 */
	Cart addAllToCart(String userEmail, List<CartItem> itemsToCopy);

	/**
	 * Changes delivery option of the specified user cart.
	 * @return updated cart
	 */
	Cart setDelivery(String userEmail, boolean deliveryIncluded);

	/**
	 * Clears the specified user cart.
	 * @return updated cart
	 */
	Cart clearCart(String userEmail);
}
