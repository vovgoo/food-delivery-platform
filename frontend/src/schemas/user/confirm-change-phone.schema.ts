import { z } from "zod";

export const confirmChangePhoneSchema = z.object({
  code: z
    .string()
    .nonempty("Код не может быть пустым")
    .regex(/^\d{6}$/, "Код должен состоять ровно из 6 цифр"),
});

export type ConfirmChangePhoneFormData = z.infer<typeof confirmChangePhoneSchema>;