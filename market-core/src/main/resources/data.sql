INSERT INTO bill (id, number, date_created, total_cost, payed, cc_number) VALUES (1, 271320540, '2019-12-27', 8527, true, '1111222233334444');

INSERT INTO cart (id, total_items, products_cost, delivery_included) VALUES (5, 0, 0, true);
INSERT INTO cart (id, total_items, products_cost, delivery_included) VALUES (6, 0, 0, true);
INSERT INTO cart (id, total_items, products_cost, delivery_included) VALUES (7, 0, 0, true);
INSERT INTO cart (id, total_items, products_cost, delivery_included) VALUES (4, 1, 6517, true);

INSERT INTO cart_item (cart_id, product_id, quantity) VALUES (4, 5, 1);

INSERT INTO contacts (phone, address, id, city_region) VALUES ('+7 123 456 78 90', 'Riesstrasse 18', 4, '13');
INSERT INTO contacts (phone, address, id, city_region) VALUES ('+79211234567', 'sdf', 7, '13');

INSERT INTO customer_order (id, user_account_id, date_created, executed, products_cost, delivery_included, delivery_cost) VALUES (1, 4, '2019-12-27', false, 8127, true, 400);

INSERT INTO distillery (id, title, region_id, description) VALUES (2, 'Balvenie', 6, 'balvenie');
INSERT INTO distillery (id, title, region_id, description) VALUES (6, 'Lagavulin', 4, 'lagavulin');
INSERT INTO distillery (id, title, region_id, description) VALUES (7, 'Laphroaig', 4, 'laphroaig');
INSERT INTO distillery (id, title, region_id, description) VALUES (1, 'Ardbeg', 4, 'ardbeg');
INSERT INTO distillery (id, title, region_id, description) VALUES (3, 'Caol Ila', 4, 'caol ila');
INSERT INTO distillery (id, title, region_id, description) VALUES (4, 'Dalwhinnie', 2, 'dalwhinnie');
INSERT INTO distillery (id, title, region_id, description) VALUES (5, 'Glenkinchie', 5, 'glenkinchie');
INSERT INTO distillery (id, title, region_id, description) VALUES (9, 'Talisker', 3, 'talisker');
INSERT INTO distillery (id, title, region_id, description) VALUES (8, 'Springbank', 1, 'springbank');

INSERT INTO ordered_product (customer_order_id, product_id, quantity) VALUES (1, 8, 1);

INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (2, 'Uigeadail', 1, 0, 54.200000762939453, 700, 7020, 'Ardbeg Uigedael is named after Loch Uigedael, the lake in the grounds of the distillery Ardbeg which are the water is an important factor in the distilling process. The Uigedael a vatted malt, bottled at 54.2% ABV without cold filtering. Ardbeg Uigedael has no age indication for the expression consists of various malts of different ages. Malts are used partly matured in ex-bourbon and partly on ex-sherry casks.', true);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (10, '10 y.o.', 9, 10, 45.799999237060547, 750, 4683, 'Powerful peat smoke with the salinity of sea water and the moisture of fresh oysters. Full sweetness of dried fruit with smoke and strong aromas of malted barley, warm and intense. Peppery in the mouth. Big, long, warming and peppery on the finish with an attractive sweetness.', true);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (4, '12 y.o.', 3, 12, 43, 700, 4913, 'Smoked ham comes across delicately with citrus and cigar leaves. Fresh and smoky, probably the best part of this whisky. With an almost full body, oil and tar meet subtle smoke. Hints of sweet tastes like honey. Some spices like pepper combined with little sweetness.', true);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (3, '12 y.o. Doublewood', 2, 12, 40, 700, 5403, 'Has clear influences from both bourbon and sherry wood. This malt has only 12 years aged in bourbon casks and then 3 months in young Oloroso casks. The peppery character from the bourbon barrels, penetrates, as it were by the rich and full aroma of Oloroso casks it. The Balvenie Double Wood is therefore a very complex malt.', true);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (7, 'Quarter Cask', 7, 0, 48, 700, 5100, 'A vibrant young Laphroaig whose maturation has been accelerated by ageing in quarter casks. This shows soft sweetness and a velvety feel when first tasted, then the intense peatiness so unique to Laphroaig comes bursting through. A terrific whisky and great value.', true);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (6, '16 y.o.', 6, 16, 43, 750, 6620, 'The Islay representative in the ''Classic Malts'' series is a deep, dry and exceptionally peaty bruiser. Probably the most pungent of all Islay malts, Lagavulin is not for the faint-hearted but inspires fanatical devotion in its many followers.', true);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (8, '12 y.o. Cask Strength Batch 6', 8, 12, 53.099998474121094, 700, 8127, 'Like a storm gathering of the Kintyre coast, dark and ominous, yet tastes so good. The richness comes from the high percentage of sherry casks used in maturation. This is a truly classic Springbank, best enjoyed after dinner, or with your favourite cigar.', true);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (5, '15 y.o.', 4, 15, 43, 750, 6517, 'A good introduction to the delights of single malt whisky – elegant, smooth and medium-bodied, with a light, fruity palate and a whiff of heather on the finish. Part of Diageo''s Classic Malt range. ', true);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (1, 'Ten', 1, 10, 46, 700, 4420, 'Ten Years Old is the basis of the Ardbeg range. After 10 years of maturation in ex-bourbon casks, the whiskey bottled at 46% ABV without cold filtering. The characteristic peat, although clearly present but in perfect balance with the natural sweetness and not predominant in the taste.', false);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (11, '12 y.o.', 5, 12, 43, 700, 4547, 'A light, delicate whiskey; sweet and creamy with a subtle floral aroma. This subtle, refined Lowland is ideal as an aperitif; try it straight from the fridge or freezer.', false);
INSERT INTO product (id, name, distillery_id, age, alcohol, volume, price, description, available) VALUES (9, '18 y.o.', 8, 18, 46, 700, 14490, 'This heavily sherried 18-year-old Springbank has been bottled by Mark Reynier as part of his Renegade series. Distilled in 1995, this sherry hogshead was aged for 10 years in Campbeltown and then moved to Bruichladdich for a further eight years.', true);

