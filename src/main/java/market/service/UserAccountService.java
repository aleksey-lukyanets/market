package market.service;

import market.domain.UserAccount;
import market.dto.UserDTO;
import market.exception.EmailExistsException;

import java.util.List;

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
     * Создание нового аккаунта.
     *
     * @param user данные нового пользователя
     * @return вновь созданный аккаунт
     * @throws EmailExistsException если пользователь с таким адресом уже существует
     */
    UserAccount createUser(UserDTO user) throws EmailExistsException;
    
    /**
     * Получение аккаунта пользователя.
     *
     * @param userLogin логин пользователя
     * @return аккаунт пользователя с запрошенным логином
     */
    UserAccount getUserAccount(String userLogin);
}
