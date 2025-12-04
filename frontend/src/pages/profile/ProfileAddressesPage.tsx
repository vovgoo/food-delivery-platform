import { AddressList, CreateAddressFormDialog } from '@/features';
import React from 'react';

export const ProfileAddressesPage: React.FC = () => {
  return (
    <div className="w-full">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Ваши адреса</h1>
        <CreateAddressFormDialog />
      </div>

      <AddressList pageSize={6} />
    </div>
  );
};
