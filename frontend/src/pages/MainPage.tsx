import { RestaurantList } from '@/features';
import React from 'react';

export const MainPage: React.FC = () => {
  return (
    <div className="my-10 h-full w-full">
      <RestaurantList pageSize={12} />
    </div>
  );
};
