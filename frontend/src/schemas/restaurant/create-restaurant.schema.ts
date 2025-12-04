import { z } from 'zod';

export const restaurantCreateSchema = z.object({
  name: z.string().min(2, 'Название ресторана должно быть от 2 до 100 символов').max(100),
  description: z
    .string()
    .max(1000, 'Описание ресторана не должно превышать 1000 символов')
    .optional(),
  cuisine: z.string().min(2, 'Кухня должна быть от 2 до 50 символов').max(50),
  address: z.string().min(5, 'Адрес должен быть от 5 до 200 символов').max(200),
  website: z.string().max(200).url('Некорректный формат сайта').optional(),
  phone: z.string().nonempty('Телефон не может быть пустым'),
  openingTime: z.string().nonempty('Время открытия должно быть указано'),
  closingTime: z.string().nonempty('Время закрытия должно быть указано'),
  deliveryAvailable: z.boolean(),
  parkingAvailable: z.boolean(),
});

export type RestaurantCreateFormData = z.infer<typeof restaurantCreateSchema>;
