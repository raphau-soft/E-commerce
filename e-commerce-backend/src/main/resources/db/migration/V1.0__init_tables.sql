CREATE TABLE "users" (
  "id" bigint PRIMARY KEY,
  "username" varchar,
  "name" varchar,
  "surname" varchar,
  "phone_number" varchar,
  "email" varchar UNIQUE,
  "password" varchar,
  "role" varchar,
  "active" boolean,
  "locked_until" timestamp,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 1 INCREMENT BY 1;

CREATE UNIQUE INDEX idx_user_username ON "users" ("username");
CREATE UNIQUE INDEX idx_user_name ON "users" ("name");
CREATE UNIQUE INDEX idx_user_surname ON "users" ("surname");
CREATE UNIQUE INDEX idx_user_phone_number ON "users" ("phone_number");
CREATE UNIQUE INDEX idx_user_email ON "users" ("email");
CREATE INDEX idx_user_role ON "users" ("role");
CREATE INDEX idx_user_created_at ON "users" ("created_at");
CREATE INDEX idx_user_updated_at ON "users" ("updated_at");

CREATE TABLE "address" (
  "id" bigint PRIMARY KEY,
  "user_id" bigint,
  "street" varchar,
  "city" varchar,
  "postal_code" varchar,
  "country" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE SEQUENCE IF NOT EXISTS address_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_address_street ON "address" ("street");
CREATE INDEX idx_address_city ON "address" ("city");
CREATE INDEX idx_address_postal_code ON "address" ("postal_code");
CREATE INDEX idx_address_country ON "address" ("country");
CREATE INDEX idx_address_created_at ON "address" ("created_at");

CREATE TABLE "product" (
  "id" bigint PRIMARY KEY,
  "user_id" bigint,
  "name" varchar,
  "description" varchar,
  "image_url" varchar,
  "price" decimal,
  "quantity" integer,
  "category_id" integer,
  "sku" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_product_user_id ON "product" ("user_id");
CREATE INDEX idx_product_name ON "product" ("name");
CREATE INDEX idx_product_price ON "product" ("price");
CREATE INDEX idx_product_quantity ON "product" ("quantity");
CREATE INDEX idx_product_category_id ON "product" ("category_id");
CREATE INDEX idx_product_sku ON "product" ("sku");
CREATE INDEX idx_product_created_at ON "product" ("created_at");

CREATE TABLE "category" (
  "id" bigint PRIMARY KEY,
  "parent_category_id" bigint,
  "name" varchar,
  "description" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_category_name ON "category" ("name");
CREATE INDEX idx_category_created_at ON "category" ("created_at");

CREATE TABLE "product_category" (
  "product_id" bigint,
  "category_id" bigint
);

CREATE INDEX idx_product_category_product_id ON "product_category" ("product_id");
CREATE INDEX idx_product_category_category_id ON "product_category" ("category_id");

CREATE TABLE "order" (
  "id" bigint PRIMARY KEY,
  "user_id" bigint,
  "order_date" timestamp,
  "total" decimal,
  "status" varchar,
  "shipping_address" varchar,
  "billing_address" varchar,
  "payment_method" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE SEQUENCE IF NOT EXISTS order_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_order_user_id ON "order" ("user_id");
CREATE INDEX idx_order_order_date ON "order" ("order_date");
CREATE INDEX idx_order_total ON "order" ("total");
CREATE INDEX idx_order_status ON "order" ("status");
CREATE INDEX idx_order_created_at ON "order" ("created_at");

CREATE TABLE "order_products" (
  "id" bigint PRIMARY KEY,
  "order_id" bigint,
  "product_id" bigint,
  "quantity" integer,
  "price" decimal
);

CREATE SEQUENCE IF NOT EXISTS order_products_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_order_products_order_id ON "order_products" ("order_id");
CREATE INDEX idx_order_products_product_id ON "order_products" ("product_id");

CREATE TABLE "token" (
  "id" bigint PRIMARY KEY,
  "user_id" bigint,
  "token" varchar,
  "token_type" varchar,
  "active" boolean,
  "created_at" timestamp,
  "expired_at" timestamp,
  "updated_at" timestamp
);

CREATE SEQUENCE IF NOT EXISTS token_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_token ON "token" ("token");
CREATE INDEX idx_token_user_id ON "token" ("user_id");

CREATE TABLE "reviews" (
  "id" bigint PRIMARY KEY,
  "product_id" bigint,
  "user_id" bigint,
  "rating" integer,
  "comment" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE SEQUENCE IF NOT EXISTS reviews_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_reviews_product_id ON "reviews" ("product_id");
CREATE INDEX idx_reviews_user_id ON "reviews" ("user_id");
CREATE INDEX idx_reviews_rating ON "reviews" ("rating");
CREATE INDEX idx_reviews_created_at ON "reviews" ("created_at");

CREATE TABLE "shipment" (
  "id" bigint PRIMARY KEY,
  "order_id" bigint,
  "shipping_date" timestamp,
  "delivery_date" timestamp,
  "carrier" varchar,
  "tracking_number" varchar,
  "status" varchar
);

CREATE SEQUENCE IF NOT EXISTS shipment_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_shipment_order_id ON "shipment" ("order_id");
CREATE INDEX idx_shipment_shipping_date ON "shipment" ("shipping_date");
CREATE INDEX idx_shipment_delivery_date ON "shipment" ("delivery_date");
CREATE INDEX idx_shipment_carrier ON "shipment" ("carrier");
CREATE INDEX idx_shipment_tracking_number ON "shipment" ("tracking_number");
CREATE INDEX idx_shipment_status ON "shipment" ("status");

CREATE TABLE "cart" (
  "id" bigint PRIMARY KEY,
  "user_id" bigint
);

CREATE SEQUENCE IF NOT EXISTS cart_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_cart_user_id ON "cart" ("user_id");

CREATE TABLE "cart_product" (
  "id" bigint PRIMARY KEY,
  "product_id" bigint,
  "cart_id" bigint,
  "quantity" integer
);

CREATE SEQUENCE IF NOT EXISTS cart_product_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_cart_product_product_id ON "cart_product" ("product_id");
CREATE INDEX idx_cart_product_cart_id ON "cart_product" ("cart_id");

ALTER TABLE "order" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "address" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "product_category" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "product_category" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id");

ALTER TABLE "product" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "reviews" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "reviews" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "cart" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "cart_product" ADD FOREIGN KEY ("cart_id") REFERENCES "cart" ("id");

ALTER TABLE "cart_product" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "shipment" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("id");
