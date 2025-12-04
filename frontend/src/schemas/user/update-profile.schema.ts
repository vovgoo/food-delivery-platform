import { z } from 'zod';
import dayjs from 'dayjs';

export const updateUserProfileSchema = z.object({
  fullName: z
    .string()
    .nonempty('Полное имя не может быть пустым')
    .min(2, 'Полное имя должно быть от 2 до 100 символов')
    .max(100, 'Полное имя должно быть от 2 до 100 символов'),

  birthDate: z
    .string()
    .nonempty('День рождения обязательна')
    .refine((value) => value && dayjs(value, 'YYYY-MM-DD', true).isValid(), {
      message: 'Некорректная дата',
    })
    .refine((value) => value && dayjs().diff(dayjs(value, 'YYYY-MM-DD'), 'year') >= 16, {
      message: 'Пользователь должен быть старше 16 лет',
    }),
});

export type UpdateUserProfileFormData = z.infer<typeof updateUserProfileSchema>;
