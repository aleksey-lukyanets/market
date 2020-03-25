package market.domain;

import market.util.FixturesFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class CartTest {
	private static final long PRODUCT_ID = 10L;

	private Cart cart;
	private Product product;

	@BeforeEach
	public void setUp() {
		cart = new Cart();
		product = new Product.Builder()
			.setId(PRODUCT_ID)
			.setPrice(100.0)
			.build();
	}

	@Test
	public void update_AddProduct_NullProduct() {
		cart.update(null, 0);

		assertThat(cart.isEmpty(), equalTo(true));
		assertThat(cart.getItemsCount(), equalTo(0));
		assertThat(cart.getItemsCost(), equalTo(0.0));
	}

	@Test
	public void update_AddProduct_EmptyCart() {
		int quantityToSet = 3;

		cart.update(product, quantityToSet);

		assertThat(cart.isEmpty(), equalTo(false));
		assertThat(cart.getItemsCount(), equalTo(1));
		assertThat(cart.getItemsCost(), equalTo(quantityToSet * product.getPrice()));
		List<CartItem> items = cart.getCartItems();
		assertThat(items, hasSize(1));
		assertCartItem(items.get(0), product, quantityToSet);
	}

	@Test
	public void update_AddProduct_OverSameProduct() {
		int quantityToSet = 3;
		cart.update(product, 5);

		cart.update(product, quantityToSet);

		assertThat(cart.isEmpty(), equalTo(false));
		assertThat(cart.getItemsCount(), equalTo(1));
		assertThat(cart.getItemsCost(), equalTo(quantityToSet * product.getPrice()));
		List<CartItem> items = cart.getCartItems();
		assertThat(items, hasSize(1));
		assertCartItem(items.get(0), product, quantityToSet);
	}

	@Test
	public void update_AddProduct_SameProductSameQuantity() {
		int quantityToSet = 3;
		cart.update(product, quantityToSet);

		cart.update(product, quantityToSet);

		assertThat(cart.isEmpty(), equalTo(false));
		assertThat(cart.getItemsCount(), equalTo(1));
		assertThat(cart.getItemsCost(), equalTo(quantityToSet * product.getPrice()));
		List<CartItem> items = cart.getCartItems();
		assertThat(items, hasSize(1));
		assertCartItem(items.get(0), product, quantityToSet);
	}

	@Test
	public void update_AddProduct_OverAnotherProduct() {
		int quantityProduct1 = 3;
		int quantityProduct2 = 7;
		Product product2 = new Product.Builder()
			.setId(PRODUCT_ID + 1)
			.setPrice(63.0)
			.build();
		cart.update(product, quantityProduct1);

		cart.update(product2, quantityProduct2);

		assertThat(cart.isEmpty(), equalTo(false));
		assertThat(cart.getItemsCount(), equalTo(2));
		assertThat(cart.getItemsCost(), equalTo(quantityProduct1 * product.getPrice() + quantityProduct2 * product2.getPrice()));
		List<CartItem> items = cart.getCartItems();
		assertThat(items, hasSize(2));
		assertCartItem(items.get(0), product, quantityProduct1);
		assertCartItem(items.get(1), product2, quantityProduct2);
	}

	@Test
	public void update_DropProduct_EmptyCart() {
		cart.update(product, 0);

		assertThat(cart.isEmpty(), equalTo(true));
		assertThat(cart.getItemsCount(), equalTo(0));
		assertThat(cart.getItemsCost(), equalTo(0.0));
	}

	@Test
	public void update_DropProduct_OverSameProduct() {
		cart.update(product, 5);

		cart.update(product, 0);

		assertThat(cart.isEmpty(), equalTo(true));
		assertThat(cart.getItemsCount(), equalTo(0));
		assertThat(cart.getItemsCost(), equalTo(0.0));
	}

	@Test
	public void update_DropProduct_OverAnotherProduct() {
		int quantityProduct1 = 3;
		Product product2 = new Product.Builder()
			.setId(PRODUCT_ID + 1)
			.setPrice(63.0)
			.build();
		cart.update(product, quantityProduct1);

		cart.update(product2, 0);

		assertThat(cart.isEmpty(), equalTo(false));
		assertThat(cart.getItemsCount(), equalTo(1));
		assertThat(cart.getItemsCost(), equalTo(quantityProduct1 * product.getPrice()));
		List<CartItem> items = cart.getCartItems();
		assertThat(items, hasSize(1));
		assertCartItem(items.get(0), product, quantityProduct1);
	}

	private static void assertCartItem(CartItem actualItem, Product expectedProduct, int expectedQuantity) {
		assertThat(actualItem.getProduct(), equalTo(expectedProduct));
		assertThat(actualItem.getQuantity(), equalTo(expectedQuantity));
	}
}
