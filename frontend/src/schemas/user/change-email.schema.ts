import { z } from 'zod';

export const changeEmailSchema = z.object({
  email: z
    .string()
    .nonempty('Почта не может быть пустой')
    .email('Почта должна быть корректной')
    .max(255, 'Почта слишком длинная'),
});

export type ChangeEmailFormData = z.infer<typeof changeEmailSchema>;
