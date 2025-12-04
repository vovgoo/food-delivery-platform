import type { OrderUserResponse } from '../user';
import type { OrderAddressResponse } from '../address';
import type { PaymentMethod, PaymentResponse } from '../payment';
import type { OrderDishResponse } from '../dish';
import type { OrderRestaurantResponse } from '../restaurant';

export type OrderStatus =
  | 'CREATED'
  | 'CONFIRMED'
  | 'PREPARING'
  | 'READY'
  | 'DELIVERING'
  | 'COMPLETED'
  | 'CANCELLED';

export interface AddOrderItemRequest {
  dishId: string;
  quantity: number;
}

export interface CreateOrderRequest {
  restaurantId: string;
  deliveryAddress: string;
  items: AddOrderItemRequest[];
  payment: {
    paymentMethod: PaymentMethod;
  };
}

export interface UpdateOrderStatusRequest {
  status: OrderStatus;
}

export interface OrderItemResponse {
  id: string;
  dish: OrderDishResponse;
  quantity: number;
  price: number;
}

export interface OrderResponse {
  id: string;
  user: OrderUserResponse;
  status: OrderStatus;
  orderDate: string;
  address: OrderAddressResponse;
  restaurant: OrderRestaurantResponse;
  totalPrice: number;
  items: OrderItemResponse[];
  payment: PaymentResponse;
}
