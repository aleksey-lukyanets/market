package market.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MarketProperties {
	private final int deliveryCost;

	public MarketProperties(@Value("${deliveryCost}") int deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public int getDeliveryCost() {
		return deliveryCost;
	}
}
