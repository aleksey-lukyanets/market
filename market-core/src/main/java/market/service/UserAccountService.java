package market.service;

import market.domain.UserAccount;
import market.exception.EmailExistsException;

public interface UserAccountService {

	/**
	 * @return user account associated with the specified email
	 */
	UserAccount findByEmail(String email);

	/**
	 * Creates new account.
	 * @return newly created account
	 * @throws EmailExistsException if some account is already associated with the specified email
	 */
	UserAccount create(UserAccount userAccount);

}
