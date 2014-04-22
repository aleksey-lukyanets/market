package market.rest;

import javax.annotation.Resource;
import market.data.MarketData;
import market.util.TestUtil;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Тесты ассортимента магазина.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-security.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@WebAppConfiguration
public class ProductsRestControllerTest {

    private MockMvc mockMvc;
    
    @Resource
    private FilterChainProxy springSecurityFilterChain;
    
    @Resource
    private WebApplicationContext wac;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }
    
    /**
     * Получение всех товаров.
     * Успех: получен перечень всех товаров.
     * @throws Exception
     */
    @Test
    public void getAllProducts_Ok() throws Exception {
        
        mockMvc.perform(get("/rest/products")
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(11)));
    }
    
    /**
     * Получение сведений об одном товаре.
     * Успех: получены сведения о товаре.
     * @throws Exception
     */
    @Test
    public void getProduct_Ok() throws Exception {
        mockMvc.perform(get("/rest/products/{id}", MarketData.PRODUCT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.productId", is(MarketData.PRODUCT_ID)))
                .andExpect(jsonPath("$.distillery", is(MarketData.PRODUCT_DISTILLERY)))
                .andExpect(jsonPath("$.name", is(MarketData.PRODUCT_NAME)));
    }
    
    /**
     * Получение сведений об одном товаре. Несуществующий товар.
     * Успех: код 404, запрошен неизвестный ресурс.
     * @throws Exception
     */
    @Test
    public void getProduct_NotFound() throws Exception {
        
        mockMvc.perform(get("/rest/products/{id}", MarketData.IMPROBABLE_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isNotFound());
    }
}
