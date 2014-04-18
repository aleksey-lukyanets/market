package market.service.impl;

import java.util.List;
import market.dao.DistilleryDAO;
import market.domain.Region;
import market.domain.Distillery;
import market.service.DistilleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса винокурни.
 */
@Service
public class DistilleryServiceImpl implements DistilleryService {

    private final DistilleryDAO distilleryDAO;

    @Autowired
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
        return distilleryDAO.findOne(distilleryId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Distillery> findAllOrderById() {
        return distilleryDAO.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Distillery> findAllOrderByTitle() {
        return distilleryDAO.findAll(new Sort(Sort.Direction.ASC, "title"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Distillery> findByRegionOrderByTitle(Region region) {
        return distilleryDAO.findByRegionOrderByTitleAsc(region);
    }
}
