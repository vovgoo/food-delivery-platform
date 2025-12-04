import React from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '../ui/button';
import { useNavigate } from 'react-router-dom';
import { AppRoutes } from '@/routes';
import { ImageIcon, FireExtinguisher, Leaf, Carrot, ShoppingCart, Minus, Plus } from 'lucide-react';
import type { DishShortResponse, RestaurantResponse } from '@/api';
import { useDispatch, useSelector } from 'react-redux';
import type { RootState } from '@/store';
import { addItem, removeItem, updateQuantity } from '@/store/slices/cartSlice';

interface DishCardProps {
  dish: DishShortResponse;
  restaurant: RestaurantResponse;
}

export const DishCard: React.FC<DishCardProps> = ({ dish, restaurant }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const isActive = dish.status === 'AVAILABLE';

  const cartItems = useSelector((state: RootState) => state.cart.carts[restaurant.id]?.items ?? []);
  const cartDish = cartItems.find((i) => i.id === dish.id);
  const quantityInCart = cartDish?.quantity ?? 0;

  const token = localStorage.getItem('accessToken');

  return (
    <Card className="transition-transform hover:scale-[1.02] p-0 overflow-hidden flex flex-col">
      {dish.profileImage?.url ? (
        <img src={dish.profileImage.url} alt={dish.name} className="w-full h-48 object-cover" />
      ) : (
        <div className="w-full h-48 bg-gray-300 flex items-center justify-center">
          <ImageIcon className="w-12 h-12 text-white" />
        </div>
      )}

      <CardContent className="flex flex-col p-4 gap-2">
        <Badge
          className={`self-start ${
            isActive ? 'bg-green-500 text-white' : 'bg-orange-500 text-white'
          }`}
        >
          {isActive ? 'Активно' : 'Временно не доступно'}
        </Badge>

        <h2 className="text-xl font-semibold">{dish.name}</h2>

        <div className="grid grid-cols-2 gap-2 text-sm text-muted-foreground mt-2">
          {dish.portionInGrams && (
            <div>
              <span className="font-medium">Порция:</span> {dish.portionInGrams} г
            </div>
          )}
          <div>
            <span className="font-medium">Белки:</span> {dish.proteins ?? '-'} г
          </div>
          <div>
            <span className="font-medium">Жиры:</span> {dish.fats ?? '-'} г
          </div>
          <div>
            <span className="font-medium">Углеводы:</span> {dish.carbohydrates ?? '-'} г
          </div>
          <div className="flex items-center gap-2 text-sm font-medium">
            <FireExtinguisher
              className={`w-5 h-5 ${dish.spicy ? 'text-red-500' : 'text-gray-400'}`}
            />
            {dish.spicy ? 'Острое' : 'Не острое'}
          </div>

          <div className="flex items-center gap-2 text-sm font-medium">
            <Leaf className={`w-5 h-5 ${dish.vegan ? 'text-green-500' : 'text-gray-400'}`} />
            {dish.vegan ? 'Веганское' : 'Не веганское'}
          </div>

          <div className="flex items-center gap-2 text-sm font-medium">
            <Carrot
              className={`w-5 h-5 ${dish.vegetarian ? 'text-orange-500' : 'text-gray-400'}`}
            />
            {dish.vegetarian ? 'Вегетарианское' : 'Не вегетарианское'}
          </div>
        </div>

        <p className="text-lg font-medium mt-2">Цена: {dish.price} BYN</p>

        <div className="flex flex-col gap-2 mt-4">
          <Button
            className="bg-amber-400 text-black hover:bg-amber-300"
            onClick={() =>
              navigate(
                AppRoutes.DISH.replace(':restaurantId', restaurant.id).replace(':dishId', dish.id),
              )
            }
          >
            Подробнее
          </Button>

          {!token ? (
            <Button
              className="flex items-center gap-2 bg-gray-200 text-black hover:bg-gray-300"
              onClick={() => navigate(AppRoutes.SIGN_IN)}
            >
              <ShoppingCart className="w-5 h-5" />В корзину
            </Button>
          ) : quantityInCart === 0 ? (
            <Button
              className="flex items-center gap-2 bg-gray-200 text-black hover:bg-gray-300"
              onClick={() =>
                dispatch(addItem({ restaurantId: restaurant.id, dishId: dish.id, quantity: 1 }))
              }
            >
              <ShoppingCart className="w-5 h-5" />В корзину
            </Button>
          ) : (
            <div className="flex items-center w-full bg-gray-300 rounded-lg overflow-hidden">
              <Button
                className="flex-1 max-w-10 bg-gray-500 hover:bg-gray-400 flex items-center justify-center"
                onClick={() => {
                  if (quantityInCart === 1) {
                    dispatch(removeItem({ restaurantId: restaurant.id, dishId: dish.id }));
                  } else {
                    dispatch(
                      updateQuantity({
                        restaurantId: restaurant.id,
                        dishId: dish.id,
                        quantity: quantityInCart - 1,
                      }),
                    );
                  }
                }}
              >
                <Minus className="w-4 h-4 text-white" />
              </Button>

              <div className="flex-1 flex items-center justify-center text-black font-medium">
                {quantityInCart}
              </div>

              <Button
                className="flex-1 max-w-10 bg-gray-500 hover:bg-gray-400 flex items-center justify-center p-2"
                onClick={() =>
                  dispatch(
                    updateQuantity({
                      restaurantId: restaurant.id,
                      dishId: dish.id,
                      quantity: quantityInCart + 1,
                    }),
                  )
                }
              >
                <Plus className="w-4 h-4 text-white" />
              </Button>
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  );
};
