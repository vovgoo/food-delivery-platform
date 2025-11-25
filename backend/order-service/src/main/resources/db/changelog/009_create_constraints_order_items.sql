ALTER TABLE order_items
    ADD CONSTRAINT chk_order_items_quantity CHECK (quantity >= 1),
    ADD CONSTRAINT chk_order_items_price CHECK (price >= 0.01);
