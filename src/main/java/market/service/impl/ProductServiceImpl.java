package market.service.impl;

import market.dao.ProductDAO;
import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.exception.UnknownEntityException;
import market.service.DistilleryService;
import market.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductDAO productDAO;
	private final DistilleryService distilleryService;

	public ProductServiceImpl(ProductDAO productDAO, DistilleryService distilleryService) {
		this.productDAO = productDAO;
		this.distilleryService = distilleryService;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Product> findAll() {
		return productDAO.findAll().stream()
			.sorted(Comparator.comparing(Product::getName))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Product> findAll(PageRequest request) {
		return productDAO.findAll(request);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Product> findByDistillery(Distillery distillery, PageRequest request) {
		return productDAO.findByDistilleryOrderByName(distillery, request);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Product> findByRegion(Region region, PageRequest request) {
		return productDAO.findByRegionOrderByName(region, request);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Product> findByAvailability(String available, PageRequest request) {
		Page<Product> pagedList;
		if ("all".equals(available)) {
			pagedList = productDAO.findAll(request);
		} else {
			boolean availability = Boolean.parseBoolean(available);
			pagedList = productDAO.findByAvailableOrderByName(availability, request);
		}
		return pagedList;
	}

	@Transactional(readOnly = true)
	@Override
	public Product getProduct(long productId) {
		return productDAO.findById(productId)
			.orElseThrow(() -> new UnknownEntityException(Product.class, productId));
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Product> findById(long productId) {
		return productDAO.findById(productId);
	}

	@Transactional
	@Override
	public void create(Product product, String distilleryTitle) {
		saveInternal(product, distilleryTitle, true);
	}

	@Transactional
	@Override
	public void update(long productId, Product product, String distilleryTitle) {
		Product original = getProduct(productId);
		product.setId(original.getId());
		saveInternal(product, distilleryTitle, original.isAvailable()); // keep original availability
	}

	private void saveInternal(Product changed, String distilleryTitle, boolean available) {
		Distillery distillery = distilleryService.findByTitle(distilleryTitle);
		if (distillery != null) {
			changed.setDistillery(distillery);
			changed.setAvailable(available);
			productDAO.save(changed);
		}
	}

	@Override
	public void updateAvailability(Map<Boolean, List<Long>> productIdsByAvailability) {
		for (Map.Entry<Boolean, List<Long>> e : productIdsByAvailability.entrySet()) {
			Boolean targetAvailability = e.getKey();
			List<Product> productsToUpdate = e.getValue().stream()
				.map(this::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(product -> product.isAvailable() != targetAvailability)
				.collect(Collectors.toList());
			for (Product product : productsToUpdate) {
				product.setAvailable(targetAvailability);
				productDAO.save(product);
			}
		}
	}

	@Transactional
	@Override
	public void delete(long product) {
		productDAO.deleteById(product);
	}
}
