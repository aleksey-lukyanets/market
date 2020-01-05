package market.service.impl;

import market.dao.UserAccountDAO;
import market.domain.Cart;
import market.domain.UserAccount;
import market.exception.EmailExistsException;
import market.service.UserAccountService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса аккаунта пользователя.
 */
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountDAO userAccountDAO;

    public UserAccountServiceImpl(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    @Transactional
    @Override
    public void save(UserAccount account) {
        userAccountDAO.save(account);
    }

    @Transactional
    @Override
    public void delete(UserAccount account) {
        userAccountDAO.delete(account);
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount findOne(long accountId) {
        return userAccountDAO.findOne(accountId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserAccount> findAll() {
        return userAccountDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount findByEmail(String email) {
        return userAccountDAO.findByEmail(email);
    }
    
    //--------------------------------------- Операции с аккаунтом пользователя
    
    @Transactional(readOnly = true)
    @Override
    public UserAccount getUserAccount(String userLogin) {
        return userAccountDAO.findByEmail(userLogin);
    }

    @Transactional
    @Override
    public UserAccount createUser(UserAccount userAccount) throws EmailExistsException {
        if (findByEmail(userAccount.getEmail()) != null)
            throw new EmailExistsException();
        // todo: account data validation

        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        String hashedPassword = pe.encode(userAccount.getPassword());
        userAccount.setPassword(hashedPassword);

        userAccount.setCart(new Cart(userAccount));
        userAccountDAO.save(userAccount);
        return userAccount;
    }
}
