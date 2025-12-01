import { z } from "zod";
import dayjs from "dayjs";

const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;

export const signUpSchema = z.object({
  phone: z.string().nonempty("Телефон не может быть пустым"),

  fullName: z
    .string()
    .nonempty("Полное имя не может быть пустым")
    .min(2, "Полное имя должно быть от 2 до 100 символов")
    .max(100, "Полное имя должно быть от 2 до 100 символов"),

  birthDate: z
    .string()
    .nonempty("День рождения обязательна")
    .refine((value) => value && dayjs(value, "YYYY-MM-DD", true).isValid(), {
      message: "Некорректная дата",
    })
    .refine((value) => value && dayjs().diff(dayjs(value, "YYYY-MM-DD"), "year") >= 16, {
      message: "Пользователь должен быть старше 16 лет",
    }),

  password: z
    .string()
    .nonempty("Пароль не может быть пустым")
    .min(8, "Пароль должен быть от 8 до 100 символов")
    .max(100, "Пароль должен быть от 8 до 100 символов")
    .regex(
      passwordRegex,
      "Пароль должен содержать буквы, цифры и спецсимволы"
    ),

  confirmPassword: z.string().nonempty("Подтверждение пароля обязательно"),
}).refine((data) => data.password === data.confirmPassword, {
  message: "Пароли должны совпадать",
  path: ["confirmPassword"],
});

export type SignUpFormData = z.infer<typeof signUpSchema>;
