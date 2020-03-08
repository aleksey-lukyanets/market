package market.service;

import market.domain.Region;

import java.util.List;

public interface RegionService {

	/**
	 * @return all the existing regions sorted by region name
	 */
	List<Region> findAll();

	/**
	 * @return region with the specified id
	 */
	Region findOne(long regionId);

	/**
	 * @return region with the specified name
	 */
	Region findByName(String regionName);

	/**
	 * Creates new region.
	 */
	void create(Region newRegion);

	/**
	 * Updates existing region.
	 */
	void update(long regionId, Region changedRegion);

	/**
	 * Removes region.
	 */
	void delete(long regionId);

}
