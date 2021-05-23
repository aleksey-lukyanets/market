package market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({DataConfig.class, RestSecurityConfig.class, RestConfig.class})
@PropertySource("classpath:/market.properties")
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}
}
