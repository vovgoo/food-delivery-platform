import { z } from "zod";

export const dishCreateSchema = z.object({
  name: z
    .string()
    .min(2, "Название блюда должно быть от 2 до 100 символов")
    .max(100, "Название блюда должно быть от 2 до 100 символов")
    .nonempty("Название блюда не может быть пустым"),
  description: z.string().max(500, "Описание блюда должно быть до 500 символов").optional(),
  portionInGrams: z
    .number()
    .int()
    .nonnegative("Вес блюда должен быть положительным или нулевым")
    .optional(),
  proteins: z.number().nonnegative("Количество белков должно быть положительным или нулевым").optional(),
  fats: z.number().nonnegative("Количество жиров должно быть положительным или нулевым").optional(),
  carbohydrates: z.number().nonnegative("Количество углеводов должно быть положительным или нулевым").optional(),
  spicy: z.boolean(),
  vegan: z.boolean(),
  vegetarian: z.boolean(),
  price: z.number().min(0.01, "Цена блюда должна быть больше 0"),
});

export type DishCreateFormData = z.infer<typeof dishCreateSchema>;