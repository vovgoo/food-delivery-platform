CREATE TYPE payment_status AS ENUM (
    'PENDING',
    'PROCESSING',
    'COMPLETED',
    'FAILED',
    'CANCELLED',
    'REFUNDED'
);

CREATE TYPE payment_method AS ENUM (
    'CREDIT_CARD',
    'DEBIT_CARD',
    'PAYPAL',
    'APPLE_PAY',
    'GOOGLE_PAY',
    'BANK_TRANSFER',
    'CASH_ON_DELIVERY'
);

CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    method payment_method NOT NULL,
    amount NUMERIC(10,2) NOT NULL CHECK (amount > 0),
    status payment_status NOT NULL,
    payment_date TIMESTAMP NOT NULL DEFAULT NOW()
);
