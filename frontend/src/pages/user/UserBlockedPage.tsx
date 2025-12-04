import React from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { userService } from '@/api';
import { AppRoutes } from '@/routes';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { SpinnerButton } from '@/components';

export const UserBlockedPage: React.FC = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

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
        <h1 className="text-4xl w-full text-center font-bold">Ваш аккаунт заблокирован</h1>
        <p>Вы можете его разблокировать, если напишите в службу поддержки.</p>
        <div className="w-full flex justify-between gap-x-5">
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
