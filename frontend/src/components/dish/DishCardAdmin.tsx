import React from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '../ui/button';
import { useNavigate } from 'react-router-dom';
import { AppRoutes } from '@/routes';
import { Trash2, ImageIcon, FireExtinguisher, Leaf, Carrot } from 'lucide-react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { dishService } from '@/api/services/dish/dish.service';
import { toast } from 'sonner';
import {
  AlertDialog,
  AlertDialogTrigger,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogCancel,
  AlertDialogAction,
} from '../ui/alert-dialog';
import type { DishShortResponse, RestaurantResponse } from '@/api';

interface DishCardAdminProps {
  dish: DishShortResponse;
  restaurant: RestaurantResponse;
}

export const DishCardAdmin: React.FC<DishCardAdminProps> = ({ dish, restaurant }) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const isActive = dish.status === 'AVAILABLE';

  const deleteMutation = useMutation({
    mutationFn: () => dishService.delete(restaurant.id, dish.id),
    onSuccess: () => {
      toast.success('Блюдо удалено');
      queryClient.invalidateQueries({ queryKey: ['admin-dishes'] });
    },
    onError: () => {
      toast.error('Не удалось удалить блюдо');
    },
  });

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

        <div className="flex flex-col gap-4 mt-4">
          <Button
            className="bg-amber-400 text-black hover:bg-amber-300"
            onClick={() =>
              navigate(
                AppRoutes.ADMIN_DISH.replace(':restaurantId', restaurant.id).replace(
                  ':dishId',
                  dish.id,
                ),
              )
            }
          >
            Редактировать
          </Button>

          <AlertDialog>
            <AlertDialogTrigger asChild>
              <Button variant="destructive" className="flex items-center gap-2">
                <Trash2 className="w-4 h-4" />
                Удалить
              </Button>
            </AlertDialogTrigger>
            <AlertDialogContent>
              <AlertDialogHeader>
                <AlertDialogTitle>Удалить блюдо?</AlertDialogTitle>
                <AlertDialogDescription>
                  Вы уверены, что хотите удалить блюдо "{dish.name}"? Это действие нельзя будет
                  отменить.
                </AlertDialogDescription>
              </AlertDialogHeader>
              <AlertDialogFooter>
                <AlertDialogCancel>Отмена</AlertDialogCancel>
                <AlertDialogAction
                  className="bg-red-600 hover:bg-red-600"
                  onClick={() => deleteMutation.mutate()}
                >
                  Удалить
                </AlertDialogAction>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialog>
        </div>
      </CardContent>
    </Card>
  );
};
