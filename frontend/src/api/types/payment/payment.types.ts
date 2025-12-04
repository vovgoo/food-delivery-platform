export type PaymentMethod =
  | 'CREDIT_CARD'
  | 'DEBIT_CARD'
  | 'PAYPAL'
  | 'APPLE_PAY'
  | 'GOOGLE_PAY'
  | 'BANK_TRANSFER'
  | 'CASH_ON_DELIVERY';

export type PaymentStatus =
  | 'PENDING'
  | 'PROCESSING'
  | 'COMPLETED'
  | 'FAILED'
  | 'CANCELLED'
  | 'REFUNDED';

export interface PaymentResponse {
  id: string;
  method: PaymentMethod;
  status: PaymentStatus;
}

export interface AddPaymentRequest {
  paymentMethod: PaymentMethod;
}
