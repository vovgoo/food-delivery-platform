import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { restaurantService } from '@/api/services/restaurant/restaurant.service';
import { dishService } from '@/api/services/dish/dish.service';
import { Skeleton } from '@/components/ui/skeleton';
import { Badge } from '@/components/ui/badge';
import CartDishCard from './CartDishCard';
import type { RestaurantResponse, DishResponse } from '@/api';
import { CreateOrderFormDialog } from '@/features/order/CreateOrderFormDialog';

interface CartRestaurantCardProps {
  restaurantId: string;
  items: { id: string; quantity: number }[];
}

const CartRestaurantCard: React.FC<CartRestaurantCardProps> = ({ restaurantId, items }) => {
  const { data: restaurant, isPending: isRestaurantPending } = useQuery<RestaurantResponse>({
    queryKey: ['cart-restaurant', restaurantId],
    queryFn: () => restaurantService.get(restaurantId),
    retry: (failureCount, err: any) => {
      if (err?.response?.status === 404) return false;
      return failureCount < 1;
    },
  });

  const dishesQuery = useQuery<DishResponse[]>({
    queryKey: ['cart-dishes', restaurantId, items.map((i) => i.id).join(',')],
    queryFn: async () => {
      const promises = items.map((i) => dishService.get(restaurantId, i.id));
      return Promise.all(promises);
    },
    enabled: items.length > 0,
    retry: (failureCount, err: any) => {
      if (err?.response?.status === 404) return false;
      return failureCount < 1;
    },
  });

  if (items.length === 0) return null;

  const isActive = restaurant?.status === 'ACTIVE';

  const totalPrice = dishesQuery.data
    ? dishesQuery.data.reduce((sum, dish, index) => sum + dish.price * items[index].quantity, 0)
    : 0;

  return (
    <div className="border rounded-lg p-4 flex flex-col gap-4 bg-white shadow-sm">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-semibold">
          {isRestaurantPending ? (
            <Skeleton className="h-6 w-48 bg-gray-400 rounded" />
          ) : (
            restaurant?.name || `Ресторан: ${restaurantId}`
          )}
        </h2>
        {!isRestaurantPending && restaurant && (
          <Badge className={isActive ? 'bg-green-500 text-white' : 'bg-orange-500 text-white'}>
            {isActive ? 'Активен' : 'Временно не доступен'}
          </Badge>
        )}
      </div>

      <div className="flex flex-col gap-2">
        {dishesQuery.isPending || !dishesQuery.data
          ? items.map((i) => <Skeleton key={i.id} className="h-20 w-full bg-gray-200 rounded-lg" />)
          : dishesQuery.data.map((dish, index) => (
              <CartDishCard
                key={dish.id}
                restaurantId={restaurantId}
                dish={dish}
                quantity={items[index].quantity}
              />
            ))}
      </div>

      <div className="flex items-center justify-between mt-4">
        <span className="text-lg font-medium">Общая сумма: {totalPrice} BYN</span>
        <CreateOrderFormDialog restaurantId={restaurantId} items={items} />
      </div>
    </div>
  );
};

export default CartRestaurantCard;
