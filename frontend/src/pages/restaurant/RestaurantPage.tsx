import { restaurantService, type RestaurantResponse } from '@/api';
import { AppRoutes } from '@/routes';
import { useQuery } from '@tanstack/react-query';
import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'sonner';
import { Skeleton } from '@/components/ui/skeleton';
import { Badge } from '@/components/ui/badge';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselPrevious,
  CarouselNext,
} from '@/components/ui/carousel';
import { Building2, Phone, Globe, Clock, Truck, Car } from 'lucide-react';
import { DishList } from '@/features';

export const RestaurantPage: React.FC = () => {
  const { restaurantId } = useParams<{ restaurantId: string }>();
  const navigate = useNavigate();

  const {
    data: restaurant,
    isPending,
    isError,
    error,
  } = useQuery<RestaurantResponse>({
    queryKey: ['restaurant', restaurantId],
    queryFn: () => restaurantService.get(restaurantId!),
    retry: (failureCount, err: any) => {
      if (err?.response?.status === 404) return false;
      return failureCount < 1;
    },
  });

  useEffect(() => {
    if (isError) {
      if ((error as any)?.response?.status === 404) {
        toast.error('Ресторан не найден');
        navigate(AppRoutes.ADMIN_RESTAURANTS);
      } else {
        toast.error('Произошла ошибка при загрузке ресторана');
      }
    }
  }, [isError]);

  if (isPending) {
    return (
      <div className="w-full my-10 flex flex-col gap-y-6">
        <Skeleton className="bg-gray-400 w-full h-96 rounded-lg" />
        <Skeleton className="bg-gray-400 w-1/2 h-10 rounded" />
        <Skeleton className="bg-gray-400 w-1/3 h-8 rounded" />
        <Skeleton className="bg-gray-400 w-1/3 h-8 rounded" />
        <Skeleton className="bg-gray-400 w-1/3 h-8 rounded" />
        <Skeleton className="bg-gray-400 w-full h-20 rounded" />
        <div className="flex gap-4 overflow-x-hidden">
          <Skeleton className="bg-gray-400 w-full h-[600px] rounded-2xl" />
        </div>
      </div>
    );
  }

  if (!restaurant) return null;

  const isActive = restaurant.status === 'ACTIVE';

  return (
    <div className="w-full my-10 flex flex-col gap-y-6">
      {restaurant.profileImage?.url ? (
        <img
          src={restaurant.profileImage.url}
          alt={restaurant.name}
          className="w-full h-96 object-cover rounded-lg"
        />
      ) : (
        <div className="w-full h-96 bg-gray-300 flex items-center justify-center rounded-lg">
          <Building2 className="w-12 h-12 text-white" />
        </div>
      )}

      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">{restaurant.name}</h1>
        <Badge className={isActive ? 'bg-green-500 text-white' : 'bg-orange-500 text-white'}>
          {isActive ? 'Активен' : 'Временно не доступен'}
        </Badge>
      </div>

      <div className="flex items-center gap-4 text-sm">
        <Building2 className="w-5 h-5" />
        <span>
          {restaurant.cuisine}, {restaurant.address}
        </span>
        <Clock className="w-5 h-5" />
        <span>
          {restaurant.openingTime.slice(0, 5)} - {restaurant.closingTime.slice(0, 5)}
        </span>
      </div>

      <div className="flex items-center gap-4 text-sm">
        <Globe className="w-5 h-5" />
        {restaurant.website ? (
          <a
            href={restaurant.website}
            target="_blank"
            rel="noopener noreferrer"
            className="underline"
          >
            Сайт
          </a>
        ) : (
          <span>Нет сайта</span>
        )}
        <Phone className="w-5 h-5" />
        <span>{restaurant.phone}</span>
      </div>

      <div className="flex items-center gap-2 text-sm">
        <Truck className="w-5 h-5" />
        {restaurant.deliveryAvailable ? 'Доставка доступна' : 'Доставка недоступна'}
      </div>

      <div className="flex items-center gap-2 text-sm">
        <Car className="w-5 h-5" />
        {restaurant.parkingAvailable ? 'Парковка есть' : 'Парковки нет'}
      </div>

      {restaurant.description && (
        <div className="text-sm mt-4">
          <p>{restaurant.description}</p>
        </div>
      )}

      {restaurant.images && restaurant.images.length > 0 && (
        <div className="mt-4 w-full mx-auto">
          <Carousel className="w-full h-[600px] rounded-2xl overflow-hidden shadow-lg">
            <CarouselContent className="flex w-full h-full">
              {restaurant.images.map((img) => (
                <CarouselItem key={img.id} className="flex-none w-full h-full">
                  <img
                    src={img.url}
                    alt={`Фото ${restaurant.name}`}
                    className="w-full h-full object-cover object-center transition-transform duration-300"
                  />
                </CarouselItem>
              ))}
            </CarouselContent>

            <CarouselPrevious className="absolute left-4 top-1/2 -translate-y-1/2 bg-white/80 dark:bg-gray-800/60 rounded-full p-2 shadow hover:bg-white/90">
              &lt;
            </CarouselPrevious>
            <CarouselNext className="absolute right-4 top-1/2 -translate-y-1/2 bg-white/80 dark:bg-gray-800/60 rounded-full p-2 shadow hover:bg-white/90">
              &gt;
            </CarouselNext>
          </Carousel>
        </div>
      )}

      <DishList restaurant={restaurant} isPending={isPending} pageSize={6} />
    </div>
  );
};
