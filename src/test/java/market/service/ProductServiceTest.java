package market.service;

import market.dao.ProductDAO;
import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.exception.UnknownEntityException;
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
	private static final long PRODUCT_ID = 10L;
	private static final double PRODUCT_PRICE = 100.0;
	private static final String REGION_NAME = "region_name";
	private static final long DISTILLERY_ID = 234L;
	private static final String DISTILLERY_TITLE = "distillery_title";
	private static final String DISTILLERY_DESCRIPTION = "distillery_description";

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
		product = new Product.Builder()
			.setId(PRODUCT_ID)
			.setPrice(PRODUCT_PRICE)
			.build();
		region = new Region.Builder()
			.setId(123L)
			.setName(REGION_NAME)
			.build();
		distillery = new Distillery.Builder()
			.setId(DISTILLERY_ID)
			.setRegion(region)
			.setTitle(DISTILLERY_TITLE)
			.setDescription(DISTILLERY_DESCRIPTION)
			.build();
		pageRequest = PageRequest.of(1, 1);

		productService = new ProductServiceImpl(productDAO, distilleryService);
	}

	@Test
	public void findAll() {
		when(productDAO.findAll()).thenReturn(Collections.singletonList(product));

		List<Product> retrieved = productService.findAll();

		assertThat(retrieved, contains(product));
	}

	@Test
	public void findAll_Paged() {
		Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
		when(productDAO.findAll(pageRequest)).thenReturn(productPage);

		Page<Product> retrieved = productService.findAll(pageRequest);

		assertThat(retrieved, contains(product));
	}

	@Test
	public void findByDistillery() {
		Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
		when(productDAO.findByDistilleryOrderByName(distillery, pageRequest)).thenReturn(productPage);

		Page<Product> retrieved = productService.findByDistillery(distillery, pageRequest);

		assertThat(retrieved, contains(product));
	}

	@Test
	public void findByRegion() {
		Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
		when(productDAO.findByRegionOrderByName(region, pageRequest)).thenReturn(productPage);

		Page<Product> retrieved = productService.findByRegion(region, pageRequest);

		assertThat(retrieved, contains(product));
	}

	@Test
	public void findByAvailability() {
		boolean available = true;
		Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
		when(productDAO.findByAvailableOrderByName(available, pageRequest)).thenReturn(productPage);

		Page<Product> retrieved = productService.findByAvailability(Boolean.toString(available), pageRequest);

		assertThat(retrieved, contains(product));
	}

	@Test
	public void getProduct() throws UnknownEntityException {
		when(productDAO.findById(product.getId())).thenReturn(Optional.of(product));

		Product retrieved = productService.getProduct(product.getId());

		assertThat(retrieved, equalTo(product));
	}

	@Test
	public void findOne() {
		Optional<Product> productOptional = Optional.of(product);
		when(productDAO.findById(product.getId())).thenReturn(productOptional);

		Optional<Product> retrieved = productService.findOne(product.getId());

		assertThat(retrieved, equalTo(productOptional));
	}

	@Test
	public void create() {
		when(distilleryService.findByTitle(DISTILLERY_TITLE)).thenReturn(distillery);

		productService.create(product, DISTILLERY_TITLE);

		verify(productDAO).save(productCaptor.capture());
		assertThat(productCaptor.getValue(), equalTo(product));
	}

	@Test
	public void update() throws UnknownEntityException {
		Product changedProduct = new Product.Builder()
			.setId(product.getId())
			.setDistillery(distillery)
			.setPrice(PRODUCT_PRICE + 50)
			.build();
		when(distilleryService.findByTitle(DISTILLERY_TITLE)).thenReturn(distillery);
		when(productDAO.findById(product.getId())).thenReturn(Optional.of(product));

		productService.update(changedProduct, DISTILLERY_TITLE);

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
		when(productDAO.findById(product.getId())).thenReturn(Optional.of(product));

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
