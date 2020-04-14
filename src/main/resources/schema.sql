--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.12
-- Dumped by pg_dump version 12.0

-- Started on 2020-04-11 17:47:55

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 185 (class 1259 OID 16552)
-- Name: bill_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bill_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bill_id_seq OWNER TO postgres;

SET default_tablespace = '';

--
-- TOC entry 186 (class 1259 OID 16554)
-- Name: bill; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bill (
    id bigint DEFAULT nextval('public.bill_id_seq'::regclass) NOT NULL,
    number integer,
    date_created date,
    total_cost integer,
    payed boolean,
    cc_number character varying(19)
);


ALTER TABLE public.bill OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 16558)
-- Name: cart_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cart_id_seq OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 16560)
-- Name: cart; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart (
    id bigint DEFAULT nextval('public.cart_id_seq'::regclass) NOT NULL,
    total_items integer,
    products_cost integer,
    delivery_included boolean
);


ALTER TABLE public.cart OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 16564)
-- Name: cart_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart_item (
    cart_id bigint,
    product_id bigint,
    quantity integer
);


ALTER TABLE public.cart_item OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 16567)
-- Name: contacts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contacts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contacts_id_seq OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 16569)
-- Name: contacts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contacts (
    phone character varying(20),
    address character varying(100),
    id bigint DEFAULT nextval('public.contacts_id_seq'::regclass) NOT NULL,
    city_region character varying(50)
);


ALTER TABLE public.contacts OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 16573)
-- Name: customer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.customer_id_seq OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 16575)
-- Name: customer_order; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_order (
    id bigint NOT NULL,
    user_account_id bigint,
    date_created date,
    executed boolean,
    products_cost integer,
    delivery_included boolean,
    delivery_cost integer
);


ALTER TABLE public.customer_order OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 16578)
-- Name: distillery_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.distillery_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.distillery_id_seq OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 16580)
-- Name: distillery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.distillery (
    id bigint DEFAULT nextval('public.distillery_id_seq'::regclass) NOT NULL,
    title character varying(25),
    region_id bigint,
    description character varying(1000)
);


ALTER TABLE public.distillery OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16587)
-- Name: ordered_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ordered_product (
    customer_order_id bigint,
    product_id bigint,
    quantity integer
);


ALTER TABLE public.ordered_product OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16590)
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.product_id_seq OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16592)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id bigint DEFAULT nextval('public.product_id_seq'::regclass) NOT NULL,
    name character varying(45),
    distillery_id bigint,
    age smallint,
    alcohol double precision,
    volume integer,
    price double precision,
    description character varying(1000),
    available boolean
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16599)
-- Name: region_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.region_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.region_id_seq OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16601)
-- Name: region; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.region (
    id bigint DEFAULT nextval('public.region_id_seq'::regclass) NOT NULL,
    name character varying(20) NOT NULL,
    subtitle character varying(20),
    color character varying(10),
    description character varying(1000)
);


ALTER TABLE public.region OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16608)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    title character varying(20)
);


ALTER TABLE public.role OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16611)
-- Name: storage_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.storage_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.storage_id_seq OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16613)
-- Name: storage; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.storage (
    id bigint DEFAULT nextval('public.storage_id_seq'::regclass) NOT NULL,
    available boolean
);


ALTER TABLE public.storage OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16617)
-- Name: user_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_account_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_account_id_seq OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16619)
-- Name: user_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_account (
    id bigint DEFAULT nextval('public.user_account_id_seq'::regclass) NOT NULL,
    email character varying(50),
    password character varying(255),
    name character varying(50),
    active boolean
);


ALTER TABLE public.user_account OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16623)
-- Name: user_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_role (
    user_id bigint,
    role_id bigint
);


ALTER TABLE public.user_role OWNER TO postgres;

--
-- TOC entry 2248 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO market;


--
-- TOC entry 2249 (class 0 OID 0)
-- Dependencies: 185
-- Name: SEQUENCE bill_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.bill_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2250 (class 0 OID 0)
-- Dependencies: 186
-- Name: TABLE bill; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.bill TO market WITH GRANT OPTION;


--
-- TOC entry 2251 (class 0 OID 0)
-- Dependencies: 187
-- Name: SEQUENCE cart_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.cart_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2252 (class 0 OID 0)
-- Dependencies: 188
-- Name: TABLE cart; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.cart TO market WITH GRANT OPTION;


--
-- TOC entry 2253 (class 0 OID 0)
-- Dependencies: 189
-- Name: TABLE cart_item; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.cart_item TO market WITH GRANT OPTION;


--
-- TOC entry 2254 (class 0 OID 0)
-- Dependencies: 190
-- Name: SEQUENCE contacts_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.contacts_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2255 (class 0 OID 0)
-- Dependencies: 191
-- Name: TABLE contacts; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.contacts TO market WITH GRANT OPTION;


--
-- TOC entry 2256 (class 0 OID 0)
-- Dependencies: 192
-- Name: SEQUENCE customer_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.customer_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2257 (class 0 OID 0)
-- Dependencies: 193
-- Name: TABLE customer_order; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.customer_order TO market WITH GRANT OPTION;


--
-- TOC entry 2258 (class 0 OID 0)
-- Dependencies: 194
-- Name: SEQUENCE distillery_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.distillery_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2259 (class 0 OID 0)
-- Dependencies: 195
-- Name: TABLE distillery; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.distillery TO market WITH GRANT OPTION;


--
-- TOC entry 2260 (class 0 OID 0)
-- Dependencies: 196
-- Name: TABLE ordered_product; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.ordered_product TO market WITH GRANT OPTION;


--
-- TOC entry 2261 (class 0 OID 0)
-- Dependencies: 197
-- Name: SEQUENCE product_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.product_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2262 (class 0 OID 0)
-- Dependencies: 198
-- Name: TABLE product; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.product TO market WITH GRANT OPTION;


--
-- TOC entry 2263 (class 0 OID 0)
-- Dependencies: 199
-- Name: SEQUENCE region_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.region_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2264 (class 0 OID 0)
-- Dependencies: 200
-- Name: TABLE region; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.region TO market WITH GRANT OPTION;


--
-- TOC entry 2265 (class 0 OID 0)
-- Dependencies: 201
-- Name: TABLE role; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.role TO market WITH GRANT OPTION;


--
-- TOC entry 2266 (class 0 OID 0)
-- Dependencies: 202
-- Name: SEQUENCE storage_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.storage_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2267 (class 0 OID 0)
-- Dependencies: 203
-- Name: TABLE storage; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.storage TO market WITH GRANT OPTION;


--
-- TOC entry 2268 (class 0 OID 0)
-- Dependencies: 204
-- Name: SEQUENCE user_account_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.user_account_id_seq TO market WITH GRANT OPTION;


--
-- TOC entry 2269 (class 0 OID 0)
-- Dependencies: 205
-- Name: TABLE user_account; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_account TO market WITH GRANT OPTION;


--
-- TOC entry 2270 (class 0 OID 0)
-- Dependencies: 206
-- Name: TABLE user_role; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_role TO market WITH GRANT OPTION;


-- Completed on 2020-04-11 17:47:55

--
-- PostgreSQL database dump complete
--