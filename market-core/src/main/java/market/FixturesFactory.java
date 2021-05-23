package market;

import market.domain.*;

import java.util.Date;

public class FixturesFactory {
	private static final String DISTILLERY_TITLE = "distillery_title";
	private static final String DISTILLERY_DESCRIPTION = "distillery_description";

	private static final String REGION_NAME = "region name";
	private static final String REGION_SUBTITLE = "region subtitle";
	private static final String REGION_DESCRIPTION = "region description";
	private static final String REGION_COLOR = "#ffffff";

	private static final double PRODUCT_PRICE = 100.0;
	private static final String PRODUCT_NAME = "product_name";
	private static final int PRODUCT_AGE = 10;
	private static final float PRODUCT_ALCOHOL = 40;
	private static final int PRODUCT_VOLUME = 700;
	private static final String PRODUCT_DESCRIPTION = "product_description";
	private static final boolean PRODUCT_AVAILABLE = true;

	private static final String ACCOUNT_EMAIL = "email@domain.com";
	private static final String ACCOUNT_PASSWORD = "password";
	private static final String ACCOUNT_NAME = "Name";
	private static final boolean ACCOUNT_ACTIVE = true;

	private static final String CONTACTS_PHONE = "+97211234567";
	private static final String CONTACTS_ADDRESS = "some_address";

	private static final int ORDERED_PRODUCT_QUANTITY = 3;

	private static final String BILL_CARD_NUMBER = "1111222233334444";

	private static long regionId = 123L;
	private static long distilleryId = 234L;
	private static long productId = 10L;
	private static long accountId = 50L;
	private static long orderId = 3000L;
	private static int billId = 400;

	public static Region.Builder region() {
		return new Region.Builder()
			.setId(++regionId)
			.setName(REGION_NAME + regionId)
			.setSubtitle(REGION_SUBTITLE)
			.setDescription(REGION_DESCRIPTION)
			.setColor(REGION_COLOR);
	}

	public static Distillery.Builder distillery(Region region) {
		return new Distillery.Builder()
			.setId(++distilleryId)
			.setRegion(region)
			.setTitle(DISTILLERY_TITLE + distilleryId)
			.setDescription(DISTILLERY_DESCRIPTION);
	}

	public static Product.Builder product(Distillery distillery) {
		return new Product.Builder()
			.setId(++productId)
			.setDistillery(distillery)
			.setName(PRODUCT_NAME + productId)
			.setAge(PRODUCT_AGE)
			.setAlcohol(PRODUCT_ALCOHOL)
			.setPrice(PRODUCT_PRICE)
			.setVolume(PRODUCT_VOLUME)
			.setDescription(PRODUCT_DESCRIPTION)
			.setAvailable(PRODUCT_AVAILABLE);
	}

	public static UserAccount.Builder account(Cart cart) {
		return account()
			.setCart(cart);
	}

	public static UserAccount.Builder account() {
		return new UserAccount.Builder()
			.setId(++accountId)
			.setEmail(ACCOUNT_EMAIL)
			.setPassword(ACCOUNT_PASSWORD)
			.setName(ACCOUNT_NAME)
			.setActive(ACCOUNT_ACTIVE);
	}

	public static Contacts.Builder contacts() {
		return new Contacts.Builder()
			.setPhone(CONTACTS_PHONE)
			.setAddress(CONTACTS_ADDRESS);
	}

	public static Order.Builder order(UserAccount userAccount) {
		return new Order.Builder()
			.setId(++orderId)
			.setUserAccount(userAccount)
			.setDateCreated(new Date());
	}

	public static OrderedProduct.Builder orderedProduct(Order order, Product product) {
		return new OrderedProduct.Builder()
			.setProduct(product)
			.setOrder(order)
			.setQuantity(ORDERED_PRODUCT_QUANTITY);
	}

	public static Bill.Builder bill(Order order) {
		return new Bill.Builder()
			.setOrder(order)
			.setNumber(++billId)
			.setTotalCost(order.getProductsCost() + order.getDeliveryCost())
			.setPayed(true)
			.setDateCreated(new Date())
			.setCcNumber(BILL_CARD_NUMBER);
	}
}
