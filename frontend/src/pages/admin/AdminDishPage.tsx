import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { restaurantService } from "@/api/services/restaurant/restaurant.service";
import { dishService } from "@/api/services/dish/dish.service";
import type { RestaurantResponse, DishResponse } from "@/api";
import { AppRoutes } from "@/routes";
import { toast } from "sonner";
import { AdminDishBreadcrumb } from "@/components/dish/AdminDishBreadcrumb";
import { UpdateDishForm } from "@/features/dish/UpdateDishForm";
import { UpdateDishProfileImageForm } from "@/features/dish/UpdateDishProfileImageForm";
import { UpdateDishImagesForm } from "@/features/dish/UpdateDishImagesFrom";

const AdminDishPage: React.FC = () => {
  const { restaurantId, dishId } = useParams<{ restaurantId: string; dishId: string }>();
  const navigate = useNavigate();

  const {data: restaurant, isPending: isRestaurantPending, isError: isRestaurantError, error: restaurantError} = useQuery<RestaurantResponse>({
    queryKey: ["admin-restaurant", restaurantId],
    queryFn: () => restaurantService.get(restaurantId!),
    retry: (failureCount, err: any) => {
      if (err?.response?.status === 404) return false;
      return failureCount < 1;
    },
  });

  const {data: dish, isPending: isDishPending, isError: isDishError, error: dishError} = useQuery<DishResponse>({
    queryKey: ["admin-dish", restaurantId, dishId],
    queryFn: () => dishService.get(restaurantId!, dishId!),
    enabled: !!restaurantId && !!dishId,
    retry: (failureCount, err: any) => {
      if (err?.response?.status === 404) return false;
      return failureCount < 1;
    },
  });

  useEffect(() => {
    if (isRestaurantError) {
      if ((restaurantError as any)?.response?.status === 404) {
        toast.error("Ресторан не найден");
        navigate(AppRoutes.ADMIN_RESTAURANTS);
      } else {
        toast.error("Произошла ошибка при загрузке ресторана");
      }
    }
  }, [isRestaurantError]);

  useEffect(() => {
    if (isDishError) {
      if ((dishError as any)?.response?.status === 404) {
        toast.error("Блюдо не найдено");
        if (restaurantId) navigate(AppRoutes.ADMIN_RESTAURANT.replace(":restaurantId", restaurantId));
      } else {
        toast.error("Произошла ошибка при загрузке блюда");
      }
    }
  }, [isDishError]);

  const isLoading = isRestaurantPending || isDishPending;

  return (
    <>
      <AdminDishBreadcrumb restaurant={restaurant} dish={dish} isPending={isLoading} />
      <UpdateDishForm restaurant={restaurant} dish={dish} isPending={isLoading} />
      <UpdateDishProfileImageForm restaurant={restaurant} dish={dish} isPending={isLoading}/>
      <UpdateDishImagesForm restaurant={restaurant} dish={dish} isPending={isLoading}/>
    </>
  );
};

export default AdminDishPage;
