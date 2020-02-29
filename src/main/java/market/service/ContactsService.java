package market.service;

import market.domain.Contacts;

public interface ContactsService {

	/**
	 * @return contacts of the specified user
	 */
	Contacts getContacts(String userLogin);

	/**
	 * Updates contacts of the specified user.
	 */
	void updateUserContacts(Contacts changedContacts, String userLogin);

}
