package market.service.impl;

import market.dao.CartDAO;
import market.dao.ProductDAO;
import market.dao.UserAccountDAO;
import market.domain.Cart;
import market.domain.CartItem;
import market.domain.Product;
import market.domain.UserAccount;
import market.exception.UnknownProductException;
import market.service.CartService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса корзины.
 */
public class CartServiceImpl implements CartService {
	private final CartDAO cartDAO;
	private final UserAccountDAO userAccountDAO;
	private final ProductDAO productDAO;

	public CartServiceImpl(CartDAO cartDAO, UserAccountDAO userAccountDAO, ProductDAO productDAO) {
		this.cartDAO = cartDAO;
		this.userAccountDAO = userAccountDAO;
		this.productDAO = productDAO;
	}

	@Transactional
	@Override
	public Cart save(Cart cart) {
		return cartDAO.save(cart);
	}

	@Transactional
	@Override
	public void delete(Cart cart) {
		cartDAO.delete(cart);
	}

	@Transactional
	@Override
	public Cart findOne(long cartId) {
		return cartDAO.findOne(cartId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Cart> findAll() {
		return cartDAO.findAll();
	}

	@Transactional
	@Override
	public Cart updateCartObject(Cart cart, Long productId, Short quantity) throws UnknownProductException {
		Product product = productDAO.findOne(productId);
		if (product == null)
			throw new UnknownProductException();
		if (product.getStorage().isAvailable())
			cart.update(product, quantity);
		return cart;
	}

	//---------------------------------------- Операции с корзиной пользователя

	@Transactional
	@Override
	public Cart getUserCart(String userLogin) {
		UserAccount account = userAccountDAO.findByEmail(userLogin);
		return findOne(account.getId());
		// todo: handle user doesn't exist
	}

	@Transactional
	@Override
	public Cart clearUserCart(String userLogin) {
		Cart cart = getUserCart(userLogin);
		cart.clear();
		return save(cart);
	}

	@Transactional(rollbackFor = {UnknownProductException.class})
	@Override
	public Cart updateUserCart(String userLogin, Long productId, Short quantity) throws UnknownProductException {
		Cart cart = getUserCart(userLogin);
		cart = updateCartObject(cart, productId, quantity);
		return save(cart);
	}

	@Transactional
	@Override
	public Cart setUserCartDelivery(String userLogin, boolean deliveryIncluded) {
		Cart cart = getUserCart(userLogin);
		cart.setDeliveryIncluded(deliveryIncluded);
		return save(cart);
	}

	@Transactional
	@Override
	public Cart fillUserCart(String userLogin, List<CartItem> itemsToCopy) {
		Cart cart = getUserCart(userLogin);
		for (CartItem item : itemsToCopy) {
			cart.update(item.getProduct(), item.getQuantity());
		}
		return save(cart);
	}
}
