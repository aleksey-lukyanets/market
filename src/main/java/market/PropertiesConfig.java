package market;

import market.properties.MarketProperties;
import market.properties.PaginationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

	@Bean
	public MarketProperties marketProperties() {
		return new MarketProperties();
	}

	@Bean
	public PaginationProperties paginationProperties() {
		return new PaginationProperties();
	}
}
