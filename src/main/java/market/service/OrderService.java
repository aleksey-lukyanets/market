package market.service;

import java.util.Date;
import java.util.List;
import market.domain.Order;
import market.domain.UserAccount;
import market.domain.dto.CreditCardDTO;
import market.domain.dto.OrderDTO;
import market.exception.EmptyCartException;
import market.exception.OrderNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Сервис заказа.
 */
public interface OrderService {
    
    Order createUserOrder(CreditCardDTO card, String userLogin, int deliveryCost) throws EmptyCartException;
    
    List<OrderDTO> getUserOrders(String userLogin);
    
    OrderDTO getUserOrder(String userLogin, long id) throws OrderNotFoundException;

    void save(Order order);
    
    void delete(Order order);

    Order findOne(long orderId);

    List<Order> findAll();

    Page<Order> findAll(PageRequest request);

    List<Order> findByUserAccount(UserAccount userAccount);

    Page<Order> findByExecuted(boolean stored, Pageable pageable);

    Page<Order> findByExecutedAndDateCreatedGreaterThan(boolean executed, Date created, Pageable pageable);

    Page<Order> findByDateCreatedGreaterThan(Date created, Pageable pageable);

    Page<Order> fetchFilteredAndPaged(String executed, String created, PageRequest request);
}
