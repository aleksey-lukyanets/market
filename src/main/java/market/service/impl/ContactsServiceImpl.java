package market.service.impl;

import market.dao.ContactsDAO;
import market.dao.UserAccountDAO;
import market.domain.Contacts;
import market.domain.UserAccount;
import market.service.ContactsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactsServiceImpl implements ContactsService {
	private final ContactsDAO contactsDAO;
	private final UserAccountDAO userAccountDAO;

	public ContactsServiceImpl(ContactsDAO contactsDAO, UserAccountDAO userAccountDAO) {
		this.contactsDAO = contactsDAO;
		this.userAccountDAO = userAccountDAO;
	}

	//-------------------------------------- Операции с контактами пользователя

	@Transactional(readOnly = true)
	@Override
	public Contacts getContacts(String userLogin) {
		UserAccount account = userAccountDAO.findByEmail(userLogin);
		return contactsDAO.findByUserAccount(account);
	}

	@Transactional
	@Override
	public void updateUserContacts(Contacts changedContacts, String userLogin) {
		Contacts originalContacts = getContacts(userLogin);
		if (originalContacts != null) {
			originalContacts.setPhone(changedContacts.getPhone());
			originalContacts.setAddress(changedContacts.getAddress());
			contactsDAO.save(originalContacts);
		}
	}
}
