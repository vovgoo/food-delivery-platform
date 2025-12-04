import { RestaurantList } from '@/features/restaurant/RestaurantList';
import React from 'react';

const MainPage: React.FC = () => {
  return (
    <div className="my-10 h-full w-full">
      <RestaurantList pageSize={12} />
    </div>
  );
};

export default MainPage;
