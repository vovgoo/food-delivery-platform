import React, { useState } from "react";
import { useQuery, keepPreviousData as keepPrevData } from "@tanstack/react-query";
import { type PageResponse, type OrderShortResponse } from "@/api";
import { Pagination } from "@/components/common/Pagination";
import { SearchIcon } from "lucide-react";
import { orderService } from "@/api/services/order/order.service";
import { OrderCard } from "@/components/order/OrderCard";
import { OrderCardSkeleton } from "@/components/order/OrderCardSkeleton";

interface OrderListProps {
  pageSize?: number;
}

export const OrderList: React.FC<OrderListProps> = ({ pageSize = 6 }) => {
    const [page, setPage] = useState(0);
    

  const { data, isFetching } = useQuery<PageResponse<OrderShortResponse>>({
    queryKey: ["orders", page],
    queryFn: () => orderService.list({ page, size: pageSize }),
    placeholderData: keepPrevData,
  });

  const totalPages = data?.totalPages || 0;

  return (
    <div className="flex flex-col gap-6">
      <div className="w-full">
        {isFetching
            ? Array(pageSize).fill(0).map((_, i) => <OrderCardSkeleton key={i} />)
            : data?.content.length
            ? data.content.map((order) => (
                <OrderCard key={order.id} order={order} />
                ))
            : (
                <div className="col-span-full flex flex-col items-center justify-center py-20 text-gray-500">
                <SearchIcon className="w-12 h-12 mb-4" />
                <p className="text-lg font-medium">Тут пока пусто, нужно исправлять!</p>
                </div>
            )
        }
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
