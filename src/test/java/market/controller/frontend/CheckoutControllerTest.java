package market.controller.frontend;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import market.data.MarketData;
import market.data.UserData;
import market.dto.CartItemDTO;
import market.util.TestUtil;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.Assert;
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
 * Тесты процесса оформления заказа покупателем.
 * 
 * При начале тестирования покупатель авторизуется, по окончании - извлекается.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-security.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
@WebAppConfiguration
public class CheckoutControllerTest {

    private MockMvc mockMvc;
    private MockHttpSession mockSession;

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
        mockSession = (MockHttpSession)session;
    }

    @After
    public void logout() throws Exception {
        TestUtil.logout(mockMvc);
    }

    //---------------------------------- Переход к странице изменения контактов

    /**
     * Получение формы изменения контактов. Доставка включена.
     * Успех: возвращена страница изменения контактов.
     * @throws Exception
     */
    @Test
    public void getDetailsPage_DeliveryTrue() throws Exception {
        setDelivery(mockSession, true);
        mockMvc.perform(get("/checkout/details").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/details"));
    }

    /**
     * Получение формы изменения контактов. Доставка не включена.
     * Успех: покупатель переадресован к оплате.
     * @throws Exception
     */
    @Test
    public void getDetailsPage_DeliveryFalse() throws Exception {
        setDelivery(mockSession, false);
        mockMvc.perform(get("/checkout/details").session(mockSession))
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/checkout/payment"));
    }

    private void setDelivery(MockHttpSession mockSession, boolean deliveryIncluded) throws Exception {
        mockMvc.perform(put("/cart/delivery/{inluded}", deliveryIncluded)
                .session(mockSession)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryIncluded", is(deliveryIncluded)));
    }

    //---------------------------------------- Передача новых контактных данных

    /**
     * Отправка изменённых контактов. Пустые поля.
     * Успех: обнаружение пустых полей, контакты неизменны.
     * @throws Exception
     */
    @Test
    public void putDetails_EmptyValuesFailure() throws Exception {
        mockMvc.perform(put("/checkout/details")
                .session(mockSession)
                .param("phone", "")
                .param("address", "")
                .param("infoOption", "useNew")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/details"))
                .andExpect(model().attributeHasFieldErrors("contactsDTO", "phone"))
                .andExpect(model().attributeHasFieldErrors("contactsDTO", "address"));
        checkContactsAreInitial();
    }

    /**
     * Отправка изменённых контактов. Нарушение паттернов строк.
     * Успех: обнаружение нарушения паттернов, контакты неизменны.
     * @throws Exception
     */
    @Test
    public void putDetails_PatternFailure() throws Exception {
        mockMvc.perform(put("/checkout/details")
                .session(mockSession)
                .param("phone", UserData.USER_WRONG_PHONE)
                .param("address", "#$%")
                .param("infoOption", "useNew")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/details"))
                .andExpect(model().attributeHasFieldErrors("contactsDTO", "phone"))
                .andExpect(model().attributeHasFieldErrors("contactsDTO", "address"));
        checkContactsAreInitial();
    }

    private void checkContactsAreInitial() throws Exception {
        setDelivery(mockSession, true);
        mockMvc.perform(get("/checkout/details").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/details"))
                .andExpect(model().attribute("userContacts", allOf(hasProperty("phone", is(UserData.USER_PHONE)),
                                                                hasProperty("address", is(UserData.USER_ADDRESS)))));
    }

    /**
     * Отправка изменённых контактов.
     * Успех: контакты сохранены, редирект к оплате.
     * @throws Exception
     */
    @Test
    public void putDetails_Ok() throws Exception {
        String newPhone = "+7 111 222 33 44";
        String newAddress = "newAddress";
        // Изменение контактов
        mockMvc.perform(put("/checkout/details")
                .session(mockSession)
                .param("phone", newPhone)
                .param("address", newAddress)
                .param("infoOption", "useNew")
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/checkout/payment"));
        // Проверка изменений
        mockMvc.perform(get("/checkout/details").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/details"))
                .andExpect(model().attribute("userContacts", allOf(hasProperty("phone", is(newPhone)),
                                                                hasProperty("address", is(newAddress)))));
        // Запись исходных контактов
        mockMvc.perform(put("/checkout/details")
                .session(mockSession)
                .param("phone", UserData.USER_PHONE)
                .param("address", UserData.USER_ADDRESS)
                .param("infoOption", "useNew")
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/checkout/payment"));
        checkContactsAreInitial();
    }
    
    //----------------------------------------------------------- Оплата заказа

    /**
     * Оплата заказа. Пустая корзина.
     * Успех: обнаружение пустой корзины.
     * @throws Exception
     */
    @Test
    public void postPayment_EmptyCart() throws Exception {
        clearCart(mockSession);
        mockMvc.perform(post("/checkout/payment")
                .session(mockSession)
                .param("number", UserData.CREDIT_CARD_NUMBER)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/payment"))
                .andExpect(model().attributeHasFieldErrors("creditCardDTO", "items"));
    }
    
    private void clearCart(MockHttpSession mockSession) throws Exception {
        mockMvc.perform(delete("/cart")
                .session(mockSession)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/cart"));
    }

    /**
     * Оплата заказа. Нарушения паттерна номера банковской карты.
     * Успех: обнаружение нарушения паттерна.
     * @throws Exception
     */
    @Test
    public void postPayment_CardNumberFailure() throws Exception {
        CartItemDTO item = MarketData.getCartItemDTO();
        updateCart(mockSession, item);
        mockMvc.perform(post("/checkout/payment")
                .session(mockSession)
                .param("number", "abc")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/payment"))
                .andExpect(model().attributeHasFieldErrors("creditCardDTO", "number"));
    }

    private void updateCart(MockHttpSession mockSession, CartItemDTO item) throws Exception {
        mockMvc.perform(put("/cart")
                .session(mockSession)
                .param("productId", item.getProductId().toString())
                .param("quantity", item.getQuantity().toString())
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(model().hasNoErrors());
    }
    
    /**
     * Оплата заказа.
     * Успех: редирект на страницу подтверждения, корректные параметры заказа.
     * @throws Exception
     */
    @Test
    public void postPayment_Ok() throws Exception {
        CartItemDTO item = MarketData.getCartItemDTO();
        int productsCost = MarketData.PRODUCT_QUANTITY * MarketData.PRODUCT_UNIT_COST;
        int totalCost = productsCost + MarketData.DELIVERY_COST;
        
        clearCart(mockSession);
        updateCart(mockSession, item);
        
        // Добавление доставки
        mockMvc.perform(put("/cart/delivery/true").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryIncluded", is(true)));
        // Оплата
        mockMvc.perform(post("/checkout/payment")
                .session(mockSession)
                .param("number", UserData.CREDIT_CARD_NUMBER)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/checkout/confirmation"))
                .andExpect(model().hasNoErrors());
        
        // Страница подтверждения
        mockMvc.perform(get("/checkout/confirmation")
                .session(mockSession)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/confirmation"))
                .andExpect(model().attribute("userAccount", hasProperty("email", is(UserData.USER_LOGIN))))
                .andExpect(model().attribute("userDetails",
                        allOf(
                                hasProperty("phone", is(UserData.USER_PHONE)),
                                hasProperty("address", is(UserData.USER_ADDRESS)))));
        
        // Оформленный заказ в сессии
        Assert.assertThat(mockSession.getAttribute("createdOrder"),
                allOf(
                        hasProperty("user", is(UserData.USER_LOGIN)),
                        hasProperty("productsCost", is(productsCost)),
                        hasProperty("deliveryCost", is(MarketData.DELIVERY_COST)),
                        hasProperty("deliveryIncluded", is(true)),
                        hasProperty("totalCost", is(totalCost)),
                        hasProperty("payed", is(true)),
                        hasProperty("executed", is(false))
                )
        );
    }
}
