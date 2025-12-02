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

export type OrderUserResponse = {
  id: string;
  email: string;
  phone: string;
  fullName: string;
  birthDate: string;
  userStatus: UserStatus;
};

export interface UpdateUserProfileRequest {
  fullName: string;
  birthDate: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

export interface ChangeEmailRequest {
  email: string;
}

export interface ConfirmChangeEmailRequest {
  token: string;
}

export interface ChangePhoneRequest {
  phone: string;
}

export interface ConfirmChangePhoneRequest {
  code: string;
}
