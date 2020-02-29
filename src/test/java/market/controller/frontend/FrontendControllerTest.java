package market.controller.frontend;

import market.domain.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты корневых страниц магазина.
 */
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class})
@WebAppConfiguration
public class FrontendControllerTest {

	private MockMvc mockMvc;

	@Resource
	private WebApplicationContext wac;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	/**
	 * Обращение к главной странице магазина.
	 * Успех: возвращён список регионов, текущего региона нет.
	 *
	 * @throws Exception
	 */
	@Test
	public void index_Ok() throws Exception {
		mockMvc.perform(get("/index"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(model().attribute("selectedRegion", is(Region.NULL)))
			.andExpect(model().attribute("regions", hasSize(6)));
	}
}
