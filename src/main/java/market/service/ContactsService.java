package market.service;

import java.util.List;
import market.domain.Contacts;
import market.domain.dto.ContactsDTO;

/**
 * Сервис контактных данных пользователя.
 */
public interface ContactsService {
    
    Contacts save(Contacts contacts);
    
    void delete(Contacts contacts);

    Contacts findOne(long contactsId);

    List<Contacts> findAll();
    
    //-------------------------------------- Операции с контактами пользователя
    
    ContactsDTO getUserContacts(String userLogin);
    
    ContactsDTO updateUserContacts(String userLogin, ContactsDTO newContacts);
}
