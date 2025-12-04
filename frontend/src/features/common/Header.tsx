import React from 'react';
import { AppRoutes } from '@/routes';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Link, useNavigate } from 'react-router-dom';
import {
  HomeIcon,
  UserIcon,
  LogOutIcon,
  MapPinIcon,
  PackageIcon,
  SettingsIcon,
  ShieldCheck,
  ShoppingCart,
} from 'lucide-react';
import { toast } from 'sonner';
import { LinkButton } from '@/components/button/LinkButton';
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
} from '@/components/ui/dropdown-menu';
import { userService, type UserResponse } from '@/api';
import { Skeleton } from '@/components/ui/skeleton';
import { Button } from '@/components/ui/button';

const Header: React.FC = () => {
  const token = localStorage.getItem('accessToken');
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: user, isPending } = useQuery<UserResponse>({
    queryKey: ['me'],
    queryFn: () => userService.me(),
  });

  const logoutMutation = useMutation({
    mutationFn: () => userService.logout(),
    onSuccess: () => {
      queryClient.clear();
      localStorage.removeItem('accessToken');
      toast.success('Вы успешно вышли из аккаунта');
      navigate(AppRoutes.MAIN);
    },
    onError: () => {
      toast.error('Не удалось выйти из аккаунта');
    },
  });

  return (
    <header className="w-full flex py-5">
      <div className="w-full flex justify-between items-center">
        <Link to={AppRoutes.MAIN} className="flex items-center gap-x-5">
          <img src="/img/logo_black.png" alt="FDP" className="w-8 h-8" />
          <span className="text-2xl font-bold">Food Delivery</span>
        </Link>

        <div className="flex items-center gap-x-5">
          {token && (isPending || user?.defaultAddress) && (
            <div className="flex items-center gap-3 cursor-pointer">
              <HomeIcon className="w-5 h-5" />
              <div className="text-xs flex flex-col">
                {isPending ? (
                  <>
                    <Skeleton className="h-3 w-40 bg-gray-400 rounded-xs mb-1" />
                    <Skeleton className="h-3 w-36 bg-gray-400 rounded-xs" />
                  </>
                ) : user?.defaultAddress ? (
                  <>
                    <div>
                      {user.defaultAddress.country}, {user.defaultAddress.state},{' '}
                      {user.defaultAddress.city}
                    </div>
                    <div>
                      {user.defaultAddress.street}, {user.defaultAddress.house},{' '}
                      {user.defaultAddress.building}
                    </div>
                  </>
                ) : null}
              </div>
            </div>
          )}
          {token && (
            <Button
              className="flex items-center gap-2 bg-white hover:bg-white text-black cursor-pointer"
              onClick={() => navigate(AppRoutes.CART)}
            >
              <ShoppingCart className="w-5 h-5" />
              Корзина
            </Button>
          )}
          {token ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button className="flex items-center gap-2 bg-white hover:bg-white text-black cursor-pointer">
                  <UserIcon className="w-5 h-5" />
                  {isPending ? (
                    <Skeleton className="h-4 w-24 rounded-xs" />
                  ) : (
                    <h1 className="text-sm">{user?.fullName || 'Профиль'}</h1>
                  )}
                </Button>
              </DropdownMenuTrigger>

              <DropdownMenuContent align="end">
                {user?.roles?.some((role) => role.name === 'ADMIN') && (
                  <>
                    <DropdownMenuItem onClick={() => navigate(AppRoutes.ADMIN_DASHBOARD)}>
                      <ShieldCheck className="w-4 h-4 mr-2" />
                      Админ панель
                    </DropdownMenuItem>
                    <DropdownMenuSeparator />
                  </>
                )}
                <DropdownMenuItem onClick={() => navigate(AppRoutes.PROFILE)}>
                  <UserIcon className="w-4 h-4 mr-2" />
                  Профиль
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => navigate(AppRoutes.PROFILE_ADDRESSES)}>
                  <MapPinIcon className="w-4 h-4 mr-2" />
                  Адреса
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => navigate(AppRoutes.PROFILE_ORDERS)}>
                  <PackageIcon className="w-4 h-4 mr-2" />
                  Заказы
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => navigate(AppRoutes.PROFILE_SETTINGS)}>
                  <SettingsIcon className="w-4 h-4 mr-2" />
                  Настройки
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={() => logoutMutation.mutate()}>
                  <LogOutIcon className="w-4 h-4 mr-2" />
                  Выйти
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <LinkButton
              icon={<UserIcon className="w-5 h-5" />}
              text="Войти"
              to={AppRoutes.SIGN_IN}
            />
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
