package market.rest;

import market.data.UserData;
import market.dto.UserDTO;
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
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 */
@ContextConfiguration(locations = {"classpath:servlet-test-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class})
@WebAppConfiguration
public class SignupRestControllerTest {

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

	/**
	 * Регистрация покупателя. Пустые поля данных.
	 * Успех: полученя сообщения о нарушении паттернов.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step1_createUser_EmptyValidationFailure() throws Exception {

		UserDTO user = new UserDTO("", "", "", "", "");

		mockMvc.perform(post("/rest/signup")
			.contentType(TestUtil.APPLICATION_JSON_UTF8)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
			.content(TestUtil.convertObjectToJsonBytes(user))
		)
			.andExpect(status().isNotAcceptable())
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.fieldErrors", hasSize(9)))
			.andExpect(jsonPath("$.fieldErrors[*].field", hasItems(
				"email", "email", "password", "password", "name", "name", "phone", "phone", "address"
			)))
			.andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
				"Поле не должно быть пустым.",
				"Значение поля должно иметь формат адреса электронной почты.",
				"Длина должна составлять от 6 до 50 символов.",
				"Пароль должен состоять из цифр и латинских букв.",
				"Поле не должно быть пустым.",
				"В имени допустимы только буквы, пробел, дефис и апостроф.",
				"Поле не должно быть пустым.",
				"Должен состоять из знака +, кода страны, кода региона (1-4 цифр) и номера (6-7 цифр).",
				"Поле не должно быть пустым."
			)));
	}

	/**
	 * Регистрация покупателя. Нарушение паттернов строк.
	 * Успех: полученя сообщения о нарушении паттернов.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step2_createUser_PatternValidationFailure() throws Exception {

		UserDTO user = new UserDTO("some@mail", "pas#1", "@#$%^&", "123 45 67 89", "@#$%^&");

		mockMvc.perform(post("/rest/signup")
			.contentType(TestUtil.APPLICATION_JSON_UTF8)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
			.content(TestUtil.convertObjectToJsonBytes(user))
		)
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.fieldErrors", hasSize(6)))
			.andExpect(jsonPath("$.fieldErrors[*].field", hasItems(
				"email", "password", "password", "name", "phone", "address"
			)))
			.andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
				"Значение поля должно иметь формат адреса электронной почты.",
				"Пароль должен состоять из цифр и латинских букв.",
				"Длина должна составлять от 6 до 50 символов.",
				"В имени допустимы только буквы, пробел, дефис и апостроф.",
				"Должен состоять из знака +, кода страны, кода региона (1-4 цифр) и номера (6-7 цифр).",
				"Специальные символы недопустимы."
			)));
	}

	/**
	 * Регистрация покупателя. Отправка уже существующего логина.
	 * Успех: полученя сообщения о дублировании логина.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step3_createUser_DuplicatedEmail() throws Exception {

		UserDTO user = UserData.getUserDTO();

		mockMvc.perform(post("/rest/signup")
			.contentType(TestUtil.APPLICATION_JSON_UTF8)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
			.content(TestUtil.convertObjectToJsonBytes(user))
		)
			.andExpect(status().isNotAcceptable())
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
			.andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
			.andExpect(jsonPath("$.fieldErrors[0].message", is(
				"Пользователь с таким адресом электронной почты уже существует."
			)));
	}

	/**
	 * Регистрация покупателя.
	 * Успех: новый покупатель создан и возвращён.
	 *
	 * @throws Exception
	 */
	@Test
	public void Step4_createUser_Ok() throws Exception {

		UserDTO user = UserData.getUserDTO();
		String randomEmail = "tester" + new Random().nextInt(999999999) + "@ya.ru";
		user.setEmail(randomEmail);

		mockMvc.perform(post("/rest/signup")
			.contentType(TestUtil.APPLICATION_JSON_UTF8)
			.accept(TestUtil.APPLICATION_JSON_UTF8)
			.content(TestUtil.convertObjectToJsonBytes(user))
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.email", is(randomEmail)))
			.andExpect(jsonPath("$.password", is("hidden")))
			.andExpect(jsonPath("$.name", is(UserData.USER_NAME)))
			.andExpect(jsonPath("$.phone", is(UserData.USER_PHONE)))
			.andExpect(jsonPath("$.address", is(UserData.USER_ADDRESS)));
	}
}
