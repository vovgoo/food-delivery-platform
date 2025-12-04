import React from 'react';
import { toast } from 'sonner';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { authService, type SignUpRequest } from '@/api';
import { AppRoutes } from '@/routes';
import { signUpSchema, type SignUpFormData } from '@/schemas';
import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import {
  DateInput,
  LinkButton,
  PasswordInput,
  PhoneInput,
  SpinnerButton,
  TextInput,
} from '@/components';

export const SignUpForm: React.FC = () => {
  const navigate = useNavigate();

  const form = useForm<SignUpFormData>({
    resolver: zodResolver(signUpSchema),
    defaultValues: {
      phone: '',
      fullName: '',
      birthDate: '',
      password: '',
      confirmPassword: '',
    },
  });

  const mutation = useMutation({
    mutationFn: (payload: SignUpRequest) => authService.signUp(payload),
    onSuccess: (_data, variables) => {
      navigate(AppRoutes.CONFIRM_SIGN_UP, { state: { phone: variables.phone } });
      toast.success(
        'На ваш номер был отправлен временный код, введите его что бы завершить регистрацию!',
      );
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
          form.setError(fieldError.field as keyof SignUpFormData, {
            message: fieldError.messages.join(', '),
          });
        });
      } else {
        toast.error(typeof body === 'string' ? body : 'Произошла ошибка сервера');
      }
    },
  });

  const onSubmit = (data: SignUpFormData) => {
    const payload: SignUpRequest = {
      phone: data.phone.replace(/[^\d+]/g, ''),
      fullName: data.fullName,
      birthDate: data.birthDate,
      password: data.password,
    };
    mutation.mutate(payload);
  };

  return (
    <Card className="w-full max-w-[700px]">
      <CardHeader>
        <CardTitle>Регистрация</CardTitle>
        <CardDescription>Заполните форму ниже, чтобы создать аккаунт</CardDescription>
      </CardHeader>

      <CardContent>
        <Form {...form}>
          <div className="flex flex-col gap-5">
            <FormField
              control={form.control}
              name="fullName"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <TextInput
                      name={field.name}
                      control={form.control}
                      placeholder="Полное имя"
                      error={form.formState.errors.fullName?.message}
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
              <FormField
                control={form.control}
                name="phone"
                render={({ field }) => (
                  <FormItem>
                    <FormControl>
                      <PhoneInput
                        name={field.name}
                        control={form.control}
                        placeholder="Tелефон"
                        error={form.formState.errors.phone?.message}
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="birthDate"
                render={({ field }) => (
                  <FormItem>
                    <FormControl>
                      <DateInput
                        control={form.control}
                        name={field.name}
                        placeholder="Выберите дату рождения"
                        error={form.formState.errors.birthDate?.message}
                      />
                    </FormControl>
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormControl>
                      <PasswordInput
                        control={form.control}
                        name={field.name}
                        placeholder="Пароль"
                        error={form.formState.errors.password?.message}
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
              text="Зарегистрироваться"
              loadingText="Регистрация..."
              isLoading={mutation.isPending}
              onClick={form.handleSubmit(onSubmit)}
            />

            <LinkButton text="Есть аккаунт? Войти" to={AppRoutes.SIGN_IN} />
          </div>
        </Form>
      </CardContent>
    </Card>
  );
};
