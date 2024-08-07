CREATE TABLE "user" (
  "id" bigint PRIMARY KEY,
  "username" varchar,
  "email" varchar,
  "password" varchar,
  "role" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE UNIQUE INDEX idx_user_username ON "user" ("username");
CREATE UNIQUE INDEX idx_user_email ON "user" ("email");
CREATE INDEX idx_user_role ON "user" ("role");
CREATE INDEX idx_user_created_at ON "user" ("created_at");
CREATE INDEX idx_user_updated_at ON "user" ("updated_at");

CREATE TABLE "token" (
  "user_id" bigint,
  "token" varchar,
  "token_type" varchar,
  "created_at" timestamp,
  "expires_at" timestamp,
  "updated_at" timestamp
);

CREATE UNIQUE INDEX idx_token_user_id ON "token" ("user_id");
CREATE UNIQUE INDEX idx_token_token ON "token" ("token");
CREATE INDEX idx_token_expires_at ON "token" ("expires_at");
CREATE INDEX idx_token_created_at ON "token" ("created_at");

CREATE TABLE "product" (
  "id" bigint PRIMARY KEY,
  "user_id" bigint,
  "name" varchar,
  "description" varchar,
  "price" decimal,
  "quantity" integer,
  "category_id" integer,
  "sku" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE INDEX idx_product_user_id ON "product" ("user_id");
CREATE INDEX idx_product_name ON "product" ("name");
CREATE INDEX idx_product_price ON "product" ("price");
CREATE INDEX idx_product_quantity ON "product" ("quantity");
CREATE INDEX idx_product_category_id ON "product" ("category_id");
CREATE INDEX idx_product_sku ON "product" ("sku");
CREATE INDEX idx_product_created_at ON "product" ("created_at");

CREATE TABLE "category" (
  "id" bigint PRIMARY KEY,
  "name" varchar,
  "description" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE INDEX idx_category_name ON "category" ("name");
CREATE INDEX idx_category_created_at ON "category" ("created_at");

CREATE TABLE "product_category" (
  "product_id" bigint,
  "category_id" bigint
);

CREATE INDEX idx_product_category_product_id ON "product_category" ("product_id");
CREATE INDEX idx_product_category_category_id ON "product_category" ("category_id");

CREATE TABLE "orders" (
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

CREATE INDEX idx_orders_user_id ON "orders" ("user_id");
CREATE INDEX idx_orders_order_date ON "orders" ("order_date");
CREATE INDEX idx_orders_total ON "orders" ("total");
CREATE INDEX idx_orders_status ON "orders" ("status");
CREATE INDEX idx_orders_created_at ON "orders" ("created_at");

CREATE TABLE "order_products" (
  "id" bigint PRIMARY KEY,
  "order_id" bigint,
  "product_id" bigint,
  "quantity" integer,
  "price" decimal
);

CREATE INDEX idx_order_products_order_id ON "order_products" ("order_id");
CREATE INDEX idx_order_products_product_id ON "order_products" ("product_id");

CREATE TABLE "reviews" (
  "id" bigint PRIMARY KEY,
  "product_id" bigint,
  "user_id" bigint,
  "rating" integer,
  "comment" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

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

CREATE INDEX idx_cart_user_id ON "cart" ("user_id");

CREATE TABLE "cart_product" (
  "product_id" bigint,
  "cart_id" bigint,
  "quantity" integer
);

CREATE INDEX idx_cart_product_product_id ON "cart_product" ("product_id");
CREATE INDEX idx_cart_product_cart_id ON "cart_product" ("cart_id");

ALTER TABLE "token" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "orders" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "product_category" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "product_category" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id");

ALTER TABLE "product" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "reviews" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "reviews" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "cart" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "cart_product" ADD FOREIGN KEY ("cart_id") REFERENCES "cart" ("id");

ALTER TABLE "cart_product" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "shipment" ADD FOREIGN KEY ("order_id") REFERENCES "orders" ("id");
