package market.security;

import market.domain.UserAccount;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationService {

	private final AuthenticationManager authenticationManager;

	public AuthenticationService(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void authenticate(UserAccount userAccount) {
		String login = userAccount.getEmail();
		String password = userAccount.getPassword();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
		try {
			Authentication auth = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (BadCredentialsException ex) {
			// todo
		}
	}
}
