package market;

import market.properties.MarketProperties;
import market.security.CustomAuthenticationSuccessHandler;
import market.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletContext;

@Configuration
public class WebSecurityConfig extends SecurityConfigBase {

	@Autowired
	private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

	protected void configure(HttpSecurity http) throws Exception {
		http.headers()
				.frameOptions().disable().and()
			.authorizeRequests()
				.antMatchers("/admin/**").access("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')").and()
			.httpBasic().and()
			.anonymous()
				.authorities("ROLE_ANONYMOUS").and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.successHandler(customAuthenticationSuccessHandler)
				.failureUrl("/login?error").and()
			.rememberMe()
				.key("marketAppKey").and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true).clearAuthentication(true)
				.deleteCookies("JSESSIONID").and()
			.sessionManagement().maximumSessions(25).and().and()
			.csrf().disable()
		;
	}

	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler(ServletContext servletContext,
	    UserAccountService userAccountService, MarketProperties marketProperties)
	{
		return new CustomAuthenticationSuccessHandler(servletContext, userAccountService, marketProperties);
	}
}
