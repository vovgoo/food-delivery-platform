import React, { useEffect, useState } from 'react';
import { useQuery, keepPreviousData as keepPrevData } from '@tanstack/react-query';
import { type RestaurantShortResponse, type PageResponse, restaurantService } from '@/api';
import { useForm } from 'react-hook-form';
import { SearchIcon } from 'lucide-react';
import {
  Pagination,
  RestaurantCardAdmin,
  RestaurantCardAdminSkeleton,
  SearchInput,
} from '@/components';

interface AdminRestaurantListProps {
  pageSize?: number;
}

export const AdminRestaurantList: React.FC<AdminRestaurantListProps> = ({ pageSize = 6 }) => {
  const [page, setPage] = useState(0);
  const form = useForm<{ search: string }>();
  const [debouncedSearch, setDebouncedSearch] = useState('');

  const { watch } = form;
  const searchValue = watch('search');

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearch(searchValue);
    }, 500);

    return () => clearTimeout(handler);
  }, [searchValue]);

  const { data, isFetching } = useQuery<PageResponse<RestaurantShortResponse>>({
    queryKey: ['admin-restaurants', page, debouncedSearch],
    queryFn: () => restaurantService.list({ cuisine: debouncedSearch }, { page, size: pageSize }),
    placeholderData: keepPrevData,
  });

  const totalPages = data?.totalPages || 0;

  return (
    <div className="flex flex-col gap-6">
      <div className="w-full max-w-md">
        <SearchInput name="search" control={form.control} placeholder="Поиск по кухне..." />
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        {isFetching ? (
          Array(pageSize)
            .fill(0)
            .map((_, i) => <RestaurantCardAdminSkeleton key={i} />)
        ) : data?.content.length ? (
          data.content.map((restaurant) => (
            <RestaurantCardAdmin key={restaurant.id} restaurant={restaurant} />
          ))
        ) : (
          <div className="col-span-full flex flex-col items-center justify-center py-20 text-gray-500">
            <SearchIcon className="w-12 h-12 mb-4" />
            <p className="text-lg font-medium">Рестораны не найдены</p>
          </div>
        )}
      </div>

      {totalPages > 1 && (
        <Pagination
          currentPage={page}
          totalPages={totalPages}
          onPageChange={setPage}
          isFetching={isFetching}
        />
      )}
    </div>
  );
};
