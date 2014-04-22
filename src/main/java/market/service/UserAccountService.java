package market.service;

import java.util.List;
import market.domain.UserAccount;
import market.dto.UserDTO;
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

    /**
     * Создание нового аккаунта с последующей авторизацией.
     *
     * @param user данные нового пользователя
     * @return вновь созданный аккаунт
     * @throws EmailExistsException если пользователь с таким адресом уже существует
     */
    UserAccount createUserThenAuthenticate(UserDTO user) throws EmailExistsException;
    
    /**
     * Получение аккаунта пользователя.
     *
     * @param userLogin логин пользователя
     * @return аккаунт пользователя с запрошенным логином
     */
    UserAccount getUserAccount(String userLogin);
}
