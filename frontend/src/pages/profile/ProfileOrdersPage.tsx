import { OrderList } from '@/features';
import React from 'react';

export const ProfileOrdersPage: React.FC = () => {
  return (
    <div className="w-full flex flex-col gap-y-6">
      <h1 className="text-3xl font-bold">Мои заказы</h1>
      <OrderList pageSize={5} />
    </div>
  );
};
