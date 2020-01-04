package market.service;

import market.domain.Region;

import java.util.List;

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
