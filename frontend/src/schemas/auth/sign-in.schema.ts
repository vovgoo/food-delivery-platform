import { z } from 'zod';

export const signInSchema = z.object({
  phone: z.string().nonempty('Телефон не может быть пустым'),

  password: z.string().nonempty('Пароль не может быть пустым'),
});

export type SignInFormData = z.infer<typeof signInSchema>;
