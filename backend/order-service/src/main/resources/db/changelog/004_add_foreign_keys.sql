ALTER TABLE orders
    ADD CONSTRAINT fk_payment FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE SET NULL;