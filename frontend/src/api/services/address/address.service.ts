import type { AddressResponse, CreateAddressRequest, PageParams, PageResponse } from '@/api/types';
import { apiClient } from '../../core/api-client';

export class AddressService {
  private readonly basePath = '/api/v1/address';

  async setDefault(id: string): Promise<void> {
    return await apiClient.put<void>(`${this.basePath}/me/default/${id}`);
  }

  async create(data: CreateAddressRequest): Promise<AddressResponse> {
    return await apiClient.post<AddressResponse>(`${this.basePath}/me/create`, data);
  }

  async get(params: PageParams): Promise<PageResponse<AddressResponse>> {
    const query = new URLSearchParams({
      page: params.page.toString(),
      size: params.size.toString(),
    }).toString();
    return await apiClient.get<PageResponse<AddressResponse>>(`${this.basePath}/me/get?${query}`);
  }

  async remove(id: string): Promise<void> {
    await apiClient.delete<void>(`${this.basePath}/me/remove/${id}`);
  }
}

export const addressService = new AddressService();
