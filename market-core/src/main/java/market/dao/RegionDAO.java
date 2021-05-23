package market.dao;

import market.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegionDAO extends CrudRepository<Region, Long>, JpaRepository<Region, Long> {

	Optional<Region> findByName(String regionName);

}
