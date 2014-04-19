package market.service;

import java.util.List;
import market.domain.Region;

/**
 * Сервис региона.
 */
public interface RegionService {
    
    void save(Region region);
    
    void delete(Region region);

    Region findOne(long regionId);

    List<Region> findAllOrderById();

    List<Region> findAllOrderByName();
}
