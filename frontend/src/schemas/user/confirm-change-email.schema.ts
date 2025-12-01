import { z } from "zod";

export const confirmChangeEmailSchema = z.object({
  token: z.string().uuid("Неверный формат токена"),
});

export type ConfirmChangeEmailFormData = z.infer<typeof confirmChangeEmailSchema>;