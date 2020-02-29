package market.rest;

import market.data.MarketData;
import market.data.UserData;
import market.dto.ContactsDTO;
import market.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.FilterChainProxy;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 */
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class})
@WebAppConfiguration
public class UserRestControllerTest {

	private MockMvc mockMvc;

	@Resource
	private FilterChainProxy springSecurityFilterChain;

	@Resource
	private WebApplicationContext wac;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
			.addFilter(springSecurityFilterChain)
			.build();
	}

	//-------------------------------- Обращение к контактным данным покупателя

	/**
	 * Неавторизованное обращение.
	 * Успех: в доступе отказано.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step1_getContacts_Unauthorized() throws Exception {

		mockMvc.perform(get("/rest/customer/contacts")
			.header("Authorization", UserData.WRONG_BASIC_AUTH_VALUE)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isUnauthorized());
	}

	/**
	 * Получение контактных данных покупателя.
	 * Успех: возвращены текущие контактные данные.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step2_getContacts_Ok() throws Exception {

		mockMvc.perform(get("/rest/customer/contacts")
			.header("Authorization", UserData.BASIC_AUTH_VALUE)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
	}

	/**
	 * Обновление контактных данных покупателя. Нарушение паттернов.
	 * Успех: получены сообщения о нарушении паттернов.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step3_changeContacts_ValidationFailure() throws Exception {

		ContactsDTO contacts = new ContactsDTO(UserData.USER_WRONG_PHONE, "");

		mockMvc.perform(put("/rest/customer/contacts")
			.header("Authorization", UserData.BASIC_AUTH_VALUE)
			.contentType(TestUtil.APPLICATION_JSON_UTF8)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
			.content(TestUtil.convertObjectToJsonBytes(contacts))
		)
			.andExpect(status().isNotAcceptable())
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.fieldErrors", hasSize(2)))
			.andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("phone", "address")))
			.andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
				"Должен состоять из знака +, кода страны, кода региона (1-4 цифр) и номера (6-7 цифр).",
				"Поле не должно быть пустым.")));
	}

	/**
	 * Обновление контактных данных покупателя.
	 * Успех: контактные данные обновлены и возвращены.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step4_changeContacts_Ok() throws Exception {

		ContactsDTO contacts = new ContactsDTO(UserData.USER_PHONE, UserData.USER_ADDRESS);

		mockMvc.perform(put("/rest/customer/contacts")
			.header("Authorization", UserData.BASIC_AUTH_VALUE)
			.contentType(TestUtil.APPLICATION_JSON_UTF8)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
			.content(TestUtil.convertObjectToJsonBytes(contacts))
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.phone", is(UserData.USER_PHONE)))
			.andExpect(jsonPath("$.address", is(UserData.USER_ADDRESS)));

		mockMvc.perform(get("/rest/customer/contacts")
			.header("Authorization", UserData.BASIC_AUTH_VALUE)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.phone", is(UserData.USER_PHONE)))
			.andExpect(jsonPath("$.address", is(UserData.USER_ADDRESS)));
	}

	//--------------------------------------------- Обращение к истории заказов

	/**
	 * Получение истории заказов.
	 * Успех: возвращён перечень заказов пользователя.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step5_getOrders_Ok() throws Exception {

		mockMvc.perform(get("/rest/customer/orders")
			.header("Authorization", UserData.BASIC_AUTH_VALUE)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$[*].user", everyItem(is("ivan.petrov@yandex.ru"))));
	}

	/**
	 * Получение указанного заказа. Неизвестный заказ.
	 * Успех: код 404, ресурс не найден.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step6_getOrder_NotFound() throws Exception {

		mockMvc.perform(get("/rest/customer/orders/{id}", MarketData.IMPROBABLE_ID)
			.header("Authorization", UserData.BASIC_AUTH_VALUE)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isNotFound());
	}
}
