package market.service.impl;

import market.dao.ContactsDAO;
import market.dao.UserAccountDAO;
import market.domain.Contacts;
import market.domain.UserAccount;
import market.dto.ContactsDTO;
import market.dto.assembler.ContactsDtoAssembler;
import market.service.ContactsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса контактных данных пользователя.
 */
public class ContactsServiceImpl implements ContactsService {
    private final ContactsDAO contactsDAO;
    private final UserAccountDAO userAccountDAO;
    private final ContactsDtoAssembler contactsDtoAssembler;

    public ContactsServiceImpl(ContactsDAO contactsDAO, UserAccountDAO userAccountDAO,
        ContactsDtoAssembler contactsDtoAssembler)
    {
        this.contactsDAO = contactsDAO;
        this.userAccountDAO = userAccountDAO;
        this.contactsDtoAssembler = contactsDtoAssembler;
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
        return contactsDtoAssembler.toResource(contacts);
    }
    
    @Transactional
    @Override
    public ContactsDTO updateUserContacts(String userLogin, ContactsDTO newContacts) {
        UserAccount account = userAccountDAO.findByEmail(userLogin);
        Contacts contacts = account.getContacts();
        contacts.setPhone(newContacts.getPhone());
        contacts.setAddress(newContacts.getAddress());
        Contacts savedContacts = save(contacts);
        return contactsDtoAssembler.toResource(savedContacts);
    }
}
