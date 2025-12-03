import { z } from "zod";

export const uploadImageSchema = z.object({
  profileImage: z
    .instanceof(File)
    .refine(file => file.size <= 5 * 1024 * 1024, "Файл не должен превышать 5 МБ")
    .refine(file => ["image/jpeg", "image/png", "image/webp"].includes(file.type), "Неподдерживаемый формат файла"),
});

export type UploadImageFormData = z.infer<typeof uploadImageSchema>;
