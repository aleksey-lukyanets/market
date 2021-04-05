package market;

import market.interceptors.SessionCartInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {
	"market.controller.frontend",
	"market.controller.backend"
})
public class ServletConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SessionCartInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/admin/**");
	}
}
