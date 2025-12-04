import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { AppRoutes } from '@/routes';
import { Button } from '@/components/ui/button';

export const AdminSidebar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const activePath = location.pathname;

  const menuItems = [
    { label: 'Рестораны', path: AppRoutes.ADMIN_RESTAURANTS },
    { label: 'Заказы', path: AppRoutes.ADMIN_ORDERS },
  ];

  return (
    <div className="flex flex-col">
      <div className="flex flex-col gap-2">
        {menuItems.map((item) => (
          <Button
            key={item.path}
            onClick={() => navigate(item.path)}
            className={`
              w-full text-left rounded
              cursor-pointer px-2
              transition-colors
              ${
                activePath === item.path
                  ? 'bg-white text-black font-semibold hover:bg-white'
                  : 'hover:bg-white bg-transparent text-black'
              }
            `}
          >
            {item.label}
          </Button>
        ))}
      </div>
    </div>
  );
};
