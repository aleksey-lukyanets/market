package market.rest;

import javax.annotation.Resource;
import market.data.MarketData;
import market.data.UserData;
import market.domain.dto.CartItemDTO;
import market.domain.dto.CreditCardDTO;
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
 * Тесты оплаты заказа.
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
public class PaymentWSTest {

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
     * Неавторизованное обращение к оплате.
     * Успех: в доступе отказано.
     * @throws Exception
     */
    @Test
    public void Step1_payByCard_Unauthorized() throws Exception {
        
        CreditCardDTO card = new CreditCardDTO(UserData.CREDIT_CARD_NUMBER);
        
        mockMvc.perform(post("/rest/payment/card")
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
    public void Step2_payByCard_EmptyCart() throws Exception {
        
        CreditCardDTO card = new CreditCardDTO(UserData.CREDIT_CARD_NUMBER);
        
        // Очистка корзины
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
        
        // Попытка оплаты пустой корзины
        mockMvc.perform(post("/rest/payment/card")
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
    public void Step3_payByCard_NumberValidationFailure() throws Exception {
        
        CreditCardDTO card = new CreditCardDTO("1111 abcd 3333 4444");
        mockMvc.perform(post("/rest/payment/card")
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
    public void Step4_payByCard_Ok() throws Exception {
        
        CartItemDTO item = MarketData.getCartItemDTO();
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        int totalCost = productsCost + MarketData.DELIVERY_COST;
        
        CreditCardDTO card = new CreditCardDTO("1111 2222 3333 4444");
        
        // Очистка корзины
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
        
        // Добавление товара
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
                .andExpect(jsonPath("$.deliveryCost", is(MarketData.DELIVERY_COST)))
                .andExpect(jsonPath("$.totalCost", is(totalCost)));
        
        // Установка опции доставки
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
        
        // Собственно оплата заказа
        mockMvc.perform(post("/rest/payment/card")
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
