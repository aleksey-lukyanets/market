package market.controller.backend;

import market.FixturesFactory;
import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.dto.assembler.DistilleryDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.properties.PaginationProperties;
import market.service.DistilleryService;
import market.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {
	private final ProductDtoAssembler productDtoAssembler = new ProductDtoAssembler();
	private final DistilleryDtoAssembler distilleryDtoAssembler = new DistilleryDtoAssembler();

	@Autowired
	private PaginationProperties properties;

	@MockBean
	private ProductService productService;
	@MockBean
	private DistilleryService distilleryService;

	@Captor
	private ArgumentCaptor<PageRequest> pageableCaptor;
	@Captor
	private ArgumentCaptor<Product> productCaptor;
	@Captor
	private ArgumentCaptor<Long> longCaptor;

	private MockMvc mockMvc;

	private Distillery distillery;
	private List<Distillery> totalDistilleries;
	private Product product1;
	private Product product2;

	@BeforeEach
	public void beforeEach() {
		ProductController controller = new ProductController(productService, distilleryService, properties);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
			.build();

		Region region = FixturesFactory.region().build();
		distillery = FixturesFactory.distillery(region).build();
		totalDistilleries = Collections.singletonList(distillery);
		product1 = FixturesFactory.product(distillery).build();
		product2 = FixturesFactory.product(distillery).build();
	}

	@Test
	public void allProducts() throws Exception {
		PageRequest request = PageRequest.of(0, properties.getBackendProduct(), Sort.by(Sort.Direction.ASC, "price"));
		List<Product> products = Arrays.asList(product1, product2);
		Page<Product> page = new PageImpl<>(products, request, products.size());

		given(productService.findAll(pageableCaptor.capture()))
			.willReturn(page);
		given(distilleryService.findAll())
			.willReturn(totalDistilleries);

		mockMvc.perform(get("/admin/products"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/products"))
			.andExpect(model().attribute("page", productDtoAssembler.toModel(page)))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(totalDistilleries))));
		assertThat(pageableCaptor.getValue(), equalTo(request));
	}

	@Test
	public void allProducts_byDistillery() throws Exception {
		PageRequest request = PageRequest.of(0, properties.getBackendProduct(), Sort.by(Sort.Direction.ASC, "price"));
		List<Product> products = Arrays.asList(product1, product2);
		Page<Product> page = new PageImpl<>(products, request, products.size());

		given(productService.findByDistillery(eq(distillery), pageableCaptor.capture()))
			.willReturn(page);
		given(distilleryService.findAll())
			.willReturn(totalDistilleries);
		given(distilleryService.findById(distillery.getId()))
			.willReturn(distillery);

		mockMvc.perform(
			get("/admin/products")
				.param("dist", distillery.getId().toString()))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/products"))
			.andExpect(model().attribute("page", productDtoAssembler.toModel(page)))
			.andExpect(model().attribute("currentDistilleryTitle", equalTo(distillery.getTitle())))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(totalDistilleries))));
		assertThat(pageableCaptor.getValue(), equalTo(request));
	}

	@Test
	public void newProduct() throws Exception {
		given(distilleryService.findAll())
			.willReturn(totalDistilleries);

		mockMvc.perform(get("/admin/products/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/products/new"))
			.andExpect(model().attributeExists("product"))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(totalDistilleries))));
	}

	@Test
	public void postProduct() throws Exception {
		Product productWithoutId = new Product.Builder(product1)
			.setId(null)
			.setDistillery(null)
			.build();

		mockMvc.perform(
			post("/admin/products/new")
				.param("distillery", distillery.getTitle())
				.param("name", product1.getName())
				.param("price", product1.getPrice().toString())
				.param("age", product1.getAge().toString())
				.param("volume", product1.getVolume().toString())
				.param("alcohol", product1.getAlcohol().toString())
				.param("description", product1.getDescription())
				.param("available", Boolean.toString(product1.isAvailable())))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/products"));

		verify(productService).create(productCaptor.capture(), eq(distillery.getTitle()));
		assertThat(productCaptor.getValue(), equalTo(productWithoutId));
	}

	@Test
	public void editProduct() throws Exception {
		given(productService.findById(product1.getId()))
			.willReturn(Optional.of(product1));
		given(distilleryService.findAll())
			.willReturn(totalDistilleries);

		mockMvc.perform(get("/admin/products/{id}/edit", product1.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/products/edit"))
			.andExpect(model().attribute("product", productDtoAssembler.toModel(product1)))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(totalDistilleries))));
	}

	@Test
	public void putProduct() throws Exception {
		Product changedProduct = new Product.Builder(product1)
			.setId(null)
			.setDistillery(null)
			.setName(product1.getName() + "_changed")
			.setPrice(product1.getPrice() + 1)
			.setAge(product1.getAge() + 1)
			.setVolume(product1.getVolume() + 1)
			.setAlcohol(product1.getAlcohol() + 1)
			.setDescription(product1.getDescription() + "_changed")
			.setAvailable(!product1.isAvailable())
			.build();

		mockMvc.perform(
			post("/admin/products/{id}/edit", product1.getId())
				.param("distillery", distillery.getTitle())
				.param("name", changedProduct.getName())
				.param("price", changedProduct.getPrice().toString())
				.param("age", changedProduct.getAge().toString())
				.param("volume", changedProduct.getVolume().toString())
				.param("alcohol", changedProduct.getAlcohol().toString())
				.param("description", changedProduct.getDescription())
				.param("available", Boolean.toString(changedProduct.isAvailable())))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/products"));

		verify(productService).update(eq(product1.getId()), productCaptor.capture(), eq(distillery.getTitle()));
		assertThat(productCaptor.getValue(), equalTo(changedProduct));
	}

	@Test
	public void deleteProduct() throws Exception {
		mockMvc.perform(post("/admin/products/{id}/delete", product1.getId()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/products"));

		verify(productService).delete(longCaptor.capture());
		assertThat(longCaptor.getValue(), equalTo(product1.getId()));
	}
}
