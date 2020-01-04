package market.service.impl;

import market.dao.UserAccountDAO;
import market.domain.Cart;
import market.domain.Contacts;
import market.domain.UserAccount;
import market.dto.UserDTO;
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
    public UserAccount createUser(UserDTO user) throws EmailExistsException {
        UserAccount userAccount = createUserAccount(user);
        userAccount.setCart(new Cart(userAccount));
        userAccountDAO.save(userAccount);
        return userAccount;
    }

    private UserAccount createUserAccount(UserDTO user) throws EmailExistsException {
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        String hashedPassword = pe.encode(user.getPassword());
        if (findByEmail(user.getEmail()) != null) {
            throw new EmailExistsException();
        }
        UserAccount userAccount = new UserAccount(user.getEmail(), hashedPassword, user.getName(), true);
        Contacts contacts = new Contacts(userAccount, user.getPhone(), user.getAddress());
        userAccount.setContacts(contacts);
        return userAccount;
    }
}
