package market.service;

import market.FixturesFactory;
import market.dao.ProductDAO;
import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductDAO productDAO;
	@Mock
	private DistilleryService distilleryService;

	@Captor
	private ArgumentCaptor<Product> productCaptor;
	@Captor
	private ArgumentCaptor<Long> longCaptor;

	private ProductService productService;
	private Product product;
	private Distillery distillery;
	private Region region;
	private PageRequest pageRequest;

	@BeforeEach
	public void setUp() {
		region = FixturesFactory.region().build();
		distillery = FixturesFactory.distillery(region).build();
		product = FixturesFactory.product(distillery).build();
		pageRequest = PageRequest.of(1, 1);

		productService = new ProductServiceImpl(productDAO, distilleryService);
	}

	@Test
	public void findAll() {
		when(productDAO.findAll())
			.thenReturn(Collections.singletonList(product));

		List<Product> retrieved = productService.findAll();

		assertThat(retrieved, contains(product));
	}

	@Test
	public void findAll_Paged() {
		Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
		when(productDAO.findAll(pageRequest))
			.thenReturn(productPage);

		Page<Product> retrieved = productService.findAll(pageRequest);

		assertThat(retrieved, contains(product));
	}

	@Test
	public void findByDistillery() {
		Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
		when(productDAO.findByDistilleryOrderByName(distillery, pageRequest))
			.thenReturn(productPage);

		Page<Product> retrieved = productService.findByDistillery(distillery, pageRequest);

		assertThat(retrieved, contains(product));
	}

	@Test
	public void findByRegion() {
		Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
		when(productDAO.findByRegionOrderByName(region, pageRequest))
			.thenReturn(productPage);

		Page<Product> retrieved = productService.findByRegion(region, pageRequest);

		assertThat(retrieved, contains(product));
	}

	@Test
	public void findByAvailability() {
		boolean available = true;
		Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
		when(productDAO.findByAvailableOrderByName(available, pageRequest))
			.thenReturn(productPage);

		Page<Product> retrieved = productService.findByAvailability(Boolean.toString(available), pageRequest);

		assertThat(retrieved, contains(product));
	}

	@Test
	public void getProduct() {
		when(productDAO.findById(product.getId()))
			.thenReturn(Optional.of(product));

		Product retrieved = productService.getProduct(product.getId());

		assertThat(retrieved, equalTo(product));
	}

	@Test
	public void findOne() {
		Optional<Product> productOptional = Optional.of(product);
		when(productDAO.findById(product.getId()))
			.thenReturn(productOptional);

		Optional<Product> retrieved = productService.findById(product.getId());

		assertThat(retrieved, equalTo(productOptional));
	}

	@Test
	public void create() {
		when(distilleryService.findByTitle(distillery.getTitle()))
			.thenReturn(distillery);

		productService.create(product, distillery.getTitle());

		verify(productDAO).save(productCaptor.capture());
		assertThat(productCaptor.getValue(), equalTo(product));
	}

	@Test
	public void update() {
		Product changedProduct = new Product.Builder(product)
			.setPrice(product.getPrice() + 50)
			.build();
		when(distilleryService.findByTitle(distillery.getTitle()))
			.thenReturn(distillery);
		when(productDAO.findById(product.getId()))
			.thenReturn(Optional.of(product));

		productService.update(product.getId(), changedProduct, distillery.getTitle());

		verify(productDAO).save(productCaptor.capture());
		assertThat(productCaptor.getValue(), equalTo(changedProduct));
	}

	@Test
	public void updateAvailability() {
		boolean updatedAvailability = !product.isAvailable();
		Product expectedProduct = new Product.Builder(product)
			.setAvailable(updatedAvailability)
			.build();
		Map<Boolean, List<Long>> changes = new HashMap<>();
		changes.put(updatedAvailability, Collections.singletonList(product.getId()));
		when(productDAO.findById(product.getId()))
			.thenReturn(Optional.of(product));

		productService.updateAvailability(changes);

		verify(productDAO).save(productCaptor.capture());
		assertThat(productCaptor.getValue(), equalTo(expectedProduct));
	}

	@Test
	public void delete() {
		productService.delete(product.getId());

		verify(productDAO).deleteById(longCaptor.capture());
		assertThat(longCaptor.getValue(), equalTo(product.getId()));
	}
}
