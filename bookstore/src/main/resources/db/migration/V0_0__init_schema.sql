CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;

SET default_tablespace = '';
SET default_with_oids = false;

-- Create tables

CREATE TABLE IF NOT EXISTS users (
	id BIGSERIAL NOT NULL,
	usr_username character varying(100) NOT NULL,
	usr_password character varying(200) NOT NULL,
	usr_role varchar(100) NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS customers (
	id BIGSERIAL NOT NULL,
	cst_name varchar(255) NOT NULL,
	cst_cpf varchar(255) NOT NULL,
	cst_birthday date NOT NULL,
	cst_phone varchar(255) NOT NULL,
	cst_gender varchar(255) NOT NULL,
	cst_usr_id bigint NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS credit_cards (
	id BIGSERIAL NOT NULL,
	crd_number varchar(255) NOT NULL,
	crd_printed_name varchar(255),
	crd_security_code varchar(255) NOT NULL,
	crd_cst_id bigint NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS addresses (
	id BIGSERIAL NOT NULL,
	adr_cep varchar(255) NOT NULL,
	adr_public_place varchar(255) NOT NULL,
	adr_number varchar(255) NOT NULL,
	adr_complement varchar(255),
	adr_neighbourhood varchar(255) NOT NULL,
	adr_city varchar(255) NOT NULL,
	adr_state varchar(255) NOT NULL,
	adr_type varchar(255) NOT NULL,
	adr_owner_description varchar(255),
	adr_cst_id bigint NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS providers (
	id BIGSERIAL NOT NULL,
	pvd_name varchar(255) NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS categories (
	id BIGSERIAL NOT NULL,
	ctg_name varchar(255) NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS products (
	id BIGSERIAL NOT NULL,
	prt_title varchar(255) NOT NULL,
	prt_image_url varchar(1000) NOT NULL,
	prt_price double precision NOT NULL,
	prt_short_description varchar(255) NOT NULL,
	prt_long_description text,
	prt_height double precision,
	prt_width double precision,
	prt_weight double precision,
	prt_depth double precision,
	prt_pvd_id bigint NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS products_categories (
	pct_prt_id bigint NOT NULL,
	pct_ctg_id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS discount_vouchers (
	id BIGSERIAL NOT NULL,
	dvc_name varchar(255) NOT NULL,
	dvc_discount_percent int NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS sales (
	id BIGSERIAL NOT NULL,
	sls_purchase_date timestamp without time zone NOT NULL,
	sls_adr_id bigint,
	sls_cst_id bigint,
	sls_status varchar(255),
	sls_identify_number varchar(255)
);

CREATE TABLE IF NOT EXISTS sales_discount_vouchers (
	sdv_sls_id bigint NOT NULL,
	sdv_dvc_id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS sales_credit_cards (
	scc_sls_id bigint NOT NULL,
	scc_crd_id bigint NOT NULL,
	scc_value double precision NOT NULL
);

CREATE TABLE IF NOT EXISTS sales_products (
	slp_sls_id bigint NOT NULL,
	slp_prt_id bigint NOT NULL,
	slp_quantity int NOT NULL,
	slp_subtotal double precision NOT NULL
);

CREATE TABLE IF NOT EXISTS trades (
	id BIGSERIAL NOT NULL,
	trd_tracking_number varchar(50) NOT NULL,
	trd_request_date timestamp without time zone NOT NULL,
	trd_type varchar(50) NOT NULL,
	trd_status varchar(50) NOT NULL,
	trd_quantity bigint NOT NULL,
	trd_sls_id bigint NOT NULL,
	trd_prt_id bigint NOT NULL,
	inactivated boolean default FALSE
);

CREATE TABLE IF NOT EXISTS stock (
	id BIGSERIAL NOT NULL,
	stc_prt_id bigint NOT NULL,
	stc_quantity int NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_history (
	id BIGSERIAL NOT NULL,
	sth_stc_id bigint NOT NULL,
	sth_quantity int NOT NULL,
	sth_date timestamp without time zone NOT NULL
);

-- Sequences

CREATE SEQUENCE public.usr_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.usr_id_seq OWNED BY public.users.id;

CREATE SEQUENCE public.cst_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.cst_id_seq OWNED BY public.customers.id;

CREATE SEQUENCE public.crd_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.crd_id_seq OWNED BY public.credit_cards.id;

CREATE SEQUENCE public.adr_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.adr_id_seq OWNED BY public.addresses.id;

CREATE SEQUENCE public.pvd_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.pvd_id_seq OWNED BY public.providers.id;

CREATE SEQUENCE public.ctg_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.ctg_id_seq OWNED BY public.categories.id;

CREATE SEQUENCE public.prt_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.prt_id_seq OWNED BY public.products.id;

CREATE SEQUENCE public.dvc_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.dvc_id_seq OWNED BY public.discount_vouchers.id;

CREATE SEQUENCE public.sls_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.sls_id_seq OWNED BY public.sales.id;

CREATE SEQUENCE public.trd_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.trd_id_seq OWNED BY public.trades.id;

CREATE SEQUENCE public.stc_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.stc_id_seq OWNED BY public.stock.id;

CREATE SEQUENCE public.sth_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.sth_id_seq OWNED BY public.stock_history.id;

-- Add next value to sequences id

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.usr_id_seq'::regclass);

ALTER TABLE ONLY public.customers ALTER COLUMN id SET DEFAULT nextval('public.cst_id_seq'::regclass);

ALTER TABLE ONLY public.credit_cards ALTER COLUMN id SET DEFAULT nextval('public.crd_id_seq'::regclass);

ALTER TABLE ONLY public.addresses ALTER COLUMN id SET DEFAULT nextval('public.adr_id_seq'::regclass);

ALTER TABLE ONLY public.providers ALTER COLUMN id SET DEFAULT nextval('public.pvd_id_seq'::regclass);

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.ctg_id_seq'::regclass);

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.prt_id_seq'::regclass);

ALTER TABLE ONLY public.discount_vouchers ALTER COLUMN id SET DEFAULT nextval('public.dvc_id_seq'::regclass);

ALTER TABLE ONLY public.sales ALTER COLUMN id SET DEFAULT nextval('public.sls_id_seq'::regclass);

ALTER TABLE ONLY public.trades ALTER COLUMN id SET DEFAULT nextval('public.trd_id_seq'::regclass);

ALTER TABLE ONLY public.stock ALTER COLUMN id SET DEFAULT nextval('public.stc_id_seq'::regclass);

ALTER TABLE ONLY public.stock_history ALTER COLUMN id SET DEFAULT nextval('public.sth_id_seq'::regclass);


-- Primary key

ALTER TABLE ONLY  public.users
    ADD CONSTRAINT usr_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT cst_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.credit_cards
    ADD CONSTRAINT crd_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT adr_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.providers
    ADD CONSTRAINT pvd_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT ctg_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.products
    ADD CONSTRAINT prt_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.products_categories
    ADD CONSTRAINT pct_pk PRIMARY KEY (pct_prt_id, pct_ctg_id);

ALTER TABLE ONLY public.discount_vouchers
    ADD CONSTRAINT dvc_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sls_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.sales_discount_vouchers
    ADD CONSTRAINT sdv_pk PRIMARY KEY (sdv_sls_id, sdv_dvc_id);

ALTER TABLE ONLY public.sales_credit_cards
    ADD CONSTRAINT scc_pk PRIMARY KEY (scc_sls_id, scc_crd_id);

ALTER TABLE ONLY public.sales_products
    ADD CONSTRAINT slp_pk PRIMARY KEY (slp_sls_id, slp_prt_id);

ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trd_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT stc_pk PRIMARY KEY (id);

ALTER TABLE ONLY public.stock_history
    ADD CONSTRAINT sth_pk PRIMARY KEY (id);


-- Foreign key

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT cst_usr_fk FOREIGN KEY (cst_usr_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.credit_cards
    ADD CONSTRAINT crd_cst_fk FOREIGN KEY (crd_cst_id) REFERENCES public.customers(id);

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT adr_cst_fk FOREIGN KEY (adr_cst_id) REFERENCES public.customers(id);

ALTER TABLE ONLY public.products
    ADD CONSTRAINT prt_pvd_fk FOREIGN KEY (prt_pvd_id) REFERENCES public.providers(id);

ALTER TABLE ONLY public.products_categories
    ADD CONSTRAINT pct_prt_fk FOREIGN KEY (pct_prt_id) REFERENCES public.products(id);

ALTER TABLE ONLY public.products_categories
    ADD CONSTRAINT pct_ctg_fk FOREIGN KEY (pct_ctg_id) REFERENCES public.categories(id);

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sls_adr_fk FOREIGN KEY (sls_adr_id) REFERENCES public.addresses(id);

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sls_cst_fk FOREIGN KEY (sls_cst_id) REFERENCES public.customers(id);

ALTER TABLE ONLY public.sales_discount_vouchers
    ADD CONSTRAINT sdv_sls_fk FOREIGN KEY (sdv_sls_id) REFERENCES public.sales(id);

ALTER TABLE ONLY public.sales_discount_vouchers
    ADD CONSTRAINT sdv_dvc_fk FOREIGN KEY (sdv_dvc_id) REFERENCES public.discount_vouchers(id);

ALTER TABLE ONLY public.sales_credit_cards
    ADD CONSTRAINT scc_sls_fk FOREIGN KEY (scc_sls_id) REFERENCES public.sales(id);

ALTER TABLE ONLY public.sales_credit_cards
    ADD CONSTRAINT scc_crd_fk FOREIGN KEY (scc_crd_id) REFERENCES public.credit_cards(id);

ALTER TABLE ONLY public.sales_products
    ADD CONSTRAINT slp_sls_fk FOREIGN KEY (slp_sls_id) REFERENCES public.sales(id);

ALTER TABLE ONLY public.sales_products
    ADD CONSTRAINT slp_prt_fk FOREIGN KEY (slp_prt_id) REFERENCES public.products(id);

ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trd_sls_fk FOREIGN KEY (trd_sls_id) REFERENCES public.sales(id);

ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trd_prt_fk FOREIGN KEY (trd_prt_id) REFERENCES public.products(id);

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT stc_prt_fk FOREIGN KEY (stc_prt_id) REFERENCES public.products(id);

ALTER TABLE ONLY public.stock_history
    ADD CONSTRAINT sth_stc_fk FOREIGN KEY (sth_stc_id) REFERENCES public.stock(id);