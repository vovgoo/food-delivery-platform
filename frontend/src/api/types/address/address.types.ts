export interface AddressResponse {
  id: string;
  country: string;
  state: string;
  city: string;
  street: string;
  house: string;
  building?: string;
  apartment?: string;
  deliveryInstructions?: string;
  zip: string;
  isDefault: boolean;
}

export interface CreateAddressRequest {
  country: string;
  state: string;
  city: string;
  street: string;
  house: string;
  building?: string;
  apartment?: string;
  deliveryInstructions?: string;
  zip: string;
}

export type OrderAddressResponse = {
  id: string;
  city: string;
  street: string;
  house: string;
  apartment: string;
};
