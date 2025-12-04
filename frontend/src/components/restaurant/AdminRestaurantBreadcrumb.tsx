import {
  Breadcrumb,
  BreadcrumbList,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbSeparator,
  BreadcrumbPage,
} from '@/components/ui/breadcrumb';
import { Link } from 'react-router-dom';
import { Skeleton } from '../ui/skeleton';
import { AppRoutes } from '@/routes';
import { type RestaurantResponse } from '@/api';

interface AdminRestaurantBreadcrumbProps {
  restaurant?: RestaurantResponse;
  isPending: boolean;
}

export const AdminRestaurantBreadcrumb: React.FC<AdminRestaurantBreadcrumbProps> = ({
  restaurant,
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
        <BreadcrumbPage>
          {isPending ? (
            <Skeleton className="h-5 w-24 bg-gray-400 rounded" />
          ) : (
            (restaurant?.name ?? 'Без названия')
          )}
        </BreadcrumbPage>
      </BreadcrumbItem>
    </BreadcrumbList>
  </Breadcrumb>
);
