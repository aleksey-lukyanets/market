package market;

import market.security.AuthenticationService;
import market.security.UserDetailsServiceImpl;
import market.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"market.security"})
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfigBase extends WebSecurityConfigurerAdapter {

	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;

	@Bean
	public AuthenticationService authenticationService(AuthenticationManager authenticationManager) {
		return new AuthenticationService(authenticationManager);
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(daoAuthenticationProvider);
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(
		UserDetailsServiceImpl customUserDetailsService, PasswordEncoder passwordEncoder)
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public UserDetailsServiceImpl customUserDetailsService(UserAccountService userAccountService) {
		return new UserDetailsServiceImpl(userAccountService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
