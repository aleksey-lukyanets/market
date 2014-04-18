package market.security;

import market.service.UserAccountService;
import market.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса извлечения аккаунта пользователя из БД.
 */
@Component("myUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAssembler assembler;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException, DataAccessException
    {
        UserAccount userEntity = userAccountService.findByEmail(login);
        if (userEntity == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return assembler.buildUserFromUserEntity(userEntity);
    }
}
