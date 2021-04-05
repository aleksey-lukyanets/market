package market.controller;

import market.properties.MarketProperties;
import market.properties.PaginationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public MarketProperties marketProperties() {
		return new MarketProperties(400);
	}

	@Bean
	public PaginationProperties paginationProperties() {
		return new PaginationProperties(20, 10);
	}
}
