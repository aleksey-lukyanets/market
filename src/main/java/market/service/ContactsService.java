package market.service;

import market.domain.Contacts;
import market.dto.ContactsDTO;

import java.util.List;

/**
 * Сервис контактных данных пользователя.
 */
public interface ContactsService {
    
    Contacts save(Contacts contacts);
    
    void delete(Contacts contacts);

    Contacts findOne(long contactsId);

    List<Contacts> findAll();
    
    //-------------------------------------- Операции с контактами пользователя
    
    /**
     * Получение контактных данных пользователя.
     * 
     * @param userLogin логин покупателя
     * @return контактные данные
     */
        
    ContactsDTO getUserContacts(String userLogin);
    
    /**
     * Изменение контактных данных пользователя.
     *
     * @param userLogin логин покупателя
     * @param newContacts новые контактные данные
     * @return изменённые контактные данные
     */
    ContactsDTO updateUserContacts(String userLogin, ContactsDTO newContacts);
}
