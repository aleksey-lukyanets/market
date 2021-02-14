package market.service.impl;

import market.dao.ContactsDAO;
import market.domain.Contacts;
import market.domain.UserAccount;
import market.exception.CustomNotValidException;
import market.service.ContactsService;
import market.service.UserAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactsServiceImpl implements ContactsService {
	private final ContactsDAO contactsDAO;
	private final UserAccountService userAccountService;

	public ContactsServiceImpl(ContactsDAO contactsDAO, UserAccountService userAccountService) {
		this.contactsDAO = contactsDAO;
		this.userAccountService = userAccountService;
	}

	@Transactional(readOnly = true)
	@Override
	public Contacts getContacts(String userLogin) {
		UserAccount account = userAccountService.findByEmail(userLogin);
		return contactsDAO.findByUserAccount(account);
	}

	@Transactional
	@Override
	public Contacts updateUserContacts(Contacts changedContacts, String userLogin) {
		Contacts originalContacts = getContacts(userLogin);
		if (originalContacts == null)
			throw new CustomNotValidException("NotExist", "userLogin", "userLogin"); // todo: some custom exception

		originalContacts.setPhone(changedContacts.getPhone());
		originalContacts.setAddress(changedContacts.getAddress());
		contactsDAO.save(originalContacts);
		return originalContacts;
	}
}
