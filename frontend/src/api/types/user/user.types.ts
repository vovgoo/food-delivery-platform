import type { AddressResponse } from '../address';

export type UserStatus = 'ACTIVE' | 'DEACTIVATED' | 'BLOCKED';

export interface RoleResponse {
  name: string;
}

export interface UserResponse {
  id: string;
  email: string;
  phone: string;
  fullName: string;
  birthDate: string;
  userStatus: UserStatus;
  createdAt: string;
  updatedAt: string;
  defaultAddress?: AddressResponse;
  roles: RoleResponse[];
}

export interface UpdateUserProfileRequest {
  fullName: string;
  birthDate: string;
}

export type OrderUserResponse = {
  id: string;
  email: string;
  phone: string;
  fullName: string;
  birthDate: string;
  userStatus: UserStatus;
};
