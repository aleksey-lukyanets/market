package market.service;

import market.FixturesFactory;
import market.dao.OrderDAO;
import market.domain.*;
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
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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
		userAccount = FixturesFactory.account().build();
		order = FixturesFactory.order(userAccount).build();
		Region region = FixturesFactory.region().build();
		Distillery distillery = FixturesFactory.distillery(region).build();
		product = FixturesFactory.product(distillery).build();
		cart = new Cart.Builder()
			.setId(userAccount.getId())
			.setUserAccount(userAccount)
			.build();
		orderService = new OrderServiceImpl(orderDAO, userAccountService, cartService);
	}

	@Test
	public void getUserOrders() {
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);
		when(orderDAO.findByUserAccountOrderByDateCreatedDesc(userAccount))
			.thenReturn(Collections.singletonList(order));

		List<Order> retrieved = orderService.getUserOrders(userAccount.getEmail());

		assertThat(retrieved, contains(order));
	}

	@Test
	public void getUserOrder() throws UnknownEntityException {
		when(orderDAO.findById(order.getId()))
			.thenReturn(Optional.of(order));

		Order retrieved = orderService.getUserOrder(userAccount.getEmail(), order.getId());

		assertThat(retrieved, equalTo(order));
	}

	@Test
	public void createUserOrder() throws EmptyCartException {
		int quantity = 3;
		cart.update(product, quantity);
		int deliveryCost = 300;
		double productsCost = quantity * product.getPrice();
		double totalCost = productsCost + deliveryCost;
		when(cartService.getCartOrCreate(userAccount.getEmail()))
			.thenReturn(cart);
		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);

		Order createdOrder = orderService.createUserOrder(userAccount.getEmail(), deliveryCost, CARD_NUMBER);

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
		when(orderDAO.findById(order.getId()))
			.thenReturn(Optional.of(order));

		orderService.updateStatus(order.getId(), true);

		verify(orderDAO).save(orderCaptor.capture());
		assertThat(orderCaptor.getValue().isExecuted(), equalTo(true));

		orderService.updateStatus(order.getId(), false);

		verify(orderDAO, times(2)).save(orderCaptor.capture());
		assertThat(orderCaptor.getValue().isExecuted(), equalTo(false));
	}
}
