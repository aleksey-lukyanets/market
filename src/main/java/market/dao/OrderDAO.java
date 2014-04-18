package market.dao;

import java.util.Date;
import java.util.List;
import market.domain.Order;
import market.domain.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * ДАО заказа.
 */
public interface OrderDAO extends CrudRepository<Order, Long>, JpaRepository<Order, Long>
{
    List<Order> findByUserAccountOrderByDateCreatedDesc(UserAccount userAccount);

    Page<Order> findByExecuted(boolean stored, Pageable pageable);

    Page<Order> findByDateCreatedGreaterThan(Date created, Pageable pageable);

    Page<Order> findByExecutedAndDateCreatedGreaterThan(boolean executed, Date created, Pageable pageable);
}
