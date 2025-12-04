import type { 
  CreateOrderRequest, 
  OrderResponse, 
  PageParams, 
  PageResponse, 
  UpdateOrderStatusRequest 
} from '@/api/types';
import { apiClient } from '@/api/core/api-client';

export class OrderService {
  private readonly basePath = '/api/v1/orders';

  async list(page: PageParams): Promise<PageResponse<OrderResponse>> {
    return apiClient.get<PageResponse<OrderResponse>>(this.basePath, { params: page });
  }

  async create(data: CreateOrderRequest): Promise<OrderResponse> {
    return apiClient.post<OrderResponse>(this.basePath, data);
  }

  async get(orderId: string): Promise<OrderResponse> {
    return apiClient.get<OrderResponse>(`${this.basePath}/${orderId}`);
  }

  async updateStatus(orderId: string, data: UpdateOrderStatusRequest): Promise<OrderResponse> {
    return apiClient.put<OrderResponse>(`${this.basePath}/${orderId}/status`, data);
  }
}

export const orderService = new OrderService();
