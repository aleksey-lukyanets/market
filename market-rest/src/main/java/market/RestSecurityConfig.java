package market;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class RestSecurityConfig extends SecurityConfigBase {

	protected void configure(HttpSecurity http) throws Exception {
		http.headers()
				.frameOptions().disable().and()
			.authorizeRequests()
				.antMatchers("/customer**").access("hasRole('ROLE_USER')").and()
			.httpBasic().and()
			.anonymous()
				.authorities("ROLE_ANONYMOUS").and()
			.csrf().disable()
		;
	}
}
