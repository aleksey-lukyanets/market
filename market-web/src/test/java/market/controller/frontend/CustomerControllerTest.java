package market.controller.frontend;

import com.sun.security.auth.UserPrincipal;
import market.FixturesFactory;
import market.domain.Bill;
import market.domain.Cart;
import market.domain.Contacts;
import market.domain.Distillery;
import market.domain.Order;
import market.domain.OrderedProduct;
import market.domain.Product;
import market.domain.Region;
import market.domain.UserAccount;
import market.dto.assembler.OrderDtoAssembler;
import market.dto.assembler.OrderedProductDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.interceptors.SessionCartInterceptor;
import market.properties.MarketProperties;
import market.security.AuthenticationService;
import market.service.CartService;
import market.service.OrderService;
import market.service.ProductService;
import market.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

	@MockBean
	private UserAccountService userAccountService;
	@MockBean
	private AuthenticationService authenticationService;
	@MockBean
	private OrderService orderService;
	@MockBean
	private CartService cartService;
	@MockBean
	private ProductService productService;

	@Captor
	private ArgumentCaptor<UserAccount> accountCaptor;

	private final MarketProperties marketProperties = new MarketProperties(400);
	private final OrderDtoAssembler orderDtoAssembler = new OrderDtoAssembler();
	private final OrderedProductDtoAssembler orderedProductDtoAssembler = new OrderedProductDtoAssembler();
	private final ProductDtoAssembler productDtoAssembler = new ProductDtoAssembler();

	private MockMvc mockMvc;
	private Principal principal;

	private UserAccount account;
	private Order order;
	private Product product;
	private OrderedProduct orderedProduct;
	private Cart cart;

	@BeforeEach
	public void beforeEach() {
		CustomerController controller = new CustomerController(userAccountService, orderService, authenticationService, cartService, productService, marketProperties);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.addInterceptors(new SessionCartInterceptor())
			.setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
			.build();

		Contacts contacts = FixturesFactory.contacts().build();
		account = FixturesFactory.account()
			.setContacts(contacts)
			.build();
		principal = new UserPrincipal(account.getEmail());
		Region region = FixturesFactory.region().build();
		Distillery distillery = FixturesFactory.distillery(region).build();
		product = FixturesFactory.product(distillery).build();
		order = FixturesFactory.order(account).build();
		orderedProduct = FixturesFactory.orderedProduct(order, product).build();
		order.setOrderedProducts(Collections.singleton(orderedProduct));
		Bill bill = FixturesFactory.bill(order).build();
		order.setBill(bill);
		cart = new Cart.Builder()
			.setId(account.getId())
			.setUserAccount(account)
			.build();
	}

	@Test
	public void getRegistrationPage() throws Exception {
		mockMvc.perform(get("/customer/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("customer/new"))
			.andExpect(model().attribute("userAccount", notNullValue()));
	}

	@Test
	public void postRegistrationForm() throws Exception {
		UserAccount expectedAccount = new UserAccount.Builder(account)
			.setId(null)
			.build();

		given(userAccountService.create(accountCaptor.capture()))
			.willReturn(account);
		given(authenticationService.authenticate(account.getEmail(), account.getPassword()))
			.willReturn(true);
		given(cartService.addAllToCart(account.getEmail(), Collections.emptyList()))
			.willReturn(cart);

		mockMvc.perform(
			post("/customer/new")
				.param("email", account.getEmail())
				.param("password", account.getPassword())
				.param("name", account.getName())
				.param("phone", account.getContacts().getPhone())
				.param("address", account.getContacts().getAddress()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"));
		assertThat(accountCaptor.getValue(), equalTo(expectedAccount));
	}

	@Test
	public void getUserOrders() throws Exception {
		List<Order> orders = Collections.singletonList(order);

		given(orderService.getUserOrders(account.getEmail()))
			.willReturn(orders);

		mockMvc.perform(
			get("/customer/orders")
				.principal(principal))
			.andExpect(status().isOk())
			.andExpect(view().name("customer/orders"))
			.andExpect(model().attribute("userOrders", contains(orderDtoAssembler.toDtoArray(orders))))
			.andExpect(model().attribute("productsById", hasEntry(product.getId(), productDtoAssembler.toModel(product))))
			.andExpect(model().attribute("orderedProductsByOrderId", hasEntry(order.getId(), Collections.singletonList(orderedProductDtoAssembler.toModel(orderedProduct)))));
	}
}
