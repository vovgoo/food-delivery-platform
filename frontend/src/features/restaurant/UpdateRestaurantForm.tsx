import React, { useEffect } from 'react';
import { toast } from 'sonner';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';

import { type RestaurantUpdateRequest, type RestaurantResponse } from '@/api';
import {
  restaurantUpdateSchema,
  type RestaurantUpdateFormData,
} from '@/schemas/restaurant/update-restaurant.schema';

import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { TextInput } from '@/components/input/TextInput';
import { TextareaInput } from '@/components/input/TextareaInput';
import { PhoneInput } from '@/components/input/PhoneInput';
import { CheckboxInput } from '@/components/input/CheckBoxInput';
import { TimeInput } from '@/components/input/TimeInput';
import { SelectInput } from '@/components/input/SelectInput';
import { SpinnerButton } from '@/components/button/SpinnerButton';

import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { restaurantService } from '@/api/services/restaurant/restaurant.service';
import { Skeleton } from '@/components/ui/skeleton';

interface UpdateRestaurantFormProps {
  restaurant?: RestaurantResponse;
  isPending: boolean;
}

export const UpdateRestaurantForm: React.FC<UpdateRestaurantFormProps> = ({
  restaurant,
  isPending,
}) => {
  const queryClient = useQueryClient();

  const form = useForm<RestaurantUpdateFormData>({
    resolver: zodResolver(restaurantUpdateSchema),
    defaultValues: {
      name: '',
      description: '',
      cuisine: '',
      address: '',
      website: '',
      phone: '',
      openingTime: '00:00',
      closingTime: '00:00',
      deliveryAvailable: false,
      parkingAvailable: false,
      status: 'INACTIVE',
    },
  });

  useEffect(() => {
    if (restaurant) {
      form.reset({
        name: restaurant.name,
        description: restaurant.description || '',
        cuisine: restaurant.cuisine,
        address: restaurant.address,
        website: restaurant.website || '',
        phone: restaurant.phone,
        openingTime: restaurant.openingTime,
        closingTime: restaurant.closingTime,
        deliveryAvailable: restaurant.deliveryAvailable,
        parkingAvailable: restaurant.parkingAvailable,
        status:
          restaurant.status === 'ACTIVE' || restaurant.status === 'INACTIVE'
            ? restaurant.status
            : 'INACTIVE',
      });
    }
  }, [restaurant, form]);

  const mutation = useMutation({
    mutationFn: (payload: RestaurantUpdateRequest) =>
      restaurantService.update(restaurant!.id, payload),
    onSuccess: () => {
      toast.success('Ресторан успешно обновлён!');
      queryClient.invalidateQueries({ queryKey: ['admin-restaurant'] });
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
          form.setError(fieldError.field as keyof RestaurantUpdateFormData, {
            message: fieldError.messages.join(', '),
          });
        });
      } else {
        toast.error(typeof body === 'string' ? body : 'Произошла ошибка сервера');
      }
    },
  });

  const onSubmit = (data: RestaurantUpdateFormData) => {
    const payload: RestaurantUpdateRequest = {
      name: data.name,
      description: data.description || undefined,
      cuisine: data.cuisine,
      address: data.address,
      website: data.website || undefined,
      phone: data.phone,
      openingTime: data.openingTime,
      closingTime: data.closingTime,
      deliveryAvailable: data.deliveryAvailable,
      parkingAvailable: data.parkingAvailable,
      status: data.status,
    };
    mutation.mutate(payload);
  };

  return (
    <Card className="w-full max-w-[1300px]">
      <CardHeader>
        <CardTitle>Обновление ресторана</CardTitle>
        <CardDescription>Измените информацию о ресторане</CardDescription>
      </CardHeader>

      <CardContent>
        <Form {...form}>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-10 w-full mb-4" />
                    ) : (
                      <TextInput
                        name={field.name}
                        control={form.control}
                        placeholder="Название ресторана"
                        error={form.formState.errors.name?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="cuisine"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-10 w-full mb-4" />
                    ) : (
                      <TextInput
                        name={field.name}
                        control={form.control}
                        placeholder="Кухня"
                        error={form.formState.errors.cuisine?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="address"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-10 w-full mb-4" />
                    ) : (
                      <TextInput
                        name={field.name}
                        control={form.control}
                        placeholder="Адрес"
                        error={form.formState.errors.address?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="website"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-10 w-full mb-4" />
                    ) : (
                      <TextInput
                        name={field.name}
                        control={form.control}
                        placeholder="Сайт (необязательно)"
                        error={form.formState.errors.website?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="phone"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-10 w-full mb-4" />
                    ) : (
                      <PhoneInput
                        name={field.name}
                        control={form.control}
                        error={form.formState.errors.phone?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="status"
              render={({ field }) => (
                <FormItem className="w-full">
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-10 w-full mb-4" />
                    ) : (
                      <SelectInput
                        name={field.name}
                        control={form.control}
                        options={[
                          { value: 'ACTIVE', label: 'Активен' },
                          { value: 'INACTIVE', label: 'Неактивен' },
                        ]}
                        placeholder="Статус"
                        error={form.formState.errors.status?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem className="col-span-full">
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-24 w-full mb-4" />
                    ) : (
                      <TextareaInput
                        name={field.name}
                        control={form.control}
                        placeholder="Описание ресторана"
                        rows={4}
                        error={form.formState.errors.description?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="openingTime"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-10 w-full mb-4" />
                    ) : (
                      <TimeInput
                        name={field.name}
                        control={form.control}
                        placeholder="Время открытия"
                        error={form.formState.errors.openingTime?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="closingTime"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-10 w-full mb-4" />
                    ) : (
                      <TimeInput
                        name={field.name}
                        control={form.control}
                        placeholder="Время закрытия"
                        error={form.formState.errors.closingTime?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="deliveryAvailable"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-6 w-32 mb-4" />
                    ) : (
                      <CheckboxInput
                        name={field.name}
                        control={form.control}
                        label="Доступна доставка"
                        error={form.formState.errors.deliveryAvailable?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="parkingAvailable"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="bg-gray-400 h-6 w-24 mb-4" />
                    ) : (
                      <CheckboxInput
                        name={field.name}
                        control={form.control}
                        label="Есть парковка"
                        error={form.formState.errors.parkingAvailable?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />
          </div>

          <div className="mt-4 flex justify-end">
            {isPending ? (
              <Skeleton className="h-10 w-40 bg-gray-400 rounded-md" />
            ) : (
              <SpinnerButton
                text="Обновить ресторан"
                loadingText="Сохраняем..."
                isLoading={mutation.isPending}
                onClick={form.handleSubmit(onSubmit)}
              />
            )}
          </div>
        </Form>
      </CardContent>
    </Card>
  );
};
