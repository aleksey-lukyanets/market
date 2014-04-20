package market.controller.frontend;

import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import market.data.UserData;
import market.util.TestUtil;
import static org.hamcrest.Matchers.*;
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
 * Тесты покупателя.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-context.xml",
    "file:src/main/webapp/WEB-INF/spring/marketServlet/servlet-security.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
@WebAppConfiguration
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext wac;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    //----------------------------------------- Регистрация нового пользователя

    /**
     * Получение формы регистрации.
     * Успех: корзина возвращена.
     * @throws Exception
     */
    @Test
    public void getSignup_FormReceived() throws Exception {
        mockMvc.perform(get("/customer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/new"))
                .andExpect(model().attribute("userDTO", notNullValue()));
    }

    /**
     * Отправка формы регистрации. Пустые поля данных.
     * Успех: обнаружены пустые поля.
     * @throws Exception
     */
    @Test
    public void postSignup_EmptyValuesFailure() throws Exception {
        mockMvc.perform(post("/customer/new")
                .param("email", "")
                .param("password", "")
                .param("name", "")
                .param("phone", "")
                .param("address", "")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("customer/new"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "email"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "password"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "name"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "phone"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "address"));
    }

    /**
     * Отправка формы регистрации. Нарушение паттернов строк.
     * Успех: обнаружено нарушение паттернов.
     * @throws Exception
     */
    @Test
    public void postSignup_PatternFailure() throws Exception {
        mockMvc.perform(post("/customer/new")
                .param("email", "some@mail")
                .param("password", "pas#1")
                .param("name", "@#$%^&")
                .param("phone", "123 45 67 89")
                .param("address", "@#$%^&")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("customer/new"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "email"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "password"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "name"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "phone"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "address"));
    }

    /**
     * Отправка формы регистрации. Отправка уже существующего логина.
     * Успех: обнаружено нарушение.
     * @throws Exception
     */
    @Test
    public void postSignup_DuplicatedEmailFailure() throws Exception {
        mockMvc.perform(post("/customer/new")
                .param("email", UserData.USER_LOGIN)
                .param("password", UserData.USER_PASSWORD)
                .param("name", UserData.USER_NAME)
                .param("phone", UserData.USER_PHONE)
                .param("address", UserData.USER_ADDRESS)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("customer/new"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "email"));
    }

    /**
     * Отправка формы регистрации.
     * Успех: пользователь зарегистрирован, редирект на коревую страницу.
     * @throws Exception
     */
    @Test
    public void postSignup_Ok() throws Exception {
        String randomEmail = "tester" + new Random().nextInt(999999999) + "@ya.ru";
        mockMvc.perform(post("/customer/new")
                .param("email", randomEmail)
                .param("password", UserData.USER_PASSWORD)
                .param("name", UserData.USER_NAME)
                .param("phone", UserData.USER_PHONE)
                .param("address", UserData.USER_ADDRESS)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/"))
                .andExpect(model().hasNoErrors());
    }

    //--------------------------------------------- Обращение к истории заказов

    /**
     * Запрос истории заказов. Пользователь не авторизован.
     * Успех: редирект на страницу авторизации.
     * @throws Exception
     */
    @Test
    public void getUserOrders_Unauthorized() throws Exception {
        mockMvc.perform(get("/customer/orders"))
                .andExpect(status().isMovedTemporarily())
                .andExpect(header().string("Location", containsString("login")));
    }

    /**
     * Запрос истории заказов.
     * Успех: возвращён список заказов.
     * @throws Exception
     */
    @Test
    public void getUserOrders_Ok() throws Exception {
        HttpSession session = TestUtil.loginAndReturnSession(mockMvc, UserData.USER_LOGIN, UserData.USER_PASSWORD);
        mockMvc.perform(get("/customer/orders")
                .session((MockHttpSession) session)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("customer/orders"))
                .andExpect(model().attribute("userOrders", notNullValue()))
                .andExpect(model().attribute("orderedProductsMap", notNullValue()));
        TestUtil.logout(mockMvc);
    }
}
