
-- Customer Table
CREATE TABLE IF NOT EXISTS customer (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$')
);

-- Product Table
CREATE TABLE IF NOT EXISTS product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price NUMERIC(10, 2) NOT NULL CHECK (price > 0),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Orders Table (renamed to avoid SQL keyword conflict)
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    total_amount NUMERIC(10, 2),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
    CONSTRAINT status_check CHECK (status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'))
);

-- OrderItem Table
CREATE TABLE IF NOT EXISTS order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10, 2) NOT NULL CHECK (unit_price > 0),
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT
);

-- =====================================================
-- 3. CREATE INDEXES FOR PERFORMANCE
-- =====================================================

-- Indexes on Customer
CREATE INDEX idx_customer_email ON customer(email);

-- Indexes on Product
CREATE INDEX idx_product_name ON product(name);
CREATE INDEX idx_product_active ON product(is_active);

-- Indexes on Orders
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);

-- Indexes on OrderItem
CREATE INDEX idx_order_item_order_id ON order_item(order_id);
CREATE INDEX idx_order_item_product_id ON order_item(product_id);

-- =====================================================
-- 4. INSERT SAMPLE DATA FOR DEVELOPMENT
-- =====================================================

-- Sample Customers
INSERT INTO customer (name, phone, email) VALUES
    ('Juan Pérez', 3312345678, 'juan.perez@example.com'),
    ('María González', 3398765432, 'maria.gonzalez@example.com'),
    ('Carlos Rodríguez', 3387654321, 'carlos.rodriguez@example.com'),
    ('Ana Martínez', 3376543210, 'ana.martinez@example.com'),
    ('Luis Hernández', 3365432109, 'luis.hernandez@example.com')
ON CONFLICT (email) DO NOTHING;

-- Sample Products
INSERT INTO product (name, description, price, is_active) VALUES
    ('Laptop Dell XPS 15', 'Laptop de alto rendimiento con procesador Intel Core i7, 16GB RAM, 512GB SSD', 25999.99, TRUE),
    ('iPhone 15 Pro', 'Smartphone Apple con chip A17 Pro, 256GB, cámara de 48MP', 29999.99, TRUE),
    ('Mouse Logitech MX Master 3', 'Mouse inalámbrico ergonómico para productividad', 1299.99, TRUE),
    ('Teclado Mecánico Keychron K2', 'Teclado mecánico inalámbrico con switches Gateron', 1899.99, TRUE),
    ('Monitor LG UltraWide 34"', 'Monitor curvo UltraWide QHD 3440x1440, 144Hz', 12999.99, TRUE),
    ('Audífonos Sony WH-1000XM5', 'Audífonos inalámbricos con cancelación de ruido', 6999.99, TRUE),
    ('iPad Air M1', 'Tablet Apple con chip M1, 64GB, pantalla 10.9"', 14999.99, TRUE),
    ('Samsung Galaxy S23 Ultra', 'Smartphone Samsung con S Pen, 512GB', 27999.99, TRUE),
    ('Webcam Logitech C920', 'Cámara web Full HD 1080p para videollamadas', 1499.99, TRUE),
    ('SSD Samsung 980 PRO 1TB', 'Unidad de estado sólido NVMe Gen 4', 2899.99, TRUE)
ON CONFLICT DO NOTHING;

-- Sample Orders
WITH inserted_order AS (
    INSERT INTO orders (customer_id, status, total_amount, created_at)
    VALUES (1, 'DELIVERED', 53299.97, CURRENT_TIMESTAMP - INTERVAL '10 days')
    RETURNING id
)
INSERT INTO order_item (order_id, product_id, quantity, unit_price)
SELECT id, 1, 2, 25999.99 FROM inserted_order
UNION ALL
SELECT id, 3, 1, 1299.99 FROM inserted_order;

WITH inserted_order AS (
    INSERT INTO orders (customer_id, status, total_amount, created_at)
    VALUES (2, 'SHIPPED', 29999.99, CURRENT_TIMESTAMP - INTERVAL '3 days')
    RETURNING id
)
INSERT INTO order_item (order_id, product_id, quantity, unit_price)
SELECT id, 2, 1, 29999.99 FROM inserted_order;

WITH inserted_order AS (
    INSERT INTO orders (customer_id, status, total_amount, created_at)
    VALUES (1, 'PENDING', 14899.98, CURRENT_TIMESTAMP - INTERVAL '1 day')
    RETURNING id
)
INSERT INTO order_item (order_id, product_id, quantity, unit_price)
SELECT id, 4, 2, 1899.99 FROM inserted_order
UNION ALL
SELECT id, 9, 3, 1499.99 FROM inserted_order
UNION ALL
SELECT id, 10, 4, 2899.99 FROM inserted_order;