package market.service;

import market.domain.Distillery;
import market.domain.Region;

import java.util.List;

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
