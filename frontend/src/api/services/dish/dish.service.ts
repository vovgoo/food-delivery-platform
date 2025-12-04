import type {
  DishCreateRequest,
  DishResponse,
  DishShortResponse,
  DishUpdateRequest,
  PageParams,
  PageResponse,
} from '@/api/types';
import { apiClient } from '../../core/api-client';

export class DishService {
  private readonly basePath = '/api/v1/restaurants';

  async get(restaurantId: string, dishId: string): Promise<DishResponse> {
    return apiClient.get<DishResponse>(`${this.basePath}/${restaurantId}/dishes/${dishId}`);
  }

  async create(restaurantId: string, data: DishCreateRequest): Promise<DishResponse> {
    return apiClient.post<DishResponse>(`${this.basePath}/${restaurantId}/dishes`, data);
  }

  async update(
    restaurantId: string,
    dishId: string,
    data: DishUpdateRequest,
  ): Promise<DishResponse> {
    return apiClient.put<DishResponse>(`${this.basePath}/${restaurantId}/dishes/${dishId}`, data);
  }

  async delete(restaurantId: string, dishId: string): Promise<void> {
    return apiClient.delete<void>(`${this.basePath}/${restaurantId}/dishes/${dishId}`);
  }

  async list(restaurantId: string, page: PageParams): Promise<PageResponse<DishShortResponse>> {
    return apiClient.get<PageResponse<DishShortResponse>>(
      `${this.basePath}/${restaurantId}/dishes`,
      { params: page },
    );
  }

  async setProfileImage(restaurantId: string, dishId: string, file: File): Promise<void> {
    const formData = new FormData();
    formData.append('file', file);
    await apiClient.put<void>(
      `${this.basePath}/${restaurantId}/dishes/${dishId}/profile`,
      formData,
      { headers: { 'Content-Type': 'multipart/form-data' } },
    );
  }

  async removeProfileImage(restaurantId: string, dishId: string): Promise<void> {
    await apiClient.delete<void>(`${this.basePath}/${restaurantId}/dishes/${dishId}/profile`);
  }

  async addImage(restaurantId: string, dishId: string, file: File): Promise<void> {
    const formData = new FormData();
    formData.append('file', file);
    await apiClient.post<void>(
      `${this.basePath}/${restaurantId}/dishes/${dishId}/images`,
      formData,
      { headers: { 'Content-Type': 'multipart/form-data' } },
    );
  }

  async removeImage(restaurantId: string, dishId: string, imageId: string): Promise<void> {
    await apiClient.delete<void>(
      `${this.basePath}/${restaurantId}/dishes/${dishId}/images/${imageId}`,
    );
  }
}

export const dishService = new DishService();
