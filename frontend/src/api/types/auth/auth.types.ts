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