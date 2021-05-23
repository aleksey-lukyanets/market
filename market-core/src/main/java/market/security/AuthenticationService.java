package market.security;

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

	public boolean authenticate(String login, String password) {
		try {
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
			SecurityContextHolder.getContext().setAuthentication(auth);
			return auth.isAuthenticated();
		} catch (BadCredentialsException ex) {
			// todo
			return false;
		}
	}
}
