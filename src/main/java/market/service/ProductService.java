package market.service;

import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.exception.UnknownEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

	/**
	 * @return all the products, sorted by title
	 */
	List<Product> findAll();

	/**
	 * @return all the products, sorted by title and paged
	 */
	Page<Product> findAll(PageRequest request);

	/**
	 * @return all the products of the specified distillery, sorted by title
	 */
	Page<Product> findByDistillery(Distillery distillery, Pageable pageable);

	/**
	 * @return all the products of the specified region, sorted by title
	 */
	Page<Product> findByRegion(Region region, Pageable pageable);

	/**
	 * @return all the available products, sorted by title
	 */
	Page<Product> findByAvailability(String available, PageRequest request);

	/**
	 * @return product with the specified id
	 * @throws UnknownEntityException if product does not exist
	 */
	Product getProduct(long productId) throws UnknownEntityException;

	/**
	 * @return product with the specified id
	 */
	Optional<Product> findOne(long productId);

	/**
	 * Creates new product.
	 */
	void create(Product product, String distilleryTitle);

	/**
	 * Updates existing product.
	 * @throws UnknownEntityException if product does not exist
	 */
	void update(Product product, String distilleryTitle) throws UnknownEntityException;

	/**
	 * Updates availability of the specified product.
	 */
	void updateAvailability(Map<Boolean, List<Long>> productIdsByAvailability);

	/**
	 * Removes distillery.
	 */
	void delete(long product);
}
