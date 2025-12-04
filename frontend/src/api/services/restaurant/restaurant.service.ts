import type {
  PageParams,
  PageResponse,
  RestaurantCreateRequest,
  RestaurantResponse,
  RestaurantSearchRequest,
  RestaurantShortResponse,
  RestaurantUpdateRequest,
} from '@/api/types';
import { apiClient } from '../../core/api-client';

export class RestaurantService {
  private readonly basePath = '/api/v1/restaurants';

  async get(restaurantId: string): Promise<RestaurantResponse> {
    return apiClient.get<RestaurantResponse>(`${this.basePath}/${restaurantId}`);
  }

  async create(data: RestaurantCreateRequest): Promise<RestaurantResponse> {
    return apiClient.post<RestaurantResponse>(this.basePath, data);
  }

  async update(restaurantId: string, data: RestaurantUpdateRequest): Promise<RestaurantResponse> {
    return apiClient.put<RestaurantResponse>(`${this.basePath}/${restaurantId}`, data);
  }

  async delete(restaurantId: string): Promise<void> {
    return apiClient.delete<void>(`${this.basePath}/${restaurantId}`);
  }

  async list(
    search: RestaurantSearchRequest,
    page: PageParams,
  ): Promise<PageResponse<RestaurantShortResponse>> {
    return apiClient.get<PageResponse<RestaurantShortResponse>>(this.basePath, {
      params: { ...search, ...page },
    });
  }

  async setProfileImage(restaurantId: string, file: File): Promise<void> {
    const formData = new FormData();
    formData.append('file', file);
    await apiClient.put<void>(`${this.basePath}/${restaurantId}/images/profile`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  }

  async removeProfileImage(restaurantId: string): Promise<void> {
    await apiClient.delete<void>(`${this.basePath}/${restaurantId}/images/profile`);
  }

  async addImage(restaurantId: string, file: File): Promise<void> {
    const formData = new FormData();
    formData.append('file', file);
    await apiClient.post<void>(`${this.basePath}/${restaurantId}/images`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  }

  async removeImage(restaurantId: string, imageId: string): Promise<void> {
    await apiClient.delete<void>(`${this.basePath}/${restaurantId}/images/${imageId}`);
  }
}

export const restaurantService = new RestaurantService();
