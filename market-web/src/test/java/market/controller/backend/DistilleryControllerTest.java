package market.controller.backend;

import market.FixturesFactory;
import market.domain.Distillery;
import market.domain.Region;
import market.dto.assembler.DistilleryDtoAssembler;
import market.dto.assembler.RegionDtoAssembler;
import market.service.DistilleryService;
import market.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DistilleryController.class)
public class DistilleryControllerTest {
	private final RegionDtoAssembler regionDtoAssembler = new RegionDtoAssembler();
	private final DistilleryDtoAssembler distilleryDtoAssembler = new DistilleryDtoAssembler();

	@MockBean
	private RegionService regionService;
	@MockBean
	private DistilleryService distilleryService;

	@Captor
	private ArgumentCaptor<Distillery> distilleryCaptor;
	@Captor
	private ArgumentCaptor<Long> longCaptor;

	private MockMvc mockMvc;
	private Region region;
	private Distillery distillery;

	@BeforeEach
	public void beforeEach() {
		DistilleryController controller = new DistilleryController(distilleryService, regionService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
			.build();
		region = FixturesFactory.region().build();
		distillery = FixturesFactory.distillery(region).build();
	}

	@Test
	public void allDistilleries() throws Exception {
		List<Distillery> totalDistilleries = Collections.singletonList(distillery);

		given(distilleryService.findAll())
			.willReturn(totalDistilleries);

		mockMvc.perform(get("/admin/distilleries"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/distilleries"))
			.andExpect(model().attribute("distilleries", contains(distilleryDtoAssembler.toDtoArray(totalDistilleries))));
	}

	@Test
	public void newDistillery() throws Exception {
		List<Region> totalRegions = Collections.singletonList(region);

		given(regionService.findAll())
			.willReturn(totalRegions);

		mockMvc.perform(get("/admin/distilleries/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/distilleries/new"))
			.andExpect(model().attributeExists("distillery"))
			.andExpect(model().attribute("regions", contains(regionDtoAssembler.toDtoArray(totalRegions))));
	}

	@Test
	public void postDistillery() throws Exception {
		Distillery distilleryWithoutId = new Distillery.Builder(distillery)
			.setId(null)
			.setRegion(null)
			.build();

		mockMvc.perform(
			post("/admin/distilleries/new")
				.param("title", distillery.getTitle())
				.param("region", distillery.getRegion().getName())
				.param("description", distillery.getDescription()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/distilleries"));

		verify(distilleryService).create(distilleryCaptor.capture(), eq(region.getName()));
		assertThat(distilleryCaptor.getValue(), equalTo(distilleryWithoutId));
	}

	@Test
	public void editDistillery() throws Exception {
		List<Region> totalRegions = Collections.singletonList(region);

		given(regionService.findAll())
			.willReturn(totalRegions);
		given(distilleryService.findById(distillery.getId()))
			.willReturn(distillery);

		mockMvc.perform(get("/admin/distilleries/{id}/edit", distillery.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/distilleries/edit"))
			.andExpect(model().attribute("distillery", distilleryDtoAssembler.toModel(distillery)))
			.andExpect(model().attribute("regions", contains(regionDtoAssembler.toDtoArray(totalRegions))));
	}

	@Test
	public void putDistillery() throws Exception {
		Distillery changedDistillery = new Distillery.Builder(distillery)
			.setId(null)
			.setRegion(null)
			.setTitle(distillery.getTitle() + "_changed")
			.setDescription(distillery.getDescription() + "_changed")
			.build();

		mockMvc.perform(
			post("/admin/distilleries/{id}/edit", distillery.getId())
				.param("title", changedDistillery.getTitle())
				.param("region", distillery.getRegion().getName())
				.param("description", changedDistillery.getDescription()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/distilleries"));

		verify(distilleryService).update(eq(distillery.getId()), distilleryCaptor.capture(), eq(distillery.getRegion().getName()));
		assertThat(distilleryCaptor.getValue(), equalTo(changedDistillery));
	}

	@Test
	public void deleteDistillery() throws Exception {
		mockMvc.perform(post("/admin/distilleries/{id}/delete", distillery.getId()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/distilleries"));

		verify(distilleryService).delete(longCaptor.capture());
		assertThat(longCaptor.getValue(), equalTo(distillery.getId()));
	}
}
