package market.security;

import market.domain.Role;
import market.domain.UserAccount;
import market.service.UserAccountService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Реализация сервиса извлечения аккаунта пользователя из БД.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserAccountService userAccountService;

	public UserDetailsServiceImpl(UserAccountService userAccountService) {
		this.userAccountService = userAccountService;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException
	{
		UserAccount userEntity = userAccountService.findByEmail(login);
		if (userEntity == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return buildUser(userEntity);
	}

	private User buildUser(UserAccount account) {
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
