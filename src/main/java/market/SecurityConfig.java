package market;

import market.properties.MarketProperties;
import market.security.AuthenticationService;
import market.security.CustomAuthenticationSuccessHandler;
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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletContext;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"market.security"})
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;
	@Autowired
	private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

	protected void configure(HttpSecurity http) throws Exception {
		http.headers()
				.frameOptions().disable().and()
			.authorizeRequests()
				.antMatchers("/admin/**").access("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
				.antMatchers("/rest/customer**").access("hasRole('ROLE_USER')").and()
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
