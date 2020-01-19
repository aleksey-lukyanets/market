package market.service.impl;

import market.dao.DistilleryDAO;
import market.domain.Distillery;
import market.domain.Region;
import market.service.DistilleryService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса винокурни.
 */
@Service
public class DistilleryServiceImpl implements DistilleryService {
	private final DistilleryDAO distilleryDAO;

	public DistilleryServiceImpl(DistilleryDAO distilleryDAO) {
		this.distilleryDAO = distilleryDAO;
	}

	@Transactional
	@Override
	public void save(Distillery distillery) {
		distilleryDAO.save(distillery);
	}

	@Transactional
	@Override
	public void delete(Distillery distillery) {
		if (distillery != null) {
			distilleryDAO.delete(distillery);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Distillery findOne(long distilleryId) {
		return distilleryDAO.findById(distilleryId).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Distillery> findAllOrderById() {
		return distilleryDAO.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@Transactional(readOnly = true)
	@Override
	public List<Distillery> findAllOrderByTitle() {
		return distilleryDAO.findAll(Sort.by(Sort.Direction.ASC, "title"));
	}

	@Transactional(readOnly = true)
	@Override
	public List<Distillery> findByRegionOrderByTitle(Region region) {
		return distilleryDAO.findByRegionOrderByTitleAsc(region);
	}
}
