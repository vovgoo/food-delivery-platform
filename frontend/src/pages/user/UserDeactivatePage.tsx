import React from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { userService } from '@/api';
import { AppRoutes } from '@/routes';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { SpinnerButton } from '@/components';

export const UserDeactivatePage: React.FC = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const reactivateMutation = useMutation<void, any, void>({
    mutationFn: () => userService.reactivate(),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['userProfile'] });
      toast.success('Аккаунт успешно реактивирован!');
      navigate(AppRoutes.PROFILE_SETTINGS);
    },
    onError: (err: any) => {
      const message = err.response?.data?.body || 'Ошибка при реактивации аккаунта';
      toast.error(message);
    },
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
    <div className="flex w-full justify-center items-center min-h-dvh">
      <div className="flex flex-col justify-center items-center max-w-[400px] gap-y-10">
        <h1 className="text-4xl w-full text-center font-bold">Ваш аккаунт деактивирован</h1>
        <p>Вы можете его реактивировать, нажав кнопку ниже, или выйти из аккаунта.</p>
        <div className="w-full flex justify-between gap-x-5">
          <SpinnerButton
            text="Реактивировать аккаунт"
            loadingText="Реактивация..."
            isLoading={reactivateMutation.isPending}
            onClick={() => reactivateMutation.mutate()}
          />

          <SpinnerButton
            text="Выйти из аккаунта"
            loadingText="Выход..."
            isLoading={logoutMutation.isPending}
            onClick={() => logoutMutation.mutate()}
          />
        </div>
      </div>
    </div>
  );
};
