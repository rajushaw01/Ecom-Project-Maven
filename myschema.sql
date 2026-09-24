-- =========================================================
-- E-COMMERCE HIBERNATE ASSIGNMENT
-- schema.sql
-- =========================================================

-- Select the database
USE ecommerce_db;


-- =========================================================
-- 1. CATEGORY
-- =========================================================

CREATE TABLE IF NOT EXISTS category (
                                        id INT NOT NULL AUTO_INCREMENT,
                                        name VARCHAR(50) NOT NULL,
    description VARCHAR(100),

    PRIMARY KEY (id),

    CONSTRAINT uk_category_name
    UNIQUE (name)
    ) ENGINE=InnoDB;


-- =========================================================
-- 2. USERS
-- =========================================================

CREATE TABLE IF NOT EXISTS users (
                                     id INT NOT NULL AUTO_INCREMENT,
                                     username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_users_username
    UNIQUE (username),

    CONSTRAINT uk_users_email
    UNIQUE (email)
    ) ENGINE=InnoDB;


-- =========================================================
-- 3. PRODUCT
-- =========================================================

CREATE TABLE IF NOT EXISTS product (
                                       id INT NOT NULL AUTO_INCREMENT,
                                       name VARCHAR(50) NOT NULL,
    price FLOAT(53) NOT NULL,
    stock_quantity INT,
    category_id INT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_product_category
    FOREIGN KEY (category_id)
    REFERENCES category(id)
    ) ENGINE=InnoDB;


-- =========================================================
-- 4. ORDERS
-- =========================================================

CREATE TABLE IF NOT EXISTS orders (
                                      id INT NOT NULL AUTO_INCREMENT,
                                      order_date DATETIME(6) NOT NULL,
    total_amount FLOAT(53) NOT NULL,
    user_id INT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_orders_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ) ENGINE=InnoDB;


-- =========================================================
-- 5. ORDER DETAILS
-- =========================================================

CREATE TABLE IF NOT EXISTS order_details (
                                             id INT NOT NULL AUTO_INCREMENT,
                                             quantity INT NOT NULL,
                                             unit_price FLOAT(53) NOT NULL,
    order_id INT NOT NULL,
    product_id INT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_order_details_order
    FOREIGN KEY (order_id)
    REFERENCES orders(id),

    CONSTRAINT fk_order_details_product
    FOREIGN KEY (product_id)
    REFERENCES product(id)
    ) ENGINE=InnoDB;