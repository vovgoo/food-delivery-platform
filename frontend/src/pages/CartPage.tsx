import React, { useEffect } from 'react';
import { useSelector } from 'react-redux';
import type { RootState } from '@/store';
import { useNavigate } from 'react-router-dom';
import { AppRoutes } from '@/routes';
import CartRestaurantCard from '@/components/cart/CartRestaurantCard';

const CartPage: React.FC = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem('accessToken');

  useEffect(() => {
    if (!token) {
      navigate(AppRoutes.MAIN);
    }
  }, [token, navigate]);

  const carts = useSelector((state: RootState) => state.cart.carts);

  const hasItems = Object.values(carts).some((cart) => cart.items.length > 0);

  if (!hasItems) {
    return (
      <div className="w-full my-10 h-full flex flex-col gap-y-6">
        <h1 className="text-3xl font-bold">Корзина</h1>
        <p className="text-gray-500">В корзине пока нет товаров.</p>
      </div>
    );
  }

  return (
    <div className="w-full my-10 flex flex-col gap-y-6 h-full">
      <h1 className="text-3xl font-bold">Корзина</h1>

      {Object.values(carts).map((cart) => (
        <CartRestaurantCard
          key={cart.restaurantId}
          restaurantId={cart.restaurantId}
          items={cart.items}
        />
      ))}
    </div>
  );
};

export default CartPage;
