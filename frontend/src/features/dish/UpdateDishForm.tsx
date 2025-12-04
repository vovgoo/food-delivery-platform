import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';

import { type DishResponse, type RestaurantResponse, type DishUpdateRequest } from '@/api';
import { dishService } from '@/api/services/dish/dish.service';
import { dishUpdateSchema, type DishUpdateFormData } from '@/schemas/dish/update-dish.schema';

import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { TextInput } from '@/components/input/TextInput';
import { TextareaInput } from '@/components/input/TextareaInput';
import { NumberInput } from '@/components/input/NumberInput';
import { CheckboxInput } from '@/components/input/CheckBoxInput';
import { SelectInput } from '@/components/input/SelectInput';
import { SpinnerButton } from '@/components/button/SpinnerButton';

import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { Skeleton } from '@/components/ui/skeleton';

interface UpdateDishFormProps {
  dish?: DishResponse;
  restaurant?: RestaurantResponse;
  isPending: boolean;
}

export const UpdateDishForm: React.FC<UpdateDishFormProps> = ({ dish, restaurant, isPending }) => {
  const queryClient = useQueryClient();
  const form = useForm<DishUpdateFormData>({
    resolver: zodResolver(dishUpdateSchema),
    defaultValues: {
      name: '',
      description: '',
      portionInGrams: undefined,
      proteins: undefined,
      fats: undefined,
      carbohydrates: undefined,
      spicy: false,
      vegan: false,
      vegetarian: false,
      price: undefined,
      status: 'AVAILABLE',
    },
  });

  useEffect(() => {
    if (dish) {
      form.reset({
        name: dish.name,
        description: dish.description || '',
        portionInGrams: dish.portionInGrams,
        proteins: dish.proteins,
        fats: dish.fats,
        carbohydrates: dish.carbohydrates,
        spicy: dish.spicy,
        vegan: dish.vegan,
        vegetarian: dish.vegetarian,
        price: dish.price,
        status: dish.status === 'AVAILABLE' ? 'AVAILABLE' : 'TEMPORARY_UNAVAILABLE',
      });
    }
  }, [dish, form]);

  const mutation = useMutation({
    mutationFn: (data: DishUpdateRequest) => {
      if (!restaurant || !dish) throw new Error('Restaurant или Dish не загружены');
      return dishService.update(restaurant.id, dish.id, data);
    },
    onSuccess: () => {
      toast.success('Блюдо успешно обновлено!');
      if (restaurant) queryClient.invalidateQueries({ queryKey: ['admin-dish'] });
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
          form.setError(fieldError.field as keyof DishUpdateFormData, {
            message: fieldError.messages.join(', '),
          });
        });
      } else {
        toast.error(typeof body === 'string' ? body : 'Произошла ошибка сервера');
      }
    },
  });

  const onSubmit = (data: DishUpdateFormData) => {
    const payload: DishUpdateRequest = {
      name: data.name,
      description: data.description || undefined,
      portionInGrams: data.portionInGrams,
      proteins: data.proteins,
      fats: data.fats,
      carbohydrates: data.carbohydrates,
      spicy: data.spicy,
      vegan: data.vegan,
      vegetarian: data.vegetarian,
      price: data.price,
      status: data.status,
    };
    mutation.mutate(payload);
  };

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>Обновление блюда</CardTitle>
        <CardDescription>
          Измените информацию о блюде ресторана <strong>{restaurant?.name ?? '...'}</strong>
        </CardDescription>
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
                      <Skeleton className="h-10 w-full mb-4 bg-gray-400" />
                    ) : (
                      <TextInput
                        name={field.name}
                        control={form.control}
                        placeholder="Название блюда"
                        error={form.formState.errors.name?.message}
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
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-10 w-full mb-4 bg-gray-400" />
                    ) : (
                      <SelectInput
                        name={field.name}
                        control={form.control}
                        options={[
                          { value: 'AVAILABLE', label: 'Доступно' },
                          { value: 'TEMPORARY_UNAVAILABLE', label: 'Временно не доступно' },
                        ]}
                        placeholder="Статус блюда"
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
                      <Skeleton className="h-24 w-full mb-4 bg-gray-400" />
                    ) : (
                      <TextareaInput
                        name={field.name}
                        control={form.control}
                        placeholder="Описание блюда"
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
              name="portionInGrams"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-10 w-full mb-4 bg-gray-400" />
                    ) : (
                      <NumberInput
                        name={field.name}
                        control={form.control}
                        placeholder="Вес в граммах"
                        error={form.formState.errors.portionInGrams?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="proteins"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-10 w-full mb-4 bg-gray-400" />
                    ) : (
                      <NumberInput
                        name={field.name}
                        control={form.control}
                        placeholder="Белки"
                        error={form.formState.errors.proteins?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="fats"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-10 w-full mb-4 bg-gray-400" />
                    ) : (
                      <NumberInput
                        name={field.name}
                        control={form.control}
                        placeholder="Жиры"
                        error={form.formState.errors.fats?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="carbohydrates"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-10 w-full mb-4 bg-gray-400" />
                    ) : (
                      <NumberInput
                        name={field.name}
                        control={form.control}
                        placeholder="Углеводы"
                        error={form.formState.errors.carbohydrates?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="spicy"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-6 w-32 mb-4 bg-gray-400" />
                    ) : (
                      <CheckboxInput
                        name={field.name}
                        control={form.control}
                        label="Острое"
                        error={form.formState.errors.spicy?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="vegan"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-6 w-28 mb-4 bg-gray-400" />
                    ) : (
                      <CheckboxInput
                        name={field.name}
                        control={form.control}
                        label="Веган"
                        error={form.formState.errors.vegan?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="vegetarian"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-6 w-40 mb-4 bg-gray-400" />
                    ) : (
                      <CheckboxInput
                        name={field.name}
                        control={form.control}
                        label="Вегетарианец"
                        error={form.formState.errors.vegetarian?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="price"
              render={({ field }) => (
                <FormItem className="col-span-full">
                  <FormControl>
                    {isPending ? (
                      <Skeleton className="h-10 w-full mb-4 bg-gray-400" />
                    ) : (
                      <NumberInput
                        name={field.name}
                        control={form.control}
                        placeholder="Цена"
                        error={form.formState.errors.price?.message}
                      />
                    )}
                  </FormControl>
                </FormItem>
              )}
            />
          </div>

          <div className="mt-4 flex justify-end">
            {isPending ? (
              <Skeleton className="h-10 w-40 rounded-md bg-gray-400" />
            ) : (
              <SpinnerButton
                text="Обновить блюдо"
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
