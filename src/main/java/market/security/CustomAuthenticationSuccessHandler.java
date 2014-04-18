package market.security;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import market.domain.UserAccount;
import market.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Обработчик успешной аутентификации пользователя.
 */
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ServletContext servletContext;
    
    @Autowired
    private UserAccountService userAccountService;
    
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication a
    ) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(a.getAuthorities());
        if (roles.contains("ROLE_USER")) {
            UserAccount account = userAccountService.findByEmail(a.getName());
            request.getSession().setAttribute("cart", account.getCart());
        }
        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_STAFF")) {
            response.sendRedirect(servletContext.getContextPath() + "/admin/");
        } else {
            response.sendRedirect(servletContext.getContextPath() + "/");
        }
    }
}
