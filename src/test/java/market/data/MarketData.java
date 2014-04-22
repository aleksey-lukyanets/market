package market.data;

import java.util.ArrayList;
import java.util.List;
import market.domain.Region;
import market.dto.CartItemDTO;

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
        return new CartItemDTO(PRODUCT_ID, (short)PRODUCT_QUANTITY);
    }

    public static List<Region> getRegionsOrderedByName() {
        List<Region> regions = new ArrayList<>();
        regions.add(new Region("Campbeltown"));
        regions.add(new Region("Highland"));
        regions.add(new Region("Island"));
        regions.add(new Region("Islay"));
        regions.add(new Region("Lowland"));
        regions.add(new Region("Speyside"));
        return regions;
    }
}
