import type { ImageResponse } from '../common';

export type DishStatus = 'AVAILABLE' | 'TEMPORARY_UNAVAILABLE' | 'REMOVED';

export interface DishResponse {
  id: string;
  name: string;
  description?: string;
  profileImage?: ImageResponse;
  portionInGrams?: number;
  proteins?: number;
  fats?: number;
  carbohydrates?: number;
  spicy: boolean;
  vegan: boolean;
  vegetarian: boolean;
  price: number;
  status: DishStatus;
  images?: ImageResponse[];
}

export interface DishShortResponse {
  id: string;
  name: string;
  description?: string;
  profileImage?: ImageResponse;
  portionInGrams?: number;
  proteins?: number;
  fats?: number;
  carbohydrates?: number;
  spicy: boolean;
  vegan: boolean;
  vegetarian: boolean;
  price: number;
  status: DishStatus;
}

export interface DishCreateRequest {
  name: string;
  description?: string;
  portionInGrams?: number;
  proteins?: number;
  fats?: number;
  carbohydrates?: number;
  spicy: boolean;
  vegan: boolean;
  vegetarian: boolean;
  price: number;
}

export interface DishUpdateRequest extends DishCreateRequest {
  status: DishStatus;
}

export type OrderDishResponse = {
  id: string;
  name: string;
  profileImageUrl: string;
};
