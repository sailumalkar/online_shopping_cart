CREATE DATABASE shoppingcartdb;
USE shoppingcartdb;

CREATE TABLE products (
    product_id VARCHAR(10) PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    product_price DOUBLE NOT NULL
);

CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(10),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- All products (original + new)
INSERT INTO products (product_id, product_name, product_price) VALUES
('P001', 'Laptop', 999.99),
('P002', 'Smartphone', 499.99),
('P003', 'Headphones', 79.99),
('P004', 'Mouse', 29.99),
('P005', 'Keyboard', 49.99),
('P006', 'Monitor', 199.99),
('P007', 'Tablet', 299.99),
('P008', 'Smartwatch', 149.99),
('P009', 'Camera', 599.99),
('P010', 'Printer', 129.99),
('P011', 'External Hard Drive', 89.99),
('P012', 'USB Flash Drive', 19.99),
('P013', 'Gaming Console', 399.99),
('P014', 'Wireless Router', 69.99),
('P015', 'Bluetooth Speaker', 59.99);
