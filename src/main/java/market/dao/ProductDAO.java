package market.dao;

import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductDAO extends CrudRepository<Product, Long>, JpaRepository<Product, Long> {

	Page<Product> findByDistilleryOrderByName(Distillery distillery, Pageable request);

	@Query(value = "SELECT p FROM Product p WHERE p.distillery IN (SELECT d FROM Distillery d WHERE d.region = :region) order by p.name")
	Page<Product> findByRegionOrderByName(@Param("region") Region region, Pageable request);

	Page<Product> findByAvailableOrderByName(boolean available, Pageable request);
}
