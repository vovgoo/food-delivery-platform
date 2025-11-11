CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    method VARCHAR(30) NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT chk_payments_amount CHECK (amount > 0),
    CONSTRAINT chk_payments_method CHECK (method IN (
        'CREDIT_CARD','DEBIT_CARD','PAYPAL','APPLE_PAY','GOOGLE_PAY','BANK_TRANSFER','CASH_ON_DELIVERY'
    )),
    CONSTRAINT chk_payments_status CHECK (status IN (
        'PENDING','PROCESSING','COMPLETED','FAILED','CANCELLED','REFUNDED'
    ))
);
