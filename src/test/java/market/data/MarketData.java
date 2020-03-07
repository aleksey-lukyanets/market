package market.data;

import market.domain.Region;
import market.dto.CartItemDTO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MarketData {

	public static final int DELIVERY_COST = 400;

	public static final int PRODUCT_ID = 1;
	public static final int PRODUCT_QUANTITY = 2;
	public static final int PRODUCT_UNIT_COST = 4030;
	public static final String PRODUCT_DISTILLERY = "Ardbeg";
	public static final String PRODUCT_NAME = "Ten";

	public static final int IMPROBABLE_ID = 100500;

	public static CartItemDTO getCartItemDTO() {
		return new CartItemDTO(PRODUCT_ID, PRODUCT_QUANTITY);
	}

	public static List<Region> getRegionsOrderedByName() {
		List<Region> regions = new ArrayList<>();
		regions.add(newRegion("Campbeltown"));
		regions.add(newRegion("Highland"));
		regions.add(newRegion("Island"));
		regions.add(newRegion("Islay"));
		regions.add(newRegion("Lowland"));
		regions.add(newRegion("Speyside"));
		return regions;
	}

	private static Region newRegion(String speyside) {
		return new Region.Builder()
			.setName(speyside)
			.build();
	}
}
