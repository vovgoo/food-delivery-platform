import React from 'react';
import { Button } from '@/components/ui/button';
import { Plus, Minus, ImageIcon } from 'lucide-react';
import { useDispatch } from 'react-redux';
import { removeItem, updateQuantity } from '@/store/slices/cartSlice';
import type { DishResponse } from '@/api';
import { Badge } from '../ui/badge';

interface CartDishCardProps {
  restaurantId: string;
  dish: DishResponse;
  quantity: number;
}

export const CartDishCard: React.FC<CartDishCardProps> = ({ restaurantId, dish, quantity }) => {
  const dispatch = useDispatch();

  const isAvailable = dish.status === 'AVAILABLE';

  return (
    <div className="flex items-center justify-between p-2 bg-gray-100 rounded-lg">
      <div className="flex items-center gap-2">
        {dish.profileImage?.url ? (
          <img
            src={dish.profileImage.url}
            alt={dish.name}
            className="w-12 h-12 object-cover rounded"
          />
        ) : (
          <div className="w-12 h-12 bg-gray-300 flex items-center justify-center rounded">
            <ImageIcon className="w-6 h-6 text-white" />
          </div>
        )}
        <div className="flex flex-col">
          <span className="font-medium">{dish.name}</span>
          <span className="text-sm text-gray-600">{dish.price} BYN</span>
          <Badge className={isAvailable ? 'bg-green-500 text-white' : 'bg-red-500 text-white'}>
            {isAvailable ? 'Доступно' : 'Временно не доступно'}
          </Badge>
        </div>
      </div>

      <div className="flex items-center w-32 bg-gray-300 rounded-lg overflow-hidden">
        <Button
          className="flex-1 max-w-10 bg-gray-500 hover:bg-gray-400 flex items-center justify-center"
          onClick={() => {
            if (quantity === 1) {
              dispatch(removeItem({ restaurantId, dishId: dish.id }));
            } else {
              dispatch(updateQuantity({ restaurantId, dishId: dish.id, quantity: quantity - 1 }));
            }
          }}
        >
          <Minus className="w-4 h-4 text-white" />
        </Button>

        <div className="flex-1 flex items-center justify-center text-black font-medium">
          {quantity}
        </div>

        <Button
          className="flex-1 max-w-10 bg-gray-500 hover:bg-gray-400 flex items-center justify-center p-2"
          onClick={() =>
            dispatch(updateQuantity({ restaurantId, dishId: dish.id, quantity: quantity + 1 }))
          }
        >
          <Plus className="w-4 h-4 text-white" />
        </Button>
      </div>
    </div>
  );
};
