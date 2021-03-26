package market.controller.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = BackendController.class)
public class BackendControllerTest {

	private MockMvc mockMvc;

	@BeforeEach
	public void beforeEach() {
		BackendController controller = new BackendController();
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
			.build();
	}

	@Test
	public void index() throws Exception {
		mockMvc.perform(get("/admin"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/index"));

		mockMvc.perform(get("/admin/"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/index"));

		mockMvc.perform(get("/admin/index"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/index"));
	}
}
