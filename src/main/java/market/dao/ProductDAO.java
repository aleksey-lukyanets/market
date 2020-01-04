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

import java.util.List;

/**
 * ДАО товара.
 */
public interface ProductDAO extends CrudRepository<Product, Long>, JpaRepository<Product, Long>
{
    List<Product> findByDistillery(Distillery distillery);
    
    Page<Product> findByDistillery(Distillery distillery, Pageable pageable);
    
    @Query(value = "SELECT p FROM Product p WHERE p.distillery IN "
            + "(SELECT d FROM Distillery d WHERE d.region = :region)")
    public Page<Product> findByDistilleriesOfRegion(@Param("region") Region region, Pageable pageable);
}