INSERT INTO region (id, name, subtitle, color, description) VALUES (3, 'Island', '', 'blue', 'Scotch produced on the islands surrounding the mainland of Scotland offer a very diverse and different taste, they''re not however recognised by the Scotch Whisky Association but are easily grouped together for geographic reasons as one as they''re all islands. Although diverse in flavours, peat and salinity are found in all of the Islands whiskies, the latter because of the vicinity to the sea.
<p><ul>
<li>Number of distilleries: Under 10
<li>Typical flavours: Smoke, Brine, Oil, Black Pepper and Honey
</ul>');
INSERT INTO region (id, name, subtitle, color, description) VALUES (4, 'Islay', '', 'black', 'The Scottish island of Islay (pronounced eye-luh) is located to the west of the mainland and is the smallest Whisky region in terms of area coverage in Scotland. Even though it''s a relatively small island, Islay is currently home to 8 distilleries, 3 of which are World famous, Ardbeg, Laphroaig and Lagavulin. The region is known for its peaty single malts and it''s believed that whisky distillation reached Scotland from Ireland via Islay in the 13th century, hence the high number of past and present distilleries on the island.
<p><ul>
<li>Number of distilleries: Under 10
<li>Typical flavours: Seaweed, Brine, Carbolic Soap, Apple, Smoke and Kippers
</ul>');
INSERT INTO region (id, name, subtitle, color, description) VALUES (1, 'Campbeltown', '', 'purple', 'Campbeltown is part of mainland Scotland but it''s found at the foot of the Mull of Kintyre and was once a thriving whisky hotspot with over 34 distilleries, however it''s now home to just 3. A mixture of improved transportation links to the rival distilleries in the north and a decline in quality as distillers cut corners for mass-production resulting in an inferior product.
<p><ul>
<li>Number of distilleries: Under 5
<li>Typical flavours: Brine, Smoke, Dried Fruit, Vanilla and Toffee
</ul>');
INSERT INTO region (id, name, subtitle, color, description) VALUES (6, 'Speyside', '', 'green', 'The region of Speyside is located in the north east of Scotland surrounding the River Spey, it''s a sub-region to the neighbouring Highlands because of the high density of distilleries in the area. It''s home to the highest number of distilleries in Scotland with well over 60 at present. Speyside is a protected region for Scotch Whisky distilling under UK Government legislation.
<p><ul>
<li>Number of distilleries: Over 60
<li>Typical flavours: Apple, Vanilla, Oak, Malt, Nutmeg and Dried Fruit
</ul>');
INSERT INTO region (id, name, subtitle, color, description) VALUES (2, 'Highland', '', 'brown', 'The Highlands is Scotland''s largest whisky producing area, covering anywhere from the north of Glasgow (the Clyde estuary to the River Tay) all the way to Thurso in the north, not to mention the east and west regions excluding Speyside. Due to the large area, whisky in the Highlands is very diverse and offers a vast amount of different flavours so it''s hard to put a certain style on Whisky from this region.
<p><ul>
<li>Number of distilleries: Over 25
<li>Typical flavours: Fruit Cake, Malt, Oak, Heather, Dried Fruit and Smoke
</ul>');
INSERT INTO region (id, name, subtitle, color, description) VALUES (5, 'Lowland', '', 'yellow', 'Lowlands is the second biggest whisky region in terms of the area it covers, but it''s currently only home to fewer than five distilleries. The Lowlands region covers the south of Scotland up to the north of Glasgow and Edinburgh where it meets the border on the Highlands, the line follows the old county borders running from the Clyde estuary in the west to the River Tay in the east, anything south of this is to the border with England is classified as the ''Lowlands'' in whisky terms.
<p><ul>
<li>Number of distilleries: Under 5
<li>Typical flavours: Grass, Honeysuckle, Cream, Toffee, Toast and Cinnamon
</ul>');

INSERT INTO role (id, title) VALUES (0, 'ROLE_ADMIN');
INSERT INTO role (id, title) VALUES (1, 'ROLE_STAFF');
INSERT INTO role (id, title) VALUES (2, 'ROLE_USER');

INSERT INTO storage (id, available) VALUES (1, true);
INSERT INTO storage (id, available) VALUES (2, true);
INSERT INTO storage (id, available) VALUES (3, true);
INSERT INTO storage (id, available) VALUES (4, true);
INSERT INTO storage (id, available) VALUES (5, true);
INSERT INTO storage (id, available) VALUES (6, true);
INSERT INTO storage (id, available) VALUES (7, true);
INSERT INTO storage (id, available) VALUES (8, true);
INSERT INTO storage (id, available) VALUES (9, true);
INSERT INTO storage (id, available) VALUES (10, true);
INSERT INTO storage (id, available) VALUES (11, true);

INSERT INTO user_account (id, email, password, name, active) VALUES (1, 'admin', '$2a$10$Cmwx2Xr/PVpkibiiDz0s7eaVGZHPUvAu5ivdVC5BJgSYbp3c06FY6', 'Admin', true);
INSERT INTO user_account (id, email, password, name, active) VALUES (4, 'ivan.petrov@yandex.ru', '$2a$10$LfLg6vp4.wyowWP9ysg3F.yQ/udNKfRhGlHJ298xGCtBLC2dDX.OC', 'Ivan Petrov', true);

INSERT INTO user_role (user_id, role_id) VALUES (1, 0);

