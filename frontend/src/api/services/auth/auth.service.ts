import { apiClient } from '../../core/api-client';
import type { ConfirmSignUpRequest, JwtResponse, SignInRequest, SignUpRequest } from '../../types';

export class AuthService {
  private readonly basePath = '/api/v1/auth';

  async signUp(data: SignUpRequest): Promise<void> {
    await apiClient.post<void>(`${this.basePath}/signUp`, data);
  }

  async confirmSignUp(data: ConfirmSignUpRequest): Promise<JwtResponse> {
    return await apiClient.post<JwtResponse>(`${this.basePath}/confirmSignUp`, data);
  }

  async signIn(data: SignInRequest): Promise<JwtResponse> {
    return await apiClient.post<JwtResponse>(`${this.basePath}/signIn`, data);
  }

  async refresh(): Promise<JwtResponse> {
    return await apiClient.post<JwtResponse>(`${this.basePath}/refresh`, undefined, {
      withCredentials: true,
    });
  }
}

export const authService = new AuthService();
