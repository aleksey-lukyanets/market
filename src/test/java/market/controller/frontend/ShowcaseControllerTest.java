package market.controller.frontend;

import javax.annotation.Resource;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты витрины магазина.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
@WebAppConfiguration
public class ShowcaseControllerTest {

    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Получение перечня товаров региона.
     * Успех: перечень товаров возвращён с верной разбивкой на страницы.
     * @throws Exception
     */
    @Test
    public void getRegionProducts() throws Exception {

        mockMvc.perform(get("/regions/{regionId}", 4))
                .andExpect(status().isOk())
                .andExpect(view().name("regions"))
                .andExpect(model().attribute("regions", hasSize(6)))
                .andExpect(model().attribute("selectedRegion", hasProperty("name", is("Islay"))))
                .andExpect(model().attribute("distilleries", hasSize(5)))
                .andExpect(model().attribute("page", hasProperty("totalElements", is(5L))))
                .andExpect(model().attribute("page", hasProperty("totalPages", is(2))))
                .andExpect(model().attribute("page", hasProperty("size", is(3))));
    }

    /**
     * Получение товаров указанной винокурни.
     * Успех: возвращён упорядоченный список товаров винокурни с верной разбивкой на страницы.
     * @throws Exception
     */
    @Test
    public void getRegionProducts_FilteredByDistillery() throws Exception {

        mockMvc.perform(get("/regions/{regionId}?dist={distilleryId}", 4, 1))
                .andExpect(status().isOk())
                .andExpect(view().name("regions"))
                .andExpect(model().attribute("currentDistilleryTitle", is("Ardbeg")))
                .andExpect(model().attribute("page", hasProperty("totalElements", is(2L))))
                .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
                .andExpect(model().attribute("page", hasProperty("size", is(3))))
                .andExpect(model().attribute("page", hasProperty("content", contains(
                                                allOf(
                                                        hasProperty("name", is("Ten")),
                                                        hasProperty("distillery", hasProperty("title", is("Ardbeg"))),
                                                        hasProperty("age", is(10))
                                                ),
                                                allOf(
                                                        hasProperty("name", is("Uigeadail")),
                                                        hasProperty("distillery", hasProperty("title", is("Ardbeg"))),
                                                        hasProperty("age", is(12))
                                                )
                                        ))));
    }

    /**
     * Получение списка товаров, отсортированных по возрасту: страница 1.
     * Успех: возвращён упорядоченный список товаров в указанном порядке,
     * с указанным размером страницы.
     * @throws Exception
     */
    @Test
    public void getRegionProducts_SortedByAgeSize4Page1() throws Exception {

        mockMvc.perform(get("/regions/{regionId}?sort={sortBy}&size={pageSize}", 4, "age", 4))
                .andExpect(status().isOk())
                .andExpect(view().name("regions"))
                .andExpect(model().attribute("page", hasProperty("totalElements", is(5L))))
                .andExpect(model().attribute("page", hasProperty("totalPages", is(2))))
                .andExpect(model().attribute("page", hasProperty("size", is(4))))
                .andExpect(model().attribute("page", hasProperty("content", contains(
                                                allOf(
                                                        hasProperty("name", is("Quarter Cask")),
                                                        hasProperty("distillery", hasProperty("title", is("Laphroaig"))),
                                                        hasProperty("age", is(0))
                                                ),
                                                allOf(
                                                        hasProperty("name", is("Ten")),
                                                        hasProperty("distillery", hasProperty("title", is("Ardbeg"))),
                                                        hasProperty("age", is(10))
                                                ),
                                                allOf(
                                                        hasProperty("name", is("Uigeadail")),
                                                        hasProperty("distillery", hasProperty("title", is("Ardbeg"))),
                                                        hasProperty("age", is(12))
                                                ),
                                                allOf(
                                                        hasProperty("name", is("12 y.o.")),
                                                        hasProperty("distillery", hasProperty("title", is("Caol Ila"))),
                                                        hasProperty("age", is(12))
                                                )
                                        ))));
    }

    /**
     * Получение списка товаров, отсортированных по возрасту: страница 2.
     * Успех: возвращён упорядоченный список товаров в указанном порядке,
     * с указанным размером страницы.
     * @throws Exception
     */
    @Test
    public void getRegionProducts_SortedByAgeSize4Page2() throws Exception {

        mockMvc.perform(get("/regions/{regionId}?sort={sortBy}&size={pageSize}&page={page}", 4, "age", 4, 2))
                .andExpect(status().isOk())
                .andExpect(view().name("regions"))
                .andExpect(model().attribute("page", hasProperty("totalElements", is(5L))))
                .andExpect(model().attribute("page", hasProperty("totalPages", is(2))))
                .andExpect(model().attribute("page", hasProperty("size", is(4))))
                .andExpect(model().attribute("page", hasProperty("content", contains(
                                                allOf(
                                                        hasProperty("name", is("16 y.o.")),
                                                        hasProperty("distillery", hasProperty("title", is("Lagavulin"))),
                                                        hasProperty("age", is(16))
                                                )
                                        ))));
    }

    /**
     * Получение списка товаров, отсортированных по винокурне.
     * Успех: возвращён упорядоченный список товаров в указанном порядке,
     * с указанным размером страницы.
     * @throws Exception
     */
    @Test
    public void getRegionProducts_SortedByDistilleryPageSize5() throws Exception {

        mockMvc.perform(get("/regions/{regionId}?sort={sortBy}&size={pageSize}", 4, "distillery.title", 5))
                .andExpect(status().isOk())
                .andExpect(view().name("regions"))
                .andExpect(model().attribute("page", hasProperty("totalElements", is(5L))))
                .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
                .andExpect(model().attribute("page", hasProperty("size", is(5))))
                .andExpect(model().attribute("page", hasProperty("content", contains(
                                                allOf(
                                                        hasProperty("name", is("Ten")),
                                                        hasProperty("distillery", hasProperty("title", is("Ardbeg"))),
                                                        hasProperty("age", is(10))
                                                ),
                                                allOf(
                                                        hasProperty("name", is("Uigeadail")),
                                                        hasProperty("distillery", hasProperty("title", is("Ardbeg"))),
                                                        hasProperty("age", is(12))
                                                ),
                                                allOf(
                                                        hasProperty("name", is("12 y.o.")),
                                                        hasProperty("distillery", hasProperty("title", is("Caol Ila"))),
                                                        hasProperty("age", is(12))
                                                ),
                                                allOf(
                                                        hasProperty("name", is("16 y.o.")),
                                                        hasProperty("distillery", hasProperty("title", is("Lagavulin"))),
                                                        hasProperty("age", is(16))
                                                ),
                                                allOf(
                                                        hasProperty("name", is("Quarter Cask")),
                                                        hasProperty("distillery", hasProperty("title", is("Laphroaig"))),
                                                        hasProperty("age", is(0))
                                                )
                                        ))));
    }
}
