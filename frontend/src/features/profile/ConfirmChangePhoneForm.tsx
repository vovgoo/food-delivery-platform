import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';

import { userService, type ConfirmChangePhoneRequest } from '@/api';
import { AppRoutes } from '@/routes';
import {
  confirmChangePhoneSchema,
  type ConfirmChangePhoneFormData,
} from '@/schemas/user/confirm-change-phone.schema';
import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { OtpInput } from '@/components/input/OtpInput';
import { SpinnerButton } from '@/components/button/SpinnerButton';
import { LinkButton } from '@/components/button/LinkButton';

export const ConfirmChangePhoneForm: React.FC = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const form = useForm<ConfirmChangePhoneFormData>({
    resolver: zodResolver(confirmChangePhoneSchema),
    defaultValues: { code: '' },
  });

  const mutation = useMutation({
    mutationFn: (payload: ConfirmChangePhoneRequest) => userService.confirmChangePhone(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['me'] });
      toast.success('Телефон успешно изменён!');
      navigate(AppRoutes.PROFILE_SETTINGS);
    },
    onError: (err: any) => {
      if (err.response?.data?.body?.errors) {
        err.response.data.body.errors.forEach(
          (fieldError: { field: string; messages: string[] }) => {
            form.setError(fieldError.field as keyof ConfirmChangePhoneFormData, {
              type: 'server',
              message: fieldError.messages.join(', '),
            });
          },
        );
      } else {
        const message =
          typeof err.response?.data?.body === 'string'
            ? err.response.data.body
            : 'Произошла ошибка';
        toast.error(message);
      }
    },
  });

  const onSubmit = (data: ConfirmChangePhoneFormData) => {
    if (!data.code) return;

    const payload: ConfirmChangePhoneRequest = { code: data.code };
    mutation.mutate(payload);
  };

  return (
    <div className="flex flex-col justify-center items-center max-w-[300px] gap-y-10">
      <h1 className="text-4xl w-full text-center font-bold">Подтверждение телефона</h1>
      <p className="w-full text-center">Мы отправили код на указанный вами телефон</p>

      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-4">
          <FormField
            control={form.control}
            name="code"
            render={() => (
              <FormItem>
                <FormControl>
                  <OtpInput
                    name="code"
                    control={form.control}
                    error={form.formState.errors.code?.message}
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <SpinnerButton
            text="Подтвердить"
            loadingText="Подтверждаем..."
            isLoading={mutation.isPending || form.formState.isSubmitting}
            onClick={form.handleSubmit(onSubmit)}
          />

          <LinkButton text="Обратно в профиль" to={AppRoutes.PROFILE_SETTINGS} />
        </form>
      </Form>
    </div>
  );
};
