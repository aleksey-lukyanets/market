package market;

import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ApplicationInitializer extends AbstractDispatcherServletInitializer {

	@Override
	protected String getServletName() {
		return "market";
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.setInitParameter("imagesPath", "/resources/img/");
		servletContext.setInitParameter("regionImagePath", "/resources/img/regions/");
		servletContext.setInitParameter("productImagePath", "/resources/img/products/");

		super.onStartup(servletContext); // let Spring create DispatcherServlet

		servletContext.addListener(new HttpSessionEventPublisher());
	}

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		XmlWebApplicationContext rootContext = new XmlWebApplicationContext();
		rootContext.setConfigLocation("/WEB-INF/spring/root-context.xml");
		return rootContext;
	}

	@Override
	protected WebApplicationContext createServletApplicationContext() {
		XmlWebApplicationContext dispatcherContext = new XmlWebApplicationContext();
		dispatcherContext.setConfigLocations(
			"/WEB-INF/spring/servlet/servlet-context.xml",
			"/WEB-INF/spring/servlet/tiles-context.xml",
			"/WEB-INF/spring/servlet/rest-context.xml");
		return dispatcherContext;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/", "*.html"};
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[]{
			new CharacterEncodingFilter("UTF-8", true),
			new DelegatingFilterProxy("springSecurityFilterChain"),
			new HiddenHttpMethodFilter()
		};
	}
}
