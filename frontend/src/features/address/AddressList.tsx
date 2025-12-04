import React, { useState } from 'react';
import { useQuery, keepPreviousData as keepPrevData } from '@tanstack/react-query';
import { addressService, type AddressResponse, type PageResponse } from '@/api';
import { AddressCard } from '@/components/address/AddressCard';
import { AddressCardSkeleton } from '@/components/address/AddressCardSkeleton';
import { Pagination } from '@/components/common/Pagination';

interface AddressListProps {
  pageSize?: number;
}

export const AddressList: React.FC<AddressListProps> = ({ pageSize = 6 }) => {
  const [page, setPage] = useState(0);

  const { data, isFetching } = useQuery<PageResponse<AddressResponse>>({
    queryKey: ['addresses', page],
    queryFn: () => addressService.get({ page, size: pageSize }),
    placeholderData: keepPrevData,
  });

  const totalPages = data?.totalPages || 0;

  return (
    <>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        {isFetching
          ? Array(pageSize)
              .fill(0)
              .map((_, i) => <AddressCardSkeleton key={i} />)
          : data?.content.map((address) => <AddressCard key={address.id} address={address} />)}
      </div>

      {totalPages > 1 && (
        <Pagination
          currentPage={page}
          totalPages={totalPages}
          onPageChange={setPage}
          isFetching={isFetching}
        />
      )}
    </>
  );
};
