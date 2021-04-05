package market.dao;

import market.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CartDAO extends CrudRepository<Cart, Long>, JpaRepository<Cart, Long> {

}
