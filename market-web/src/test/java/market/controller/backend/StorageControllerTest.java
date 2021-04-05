package market.controller.backend;

import market.FixturesFactory;
import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.dto.assembler.ProductDtoAssembler;
import market.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StorageController.class)
public class StorageControllerTest {
	private final ProductDtoAssembler productAssembler = new ProductDtoAssembler();

	@MockBean
	private ProductService productService;

	@Captor
	private ArgumentCaptor<PageRequest> pageableCaptor;
	@Captor
	private ArgumentCaptor<String> stringCaptor;
	@Captor
	private ArgumentCaptor<Map<Boolean, List<Long>>> availabilityCaptor;

	private MockMvc mockMvc;
	private Product product1;
	private Product product2;

	@BeforeEach
	public void beforeEach() {
		StorageController controller = new StorageController(productService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
			.build();

		Region region = FixturesFactory.region().build();
		Distillery distillery = FixturesFactory.distillery(region).build();
		product1 = FixturesFactory.product(distillery)
			.setAvailable(true)
			.build();
		product2 = FixturesFactory.product(distillery)
			.setAvailable(false)
			.build();
	}

	@Test
	public void getStorageUnits() throws Exception {
		PageRequest request = PageRequest.of(0, 3);
		List<Product> products = Arrays.asList(product1, product2);
		Page<Product> page = new PageImpl<>(products, request, products.size());

		given(productService.findByAvailability(stringCaptor.capture(), pageableCaptor.capture()))
			.willReturn(page);

		mockMvc.perform(get("/admin/storage"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/storage"))
			.andExpect(model().attribute("page", productAssembler.toModel(page)))
			.andExpect(model().attribute("currentlyAvailable", equalTo(stringCaptor.getValue())));
	}

	@Test
	public void getStorageUnits_Available() throws Exception {
		PageRequest request = PageRequest.of(0, 3);
		List<Product> products = Collections.singletonList(product1);
		Page<Product> page = new PageImpl<>(products, request, products.size());

		given(productService.findByAvailability(stringCaptor.capture(), pageableCaptor.capture()))
			.willReturn(page);

		mockMvc.perform(
			get("/admin/storage")
				.param("available", "true"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/storage"))
			.andExpect(model().attribute("page", productAssembler.toModel(page)))
			.andExpect(model().attribute("currentlyAvailable", equalTo("true")));
		assertThat(stringCaptor.getValue(), equalTo("true"));
	}

	@Test
	public void postStorage() throws Exception {
		mockMvc.perform(
			post("/admin/storage")
				.param("productsIds", "1,2,3,4,5")
				.param("availableProductsIds", "2,4,5"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/storage"));

		verify(productService).updateAvailability(availabilityCaptor.capture());
		assertThat(availabilityCaptor.getValue(), hasEntry(true, Arrays.asList(2L, 4L, 5L)));
		assertThat(availabilityCaptor.getValue(), hasEntry(false, Arrays.asList(1L, 3L)));
	}
}
