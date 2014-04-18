package market.service.impl;

import market.dao.RegionDAO;
import java.util.List;
import market.service.RegionService;
import market.domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса региона.
 */
@Service
public class RegionServiceImpl implements RegionService {

    private final RegionDAO regionDAO;

    @Autowired
    public RegionServiceImpl(RegionDAO regionDAO) {
        this.regionDAO = regionDAO;
    }

    @Transactional
    @Override
    public void save(Region region) {
        regionDAO.save(region);
    }

    @Transactional
    @Override
    public void delete(Region region) {
        regionDAO.delete(region);
    }

    @Transactional(readOnly = true)
    @Override
    public Region findOne(long regionId) {
        return regionDAO.findOne(regionId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Region> findAllOrderById() {
        return regionDAO.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Region> findAllOrderByName() {
        return regionDAO.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
