package market.service;

import market.FixturesFactory;
import market.dao.CartDAO;
import market.domain.Cart;
import market.domain.CartItem;
import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
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
		UserAccount.Builder accountBuilder = FixturesFactory.account(cart);
		cart = new Cart.Builder()
			.setId(accountBuilder.getId())
			.build();
		Region region = FixturesFactory.region().build();
		Distillery distillery = FixturesFactory.distillery(region).build();
		product = FixturesFactory.product(distillery).build();
		userAccount = accountBuilder.build();
		cart.setUserAccount(userAccount);

		cartService = new CartServiceImpl(cartDAO, userAccountService, productService);
	}

	@Test
	public void getCartOrCreate_ExistingCart() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));

		Cart createdCart = cartService.getCartOrCreate(userAccount.getEmail());

		verify(cartDAO, never()).save(any(Cart.class));
		assertThat(createdCart, equalTo(cart));
	}

	@Test
	public void getCartOrCreate_AbsentCart() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class)))
			.thenReturn(cart);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.empty());

		Cart createdCart = cartService.getCartOrCreate(userAccount.getEmail());

		verify(cartDAO).save(any(Cart.class));
		assertThat(createdCart, equalTo(cart));
	}

	@Test
	public void addToCart_Normal() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class)))
			.thenReturn(cart);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));
		when(productService.getProduct(product.getId()))
			.thenReturn(product);
		int quantity = 3;
		cart.update(product, quantity);

		Cart updatedCart = cartService.addToCart(userAccount.getEmail(), product.getId(), quantity);

		verify(cartDAO).save(cart);
		assertThat(updatedCart.getItemsCount(), equalTo(1));
		assertThat(updatedCart.getItemsCost(), equalTo(quantity * product.getPrice()));
		List<CartItem> cartItems = updatedCart.getCartItems();
		assertThat(cartItems.size(), equalTo(1));
		assertThat(cartItems.get(0).getProduct(), equalTo(product));
		assertThat(cartItems.get(0).getQuantity(), equalTo(quantity));
	}

	@Test
	public void addToCart_UnavailableProduct() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));
		when(productService.getProduct(product.getId()))
			.thenReturn(product);
		product.setAvailable(false);

		Cart updatedCart = cartService.addToCart(userAccount.getEmail(), product.getId(), 3);

		verify(cartDAO, never()).save(any(Cart.class));
		assertThat(updatedCart.isEmpty(), equalTo(true));
	}

	@Test
	public void addToCart_AbsentProduct() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));
		when(productService.getProduct(product.getId())).thenThrow(UnknownEntityException.class);

		assertThrows(UnknownEntityException.class, () -> cartService.addToCart(userAccount.getEmail(), product.getId(), 3));
		verify(cartDAO, never()).save(any(Cart.class));
	}

	@Test
	public void addAllToCart_Normal() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class)))
			.thenReturn(cart);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));
		when(productService.findById(product.getId()))
			.thenReturn(Optional.of(product));
		int quantity = 3;
		cart.update(product, quantity);
		CartItem cartItem = new CartItem(cart, product, quantity);

		Cart updatedCart = cartService.addAllToCart(userAccount.getEmail(), Collections.singletonList(cartItem));

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
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));
		when(productService.findById(product.getId()))
			.thenReturn(Optional.of(product));
		product.setAvailable(false);
		CartItem cartItem = new CartItem(cart, product, 3);

		Cart updatedCart = cartService.addAllToCart(userAccount.getEmail(), Collections.singletonList(cartItem));

		verify(cartDAO, never()).save(any(Cart.class));
		assertThat(updatedCart.isEmpty(), equalTo(true));
	}

	@Test
	public void addAllToCart_AbsentProduct() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));
		when(productService.findById(product.getId()))
			.thenReturn(Optional.empty());
		CartItem cartItem = new CartItem(cart, product, 3);

		Cart updatedCart = cartService.addAllToCart(userAccount.getEmail(), Collections.singletonList(cartItem));

		verify(cartDAO, never()).save(any(Cart.class));
		assertThat(updatedCart.isEmpty(), equalTo(true));
	}

	@Test
	public void setDelivery() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class)))
			.thenReturn(cart);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));

		Cart updatedCart = cartService.setDelivery(userAccount.getEmail(), true);

		verify(cartDAO, times(1)).save(any(Cart.class));
		assertThat(updatedCart.isDeliveryIncluded(), equalTo(true));

		updatedCart = cartService.setDelivery(userAccount.getEmail(), false);

		verify(cartDAO, times(2)).save(any(Cart.class));
		assertThat(updatedCart.isDeliveryIncluded(), equalTo(false));
	}

	@Test
	public void clearCart_EmptyCart() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class)))
			.thenReturn(cart);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));

		Cart clearedCart = cartService.clearCart(userAccount.getEmail());

		verify(cartDAO).save(cart);
		assertThat(clearedCart.isEmpty(), equalTo(true));
	}

	@Test
	public void clearCart_FullCart() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(cartDAO.save(any(Cart.class)))
			.thenReturn(cart);
		when(cartDAO.findById(userAccount.getId()))
			.thenReturn(Optional.of(cart));
		cart.update(product, 3);

		Cart clearedCart = cartService.clearCart(userAccount.getEmail());

		verify(cartDAO).save(cart);
		assertThat(clearedCart.isEmpty(), equalTo(true));
	}
}
