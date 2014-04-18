package market.security;

/**
 *
 */
import java.util.ArrayList;
import java.util.Collection;
import market.domain.Role;
import market.domain.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сборщик аккаунта пользователя.
 */
@Component("assembler")
public class UserAssembler {

    @Transactional
    User buildUserFromUserEntity(UserAccount account) {

        String login = account.getEmail();
        String password = account.getPassword();
        boolean enabled = account.isActive();
        boolean accountNonExpired = account.isActive();
        boolean credentialsNonExpired = account.isActive();
        boolean accountNonLocked = account.isActive();

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : account.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getTitle()));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        User user = new User(login, password, enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        return user;
    }
}
