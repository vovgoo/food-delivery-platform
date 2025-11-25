ALTER TABLE payments
    ADD CONSTRAINT chk_payments_amount CHECK (amount >= 0.01);
