import axios, { type AxiosRequestConfig, type AxiosError, type AxiosHeaders } from 'axios';
import { AppRoutes } from '@/routes';
import { authService } from '../services';

export interface JwtResponse {
  accessToken: string;
  refreshToken?: string;
}

export class ApiClient {
  private axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
    timeout: 30000,
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true,
  });

  private isRefreshing = false;
  private failedQueue: Array<{ resolve: (token: string) => void; reject: (err: any) => void }> = [];

  constructor() {
    this.setupInterceptors();
  }

  private processQueue(error: any, token: string | null = null) {
    this.failedQueue.forEach(({ resolve, reject }) => {
      if (error) reject(error);
      else resolve(token!);
    });
    this.failedQueue = [];
  }

  private setupInterceptors() {
    this.axiosInstance.interceptors.request.use((config) => {
      if (!config.url?.includes('/api/v1/auth/refresh')) {
        const token = localStorage.getItem('accessToken');
        if (token) {
          (config.headers as AxiosHeaders).set?.('Authorization', `Bearer ${token}`) ||
          ((config.headers as any)['Authorization'] = `Bearer ${token}`);
        }
      }
      return config;
    });

    this.axiosInstance.interceptors.response.use(
      (response) => response,
      async (error: AxiosError & { config?: any }) => {
        const originalRequest = error?.config;
        if (!originalRequest) return Promise.reject(error);

        const status = error.response?.status ?? (error.request ? 401 : undefined);
        const body = (error.response?.data as any)?.body;

        if (status === 403 && body === "Пользователь деактивирован" && !originalRequest.skipDeactivateRedirect) {
          window.location.href = AppRoutes.USER_DEACTIVATE;
          return Promise.reject(error);
        }

        if (status === 403 && body === "Пользователь заблокирован" && !originalRequest.skipDeactivateRedirect) {
          window.location.href = AppRoutes.USER_BLOCKED;
          return Promise.reject(error);
        }

        if (status === 401 && originalRequest.url?.includes('/api/v1/auth/refresh')) {
          localStorage.removeItem('accessToken');
          window.location.href = AppRoutes.MAIN;
          return Promise.reject(error);
        }

        if (status === 401 && !originalRequest._retry && !originalRequest.url?.includes('/signIn')) {
          const access = localStorage.getItem('accessToken');

          if (!access) {
            window.location.href = AppRoutes.MAIN;
            return Promise.reject(error);
          }

          originalRequest._retry = true;

          if (this.isRefreshing) {
            return new Promise((resolve, reject) => {
              this.failedQueue.push({ resolve, reject });
            }).then((token) => {
              (originalRequest.headers as AxiosHeaders).set?.('Authorization', `Bearer ${token}`) ||
              ((originalRequest.headers as any)['Authorization'] = `Bearer ${token}`);
              return this.axiosInstance(originalRequest);
            });
          }

          this.isRefreshing = true;

          try {
            const response = await authService.refresh();
            localStorage.setItem('accessToken', response.accessToken);
            this.processQueue(null, response.accessToken);

            (originalRequest.headers as AxiosHeaders).set?.('Authorization', `Bearer ${response.accessToken}`) ||
            ((originalRequest.headers as any)['Authorization'] = `Bearer ${response.accessToken}`);

            return this.axiosInstance(originalRequest);
          } catch (refreshError) {
            this.processQueue(refreshError, null);
            localStorage.removeItem('accessToken');
            window.location.href = AppRoutes.MAIN;
            return Promise.reject(refreshError);
          } finally {
            this.isRefreshing = false;
          }
        }

        return Promise.reject(error);
      }
    );
  }

  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return (await this.axiosInstance.get<T>(url, config)).data;
  }

  async post<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return (await this.axiosInstance.post<T>(url, data, config)).data;
  }

  async put<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return (await this.axiosInstance.put<T>(url, data, config)).data;
  }

  async patch<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return (await this.axiosInstance.patch<T>(url, data, config)).data;
  }

  async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return (await this.axiosInstance.delete<T>(url, config)).data;
  }

  get instance() {
    return this.axiosInstance;
  }
}

export const apiClient = new ApiClient();
