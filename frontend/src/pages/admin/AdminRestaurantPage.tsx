import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { restaurantService } from '@/api/services/restaurant/restaurant.service';
import type { RestaurantResponse } from '@/api';
import { AppRoutes } from '@/routes';
import { toast } from 'sonner';
import { UpdateRestaurantForm } from '@/features/restaurant/UpdateRestaurantForm';
import { AdminRestaurantBreadcrumb } from '@/components/restaurant/AdminRestaurantBreadcrumb';
import { UpdateRestaurantProfileImageForm } from '@/features/restaurant/UpdateRestaurantProfileImageForm';
import { UpdateRestaurantImagesForm } from '@/features/restaurant/UpdateRestaurantImagesForm';
import { AdminDishList } from '@/features/dish/AdminDishList';
import { CreateDishFormDialog } from '@/features/dish/CreateDishFormDialog';

const AdminRestaurantPage: React.FC = () => {
  const { restaurantId } = useParams<{ restaurantId: string }>();

  const navigate = useNavigate();

  const {
    data: restaurant,
    isPending,
    isError,
    error,
  } = useQuery<RestaurantResponse>({
    queryKey: ['admin-restaurant', restaurantId],
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

  return (
    <>
      <AdminRestaurantBreadcrumb restaurant={restaurant} isPending={isPending} />
      <UpdateRestaurantForm restaurant={restaurant} isPending={isPending} />
      <UpdateRestaurantProfileImageForm restaurant={restaurant} isPending={isPending} />
      <UpdateRestaurantImagesForm restaurant={restaurant} isPending={isPending} />
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Список блюд</h1>
        <CreateDishFormDialog restaurant={restaurant} isPending={isPending} />
      </div>
      <AdminDishList restaurant={restaurant} isPending={isPending} pageSize={6} />
    </>
  );
};

export default AdminRestaurantPage;
