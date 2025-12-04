import React from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import type { RestaurantShortResponse } from '@/api';
import { Button } from '../ui/button';
import { useNavigate } from 'react-router-dom';
import { AppRoutes } from '@/routes';
import { Building2, Phone, Globe, Clock, Truck, Car } from 'lucide-react';

interface RestaurantCardProps {
  restaurant: RestaurantShortResponse;
}

export const RestaurantCard: React.FC<RestaurantCardProps> = ({ restaurant }) => {
  const navigate = useNavigate();
  const isActive = restaurant.status === 'ACTIVE';

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

        <Button
          className="bg-amber-400 text-black hover:bg-amber-300 mt-4"
          onClick={() => navigate(AppRoutes.RESTAURANT.replace(':restaurantId', restaurant.id))}
        >
          Заказать здесь
        </Button>
      </CardContent>
    </Card>
  );
};
