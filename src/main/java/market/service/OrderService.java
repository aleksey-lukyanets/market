package market.service;

import market.domain.Order;
import market.exception.EmptyCartException;
import market.exception.UnknownEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderService {

	/**
	 * @return all the orders of the specified user
	 */
	List<Order> getUserOrders(String userLogin);

	/**
	 * @return order of the specified user and id
	 * @throws UnknownEntityException if the requested order does not exist
	 */
	Order getUserOrder(String userLogin, long id) throws UnknownEntityException;

	/**
	 * @return orders filtered according to the passed parameters
	 */
	Page<Order> fetchFiltered(String executed, String created, PageRequest request);

	/**
	 * Creates new order for the specified user.
	 * @return newly created order
	 * @throws EmptyCartException if the specified user cart is empty
	 */
	Order createUserOrder(String userLogin, int deliveryCost, String cardNumber) throws EmptyCartException;

	/**
	 * Updates a state of the order with the specified id
	 */
	void updateStatus(long orderId, boolean executed);
}
