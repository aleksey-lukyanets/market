package market.service;

import market.dao.CartDAO;
import market.domain.Cart;
import market.domain.CartItem;
import market.domain.Product;
import market.domain.UserAccount;
import market.exception.UnknownEntityException;
import market.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
	private static final long ACCOUNT_ID = 50L;
	private static final String ACCOUNT_EMAIL = "email@domain.com";
	private static final long PRODUCT_ID = 10L;

	@Mock
	private CartDAO cartDAO;
	@Mock
	private UserAccountService userAccountService;
	@Mock
	private ProductService productService;

	private CartService cartService;
	private Cart cart;
	private Product product;
	private UserAccount userAccount;

	@BeforeEach
	public void setUp() {
		cart = new Cart();
		cart.setId(ACCOUNT_ID);

		product = new Product.Builder()
			.setId(PRODUCT_ID)
			.setPrice(100.0)
			.build();
		userAccount = new UserAccount.Builder()
			.setId(ACCOUNT_ID)
			.setEmail(ACCOUNT_EMAIL)
			.setPassword("password")
			.setName("Name")
			.setActive(true)
			.setCart(cart)
			.build();
		cart.setUserAccount(userAccount);

		cartService = new CartServiceImpl(cartDAO, userAccountService, productService);
	}

	@Test
	public void getCartOrCreate_ExistingCart() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));

		Cart createdCart = cartService.getCartOrCreate(ACCOUNT_EMAIL);

		verify(cartDAO, never()).save(any(Cart.class));
		assertThat(createdCart, equalTo(cart));
	}

	@Test
	public void getCartOrCreate_AbsentCart() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class))).thenReturn(cart);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

		Cart createdCart = cartService.getCartOrCreate(ACCOUNT_EMAIL);

		verify(cartDAO).save(any(Cart.class));
		assertThat(createdCart, equalTo(cart));
	}

	@Test
	public void addToCart_Normal() throws UnknownEntityException {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class))).thenReturn(cart);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));
		when(productService.getProduct(PRODUCT_ID)).thenReturn(product);
		int quantity = 3;
		cart.update(product, quantity);

		Cart updatedCart = cartService.addToCart(ACCOUNT_EMAIL, product.getId(), quantity);

		verify(cartDAO).save(cart);
		assertThat(updatedCart.getItemsCount(), equalTo(1));
		assertThat(updatedCart.getItemsCost(), equalTo(quantity * product.getPrice()));
		List<CartItem> cartItems = updatedCart.getCartItems();
		assertThat(cartItems.size(), equalTo(1));
		assertThat(cartItems.get(0).getProduct(), equalTo(product));
		assertThat(cartItems.get(0).getQuantity(), equalTo(quantity));
	}

	@Test
	public void addToCart_UnavailableProduct() throws UnknownEntityException {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));
		when(productService.getProduct(PRODUCT_ID)).thenReturn(product);
		product.setAvailable(false);

		Cart updatedCart = cartService.addToCart(ACCOUNT_EMAIL, product.getId(), 3);

		verify(cartDAO, never()).save(any(Cart.class));
		assertThat(updatedCart.isEmpty(), equalTo(true));
	}

	@Test
	public void addToCart_AbsentProduct() throws UnknownEntityException {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));
		when(productService.getProduct(PRODUCT_ID)).thenThrow(UnknownEntityException.class);

		assertThrows(UnknownEntityException.class, () -> cartService.addToCart(ACCOUNT_EMAIL, product.getId(), 3));
		verify(cartDAO, never()).save(any(Cart.class));
	}

	@Test
	public void addAllToCart_Normal() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class))).thenReturn(cart);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));
		when(productService.findOne(PRODUCT_ID)).thenReturn(Optional.of(product));
		int quantity = 3;
		cart.update(product, quantity);
		CartItem cartItem = new CartItem(cart, product, quantity);

		Cart updatedCart = cartService.addAllToCart(ACCOUNT_EMAIL, Collections.singletonList(cartItem));

		verify(cartDAO).save(cart);
		assertThat(updatedCart.getItemsCount(), equalTo(1));
		assertThat(updatedCart.getItemsCost(), equalTo(quantity * product.getPrice()));
		List<CartItem> cartItems = updatedCart.getCartItems();
		assertThat(cartItems.size(), equalTo(1));
		assertThat(cartItems.get(0).getProduct(), equalTo(product));
		assertThat(cartItems.get(0).getQuantity(), equalTo(quantity));
	}

	@Test
	public void addAllToCart_UnavailableProduct() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));
		when(productService.findOne(PRODUCT_ID)).thenReturn(Optional.of(product));
		product.setAvailable(false);
		CartItem cartItem = new CartItem(cart, product, 3);

		Cart updatedCart = cartService.addAllToCart(ACCOUNT_EMAIL, Collections.singletonList(cartItem));

		verify(cartDAO, never()).save(any(Cart.class));
		assertThat(updatedCart.isEmpty(), equalTo(true));
	}

	@Test
	public void addAllToCart_AbsentProduct() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));
		when(productService.findOne(PRODUCT_ID)).thenReturn(Optional.empty());
		CartItem cartItem = new CartItem(cart, product, 3);

		Cart updatedCart = cartService.addAllToCart(ACCOUNT_EMAIL, Collections.singletonList(cartItem));

		verify(cartDAO, never()).save(any(Cart.class));
		assertThat(updatedCart.isEmpty(), equalTo(true));
	}

	@Test
	public void setDelivery() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class))).thenReturn(cart);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));

		Cart updatedCart = cartService.setDelivery(ACCOUNT_EMAIL, true);

		verify(cartDAO, times(1)).save(any(Cart.class));
		assertThat(updatedCart.isDeliveryIncluded(), equalTo(true));

		updatedCart = cartService.setDelivery(ACCOUNT_EMAIL, false);

		verify(cartDAO, times(2)).save(any(Cart.class));
		assertThat(updatedCart.isDeliveryIncluded(), equalTo(false));
	}

	@Test
	public void clearCart_EmptyCart() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class))).thenReturn(cart);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));

		Cart clearedCart = cartService.clearCart(ACCOUNT_EMAIL);

		verify(cartDAO).save(cart);
		assertThat(clearedCart.isEmpty(), equalTo(true));
	}

	@Test
	public void clearCart_FullCart() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class))).thenReturn(cart);
		when(cartDAO.findById(ACCOUNT_ID)).thenReturn(Optional.of(cart));
		cart.update(product, 3);

		Cart clearedCart = cartService.clearCart(ACCOUNT_EMAIL);

		verify(cartDAO).save(cart);
		assertThat(clearedCart.isEmpty(), equalTo(true));
	}
}
