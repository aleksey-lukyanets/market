package market.dao;

import market.domain.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderedProductDAO extends CrudRepository<OrderedProduct, Long>, JpaRepository<OrderedProduct, Long> {

}
