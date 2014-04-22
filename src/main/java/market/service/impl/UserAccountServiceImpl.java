package market.service.impl;

import java.util.List;
import market.dao.UserAccountDAO;
import market.domain.Cart;
import market.domain.Contacts;
import market.service.UserAccountService;
import market.domain.UserAccount;
import market.dto.UserDTO;
import market.exception.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса аккаунта пользователя.
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountDAO userAccountDAO;
    
    @Autowired
    @Qualifier(value = "authenticationManager")
    private AuthenticationManager authenticationManager;

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
    public UserAccount createUserThenAuthenticate(UserDTO user) throws EmailExistsException {
        UserAccount userAccount = createUserAccount(user);
        userAccount.setCart(new Cart(userAccount));
        userAccountDAO.save(userAccount);
        authenticateUser(user.getEmail(), user.getPassword());
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
    
    private void authenticateUser(String email, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        try {
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (BadCredentialsException ex) {
        }
    }
}
