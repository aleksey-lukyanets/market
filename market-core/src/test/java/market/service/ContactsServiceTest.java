package market.service;

import market.FixturesFactory;
import market.dao.ContactsDAO;
import market.domain.Contacts;
import market.domain.UserAccount;
import market.service.impl.ContactsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactsServiceTest {

	@Mock
	private ContactsDAO contactsDAO;
	@Mock
	private UserAccountService userAccountService;

	@Captor
	private ArgumentCaptor<Contacts> contactsCaptor;

	private ContactsService contactsService;
	private Contacts contacts;
	private UserAccount userAccount;

	@BeforeEach
	public void setUp() {
		contacts = FixturesFactory.contacts().build();
		userAccount = FixturesFactory.account()
			.setContacts(contacts)
			.build();
		contacts.setUserAccount(userAccount);

		when(userAccountService.findByEmail(userAccount.getEmail()))
			.thenReturn(userAccount);

		contactsService = new ContactsServiceImpl(contactsDAO, userAccountService);
	}

	@Test
	public void getContacts() {
		when(contactsDAO.findByUserAccount(any(UserAccount.class)))
			.thenReturn(contacts);

		Contacts retrievedContacts = contactsService.getContacts(userAccount.getEmail());

		verify(contactsDAO).findByUserAccount(any(UserAccount.class));
		assertThat(retrievedContacts, equalTo(contacts));
	}

	@Test
	public void updateUserContacts() {
		Contacts changedContacts = new Contacts.Builder(contacts)
			.setPhone(contacts.getPhone() + "_changed")
			.setAddress(contacts.getAddress() + "_changed")
			.build();
		when(contactsDAO.findByUserAccount(any(UserAccount.class)))
			.thenReturn(contacts);

		contactsService.updateUserContacts(changedContacts, userAccount.getEmail());

		verify(contactsDAO).save(contactsCaptor.capture());
		assertThat(contactsCaptor.getValue(), equalTo(changedContacts));
	}
}
