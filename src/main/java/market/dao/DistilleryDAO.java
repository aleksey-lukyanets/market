package market.dao;

import java.util.List;
import market.domain.Region;
import market.domain.Distillery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * ДАО винокурни. 
 */
public interface DistilleryDAO extends CrudRepository<Distillery, Long>, JpaRepository<Distillery, Long>
{
    List<Distillery> findByRegionOrderByTitleAsc(Region region);
}
