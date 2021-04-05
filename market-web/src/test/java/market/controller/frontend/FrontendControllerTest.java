package market.controller.frontend;

import market.FixturesFactory;
import market.domain.Region;
import market.dto.assembler.RegionDtoAssembler;
import market.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FrontendController.class)
public class FrontendControllerTest {
	private final RegionDtoAssembler regionDtoAssembler = new RegionDtoAssembler();

	@MockBean
	private RegionService regionService;

	private MockMvc mockMvc;
	private Region region;

	@BeforeEach
	public void beforeEach() {
		FrontendController controller = new FrontendController(regionService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
			.build();
		region = FixturesFactory.region().build();
	}

	@Test
	public void index() throws Exception {
		given(regionService.findAll())
			.willReturn(Collections.singletonList(region));

		mockMvc.perform(get("http://localhost"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(model().attribute("regions", contains(regionDtoAssembler.toModel(region))))
			.andExpect(model().attribute("selectedRegion", equalTo(Region.NULL)));

		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"));

		mockMvc.perform(get("/index"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"));
	}

	@Test
	public void login() throws Exception {
		mockMvc.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"));
	}
}
