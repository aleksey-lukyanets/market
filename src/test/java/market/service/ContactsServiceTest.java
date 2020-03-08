package market.service;

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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactsServiceTest {
	private static final long ACCOUNT_ID = 50L;
	private static final String ACCOUNT_EMAIL = "email@domain.com";
	private static final String CONTACTS_PHONE = "some_phone";
	private static final String CONTACTS_ADDRESS = "some_address";

	@Mock
	private ContactsDAO contactsDAO;
	@Mock
	private UserAccountService userAccountService;

	@Captor
	private ArgumentCaptor<Contacts> contactsCaptor;

	private ContactsService contactsService;
	private Contacts contacts;

	@BeforeEach
	public void setUp() {
		contacts = new Contacts.Builder()
			.setPhone(CONTACTS_PHONE)
			.setAddress(CONTACTS_ADDRESS)
			.build();
		UserAccount userAccount = new UserAccount.Builder()
			.setId(ACCOUNT_ID)
			.setEmail(ACCOUNT_EMAIL)
			.setPassword("password")
			.setName("Name")
			.setActive(true)
			.setContacts(contacts)
			.build();
		contacts.setUserAccount(userAccount);

		when(userAccountService.findByEmail(ACCOUNT_EMAIL)).thenReturn(userAccount);

		contactsService = new ContactsServiceImpl(contactsDAO, userAccountService);
	}

	@Test
	public void getContacts() {
		when(contactsDAO.findByUserAccount(any(UserAccount.class))).thenReturn(contacts);

		Contacts retrievedContacts = contactsService.getContacts(ACCOUNT_EMAIL);

		verify(contactsDAO).findByUserAccount(any(UserAccount.class));
		assertThat(retrievedContacts, equalTo(contacts));
	}

	@Test
	public void updateUserContacts() {
		Contacts changedContacts = new Contacts.Builder()
			.setUserAccount(contacts.getUserAccount())
			.setPhone(CONTACTS_PHONE + "_changed")
			.setAddress(CONTACTS_ADDRESS + "_changed")
			.build();
		when(contactsDAO.findByUserAccount(any(UserAccount.class))).thenReturn(contacts);

		contactsService.updateUserContacts(changedContacts, ACCOUNT_EMAIL);

		verify(contactsDAO).save(contactsCaptor.capture());
		assertThat(contactsCaptor.getValue(), equalTo(changedContacts));
	}
}
