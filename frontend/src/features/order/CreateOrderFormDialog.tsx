import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQuery } from '@tanstack/react-query';
import { toast } from 'sonner';
import {
  dishService,
  orderService,
  restaurantService,
  userService,
  type CreateOrderRequest,
} from '@/api';
import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { Button } from '@/components/ui/button';
import {
  AlertDialog,
  AlertDialogTrigger,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogCancel,
} from '@/components/ui/alert-dialog';
import { useDispatch } from 'react-redux';
import { clearCart } from '@/store/slices/cartSlice';
import { createOrderSchema, type CreateOrderFormData } from '@/schemas';
import { SelectInput, SpinnerButton } from '@/components';

interface CreateOrderFormDialogProps {
  restaurantId: string;
  items: { id: string; quantity: number }[];
}

export const CreateOrderFormDialog: React.FC<CreateOrderFormDialogProps> = ({
  restaurantId,
  items,
}) => {
  const [open, setOpen] = useState(false);
  const dispatch = useDispatch();

  const { data: userProfile } = useQuery({
    queryKey: ['order-me'],
    queryFn: () => userService.me(),
    staleTime: 0,
  });

  const { data: restaurant } = useQuery({
    queryKey: ['order-restaurant', restaurantId],
    queryFn: () => restaurantService.get(restaurantId),
  });

  const dishesQuery = useQuery({
    queryKey: ['order-dishes', restaurantId, items.map((i) => i.id).join(',')],
    queryFn: async () => Promise.all(items.map((i) => dishService.get(restaurantId, i.id))),
    enabled: items.length > 0,
  });

  const totalPrice = dishesQuery.data
    ? dishesQuery.data.reduce((sum, dish, idx) => sum + dish.price * items[idx].quantity, 0)
    : 0;

  const form = useForm<CreateOrderFormData>({
    resolver: zodResolver(createOrderSchema),
    defaultValues: {
      restaurantId,
      deliveryAddress: '',
      items: items.map((i) => ({ dishId: i.id, quantity: i.quantity })),
      payment: { paymentMethod: 'CREDIT_CARD' },
    },
  });

  useEffect(() => {
    if (userProfile?.defaultAddress?.id) {
      form.reset({
        restaurantId,
        deliveryAddress: userProfile.defaultAddress.id,
        items: items.map((i) => ({ dishId: i.id, quantity: i.quantity })),
        payment: { paymentMethod: 'CREDIT_CARD' },
      });
    }
  }, [userProfile, restaurantId, items, form]);

  const mutation = useMutation({
    mutationFn: (payload: CreateOrderRequest) => orderService.create(payload),
    onSuccess: () => {
      toast.success('Заказ успешно создан!');
      dispatch(clearCart({ restaurantId }));
      form.reset();
      setOpen(false);
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
          form.setError(fieldError.field as keyof CreateOrderFormData, {
            message: fieldError.messages.join(', '),
          });
        });
      } else {
        toast.error(typeof body === 'string' ? body : 'Произошла ошибка сервера');
      }
    },
  });

  const onSubmit = (data: CreateOrderFormData) => {
    if (!data.deliveryAddress || data.deliveryAddress === '') {
      toast.error('Выберите адрес доставки в личном кабинете');
      return;
    }

    const payload: CreateOrderRequest = {
      restaurantId: data.restaurantId,
      deliveryAddress: data.deliveryAddress,
      items: data.items.map((item) => ({ dishId: item.dishId, quantity: item.quantity })),
      payment: { paymentMethod: data.payment.paymentMethod },
    };

    mutation.mutate(payload);
  };

  return (
    <AlertDialog open={open} onOpenChange={setOpen}>
      <AlertDialogTrigger asChild>
        <Button>Оформить заказ</Button>
      </AlertDialogTrigger>

      <AlertDialogContent className="sm:max-w-none w-[600px]">
        <AlertDialogHeader>
          <AlertDialogTitle>Подтверждение заказа</AlertDialogTitle>
          <AlertDialogDescription>
            Проверьте информацию перед оформлением заказа
          </AlertDialogDescription>
        </AlertDialogHeader>

        <div className="flex flex-col gap-4 my-4">
          {restaurant && <div className="font-semibold text-lg">Ресторан: {restaurant.name}</div>}

          {userProfile?.defaultAddress && (
            <div className="border p-2 rounded bg-gray-50">
              <div className="font-medium">Адрес доставки:</div>
              <div>
                {userProfile.defaultAddress.country}, {userProfile.defaultAddress.state},{' '}
                {userProfile.defaultAddress.city}
              </div>
              <div>
                {userProfile.defaultAddress.street}, {userProfile.defaultAddress.house}
                {userProfile.defaultAddress.building
                  ? `, ${userProfile.defaultAddress.building}`
                  : ''}
              </div>
            </div>
          )}

          {dishesQuery.data && (
            <div className="border p-2 rounded bg-gray-50">
              <div className="font-medium mb-2">Ваш заказ:</div>
              <ul className="list-disc list-inside">
                {dishesQuery.data.map((dish, idx) => (
                  <li key={dish.id}>
                    {dish.name} — {items[idx].quantity}
                  </li>
                ))}
              </ul>
              <div className="mt-2 font-semibold">Общая сумма: {totalPrice} BYN</div>
            </div>
          )}

          <Form {...form}>
            <FormField
              control={form.control}
              name="payment.paymentMethod"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <SelectInput
                      name={field.name}
                      control={form.control}
                      options={[
                        { value: 'CREDIT_CARD', label: 'Кредитная карта' },
                        { value: 'DEBIT_CARD', label: 'Дебетовая карта' },
                        { value: 'PAYPAL', label: 'PayPal' },
                        { value: 'APPLE_PAY', label: 'Apple Pay' },
                        { value: 'GOOGLE_PAY', label: 'Google Pay' },
                        { value: 'BANK_TRANSFER', label: 'Банковский перевод' },
                        { value: 'CASH_ON_DELIVERY', label: 'Наличные при доставке' },
                      ]}
                      placeholder="Выберите способ оплаты"
                      error={form.formState.errors.payment?.paymentMethod?.message}
                    />
                  </FormControl>
                </FormItem>
              )}
            />
          </Form>
        </div>

        <AlertDialogFooter className="mt-4 flex justify-end gap-2">
          <AlertDialogCancel>Отмена</AlertDialogCancel>
          <SpinnerButton
            text="Оформить заказ"
            loadingText="Создаём заказ..."
            isLoading={mutation.isPending}
            onClick={form.handleSubmit(onSubmit)}
          />
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
