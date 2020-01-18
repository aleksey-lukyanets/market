package market.service.impl;

import market.dao.OrderDAO;
import market.dao.UserAccountDAO;
import market.domain.*;
import market.exception.EmptyCartException;
import market.exception.OrderNotFoundException;
import market.service.CartService;
import market.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Реализация сервиса заказа.
 */
public class OrderServiceImpl implements OrderService {
	private final OrderDAO orderDAO;
	private final UserAccountDAO userAccountDAO;
	private final CartService cartService;

	public OrderServiceImpl(OrderDAO orderDAO, UserAccountDAO userAccountDAO, CartService cartService) {
		this.orderDAO = orderDAO;
		this.userAccountDAO = userAccountDAO;
		this.cartService = cartService;
	}

	@Transactional
	@Override
	public void save(Order order) {
		orderDAO.save(order);
	}

	@Transactional
	@Override
	public void delete(Order order) {
		orderDAO.delete(order);
	}

	@Transactional(readOnly = true)
	@Override
	public Order findOne(long orderId) {
		return orderDAO.findOne(orderId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Order> findAll() {
		return orderDAO.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Order> findAll(PageRequest request) {
		return orderDAO.findAll(request);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Order> findByUserAccount(UserAccount userAccount) {
		return orderDAO.findByUserAccountOrderByDateCreatedDesc(userAccount);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Order> findByExecuted(boolean stored, Pageable pageable) {
		return orderDAO.findByExecuted(stored, pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Order> findByExecutedAndDateCreatedGreaterThan(boolean executed, Date created, Pageable pageable) {
		return orderDAO.findByExecutedAndDateCreatedGreaterThan(executed, created, pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Order> findByDateCreatedGreaterThan(Date created, Pageable pageable) {
		return orderDAO.findByDateCreatedGreaterThan(created, pageable);
	}

	//---------------------------------------- Операции с заказами пользователя

	@Transactional(readOnly = true)
	@Override
	public Page<Order> fetchFilteredAndPaged(String executed, String created, PageRequest request) {
		Page<Order> pagedList;
		Date dateCreated = new Date();
		if (!created.equals("all")) {
			int days = Integer.valueOf(created);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.HOUR_OF_DAY, -(days * 24));
			dateCreated = c.getTime();
		}
		if (!executed.equals("all") && !created.equals("all")) {
			boolean executedState = Boolean.valueOf(executed);
			pagedList = findByExecutedAndDateCreatedGreaterThan(executedState, dateCreated, request);
		} else if (!executed.equals("all")) {
			boolean executedState = Boolean.valueOf(executed);
			pagedList = findByExecuted(executedState, request);
		} else if (!created.equals("all")) {
			pagedList = findByDateCreatedGreaterThan(dateCreated, request);
		} else {
			pagedList = findAll(request);
		}
		return pagedList;
	}

	@Transactional
	@Override
	public Order createUserOrder(String userLogin, int deliveryCost, String cardNumber) throws EmptyCartException {
		Cart cart = cartService.getUserCart(userLogin);
		if (cart.isEmpty())
			throw new EmptyCartException();

		Order order = createNewOrder(userLogin, cart, deliveryCost);
		Bill bill = createBill(order, cardNumber);
		order.setBill(bill);
		orderDAO.saveAndFlush(order);

		fillOrderItems(cart, order);
		save(order);
		cartService.clearUserCart(userLogin);

		return order;
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
		order.setUserAccount(userAccountDAO.findByEmail(userLogin));
		order.setProductsCost(cart.getProductsCost());
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

	@Transactional(readOnly = true)
	@Override
	public List<Order> getUserOrders(String userLogin) {
		UserAccount account = userAccountDAO.findByEmail(userLogin);
		// todo: handle account doesn't exist
		return findByUserAccount(account);
	}

	@Transactional(readOnly = true)
	@Override
	public Order getUserOrder(String userLogin, long id) throws OrderNotFoundException {
		// todo: add user check
		Order order = findOne(id);
		if ((order == null) || !order.getUserAccount().getEmail().equals(userLogin))
			throw new OrderNotFoundException("У пользователя " + userLogin + " не существует заказа с id " + id);
		return order;
	}
}
