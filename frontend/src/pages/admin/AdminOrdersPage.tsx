import { AdminOrderList } from '@/features';
import React from 'react';

export const AdminOrdersPage: React.FC = () => {
  return (
    <>
      <AdminOrderList pageSize={6} />
    </>
  );
};
