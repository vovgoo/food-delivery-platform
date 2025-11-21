CREATE TABLE orders (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        status TEXT NOT NULL CHECK (status IN (
                                                               'CREATED',
                                                               'CONFIRMED',
                                                               'PREPARING',
                                                               'READY',
                                                               'DELIVERING',
                                                               'COMPLETED',
                                                               'CANCELLED'
                            )),
                        order_date TIMESTAMP NOT NULL DEFAULT NOW(),
                        user_id UUID NOT NULL,
                        delivery_address UUID NOT NULL,
                        restaurant_id UUID NOT NULL,
                        total_price NUMERIC(10,2) NOT NULL CHECK (total_price >= 0)
);