package market.rest;

import javax.annotation.Resource;
import market.data.MarketData;
import market.data.UserData;
import market.dto.CartItemDTO;
import market.dto.CreditCardDTO;
import market.util.TestUtil;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Тесты корзины покупателя.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-security.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartRestControllerTest {

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
    
    //----------------------------------------------------- Операции с корзиной
    
    /**
     * Неавторизованное обращение к корзине.
     * Успех: в доступе отказано.
     * @throws Exception
     */
    @Test
    public void getCart_Unauthorized() throws Exception {
        
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
    public void clearCart_Ok() throws Exception {
        
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
    public void updateCart_UnknownProduct() throws Exception {
        
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
    public void updateClearedCart_Ok() throws Exception {
        
        CartItemDTO item = MarketData.getCartItemDTO();
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        
        clearCart_Ok();
        
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
    public void setDeliveryFalseForUpdatedCart_Ok() throws Exception {
        
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        
        updateClearedCart_Ok();
        
        mockMvc.perform(put("/rest/cart/delivery/false")
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
    public void setDeliveryTrueForUpdatedCart_Ok() throws Exception {
        
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        int totalCost = productsCost + MarketData.DELIVERY_COST;
        
        updateClearedCart_Ok();
        
        mockMvc.perform(put("/rest/cart/delivery/true")
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
    public void getPreparedUserCart_Ok() throws Exception {
        
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        int totalCost = productsCost + MarketData.DELIVERY_COST;
        
        setDeliveryTrueForUpdatedCart_Ok();
        
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
    
    //----------------------------------------------------------- Оплата заказа
    
    /**
     * Неавторизованное обращение к оплате.
     * Успех: в доступе отказано.
     * @throws Exception
     */
    @Test
    public void payByCard_Unauthorized() throws Exception {
        
        CreditCardDTO card = new CreditCardDTO(UserData.CREDIT_CARD_NUMBER);
        
        setDeliveryTrueForUpdatedCart_Ok();
        
        mockMvc.perform(post("/rest/cart/payment")
                .header("Authorization", UserData.WRONG_BASIC_AUTH_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card))
        )
                .andExpect(status().isUnauthorized());
    }
    
    /**
     * Оплата заказа. Корзина пуста.
     * Успех: получение сообщения о пустой корзине.
     * @throws Exception
     */
    @Test
    public void payByCard_EmptyCart() throws Exception {
        
        CreditCardDTO card = new CreditCardDTO(UserData.CREDIT_CARD_NUMBER);
        
        clearCart_Ok();
        
        // Попытка оплаты пустой корзины
        mockMvc.perform(post("/rest/cart/payment")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card))
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field", is("items")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is(
                        "Невозможно оформить заказ: корзина пуста."
                )));
    }
    
    /**
     * Оплата заказа. Нарушение паттерна номера карты.
     * Успех: получение сообщения о нарушении паттерна.
     * @throws Exception
     */
    @Test
    public void payByCard_NumberValidationFailure() throws Exception {
        
        CreditCardDTO card = new CreditCardDTO("1111 abcd 3333 4444");
        mockMvc.perform(post("/rest/cart/payment")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card))
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field", is("number")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is(
                        "Номер карты должен состоять из 13-16 цифр."
                )));
    }
    
    /**
     * Оплата заказа.
     * Успех: получение созданного заказа.
     * @throws Exception
     */
    @Test
    public void payByCard_Ok() throws Exception {
        
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        int totalCost = productsCost + MarketData.DELIVERY_COST;
        CreditCardDTO card = new CreditCardDTO(UserData.CREDIT_CARD_NUMBER);
        
        setDeliveryTrueForUpdatedCart_Ok();
        
        // Собственно оплата заказа
        mockMvc.perform(post("/rest/cart/payment")
                .header("Authorization", UserData.BASIC_AUTH_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card))
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/customer/orders/")))
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.user", is(UserData.USER_LOGIN)))
                .andExpect(jsonPath("$.billNumber", any(Integer.class)))
                .andExpect(jsonPath("$.productsCost", is(productsCost)))
                .andExpect(jsonPath("$.deliveryCost", is(MarketData.DELIVERY_COST)))
                .andExpect(jsonPath("$.deliveryIncluded", is(true)))
                .andExpect(jsonPath("$.totalCost", is(totalCost)))
                .andExpect(jsonPath("$.payed", is(true)))
                .andExpect(jsonPath("$.executed", is(false)));
    }
}
