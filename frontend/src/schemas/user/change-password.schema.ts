import { z } from "zod";

const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;

export const changePasswordSchema = z
  .object({
    oldPassword: z
      .string()
      .nonempty("Пароль не может быть пустым")
      .min(8, "Пароль должен быть от 8 до 100 символов")
      .max(100, "Пароль должен быть от 8 до 100 символов")
      .regex(passwordRegex, "Пароль должен содержать буквы, цифры и спецсимволы"),

    newPassword: z
      .string()
      .nonempty("Пароль не может быть пустым")
      .min(8, "Пароль должен быть от 8 до 100 символов")
      .max(100, "Пароль должен быть от 8 до 100 символов")
      .regex(passwordRegex, "Пароль должен содержать буквы, цифры и спецсимволы"),

    confirmPassword: z
      .string()
      .nonempty("Подтверждение пароля обязательно"),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: "Пароли должны совпадать",
    path: ["confirmPassword"],
  });

export type ChangePasswordFormData = z.infer<typeof changePasswordSchema>;
