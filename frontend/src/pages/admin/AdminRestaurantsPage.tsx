import { AdminRestaurantList, CreateRestaurantFormDialog } from '@/features';
import React from 'react';

export const AdminRestaurantsPage: React.FC = () => {
  return (
    <div className="w-full">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Список ресторанов</h1>
        <CreateRestaurantFormDialog />
      </div>
      <AdminRestaurantList pageSize={6} />
    </div>
  );
};
