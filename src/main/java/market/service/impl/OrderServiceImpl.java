package market.service.impl;

import market.dao.OrderDAO;
import market.domain.*;
import market.exception.EmptyCartException;
import market.exception.UnknownEntityException;
import market.service.CartService;
import market.service.OrderService;
import market.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
	private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

	private final OrderDAO orderDAO;
	private final UserAccountService userAccountService;
	private final CartService cartService;

	public OrderServiceImpl(OrderDAO orderDAO, UserAccountService userAccountService, CartService cartService) {
		this.orderDAO = orderDAO;
		this.userAccountService = userAccountService;
		this.cartService = cartService;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Order> getUserOrders(String userLogin) {
		UserAccount account = userAccountService.findByEmail(userLogin);
		return orderDAO.findByUserAccountOrderByDateCreatedDesc(account);
	}

	@Transactional(readOnly = true)
	@Override
	public Order getUserOrder(String userLogin, long id) throws UnknownEntityException {
		// todo: add user check
		Order order = orderDAO.findById(id).orElse(null);
		if ((order == null) || !order.getUserAccount().getEmail().equals(userLogin))
			throw new UnknownEntityException(Order.class, id);
		return order;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Order> fetchFiltered(String executed, String orderAgeInDays, PageRequest request) {
		Page<Order> pagedList;
		Date dateCreated = new Date();
		if (!orderAgeInDays.equals("all")) {
			int days = Integer.parseInt(orderAgeInDays);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.HOUR_OF_DAY, -(days * 24));
			dateCreated = c.getTime();
		}
		if (!executed.equals("all") && !orderAgeInDays.equals("all")) {
			boolean executedState = Boolean.parseBoolean(executed);
			pagedList = orderDAO.findByExecutedAndDateCreatedGreaterThan(executedState, dateCreated, request);
		} else if (!executed.equals("all")) {
			boolean executedState = Boolean.parseBoolean(executed);
			pagedList = orderDAO.findByExecuted(executedState, request);
		} else if (!orderAgeInDays.equals("all")) {
			pagedList = orderDAO.findByDateCreatedGreaterThan(dateCreated, request);
		} else {
			pagedList = orderDAO.findAll(request);
		}
		return pagedList;
	}

	@Transactional
	@Override
	public Order createUserOrder(String userLogin, int deliveryCost, String cardNumber) throws EmptyCartException {
		Cart cart = cartService.getCartOrCreate(userLogin);
		if (cart.isEmpty())
			throw new EmptyCartException();

		Order order = createNewOrder(userLogin, cart, deliveryCost);
		Bill bill = createBill(order, cardNumber);
		order.setBill(bill);
		orderDAO.saveAndFlush(order);

		fillOrderItems(cart, order);
		orderDAO.save(order);
		cartService.clearCart(userLogin);

		return order;
	}

	@Override
	public void updateStatus(long orderId, boolean executed) {
		Order order = orderDAO.findById(orderId).orElse(null);
		if (order != null) {
			order.setExecuted(executed);
			orderDAO.save(order);
		}
	}

	private Order createNewOrder(String userLogin, Cart cart, int deliveryCost) {
		Order order = new Order();
		if (cart.isDeliveryIncluded()) {
			order.setDeliveryIncluded(true);
			order.setDeliveryСost(deliveryCost);
		} else {
			order.setDeliveryIncluded(false);
			order.setDeliveryСost(0);
		}
		order.setUserAccount(userAccountService.findByEmail(userLogin));
		order.setProductsCost(cart.getItemsCost());
		order.setDateCreated(new Date());
		order.setExecuted(false);
		return order;
	}

	private Bill createBill(Order order, String cardNumber) {
		Bill bill = new Bill();
		bill.setOrder(order);
		bill.setNumber(new Random().nextInt(999999999));
		bill.setTotalCost(order.getProductsCost() + order.getDeliveryСost());
		bill.setPayed(true);
		bill.setDateCreated(new Date());
		bill.setCcNumber(cardNumber);
		return bill;
	}

	private void fillOrderItems(Cart cart, Order order) {
		Set<OrderedProduct> ordered = new HashSet<>();
		for (CartItem item : cart.getCartItems()) {
			OrderedProduct orderedProduct = new OrderedProduct();
			Product product = item.getProduct();
			orderedProduct.setProduct(product);
			orderedProduct.setOrder(order);
			orderedProduct.setQuantity(item.getQuantity());
			ordered.add(orderedProduct);
		}
		order.setOrderedProducts(ordered);
	}
}
