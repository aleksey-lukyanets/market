package market.service;

import java.util.List;
import market.domain.UserAccount;
import market.domain.dto.UserDTO;
import market.exception.EmailExistsException;

/**
 * Сервис аккаунта пользователя.
 */
public interface UserAccountService {

    void save(UserAccount account);
    
    void delete(UserAccount account);

    UserAccount findOne(long accountId);

    List<UserAccount> findAll();

    UserAccount findByEmail(String email);
    
    //--------------------------------------- Операции с аккаунтом пользователя

    UserAccount createUserThenAuthenticate(UserDTO user) throws EmailExistsException;
    
    UserAccount getUserAccount(String userLogin);
}
