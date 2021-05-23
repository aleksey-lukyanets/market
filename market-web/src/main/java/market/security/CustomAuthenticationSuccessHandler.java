package market.security;

import market.domain.Cart;
import market.domain.UserAccount;
import market.dto.CartDTO;
import market.dto.assembler.CartDtoAssembler;
import market.properties.MarketProperties;
import market.service.UserAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private static final Logger log = LogManager.getLogger(CustomAuthenticationSuccessHandler.class);

	private final ServletContext servletContext;
	private final UserAccountService userAccountService;
	private final CartDtoAssembler cartDtoAssembler;

	public CustomAuthenticationSuccessHandler(ServletContext servletContext, UserAccountService userAccountService,
	    MarketProperties marketProperties)
	{
		this.servletContext = servletContext;
		this.userAccountService = userAccountService;
		cartDtoAssembler = new CartDtoAssembler(marketProperties);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException
	{
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		if (roles.contains("ROLE_USER")) {
			UserAccount account = userAccountService.findByEmail(authentication.getName());
			CartDTO cartDto = prepareCartDto(account);
			request.getSession().setAttribute("cart", cartDto);
		}
		if (isStaff(roles)) {
			response.sendRedirect(servletContext.getContextPath() + "/admin/");
		} else {
			response.sendRedirect(servletContext.getContextPath() + "/");
		}
		request.getSession(false).setMaxInactiveInterval(30);
	}

	private CartDTO prepareCartDto(UserAccount account) {
		Cart cart = account.getCart();
		if (cart == null) {
			log.warn(String.format("Account #%d has no cart, this shall never happen", account.getId()));
			return null;
		}
		return cartDtoAssembler.toModel(cart);
	}

	private boolean isStaff(Set<String> roles) {
		return roles.contains("ROLE_ADMIN") || roles.contains("ROLE_STAFF");
	}
}
