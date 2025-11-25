ALTER TABLE orders
    ADD CONSTRAINT chk_orders_total_price CHECK (total_price >= 0);
