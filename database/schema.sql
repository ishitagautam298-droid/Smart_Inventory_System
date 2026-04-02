CREATE DATABASE IF NOT EXISTS smart_inventory;
USE smart_inventory;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id   INT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role      VARCHAR(20)  NOT NULL DEFAULT 'STAFF'
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    product_id    INT AUTO_INCREMENT PRIMARY KEY,
    product_name  VARCHAR(150) NOT NULL,
    category      VARCHAR(60)  NOT NULL,
    quantity      INT          NOT NULL DEFAULT 0,
    price         DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    reorder_level INT          NOT NULL DEFAULT 10
);

-- Suppliers table
CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id      INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name    VARCHAR(150) NOT NULL,
    contact_person   VARCHAR(100),
    email            VARCHAR(120),
    phone            VARCHAR(30),
    product_category VARCHAR(60),
    lead_time_days   INT         NOT NULL DEFAULT 5,
    status           VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    order_id     INT AUTO_INCREMENT PRIMARY KEY,
    product_id   INT           NOT NULL,
    supplier_id  INT           NOT NULL,
    quantity     INT           NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    order_date   DATE          NOT NULL,
    status       VARCHAR(30)   NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (product_id)  REFERENCES products(product_id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
);

-- Shipments table
CREATE TABLE IF NOT EXISTS shipments (
    shipment_id       INT AUTO_INCREMENT PRIMARY KEY,
    tracking_number   VARCHAR(60)  NOT NULL UNIQUE,
    order_id          INT          NOT NULL,
    product_name      VARCHAR(150),
    from_location     VARCHAR(150),
    to_location       VARCHAR(150),
    carrier           VARCHAR(60),
    estimated_arrival VARCHAR(30),
    status            VARCHAR(30)  NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- ─────────────────────────────────────────────
--  Sample seed data
-- ─────────────────────────────────────────────

INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'Admin User', 'ADMIN');

INSERT INTO products (product_name, category, quantity, price, reorder_level) VALUES
('Laptop 15" Pro',       'Electronics', 142, 1299.99, 20),
('USB-C Hub 7-Port',     'Accessories', 215,   49.99, 30),
('Office Chair Ergo',    'Furniture',    12,  399.00, 15),
('Mechanical Keyboard',  'Peripherals',  87,  129.00, 25),
('Monitor 27" IPS',      'Electronics',  63,  459.99, 10),
('Wireless Mouse',       'Peripherals', 134,   59.99, 40),
('Printer Paper A4',     'Stationery',   20,   12.50, 50),
('USB-C Cable 2m',       'Accessories',   2,   15.99, 30),
('Standing Desk',        'Furniture',     8,  649.00,  5),
('Webcam 1080p',         'Electronics',  45,   99.99, 15);

INSERT INTO suppliers (supplier_name, contact_person, email, phone, product_category, lead_time_days, status) VALUES
('TechWorld Inc.',  'John Smith',   'john@techworld.com',  '555-1001', 'Electronics', 5, 'ACTIVE'),
('AccessPro Ltd.',  'Emma Johnson', 'emma@accesspro.com',  '555-1002', 'Accessories', 3, 'ACTIVE'),
('OfficeFurn Co.',  'Bob Williams', 'bob@officefurn.com',  '555-1003', 'Furniture',   7, 'ACTIVE'),
('PaperMills Ltd.', 'Alice Brown',  'alice@papermills.com','555-1004', 'Stationery',  2, 'ACTIVE'),
('PeriphWorld.',    'Carlos Diaz',  'carlos@periph.com',   '555-1005', 'Peripherals', 4, 'ACTIVE');

INSERT INTO orders (product_id, supplier_id, quantity, total_amount, order_date, status) VALUES
(1, 1, 10, 12999.90, '2026-03-28', 'SHIPPED'),
(2, 2, 50,  2499.50, '2026-03-27', 'PROCESSING'),
(3, 3,  5,  1995.00, '2026-03-26', 'PENDING'),
(7, 4,100,  1250.00, '2026-03-25', 'DELIVERED');
