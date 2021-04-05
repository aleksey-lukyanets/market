package market.service;

import market.FixturesFactory;
import market.dao.UserAccountDAO;
import market.domain.UserAccount;
import market.service.impl.UserAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

	@Mock
	private UserAccountDAO userAccountDAO;

	@Captor
	private ArgumentCaptor<UserAccount> userAccountCaptor;

	private UserAccountService userAccountService;
	private UserAccount userAccount;

	@BeforeEach
	public void setUp() {
		userAccount = FixturesFactory.account().build();
		userAccountService = new UserAccountServiceImpl(userAccountDAO);
	}

	@Test
	public void findByEmail() {
		when(userAccountDAO.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);

		UserAccount retrieved = userAccountService.findByEmail(userAccount.getEmail());

		assertThat(retrieved, equalTo(userAccount));
	}

	@Test
	public void create() {
		when(userAccountDAO.findByEmail(userAccount.getEmail()))
			.thenReturn(null);

		userAccountService.create(userAccount);

		verify(userAccountDAO).save(userAccountCaptor.capture());
		assertThat(userAccountCaptor.getValue(), equalTo(userAccount));
	}
}
