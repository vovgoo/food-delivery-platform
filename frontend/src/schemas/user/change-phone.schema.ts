import { z } from "zod";
export const changePhoneSchema = z.object({
  phone: z
    .string()
    .nonempty("Телефон не может быть пустым")
});

export type ChangePhoneFormData = z.infer<typeof changePhoneSchema>;
