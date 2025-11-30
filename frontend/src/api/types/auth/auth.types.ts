export interface SignUpRequest {
  phone: string;
  fullName: string;
  birthDate: string;
  password: string;
}

export interface SignInRequest {
  phone: string;
  password: string;
}

export interface ConfirmSignUpRequest {
  phone: string;
  code: string;
}

export interface JwtResponse {
  accessToken: string;
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
