package market.service.impl;

import market.dao.UserAccountDAO;
import market.domain.Cart;
import market.domain.UserAccount;
import market.exception.EmailExistsException;
import market.service.UserAccountService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	private final UserAccountDAO userAccountDAO;

	public UserAccountServiceImpl(UserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	@Transactional(readOnly = true)
	@Override
	public UserAccount findByEmail(String email) { // todo: return optional
		return userAccountDAO.findByEmail(email);
	}

	@Transactional
	@Override
	public UserAccount create(UserAccount userAccount) {
		if (findByEmail(userAccount.getEmail()) != null)
			throw new EmailExistsException(UserAccount.class);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode(userAccount.getPassword());
		userAccount.setPassword(hashedPassword);
		userAccount.setCart(new Cart(userAccount));
		userAccountDAO.save(userAccount);

		return userAccount;
	}
}
