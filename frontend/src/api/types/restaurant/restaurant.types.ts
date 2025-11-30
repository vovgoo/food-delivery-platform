import type { ImageResponse } from '../common';

export type RestaurantStatus = 'ACTIVE' | 'INACTIVE' | 'CLOSED';

export interface RestaurantResponse {
  id: string;
  name: string;
  description?: string;
  cuisine: string;
  address: string;
  website?: string;
  profileImage?: ImageResponse;
  phone: string;
  openingTime: string;
  closingTime: string;
  deliveryAvailable: boolean;
  parkingAvailable: boolean;
  status: RestaurantStatus;
  images?: ImageResponse[];
}

export interface RestaurantCreateRequest {
  name: string;
  description?: string;
  cuisine: string;
  address: string;
  website?: string;
  phone: string;
  openingTime: string;
  closingTime: string;
  deliveryAvailable: boolean;
  parkingAvailable: boolean;
}

export interface RestaurantUpdateRequest extends RestaurantCreateRequest {
  status: RestaurantStatus;
}

export type OrderRestaurantResponse = {
  id: string;
  name: string;
  cuisine: string;
  address: string;
  phone: string;
  profileImageUrl: string;
};
