import {
  Breadcrumb,
  BreadcrumbList,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbSeparator,
  BreadcrumbPage,
} from "@/components/ui/breadcrumb";
import { Link } from "react-router-dom";
import { Skeleton } from "../ui/skeleton";
import { AppRoutes } from "@/routes";
import type { RestaurantResponse, DishShortResponse } from "@/api";

interface AdminDishBreadcrumbProps {
  restaurant?: RestaurantResponse;
  dish?: DishShortResponse;
  isPending: boolean;
}

export const AdminDishBreadcrumb: React.FC<AdminDishBreadcrumbProps> = ({
  restaurant,
  dish,
  isPending,
}) => (
  <Breadcrumb>
    <BreadcrumbList>
      <BreadcrumbItem>
        <BreadcrumbLink asChild>
          <Link to={AppRoutes.ADMIN_RESTAURANTS}>Рестораны</Link>
        </BreadcrumbLink>
      </BreadcrumbItem>

      <BreadcrumbSeparator />

      <BreadcrumbItem>
        <BreadcrumbLink asChild>
          <Link to={restaurant ? AppRoutes.ADMIN_RESTAURANT.replace(":restaurantId", restaurant.id) : "#"}>
            {isPending ? <Skeleton className="h-5 w-24 bg-gray-400 rounded" /> : restaurant?.name ?? "Без названия"}
          </Link>
        </BreadcrumbLink>
      </BreadcrumbItem>

      <BreadcrumbSeparator />

      <BreadcrumbItem>
        <BreadcrumbPage>
          {isPending ? (
            <Skeleton className="h-5 w-32 bg-gray-400 rounded" />
          ) : (
            dish?.name ?? "Без названия блюда"
          )}
        </BreadcrumbPage>
      </BreadcrumbItem>
    </BreadcrumbList>
  </Breadcrumb>
);
