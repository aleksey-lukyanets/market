package market.dao;

import market.domain.Order;
import market.domain.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderDAO extends CrudRepository<Order, Long>, JpaRepository<Order, Long> {

	List<Order> findByUserAccountOrderByDateCreatedDesc(UserAccount userAccount);

	Page<Order> findByExecuted(boolean stored, Pageable pageable);

	Page<Order> findByDateCreatedGreaterThan(Date created, Pageable pageable);

	Page<Order> findByExecutedAndDateCreatedGreaterThan(boolean executed, Date created, Pageable pageable);
}
