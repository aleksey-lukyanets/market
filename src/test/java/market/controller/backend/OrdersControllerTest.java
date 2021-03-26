package market.controller.backend;

import market.FixturesFactory;
import market.domain.*;
import market.dto.assembler.OrderDtoAssembler;
import market.dto.assembler.OrderedProductDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.properties.PaginationProperties;
import market.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrdersController.class)
public class OrdersControllerTest {
	private final OrderDtoAssembler orderDtoAssembler = new OrderDtoAssembler();
	private final OrderedProductDtoAssembler orderedProductDTOAssembler = new OrderedProductDtoAssembler();
	private final ProductDtoAssembler productDTOAssembler = new ProductDtoAssembler();

	@Autowired
	private PaginationProperties properties;

	@MockBean
	private OrderService orderService;

	@Captor
	private ArgumentCaptor<PageRequest> pageableCaptor;
	@Captor
	private ArgumentCaptor<Boolean> booleanCaptor;
	@Captor
	private ArgumentCaptor<Long> longCaptor;

	private MockMvc mockMvc;

	private Product product;
	private Order order;
	private List<Order> totalOrders;
	private OrderedProduct orderedProduct;

	@BeforeEach
	public void beforeEach() {
		OrdersController controller = new OrdersController(orderService, properties);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
			.build();

		Region region = FixturesFactory.region().build();
		Distillery distillery = FixturesFactory.distillery(region).build();
		product = FixturesFactory.product(distillery).build();

		UserAccount userAccount = FixturesFactory.account().build();
		userAccount.setContacts(FixturesFactory.contacts().build());
		order = FixturesFactory.order(userAccount).build();
		totalOrders = Collections.singletonList(order);

		orderedProduct = FixturesFactory.orderedProduct(order, product).build();
		order.setOrderedProducts(Collections.singleton(orderedProduct));
		Bill bill = FixturesFactory.bill(order).build();
		order.setBill(bill);
	}

	@Test
	public void getOrders() throws Exception {
		PageRequest request = PageRequest.of(0, properties.getBackendOrder(), Sort.by(Sort.Direction.ASC, "dateCreated"));
		Page<Order> page = new PageImpl<>(totalOrders, request, totalOrders.size());
		String executed = "all";
		String created = "all";

		given(orderService.fetchFiltered(eq(executed), eq(created), pageableCaptor.capture()))
			.willReturn(page);

		mockMvc.perform(get("/admin/orders"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/orders"))
			.andExpect(model().attribute("page", orderDtoAssembler.toModel(page)))
			.andExpect(model().attribute("orderedProductsByOrderId", hasEntry(order.getId(), Collections.singletonList(orderedProductDTOAssembler.toModel(orderedProduct)))))
			.andExpect(model().attribute("productsById", hasEntry(product.getId(), productDTOAssembler.toModel(product))))
			.andExpect(model().attribute("currentExecuted", equalTo(executed)))
			.andExpect(model().attribute("currentCreated", equalTo(created)));
		assertThat(pageableCaptor.getValue(), equalTo(request));
	}

	@Test
	public void setExecutionStatus() throws Exception {
		boolean executed = true;

		mockMvc.perform(
			post("/admin/orders/{id}", order.getId())
				.param("executed", Boolean.toString(executed)))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/orders"));

		verify(orderService).updateStatus(longCaptor.capture(), booleanCaptor.capture());
		assertThat(longCaptor.getValue(), equalTo(order.getId()));
		assertThat(booleanCaptor.getValue(), equalTo(executed));
	}
}
