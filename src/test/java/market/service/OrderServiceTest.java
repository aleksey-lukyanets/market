package market.service;

import market.dao.OrderDAO;
import market.domain.Bill;
import market.domain.Cart;
import market.domain.Order;
import market.domain.OrderedProduct;
import market.domain.Product;
import market.domain.UserAccount;
import market.exception.EmptyCartException;
import market.exception.UnknownEntityException;
import market.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	private static final long ACCOUNT_ID = 50L;
	private static final String ACCOUNT_EMAIL = "email@domain.com";
	private static final long ORDER_ID = 123L;
	private static final long PRODUCT_ID = 10L;
	private static final String CARD_NUMBER = "1234132412341234";

	@Mock
	private OrderDAO orderDAO;
	@Mock
	private UserAccountService userAccountService;
	@Mock
	private CartService cartService;

	@Captor
	private ArgumentCaptor<Order> orderCaptor;

	private OrderService orderService;
	private UserAccount userAccount;
	private Order order;
	private Cart cart;
	private Product product;

	private static LocalDate toDate(Date date) {
		long sourceTime = date.getTime();
		ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(sourceTime), ZoneId.of("GMT"));
		return zdt.toLocalDate();
	}

	@BeforeEach
	public void setUp() {
		userAccount = new UserAccount.Builder()
			.setId(ACCOUNT_ID)
			.setEmail(ACCOUNT_EMAIL)
			.setPassword("password")
			.setName("Name")
			.setActive(true)
			.build();
		order = new Order.Builder()
			.setId(ORDER_ID)
			.setUserAccount(userAccount)
			.setDateCreated(new Date())
			.build();
		product = new Product.Builder()
			.setId(PRODUCT_ID)
			.setPrice(100.0)
			.build();
		cart = new Cart();
		cart.setId(ACCOUNT_ID);
		cart.setUserAccount(userAccount);

		orderService = new OrderServiceImpl(orderDAO, userAccountService, cartService);
	}

	@Test
	public void getUserOrders() {
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);
		when(orderDAO.findByUserAccountOrderByDateCreatedDesc(userAccount)).thenReturn(Collections.singletonList(order));

		List<Order> retrieved = orderService.getUserOrders(ACCOUNT_EMAIL);

		assertThat(retrieved, contains(order));
	}

	@Test
	public void getUserOrder() throws UnknownEntityException {
		when(orderDAO.findById(ORDER_ID)).thenReturn(Optional.of(order));

		Order retrieved = orderService.getUserOrder(ACCOUNT_EMAIL, ORDER_ID);

		assertThat(retrieved, equalTo(order));
	}

	@Test
	public void createUserOrder() throws EmptyCartException {
		int quantity = 3;
		cart.update(product, quantity);
		int deliveryCost = 300;
		double productsCost = quantity * product.getPrice();
		double totalCost = productsCost + deliveryCost;
		when(cartService.getCartOrCreate(ACCOUNT_EMAIL)).thenReturn(cart);
		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);

		Order createdOrder = orderService.createUserOrder(ACCOUNT_EMAIL, deliveryCost, CARD_NUMBER);

		assertThat(createdOrder.getUserAccount(), equalTo(userAccount));
		assertThat(createdOrder.getProductsCost(), equalTo(productsCost));
		assertThat(createdOrder.isDeliveryIncluded(), equalTo(true));
		assertThat(createdOrder.getDeliveryCost(), equalTo(deliveryCost));
		assertThat(createdOrder.isExecuted(), equalTo(false));
		assertThat(toDate(createdOrder.getDateCreated()), equalTo(LocalDate.now()));

		Bill bill = createdOrder.getBill();
		assertThat(bill.getOrder(), equalTo(createdOrder));
		assertThat(bill.getTotalCost(), equalTo(totalCost));
		assertThat(bill.isPayed(), equalTo(true));
		assertThat(toDate(bill.getDateCreated()), equalTo(LocalDate.now()));
		assertThat(bill.getCcNumber(), equalTo(CARD_NUMBER));

		Set<OrderedProduct> orderedProducts = createdOrder.getOrderedProducts();
		assertThat(orderedProducts, hasSize(1));
		OrderedProduct orderedProduct = orderedProducts.iterator().next();
		assertThat(orderedProduct.getOrder(), equalTo(createdOrder));
		assertThat(orderedProduct.getProduct(), equalTo(product));
		assertThat(orderedProduct.getQuantity(), equalTo(quantity));
	}

	@Test
	public void updateStatus() {
		when(orderDAO.findById(ORDER_ID)).thenReturn(Optional.of(order));

		orderService.updateStatus(ORDER_ID, true);

		verify(orderDAO).save(orderCaptor.capture());
		assertThat(orderCaptor.getValue().isExecuted(), equalTo(true));

		orderService.updateStatus(ORDER_ID, false);

		verify(orderDAO, times(2)).save(orderCaptor.capture());
		assertThat(orderCaptor.getValue().isExecuted(), equalTo(false));
	}
}
