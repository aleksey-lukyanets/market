package market.service.impl;

import java.util.List;
import market.dao.ContactsDAO;
import market.dao.UserAccountDAO;
import market.service.ContactsService;
import market.domain.Contacts;
import market.domain.UserAccount;
import market.domain.dto.ContactsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса контактных данных пользователя.
 */
@Service
public class ContactsServiceImpl implements ContactsService {

    private final ContactsDAO contactsDAO;
    private final UserAccountDAO userAccountDAO;

    @Autowired
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
        return contactsDAO.findOne(contactsId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Contacts> findAll() {
        return contactsDAO.findAll();
    }
    
    //-------------------------------------- Операции с контактами пользователя
    
    @Transactional(readOnly = true)
    @Override
    public ContactsDTO getUserContacts(String userLogin) {
        UserAccount account = userAccountDAO.findByEmail(userLogin);
        Contacts contacts = account.getContacts();
        return contacts.createDTO();
    }
    
    @Transactional
    @Override
    public ContactsDTO updateUserContacts(String userLogin, ContactsDTO newContacts) {
        UserAccount account = userAccountDAO.findByEmail(userLogin);
        Contacts contacts = account.getContacts();
        contacts.setPhone(newContacts.getPhone());
        contacts.setAddress(newContacts.getAddress());
        return save(contacts).createDTO();
    }
}
