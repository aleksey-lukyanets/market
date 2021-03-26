package market.controller.frontend;

import market.FixturesFactory;
import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.dto.assembler.DistilleryDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.dto.assembler.RegionDtoAssembler;
import market.service.DistilleryService;
import market.service.ProductService;
import market.service.RegionService;
import market.sorting.AbstractSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShowcaseController.class)
public class ShowcaseControllerTest {
	private final RegionDtoAssembler regionDtoAssembler = new RegionDtoAssembler();
	private final ProductDtoAssembler productAssembler = new ProductDtoAssembler();
	private final DistilleryDtoAssembler distilleryDtoAssembler = new DistilleryDtoAssembler();

	@MockBean
	private RegionService regionService;
	@MockBean
	private ProductService productService;
	@MockBean
	private DistilleryService distilleryService;

	@Captor
	private ArgumentCaptor<PageRequest> pageableCaptor;

	private MockMvc mockMvc;

	private Region region1;
	private List<Region> totalRegions;

	private Distillery distillery1;
	private List<Distillery> distilleriesOfRegion1;

	private Product product11;
	private Product product12;
	private Product product13;
	private Product product14;
	private Product product21;
	private List<Product> productsRegion1;

	@BeforeEach
	public void beforeEach() {
		ShowcaseController controller = new ShowcaseController(regionService, productService, distilleryService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
			.build();

		region1 = FixturesFactory.region().build();
		Region region2 = FixturesFactory.region().build();
		totalRegions = Arrays.asList(region1, region2);

		distillery1 = FixturesFactory.distillery(region1).build();
		Distillery distillery2 = FixturesFactory.distillery(region1).build();
		distilleriesOfRegion1 = Arrays.asList(distillery1, distillery2);

		product11 = FixturesFactory.product(distillery1).build();
		product12 = FixturesFactory.product(distillery1).build();
		product13 = FixturesFactory.product(distillery1).build();
		product14 = FixturesFactory.product(distillery1).build();
		product21 = FixturesFactory.product(distillery2).build();
		productsRegion1 = Arrays.asList(product11, product12, product13, product14, product21);

		given(regionService.findOne(region1.getId()))
			.willReturn(region1);
		given(regionService.findAll())
			.willReturn(totalRegions);
		given(distilleryService.findByRegion(any(Region.class)))
			.willReturn(distilleriesOfRegion1);
	}

	@Test
	public void getRegionProducts() throws Exception {
		PageRequest request = PageRequest.of(0, AbstractSorter.PAGE_SIZE_DEFAULT, Sort.by(Sort.Direction.ASC, "price"));
		Page<Product> page = new PageImpl<>(
			Arrays.asList(product11, product12, product13),
			request,
			productsRegion1.size());

		given(productService.findByRegion(any(Region.class), pageableCaptor.capture()))
			.willReturn(page);

		mockMvc.perform(get("/regions/{regionId}", region1.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("regions"))
			.andExpect(model().attribute("selectedRegion", equalTo(regionDtoAssembler.toModel(region1))))
			.andExpect(model().attribute("regions", contains(regionDtoAssembler.toDtoArray(totalRegions))))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(distilleriesOfRegion1))))
			.andExpect(model().attribute("page", productAssembler.toModel(page)));
		assertThat(pageableCaptor.getValue(), equalTo(request));
	}

	@Test
	public void getRegionProducts_FilteredByDistillery() throws Exception {
		PageRequest request = PageRequest.of(0, AbstractSorter.PAGE_SIZE_DEFAULT, Sort.by(Sort.Direction.ASC, "price"));
		Page<Product> page = new PageImpl<>(
			Arrays.asList(product11, product12, product13),
			request,
			productsRegion1.size());

		given(productService.findByDistillery(any(Distillery.class), pageableCaptor.capture()))
			.willReturn(page);
		given(distilleryService.findById(eq(distillery1.getId())))
			.willReturn(distillery1);

		mockMvc.perform(
			get("/regions/{regionId}", region1.getId())
				.param("dist", distillery1.getId().toString()))
			.andExpect(status().isOk())
			.andExpect(view().name("regions"))
			.andExpect(model().attribute("currentDistilleryTitle", is(distillery1.getTitle())))
			.andExpect(model().attribute("selectedRegion", equalTo(regionDtoAssembler.toModel(region1))))
			.andExpect(model().attribute("regions", contains(regionDtoAssembler.toDtoArray(totalRegions))))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(distilleriesOfRegion1))))
			.andExpect(model().attribute("page", productAssembler.toModel(page)));
		assertThat(pageableCaptor.getValue(), equalTo(request));
	}

	@Test
	public void getRegionProducts_SortedByAge_PageSize4() throws Exception {
		String sortBy = "age";
		PageRequest request = PageRequest.of(0, 4, Sort.by(Sort.Direction.ASC, sortBy));
		Page<Product> page = new PageImpl<>(
			Arrays.asList(product11, product12, product13, product14),
			request,
			productsRegion1.size());

		given(productService.findByRegion(any(Region.class), pageableCaptor.capture()))
			.willReturn(page);

		mockMvc.perform(
			get("/regions/{regionId}", region1.getId())
				.param("sort", sortBy)
				.param("size", Integer.toString(request.getPageSize())))
			.andExpect(status().isOk())
			.andExpect(view().name("regions"))
			.andExpect(model().attribute("selectedRegion", equalTo(regionDtoAssembler.toModel(region1))))
			.andExpect(model().attribute("regions", contains(regionDtoAssembler.toDtoArray(totalRegions))))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(distilleriesOfRegion1))))
			.andExpect(model().attribute("page", productAssembler.toModel(page)));
		assertThat(pageableCaptor.getValue(), equalTo(request));
	}

	@Test
	public void getRegionProducts_SortedByAge_PageSize4_Page2() throws Exception {
		String sortBy = "age";
		PageRequest request = PageRequest.of(1, 4, Sort.by(Sort.Direction.ASC, sortBy));
		Page<Product> page = new PageImpl<>(
			Collections.singletonList(product21),
			request,
			productsRegion1.size());

		given(productService.findByRegion(any(Region.class), pageableCaptor.capture()))
			.willReturn(page);

		mockMvc.perform(
			get("/regions/{regionId}", region1.getId())
				.param("sort", sortBy)
				.param("size", Integer.toString(request.getPageSize()))
				.param("page", Integer.toString(request.getPageNumber() + 1))) // todo: introduce page base number
			.andExpect(status().isOk())
			.andExpect(view().name("regions"))
			.andExpect(model().attribute("selectedRegion", equalTo(regionDtoAssembler.toModel(region1))))
			.andExpect(model().attribute("regions", contains(regionDtoAssembler.toDtoArray(totalRegions))))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(distilleriesOfRegion1))))
			.andExpect(model().attribute("page", productAssembler.toModel(page)));
		assertThat(pageableCaptor.getValue(), equalTo(request));
	}

	@Test
	public void getRegionProducts_SortedByDistillery_AllProductsOnPage() throws Exception {
		String sortBy = "distillery.title";
		PageRequest request = PageRequest.of(0, productsRegion1.size(), Sort.by(Sort.Direction.ASC, sortBy));
		Page<Product> page = new PageImpl<>(
			Arrays.asList(product11, product12, product13, product14, product21),
			request,
			productsRegion1.size());

		given(productService.findByRegion(any(Region.class), pageableCaptor.capture()))
			.willReturn(page);

		mockMvc.perform(
			get("/regions/{regionId}", region1.getId())
				.param("sort", sortBy)
				.param("size", Integer.toString(request.getPageSize())))
			.andExpect(status().isOk())
			.andExpect(view().name("regions"))
			.andExpect(model().attribute("selectedRegion", equalTo(regionDtoAssembler.toModel(region1))))
			.andExpect(model().attribute("regions", contains(regionDtoAssembler.toDtoArray(totalRegions))))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(distilleriesOfRegion1))))
			.andExpect(model().attribute("page", productAssembler.toModel(page)));
		assertThat(pageableCaptor.getValue(), equalTo(request));
	}
}
