import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { restaurantService } from "@/api/services/restaurant/restaurant.service";
import { dishService } from "@/api/services/dish/dish.service";
import type { RestaurantResponse, DishResponse } from "@/api";
import { AppRoutes } from "@/routes";
import { toast } from "sonner";
import { Carrot, FireExtinguisher, ImageIcon, Leaf, Minus, Plus, ShoppingCart } from "lucide-react";
import { useDispatch, useSelector } from "react-redux";
import type { RootState } from "@/store";
import { Button } from "@/components/ui/button";
import { addItem, removeItem, updateQuantity } from "@/store/slices/cartSlice";
import { Skeleton } from "@/components/ui/skeleton";
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel";
import { Badge } from "@/components/ui/badge";

const DishPage: React.FC = () => {
  const { restaurantId, dishId } = useParams<{ restaurantId: string; dishId: string }>();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const { isPending: isRestaurantPending, isError: isRestaurantError, error: restaurantError } = useQuery<RestaurantResponse>({
    queryKey: ["restaurant", restaurantId],
    queryFn: () => restaurantService.get(restaurantId!),
    retry: (failureCount, err: any) => {
      if (err?.response?.status === 404) return false;
      return failureCount < 1;
    },
  });

  const { data: dish, isPending: isDishPending, isError: isDishError, error: dishError } = useQuery<DishResponse>({
    queryKey: ["dish", restaurantId, dishId],
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

  const cartItems = useSelector(
    (state: RootState) => state.cart.carts[restaurantId!]?.items ?? []
  );
  const cartDish = cartItems.find((i) => i.id === dishId);
  const quantityInCart = cartDish?.quantity ?? 0;

  if (isLoading) {
    return (
      <div className="w-full my-10 flex flex-col gap-y-6">
        <Skeleton className="bg-gray-400 w-full h-96 rounded-lg" />
        <Skeleton className="bg-gray-400 w-1/2 h-10 rounded" />
        <Skeleton className="bg-gray-400 w-1/3 h-8 rounded" />
        <Skeleton className="bg-gray-400 w-full h-6 rounded" />
        <Skeleton className="bg-gray-400 w-full h-6 rounded" />
        <Skeleton className="bg-gray-400 w-full h-6 rounded" />
        <div className="flex gap-4 overflow-x-hidden mt-4">
          <Skeleton className="bg-gray-400 w-full h-[600px] rounded-2xl" />
        </div>
      </div>
    );
  }

  if (!dish) return <div>Блюдо не найдено</div>;

  const isActive = dish.status === "AVAILABLE";

  return (
    <div className="w-full my-10 flex flex-col gap-y-6">
      {dish.profileImage?.url ? (
        <img
          src={dish.profileImage.url}
          alt={dish.name}
          className="w-full h-96 object-cover rounded-lg"
        />
      ) : (
        <div className="w-full h-96 bg-gray-300 flex items-center justify-center rounded-lg">
          <ImageIcon className="w-12 h-12 text-white" />
        </div>
      )}

      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">{dish.name}</h1>
        <div className="flex gap-x-4 items-center">
          <Badge className={isActive ? "bg-green-500 text-white" : "bg-orange-500 text-white"}>
            {isActive ? "Активен" : "Временно не доступен"}
          </Badge>
          {quantityInCart === 0 ? (
            <Button
              className="flex items-center gap-2 bg-amber-400 text-black hover:bg-amber-300"
              onClick={() =>
                dispatch(addItem({ restaurantId: restaurantId!, dishId: dish.id, quantity: 1 }))
              }
            >
              <ShoppingCart className="w-5 h-5" />
              В корзину
            </Button>
          ) : (
            <div className="flex items-center w-32 bg-amber-400 rounded-lg overflow-hidden">
              <Button
                className="w-10 bg-amber-300 hover:bg-amber-200 flex items-center justify-center"
                onClick={() => {
                  if (quantityInCart === 1) {
                    dispatch(removeItem({ restaurantId: restaurantId!, dishId: dish.id }));
                  } else {
                    dispatch(updateQuantity({
                      restaurantId: restaurantId!,
                      dishId: dish.id,
                      quantity: quantityInCart - 1
                    }));
                  }
                }}
              >
                <Minus className="w-4 h-4 text-black" />
              </Button>
              <div className="flex-1 flex items-center justify-center text-black font-medium">{quantityInCart}</div>
              <Button
                className="w-10 bg-amber-300 hover:bg-amber-200 flex items-center justify-center"
                onClick={() =>
                  dispatch(updateQuantity({
                    restaurantId: restaurantId!,
                    dishId: dish.id,
                    quantity: quantityInCart + 1
                  }))
                }
              >
                <Plus className="w-4 h-4 text-black" />
              </Button>
            </div>
          )}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-2 text-sm text-muted-foreground mt-2">
        {dish.portionInGrams && (
          <div>
            <span className="font-medium">Порция:</span> {dish.portionInGrams} г
          </div>
        )}
        <div>
          <span className="font-medium">Белки:</span> {dish.proteins ?? "-"} г
        </div>
        <div>
          <span className="font-medium">Жиры:</span> {dish.fats ?? "-"} г
        </div>
        <div>
          <span className="font-medium">Углеводы:</span> {dish.carbohydrates ?? "-"} г
        </div>

        <div className="flex items-center gap-2 text-sm font-medium">
          <FireExtinguisher className={`w-5 h-5 ${dish.spicy ? "text-red-500" : "text-gray-400"}`} />
          {dish.spicy ? "Острое" : "Не острое"}
        </div>

        <div className="flex items-center gap-2 text-sm font-medium">
          <Leaf className={`w-5 h-5 ${dish.vegan ? "text-green-500" : "text-gray-400"}`} />
          {dish.vegan ? "Веганское" : "Не веганское"}
        </div>

        <div className="flex items-center gap-2 text-sm font-medium">
          <Carrot className={`w-5 h-5 ${dish.vegetarian ? "text-orange-500" : "text-gray-400"}`} />
          {dish.vegetarian ? "Вегетарианское" : "Не вегетарианское"}
        </div>
      </div>

      <p className="text-lg font-medium">Цена: {dish.price} BYN</p>

      {dish.images && dish.images.length > 0 && (
        <div className="mt-4 w-full mx-auto">
          <Carousel className="w-full h-[600px] rounded-2xl overflow-hidden shadow-lg">
            <CarouselContent className="flex w-full h-full">
              {dish.images.map((img) => (
                <CarouselItem key={img.id} className="flex-none w-full h-full">
                  <img
                    src={img.url}
                    alt={`Фото ${dish.name}`}
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
    </div>
  );
};

export default DishPage;
