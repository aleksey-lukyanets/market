package market.service;

import java.util.List;
import market.domain.Region;
import market.domain.Distillery;

/**
 * Сервис винокурни.
 */
public interface DistilleryService {

    void save(Distillery distillery);
    
    void delete(Distillery distillery);

    Distillery findOne(long distilleryId);

    List<Distillery> findByRegionOrderByTitle(Region region);

    List<Distillery> findAllOrderById();

    List<Distillery> findAllOrderByTitle();
}
