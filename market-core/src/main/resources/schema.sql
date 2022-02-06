DROP TABLE IF EXISTS bill;
CREATE TABLE bill (
    id bigserial NOT NULL,
    number integer,
    date_created date,
    total_cost integer,
    payed boolean,
    cc_number character varying(19)
);

DROP TABLE IF EXISTS cart;
CREATE TABLE cart (
    id bigserial NOT NULL,
    total_items integer,
    products_cost integer,
    delivery_included boolean
);

DROP TABLE IF EXISTS cart_item;
CREATE TABLE cart_item (
    cart_id bigint,
    product_id bigint,
    quantity integer
);

DROP TABLE IF EXISTS contacts;
CREATE TABLE contacts (
    id bigserial NOT NULL,
    phone character varying(20),
    address character varying(100),
    city_region character varying(50)
);

DROP TABLE IF EXISTS customer_order;
CREATE TABLE customer_order (
    id bigint NOT NULL,
    user_account_id bigint,
    date_created date,
    executed boolean,
    products_cost integer,
    delivery_included boolean,
    delivery_cost integer
);

DROP TABLE IF EXISTS distillery;
CREATE TABLE distillery (
    id bigserial NOT NULL,
    title character varying(25),
    region_id bigint,
    description character varying(1000)
);

DROP TABLE IF EXISTS ordered_product;
CREATE TABLE ordered_product (
    customer_order_id bigint,
    product_id bigint,
    quantity integer
);

DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id bigserial NOT NULL,
    name character varying(45),
    distillery_id bigint,
    age smallint,
    alcohol double precision,
    volume integer,
    price double precision,
    description character varying(1000),
    available boolean
);

DROP TABLE IF EXISTS region;
CREATE TABLE region (
    id bigserial NOT NULL,
    name character varying(20) NOT NULL,
    subtitle character varying(20),
    color character varying(10),
    description character varying(1000)
);

DROP TABLE IF EXISTS role;
CREATE TABLE role (
    id bigint NOT NULL,
    title character varying(20)
);

DROP TABLE IF EXISTS storage;
CREATE TABLE storage (
    id bigserial NOT NULL,
    available boolean
);

DROP TABLE IF EXISTS user_account;
CREATE TABLE user_account (
    id bigserial NOT NULL,
    email character varying(50),
    password character varying(255),
    name character varying(50),
    active boolean
);

DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role (
    user_id bigint,
    role_id bigint
);
