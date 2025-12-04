import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';

import { updateUserProfileSchema, type UpdateUserProfileFormData } from '@/schemas';
import { userService, type UpdateUserProfileRequest, type UserResponse } from '@/api';

import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { DateInput, SpinnerButton, TextInput } from '@/components';

export const UpdateUserProfileForm: React.FC = () => {
  const queryClient = useQueryClient();

  const { data: user, isLoading } = useQuery<UserResponse>({
    queryKey: ['me'],
    queryFn: () => userService.me(),
  });

  const form = useForm<UpdateUserProfileFormData>({
    resolver: zodResolver(updateUserProfileSchema),
    defaultValues: {
      fullName: '',
      birthDate: '',
    },
  });

  useEffect(() => {
    if (user) {
      form.reset({
        fullName: user.fullName,
        birthDate: user.birthDate,
      });
    }
  }, [user, form]);

  const mutation = useMutation({
    mutationFn: (payload: UpdateUserProfileRequest) => userService.changeProfile(payload),

    onSuccess: (updatedUser) => {
      toast.success('Профиль успешно обновлён!');
      queryClient.setQueryData(['me'], updatedUser);

      form.reset({
        fullName: updatedUser.fullName,
        birthDate: updatedUser.birthDate,
      });
    },

    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};

      if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
          form.setError(fieldError.field as keyof UpdateUserProfileFormData, {
            message: fieldError.messages.join(', '),
          });
        });
      } else {
        toast.error(typeof body === 'string' ? body : 'Произошла ошибка сервера');
      }
    },
  });

  const onSubmit = (data: UpdateUserProfileFormData) => {
    const payload: UpdateUserProfileRequest = {
      fullName: data.fullName,
      birthDate: data.birthDate,
    };
    mutation.mutate(payload);
  };

  return (
    <Card className="w-full max-w-[1300px]">
      <CardHeader>
        <CardTitle>Обновление профиля</CardTitle>
        <CardDescription>Измените свои персональные данные</CardDescription>
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

            <FormField
              control={form.control}
              name="birthDate"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <DateInput
                      name={field.name}
                      control={form.control}
                      placeholder="Дата рождения"
                      error={form.formState.errors.birthDate?.message}
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <SpinnerButton
              text="Сохранить изменения"
              loadingText="Сохранение..."
              isLoading={mutation.isPending || isLoading}
              onClick={form.handleSubmit(onSubmit)}
            />
          </div>
        </Form>
      </CardContent>
    </Card>
  );
};
