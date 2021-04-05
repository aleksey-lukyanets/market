package market.dao;

import market.domain.Distillery;
import market.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DistilleryDAO extends CrudRepository<Distillery, Long>, JpaRepository<Distillery, Long> {

	List<Distillery> findByRegionOrderByTitleAsc(Region region);

	Distillery findByTitle(String title);
}
