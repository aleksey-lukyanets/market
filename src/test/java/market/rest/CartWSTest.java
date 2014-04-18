package market.rest;

import javax.annotation.Resource;
import market.data.MarketData;
import market.data.UserData;
import market.domain.dto.CartItemDTO;
import market.util.TestUtil;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
 * Тесты корзины покупателя.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-security.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartWSTest {

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
     * Неавторизованное обращение к корзине.
     * Успех: в доступе отказано.
     * @throws Exception
     */
    @Test
    public void Step1_getCart_Unauthorized() throws Exception {
        
        mockMvc.perform(get("/rest/cart")
                .header("Authorization", UserData.WRONG_BASIC_AUTH_VALUE)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isUnauthorized());
    }
    
    /**
     * Очистка корзины.
     * Успех: возвращена очищенная корзина.
     * @throws Exception
     */
    @Test
    public void Step2_clearCart_Ok() throws Exception {
        
        mockMvc.perform(delete("/rest/cart")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user", is(UserData.USER_LOGIN)))
                .andExpect(jsonPath("$.items", empty()))
                .andExpect(jsonPath("$.productsCost", is(0)))
                .andExpect(jsonPath("$.deliveryCost", is(MarketData.DELIVERY_COST)))
                .andExpect(jsonPath("$.totalCost", is(0)));
    }
    
    /**
     * Добавление товара в корзину. Неизвестный товар.
     * Успех: возвращено сообщение о неизвестном товаре.
     * @throws Exception
     */
    @Test
    public void Step4_updateCart_UnknownProduct() throws Exception {
        
        CartItemDTO item = MarketData.getCartItemDTO();
        item.setProductId(Long.valueOf(100));
        
        mockMvc.perform(put("/rest/cart")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item))
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field", is("id")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is(
                        "Запрошенный товар не существует."
                )));
    }
    
    /**
     * Добавление товара в корзину.
     * Успех: товар добавлен, возвращена обновлённая корзина.
     * @throws Exception
     */
    @Test
    public void Step3_updateCart_Ok() throws Exception {
        
        CartItemDTO item = MarketData.getCartItemDTO();
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        
        Step2_clearCart_Ok();
        
        mockMvc.perform(put("/rest/cart")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user", is(UserData.USER_LOGIN)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.productsCost", is(productsCost)))
                .andExpect(jsonPath("$.deliveryCost", is(MarketData.DELIVERY_COST)));
    }
    
    /**
     * Изменение опции доставки: исключение.
     * Успех: доставка исключена, возвращена обновлённая корзина.
     * @throws Exception
     */
    @Test
    public void Step5_setDeliveryFalse() throws Exception {
        
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        
        mockMvc.perform(post("/rest/cart/delivery/false")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user", is(UserData.USER_LOGIN)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.productsCost", is(productsCost)))
                .andExpect(jsonPath("$.deliveryCost", is(MarketData.DELIVERY_COST)))
                .andExpect(jsonPath("$.deliveryIncluded", is(false)))
                .andExpect(jsonPath("$.totalCost", is(productsCost)));
    }
    
    /**
     * Изменение опции доставки: добавление.
     * Успех: доставка добавлена, возвращена обновлённая корзина.
     * @throws Exception
     */
    @Test
    public void Step6_setDeliveryTrue() throws Exception {
        
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        int totalCost = productsCost + MarketData.DELIVERY_COST;
        
        mockMvc.perform(post("/rest/cart/delivery/true")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user", is(UserData.USER_LOGIN)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.productsCost", is(productsCost)))
                .andExpect(jsonPath("$.deliveryCost", is(MarketData.DELIVERY_COST)))
                .andExpect(jsonPath("$.deliveryIncluded", is(true)))
                .andExpect(jsonPath("$.totalCost", is(totalCost)));
    }
    
    /**
     * Получение корзины.
     * Успех: корректная корзина возвращена.
     * @throws Exception
     */
    @Test
    public void Step7_getUserCart_Ok() throws Exception {
        
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        int totalCost = productsCost + MarketData.DELIVERY_COST;
        
        mockMvc.perform(get("/rest/cart")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user", is(UserData.USER_LOGIN)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.productsCost", is(productsCost)))
                .andExpect(jsonPath("$.deliveryCost", is(MarketData.DELIVERY_COST)))
                .andExpect(jsonPath("$.deliveryIncluded", is(true)))
                .andExpect(jsonPath("$.totalCost", is(totalCost)));
    }
}
