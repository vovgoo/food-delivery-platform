import React, { useState } from 'react';
import { useQuery, keepPreviousData as keepPrevData } from '@tanstack/react-query';
import {
  dishService,
  type DishShortResponse,
  type PageResponse,
  type RestaurantResponse,
} from '@/api';
import { SearchIcon } from 'lucide-react';
import { DishCard, DishCardSkeleton, Pagination } from '@/components';

interface DishListProps {
  restaurant?: RestaurantResponse;
  isPending: boolean;
  pageSize?: number;
}

export const DishList: React.FC<DishListProps> = ({ restaurant, isPending, pageSize = 6 }) => {
  const [page, setPage] = useState(0);

  const restaurantId = restaurant?.id;

  const { data, isFetching } = useQuery<PageResponse<DishShortResponse>>({
    enabled: !!restaurantId && !isPending,
    queryKey: ['dishes', restaurantId, page],
    queryFn: () => dishService.list(restaurantId!, { page, size: pageSize }),
    placeholderData: keepPrevData,
  });

  const totalPages = data?.totalPages || 0;

  return (
    <div className="flex flex-col gap-6">
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        {isPending || isFetching || !restaurantId ? (
          Array(pageSize)
            .fill(0)
            .map((_, i) => <DishCardSkeleton key={i} />)
        ) : data?.content.length ? (
          data.content.map((dish) => (
            <DishCard key={dish.id} dish={dish} restaurant={restaurant!} />
          ))
        ) : (
          <div className="col-span-full flex flex-col items-center justify-center py-20 text-gray-500">
            <SearchIcon className="w-12 h-12 mb-4" />
            <p className="text-lg font-medium">Блюда не найдены</p>
          </div>
        )}
      </div>

      {totalPages > 1 && (
        <Pagination
          currentPage={page}
          totalPages={totalPages}
          onPageChange={setPage}
          isFetching={isFetching || isPending}
        />
      )}
    </div>
  );
};
