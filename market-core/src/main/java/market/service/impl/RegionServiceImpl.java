package market.service.impl;

import market.dao.RegionDAO;
import market.domain.Region;
import market.service.RegionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionServiceImpl implements RegionService {
	private final RegionDAO regionDAO;

	public RegionServiceImpl(RegionDAO regionDAO) {
		this.regionDAO = regionDAO;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Region> findAll() {
		return regionDAO.findAll().stream()
			.sorted(Comparator.comparing(Region::getName))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public Region findOne(long regionId) {
		return regionDAO.findById(regionId).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public Region findByName(String regionName) {
		return regionDAO.findByName(regionName).orElse(null);
	}

	@Transactional
	@Override
	public void create(Region newRegion) {
		regionDAO.save(newRegion);
	}

	@Override
	public void update(long regionId, Region changedRegion) {
		Optional<Region> originalOptional = regionDAO.findById(regionId);
		if (originalOptional.isPresent()) {
			changedRegion.setId(originalOptional.get().getId());
			regionDAO.save(changedRegion);
		}
	}

	@Transactional
	@Override
	public void delete(long regionId) {
		regionDAO.deleteById(regionId);
	}
}
