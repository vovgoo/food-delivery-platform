import React from 'react';
import { toast } from 'sonner';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';

import { userService, type ChangePasswordRequest } from '@/api';
import { changePasswordSchema, type ChangePasswordFormData } from '@/schemas';

import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';

import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { PasswordInput, SpinnerButton } from '@/components';

export const ChangePasswordForm: React.FC = () => {
  const queryClient = useQueryClient();

  const form = useForm<ChangePasswordFormData>({
    resolver: zodResolver(changePasswordSchema),
    defaultValues: {
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
    },
  });

  const mutation = useMutation({
    mutationFn: (payload: ChangePasswordRequest) => userService.changePassword(payload),

    onSuccess: () => {
      toast.success('Пароль успешно изменён!');
      queryClient.invalidateQueries({ queryKey: ['me'] });
      form.reset();
    },

    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};

      if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
          form.setError(fieldError.field as keyof ChangePasswordFormData, {
            message: fieldError.messages.join(', '),
          });
        });
      } else {
        toast.error(typeof body === 'string' ? body : 'Произошла ошибка сервера');
      }
    },
  });

  const onSubmit = (data: ChangePasswordFormData) => {
    const payload: ChangePasswordRequest = {
      oldPassword: data.oldPassword,
      newPassword: data.newPassword,
    };

    mutation.mutate(payload);
  };

  return (
    <Card className="w-full max-w-[1300px]">
      <CardHeader>
        <CardTitle>Изменение пароля</CardTitle>
        <CardDescription>Введите текущий пароль и новый пароль для обновления</CardDescription>
      </CardHeader>

      <CardContent>
        <Form {...form}>
          <div className="flex flex-col gap-5">
            <FormField
              control={form.control}
              name="oldPassword"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <PasswordInput
                      control={form.control}
                      name={field.name}
                      placeholder="Текущий пароль"
                      error={form.formState.errors.oldPassword?.message}
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
              <FormField
                control={form.control}
                name="newPassword"
                render={({ field }) => (
                  <FormItem>
                    <FormControl>
                      <PasswordInput
                        control={form.control}
                        name={field.name}
                        placeholder="Новый пароль"
                        error={form.formState.errors.newPassword?.message}
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="confirmPassword"
                render={({ field }) => (
                  <FormItem>
                    <FormControl>
                      <PasswordInput
                        control={form.control}
                        name={field.name}
                        placeholder="Подтверждение пароля"
                        error={form.formState.errors.confirmPassword?.message}
                      />
                    </FormControl>
                  </FormItem>
                )}
              />
            </div>

            <SpinnerButton
              text="Изменить пароль"
              loadingText="Сохранение..."
              isLoading={mutation.isPending}
              onClick={form.handleSubmit(onSubmit)}
            />
          </div>
        </Form>
      </CardContent>
    </Card>
  );
};
