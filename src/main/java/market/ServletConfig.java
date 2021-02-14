package market;

import market.interceptors.SessionCartInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import java.util.Locale;

@Configuration
public class ServletConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SessionCartInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/rest/**");
		registry.addInterceptor(new SessionCartInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/admin/**");
	}

	@Bean
	public ViewResolver viewResolver() {
		return new TilesViewResolver();
	}

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions("/WEB-INF/view/**/view.xml");
		return tilesConfigurer;
	}

	@Bean
	public UrlBasedViewResolver urlBasedViewResolver() {
		return new InternalResourceViewResolver("/WEB-INF/view/", ".jsp");
	}

	@Bean
	public LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}
}
