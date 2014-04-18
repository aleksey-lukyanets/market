package market.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import market.exception.RestNotAuthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Перехватчик проверки авторизации для REST-службы.
 * 
 * При неавторизованном обращении к службе создаёт исключение RestNotAuthenticatedException,
 * которое возвращает клиенту соответствующий HTTP-статус.
 */
public class RestUserCheckInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RestNotAuthenticatedException();
        }
        return super.preHandle(request, response, handler);
    }
}
