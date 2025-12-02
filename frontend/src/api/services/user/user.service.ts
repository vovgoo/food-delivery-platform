import type { ChangeEmailRequest, ChangePasswordRequest, ChangePhoneRequest, ConfirmChangeEmailRequest, ConfirmChangePhoneRequest, UpdateUserProfileRequest, UserResponse } from '@/api/types';
import { apiClient } from '../../core/api-client';

export class UserService {
  private readonly basePath = '/api/v1/users';

  async me(): Promise<UserResponse> {
    return await apiClient.get<UserResponse>(`${this.basePath}/me`);
  }

  async deactivate(): Promise<void> {
    await apiClient.put<void>(`${this.basePath}/me/deactivate`);
  }

  async reactivate(): Promise<void> {
    await apiClient.put<void>(`${this.basePath}/me/reactivate`);
  }

  async changeProfile(data: UpdateUserProfileRequest): Promise<UserResponse> {
    return await apiClient.put<UserResponse>(`${this.basePath}/me/profile`, data);
  }

  async changePhone(data: ChangePhoneRequest): Promise<void> {
    await apiClient.put<void>(`${this.basePath}/me/phone`, data);
  }

  async confirmChangePhone(data: ConfirmChangePhoneRequest): Promise<void> {
    await apiClient.put<void>(`${this.basePath}/me/phone/confirm`, data);
  }

  async changePassword(data: ChangePasswordRequest): Promise<void> {
    await apiClient.put<void>(`${this.basePath}/me/password`, data);
  }

  async changeEmail(data: ChangeEmailRequest): Promise<void> {
    await apiClient.put<void>(`${this.basePath}/me/email`, data);
  }

  async confirmChangeEmail(data: ConfirmChangeEmailRequest): Promise<void> {
    await apiClient.put<void>(`${this.basePath}/me/email/confirm`, data);
  }

  async logout(): Promise<void> {
    await apiClient.post<void>(`${this.basePath}/logout`);
  }
}

export const userService = new UserService();
