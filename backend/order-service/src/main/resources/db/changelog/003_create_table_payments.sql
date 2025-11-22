CREATE TABLE payments (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          order_id UUID NOT NULL UNIQUE,
                          method TEXT NOT NULL CHECK (method IN (
                                                                 'CREDIT_CARD',
                                                                 'DEBIT_CARD',
                                                                 'PAYPAL',
                                                                 'APPLE_PAY',
                                                                 'GOOGLE_PAY',
                                                                 'BANK_TRANSFER',
                                                                 'CASH_ON_DELIVERY'
                              )),
                          amount NUMERIC(10,2) NOT NULL CHECK (amount > 0),
                          status TEXT NOT NULL CHECK (status IN (
                                                                 'PENDING',
                                                                 'PROCESSING',
                                                                 'COMPLETED',
                                                                 'FAILED',
                                                                 'CANCELLED',
                                                                 'REFUNDED'
                              )),
                          payment_date TIMESTAMP NOT NULL DEFAULT NOW(),
                          CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);