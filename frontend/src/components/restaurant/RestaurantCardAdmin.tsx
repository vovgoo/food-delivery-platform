import React from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import type { RestaurantShortResponse } from '@/api';
import { Button } from '../ui/button';
import { useNavigate } from 'react-router-dom';
import { AppRoutes } from '@/routes';
import { Building2, Phone, Globe, Clock, Truck, Car, Trash2 } from 'lucide-react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { restaurantService } from '@/api/services/restaurant/restaurant.service';
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

interface RestaurantCardAdminProps {
  restaurant: RestaurantShortResponse;
}

export const RestaurantCardAdmin: React.FC<RestaurantCardAdminProps> = ({ restaurant }) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const isActive = restaurant.status === 'ACTIVE';

  const deleteMutation = useMutation({
    mutationFn: () => restaurantService.delete(restaurant.id),
    onSuccess: () => {
      toast.success('Ресторан удалён');
      queryClient.invalidateQueries({ queryKey: ['admin-restaurants'] });
    },
    onError: () => {
      toast.error('Не удалось удалить ресторан');
    },
  });

  return (
    <Card className="transition-transform hover:scale-[1.02] p-0 overflow-hidden flex flex-col">
      {restaurant.profileImage?.url ? (
        <img
          src={restaurant.profileImage.url}
          alt={restaurant.name}
          className="w-full h-48 object-cover"
        />
      ) : (
        <div className="w-full h-48 bg-gray-300 flex items-center justify-center">
          <Building2 className="w-12 h-12 text-white" />
        </div>
      )}

      <CardContent className="flex flex-col p-4 gap-2">
        <Badge
          className={`self-start ${isActive ? 'bg-green-500 text-white' : 'bg-orange-500 text-white'}`}
        >
          {isActive ? 'Активен' : 'Временно не доступен'}
        </Badge>

        <h2 className="text-xl font-semibold">{restaurant.name}</h2>

        <div className="flex flex-col gap-1 text-sm text-muted-foreground">
          <p>
            <span className="font-medium">Кухня:</span> {restaurant.cuisine}
          </p>
          <p>
            <span className="font-medium">Адрес:</span> {restaurant.address}
          </p>
        </div>

        <div className="grid grid-cols-2 gap-2 text-sm text-muted-foreground mt-2">
          <div className="flex items-center gap-1">
            <Phone className="w-4 h-4" />
            {restaurant.phone}
          </div>
          {restaurant.website && (
            <div className="flex items-center gap-1">
              <Globe className="w-4 h-4" />
              <a
                href={restaurant.website}
                target="_blank"
                rel="noopener noreferrer"
                className="underline"
              >
                Сайт
              </a>
            </div>
          )}
          <div className="flex items-center gap-1">
            <Clock className="w-4 h-4" />
            {restaurant.openingTime.slice(0, 5)} - {restaurant.closingTime.slice(0, 5)}
          </div>
          <div className="flex items-center gap-1">
            <Truck className="w-4 h-4" />
            {restaurant.deliveryAvailable ? 'Доставка' : 'Без доставки'}
          </div>
          <div className="flex items-center gap-1">
            <Car className="w-4 h-4" />
            {restaurant.parkingAvailable ? 'Парковка есть' : 'Парковки нет'}
          </div>
        </div>

        <div className="flex flex-col gap-4 mt-4">
          <Button
            className="bg-amber-400 text-black hover:bg-amber-300"
            onClick={() =>
              navigate(AppRoutes.ADMIN_RESTAURANT.replace(':restaurantId', restaurant.id))
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
                <AlertDialogTitle>Удалить ресторан?</AlertDialogTitle>
                <AlertDialogDescription>
                  Вы уверены, что хотите удалить ресторан "{restaurant.name}"? Это действие нельзя
                  будет отменить.
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
