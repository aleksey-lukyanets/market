package market.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import market.domain.Cart;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Перехватчик сеансовой корзины.
 * 
 * При отсутствии корзины в сессии создаёт новую корзину.
 */
public class SessionCartInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, 
            Object handler) throws Exception
    {
        HttpSession session = request.getSession(true);
        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new Cart());
        }
        return super.preHandle(request, response, handler);
    }
}
