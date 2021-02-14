package market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({DataConfig.class, SecurityConfig.class, ServletConfig.class, RestConfig.class})
@ComponentScan(basePackages = {
	"market.properties",
	"market.service",
	"market.controller.frontend",
	"market.controller.backend",
	"market.rest"
})
@PropertySource("classpath:/market.properties")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
