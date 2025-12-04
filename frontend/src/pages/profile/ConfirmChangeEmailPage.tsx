import React, { useEffect, useRef } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { userService, type ConfirmChangeEmailRequest } from '@/api';
import { AppRoutes } from '@/routes';
import { Spinner } from '@/components/ui/spinner';
import { toast } from 'sonner';

export const ConfirmChangeEmailPage: React.FC = () => {
  const navigate = useNavigate();
  const hasMutated = useRef(false);
  const [searchParams] = useSearchParams();
  const tokenFromUrl = searchParams.get('token') || '';
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (payload: ConfirmChangeEmailRequest) => userService.confirmChangeEmail(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['me'] });
      toast.success('Почта успешно изменена!');
      navigate(AppRoutes.PROFILE_SETTINGS);
    },
    onError: (err: any) => {
      if (err.response?.data?.body?.errors) {
        err.response.data.body.errors.forEach(
          (fieldError: { field: string; messages: string[] }) => {
            toast.error(`${fieldError.field}: ${fieldError.messages.join(', ')}`);
          },
        );
      } else {
        const message =
          typeof err.response?.data?.body === 'string'
            ? err.response.data.body
            : 'Произошла ошибка при подтверждении почты';
        toast.error(message);
      }
      navigate(AppRoutes.PROFILE_SETTINGS);
    },
  });

  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
      navigate(AppRoutes.MAIN, { replace: true });
      return;
    }

    if (!tokenFromUrl) {
      toast.error('Токен не найден');
      navigate(AppRoutes.PROFILE_SETTINGS);
      return;
    }

    if (!hasMutated.current) {
      const payload: ConfirmChangeEmailRequest = { token: tokenFromUrl };
      mutation.mutate(payload);
      hasMutated.current = true;
    }
  }, [mutation, navigate, tokenFromUrl]);

  return (
    <div className="flex w-full justify-center">
      <div className="flex flex-col justify-center items-center max-w-[300px] gap-y-10">
        <h1 className="text-4xl w-full text-center font-bold">Подтверждение вашей почты</h1>

        {mutation.isPending && (
          <div className="flex flex-col items-center gap-2">
            <Spinner />
            <p className="text-center text-sm text-gray-600">Проверяем ваш токен...</p>
          </div>
        )}
      </div>
    </div>
  );
};
