package market.controller.frontend;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import market.data.MarketData;
import market.data.UserData;
import market.dto.CartItemDTO;
import market.util.TestUtil;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
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
 * Тесты корзины: гостя и пользователя.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-security.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
@WebAppConfiguration
public class CartControllerTest {

    private MockMvc mockMvc;
    private MockHttpSession mockUserSession;

    @Resource
    private WebApplicationContext wac;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
        HttpSession session = TestUtil.loginAndReturnSession(mockMvc, UserData.USER_LOGIN, UserData.USER_PASSWORD);
        mockUserSession = (MockHttpSession)session;
    }

    @After
    public void logout() throws Exception {
        TestUtil.logout(mockMvc);
    }
    
    //------------------------------------------------------- Получение корзины

    /**
     * Получение корзины гостя.
     * Успех: корзина возвращена.
     * @throws Exception
     */
    @Test
    public void getSessionCart() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("deliveryCost", is(MarketData.DELIVERY_COST)));
    }

    /**
     * Получение корзины покупателя.
     * Успех: корзина возвращена.
     * @throws Exception
     */
    @Test
    public void getUserCart() throws Exception {
        mockMvc.perform(get("/cart")
                .session(mockUserSession)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("deliveryCost", is(MarketData.DELIVERY_COST)));
    }

    //--------------------------------------------------------- Очистка корзины

    /**
     * Очистка корзины гостя.
     * Успех: очищенная корзина возвращена.
     * @throws Exception
     */
    @Test
    public void clearSessionCart() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        clearCart(mockSession);
    }

    /**
     * Очистка корзины покупателя.
     * Успех: очищенная корзина возвращена.
     * @throws Exception
     */
    @Test
    public void clearUserCart() throws Exception {
        clearCart(mockUserSession);
    }

    private void clearCart(MockHttpSession mockSession) throws Exception {
        mockMvc.perform(delete("/cart").session(mockSession))
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/cart"));
        checkCartEmpty(mockSession);
    }

    private void checkCartEmpty(MockHttpSession mockSession) throws Exception {
        mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("cart", allOf(hasProperty("cartItems", empty()),
                                                            hasProperty("totalItems", is(0)),
                                                            hasProperty("productsCost", is(0)))))
                .andExpect(model().attribute("deliveryCost", is(MarketData.DELIVERY_COST)));
    }

    //---------------------------------------- Добавление в корзину через форму

    /**
     * Добавление товара в корзину гостя: через форму. Товар с неизвестным id.
     * Успех: отсутствие товара обнаружено, корзина не изменилась.
     * @throws Exception
     */
    @Test
    public void updateSessionCartByForm_UnknownProduct() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        clearCart(mockSession);
        updateCartByForm_UnknownProduct(mockSession);
    }
    
    /**
     * Добавление товара в корзину покупателя: через форму. Товар с неизвестным id.
     * Успех: отсутствие товара обнаружено, корзина не изменилась.
     * @throws Exception
     */
    @Test
    public void updateUserCartByForm_UnknownProduct() throws Exception {
        clearUserCart();
        updateCartByForm_UnknownProduct(mockUserSession);
    }

    private void updateCartByForm_UnknownProduct(MockHttpSession mockSession) throws Exception {
        CartItemDTO item = MarketData.getCartItemDTO();
        item.setProductId(Long.valueOf(100));
        mockMvc.perform(put("/cart")
                .session(mockSession)
                .param("productId", item.getProductId().toString())
                .param("quantity", item.getQuantity().toString())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeHasFieldErrors("cartItem", "id"));
        checkCartEmpty(mockSession);
    }
    
    /**
     * Добавление товара в корзину гостя: через форму.
     * Успех: товар добавлен в корзину.
     * @throws Exception
     */
    @Test
    public void updateSessionCartByForm_Ok() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        clearCart(mockSession);
        updateCartByForm_Ok(mockSession);
    }
    
    /**
     * Добавление товара в корзину покупателя: через форму.
     * Успех: товар добавлен в корзину.
     * @throws Exception
     */
    @Test
    public void updateUserCartByForm_Ok() throws Exception {
        clearUserCart();
        updateCartByForm_Ok(mockUserSession);
    }

    private void updateCartByForm_Ok(MockHttpSession mockSession) throws Exception {
        CartItemDTO item = MarketData.getCartItemDTO();
        mockMvc.perform(put("/cart")
                .session(mockSession)
                .param("productId", item.getProductId().toString())
                .param("quantity", item.getQuantity().toString())
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(model().hasNoErrors());
        checkCartUpdated(mockSession);
    }
    
    private void checkCartUpdated(MockHttpSession mockSession) throws Exception {
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("cart", allOf(hasProperty("cartItems", hasSize(1)),
                                                            hasProperty("totalItems", is(MarketData.PRODUCT_QUANTITY)),
                                                            hasProperty("productsCost", is(productsCost)))))
                .andExpect(model().attribute("deliveryCost", is(MarketData.DELIVERY_COST)));
    }

    //----------------------------------------- Добавление в корзину через AJAX

    /**
     * Добавление товара в корзину гостя: через AJAX. Товар с неизвестным id.
     * Успех: отсутствие товара обнаружено, корзина не изменилась.
     * @throws Exception
     */
    @Test
    public void updateSessionCartByAjax_UnknownProduct() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        clearCart(mockSession);
        updateCartByAjax_UnknownProduct(mockSession);
    }
    
    /**
     * Добавление товара в корзину покупателя: через AJAX. Товар с неизвестным id.
     * Успех: отсутствие товара обнаружено, корзина не изменилась.
     * @throws Exception
     */
    @Test
    public void updateUserCartByAjax_UnknownProduct() throws Exception {
        clearUserCart();
        updateCartByAjax_UnknownProduct(mockUserSession);
    }
    
    private void updateCartByAjax_UnknownProduct(MockHttpSession mockSession) throws Exception {
        CartItemDTO item = MarketData.getCartItemDTO();
        item.setProductId(Long.valueOf(100));
        mockMvc.perform(put("/cart")
                .session(mockSession)
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
        checkCartEmpty(mockSession);
    }
    
    /**
     * Добавление товара в корзину гостя: через AJAX.
     * Успех: товар добавлен в корзину.
     * @throws Exception
     */
    @Test
    public void updateSessionCartByAjax_Ok() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        clearCart(mockSession);
        updateCartByAjax_Ok(mockSession);
    }
    
    /**
     * Добавление товара в корзину покупателя: через AJAX.
     * Успех: товар добавлен в корзину.
     * @throws Exception
     */
    @Test
    public void updateUserCartByAjax_Ok() throws Exception {
        clearUserCart();
        updateCartByAjax_Ok(mockUserSession);
    }
    
    private void updateCartByAjax_Ok(MockHttpSession mockSession) throws Exception {
        CartItemDTO item = MarketData.getCartItemDTO();
        mockMvc.perform(put("/cart")
                .session(mockSession)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)));
        checkCartUpdated(mockSession);
    }

    //------------------------------------------------ Установка опции доставки

    /**
     * Изменение опции доставки: корзина гостя.
     * Успех: значение опции доставки изменяется.
     * @throws Exception
     */
    @Test
    public void setSessionDelivery_TrueFalse() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        setDelivery(mockSession);
    }

    /**
     * Изменение опции доставки: корзина покупателя.
     * Успех: значение опции доставки изменяется.
     * @throws Exception
     */
    @Test
    public void setUserDelivery_TrueFalse() throws Exception {
        setDelivery(mockUserSession);
    }

    private void setDelivery(MockHttpSession mockSession) throws Exception {
        
        // Установка и проверка: убрать доставку
        mockMvc.perform(put("/cart/delivery/false").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryIncluded", is(false)));
        mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cart", hasProperty("deliveryIncluded", is(false))));
        
        // Установка и проверка: включить доставку
        mockMvc.perform(put("/cart/delivery/true").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryIncluded", is(true)));
        mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cart", hasProperty("deliveryIncluded", is(true))));
    }
}
