package market.service;

import market.domain.Contacts;

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

	Contacts getUserContacts(String userLogin);

	/**
	 * Изменение контактных данных пользователя.
	 *
	 * @param userLogin логин покупателя
	 * @param phone
	 * @param address
	 * @return изменённые контактные данные
	 */
	Contacts updateUserContacts(String userLogin, String phone, String address);
}
