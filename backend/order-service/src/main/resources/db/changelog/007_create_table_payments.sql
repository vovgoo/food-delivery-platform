CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
    method payment_method NOT NULL,
    amount NUMERIC(10,2) NOT NULL CHECK (amount >= 0.01),
    status payment_status NOT NULL,
    payment_date TIMESTAMP NOT NULL DEFAULT now()
);
