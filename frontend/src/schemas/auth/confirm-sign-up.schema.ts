import { z } from "zod";

export const confirmSignUpSchema = z.object({
  phone: z
    .string()
    .nonempty("Номер телефона обязателен"),
  code: z
    .string()
    .nonempty("Код обязателен")
    .length(6, "Код должен быть ровно 6 символов"),
});

export type ConfirmSignUpFormData = z.infer<typeof confirmSignUpSchema>;
