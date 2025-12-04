import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation } from '@tanstack/react-query';
import { useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { AppRoutes } from '@/routes';
import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { confirmSignUpSchema, type ConfirmSignUpFormData } from '@/schemas';
import { authService, type ConfirmSignUpRequest, type JwtResponse } from '@/api';
import { LinkButton, OtpInput, SpinnerButton } from '@/components';

export const ConfirmSignUpForm: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const phone = (location.state as { phone?: string })?.phone;

  useEffect(() => {
    if (!phone) {
      navigate(AppRoutes.MAIN, { replace: true });
    }
  }, [phone, navigate]);

  if (!phone) return null;

  const form = useForm<ConfirmSignUpFormData>({
    resolver: zodResolver(confirmSignUpSchema),
    defaultValues: { code: '', phone: phone || '' },
  });

  const mutation = useMutation({
    mutationFn: (payload: ConfirmSignUpRequest) => authService.confirmSignUp(payload),
    onSuccess: (data: JwtResponse) => {
      localStorage.setItem('accessToken', data.accessToken);
      toast.success('Регистрация успешно подтверждена!');
      navigate(AppRoutes.MAIN);
    },
    onError: (err: any) => {
      if (err.response?.data?.body?.errors) {
        err.response.data.body.errors.forEach(
          (fieldError: { field: string; messages: string[] }) => {
            form.setError(fieldError.field as keyof ConfirmSignUpFormData, {
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

  const onSubmit = (data: ConfirmSignUpFormData) => {
    if (!data.phone || !data.code) return;

    const payload: ConfirmSignUpRequest = {
      phone: data.phone,
      code: data.code,
    };

    mutation.mutate(payload);
  };

  function maskPhone(phone: string) {
    if (!phone || phone.length <= 5) return phone;
    const start = phone.slice(0, 4);
    const end = phone.slice(-3);
    const middleLength = phone.length - start.length - end.length;
    const middle = 'x'.repeat(middleLength);
    return `${start}${middle}${end}`;
  }

  return (
    <div className="flex flex-col justify-center items-center max-w-[300px] gap-y-10">
      <h1 className="text-4xl w-full text-center font-bold">Подтверждение телефона</h1>
      <p className="w-full text-center">Мы отправили код на {maskPhone(phone)}</p>

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
          <LinkButton text="Обратно к регистрации" to={AppRoutes.SIGN_UP} />
        </form>
      </Form>
    </div>
  );
};
