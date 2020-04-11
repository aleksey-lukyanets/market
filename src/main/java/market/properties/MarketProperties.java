package market.properties;

import org.springframework.beans.factory.annotation.Value;

public class MarketProperties {

	private int deliveryCost;

	public MarketProperties() {
	}

	public MarketProperties(@Value("${deliveryCost}") int deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public int getDeliveryCost() {
		return deliveryCost;
	}
}
