package market.service.impl;

import market.dao.ContactsDAO;
import market.dao.UserAccountDAO;
import market.domain.Contacts;
import market.domain.UserAccount;
import market.service.ContactsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса контактных данных пользователя.
 */
@Service
public class ContactsServiceImpl implements ContactsService {
	private final ContactsDAO contactsDAO;
	private final UserAccountDAO userAccountDAO;

	public ContactsServiceImpl(ContactsDAO contactsDAO, UserAccountDAO userAccountDAO) {
		this.contactsDAO = contactsDAO;
		this.userAccountDAO = userAccountDAO;
	}

	@Transactional
	@Override
	public Contacts save(Contacts contacts) {
		return contactsDAO.save(contacts);
	}

	@Transactional
	@Override
	public void delete(Contacts contacts) {
		contactsDAO.delete(contacts);
	}

	@Transactional(readOnly = true)
	@Override
	public Contacts findOne(long contactsId) {
		return contactsDAO.findById(contactsId).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Contacts> findAll() {
		return contactsDAO.findAll();
	}

	//-------------------------------------- Операции с контактами пользователя

	@Transactional(readOnly = true)
	@Override
	public Contacts getUserContacts(String userLogin) {
		UserAccount account = userAccountDAO.findByEmail(userLogin);
		return account.getContacts();
	}

	@Transactional
	@Override
	public Contacts updateUserContacts(String userLogin, String phone, String address) {
		UserAccount account = userAccountDAO.findByEmail(userLogin);
		// todo: handle if user doesn't exist
		Contacts contacts = account.getContacts();
		contacts.setPhone(phone);
		contacts.setAddress(address);
		return save(contacts);
	}
}
