import React, { useState } from 'react';
import { toast } from 'sonner';
import { useMutation, useQueryClient } from '@tanstack/react-query';

import { userService } from '@/api';

import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { SpinnerButton } from '@/components/button/SpinnerButton';
import { useNavigate } from 'react-router-dom';
import { AppRoutes } from '@/routes';

import {
  AlertDialog,
  AlertDialogTrigger,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogCancel,
  AlertDialogAction,
} from '@/components/ui/alert-dialog';

export const DeactivateAccountForm: React.FC = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  const mutation = useMutation({
    mutationFn: () => userService.deactivate(),

    onSuccess: () => {
      toast.success('Аккаунт успешно деактивирован!');
      queryClient.invalidateQueries({ queryKey: ['me'] });
      navigate(AppRoutes.USER_DEACTIVATE);
    },

    onError: (err: any) => {
      const message = err.response?.data?.body || 'Произошла ошибка при деактивации аккаунта';
      toast.error(message);
    },
  });

  const handleConfirm = () => {
    mutation.mutate();
    setOpen(false);
  };

  return (
    <Card className="w-full max-w-[1300px]">
      <CardHeader>
        <CardTitle>Деактивация аккаунта</CardTitle>
        <CardDescription>
          Вы можете деактивировать свой аккаунт, если он вам больше не нужен. Вы можете в любой
          момент его реактивировать.
        </CardDescription>
      </CardHeader>

      <CardContent className="flex justify-start">
        <AlertDialog open={open} onOpenChange={setOpen}>
          <AlertDialogTrigger asChild>
            <SpinnerButton
              text="Деактивировать аккаунт"
              loadingText="Деактивация..."
              isLoading={mutation.isPending}
            />
          </AlertDialogTrigger>

          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>Вы действительно хотите деактивировать аккаунт?</AlertDialogTitle>
              <AlertDialogDescription>
                Вы в любой момент можете реактивировать свой аккаунт в случае надобности.
              </AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel>Отмена</AlertDialogCancel>
              <AlertDialogAction onClick={handleConfirm}>Деактивировать</AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </CardContent>
    </Card>
  );
};
