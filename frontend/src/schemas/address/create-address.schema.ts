import { z } from "zod";

export const createAddressSchema = z.object({
  country: z
    .string()
    .nonempty("Страна не может быть пустой")
    .max(100, "Слишком длинная страна")
    .regex(/^[\p{L}\s\-]+$/u, "Страна содержит недопустимые символы"),

  state: z
    .string()
    .nonempty("Штат/регион не может быть пустым")
    .max(100, "Слишком длинный штат/регион")
    .regex(/^[\p{L}\s\-]+$/u, "Штат/регион содержит недопустимые символы"),

  city: z
    .string()
    .nonempty("Город не может быть пустым")
    .max(100, "Слишком длинный город")
    .regex(/^[\p{L}\s\-]+$/u, "Город содержит недопустимые символы"),

  street: z
    .string()
    .nonempty("Улица не может быть пустой")
    .max(255, "Слишком длинная улица")
    .regex(/^[\p{L}0-9\s\-\.]+$/u, "Улица содержит недопустимые символы"),

  house: z
    .string()
    .nonempty("Номер дома обязателен")
    .max(20, "Номер дома слишком длинный")
    .regex(/^[0-9\p{L}\-\/]+$/u, "Номер дома содержит недопустимые символы"),

  building: z
    .string()
    .max(10, "Слишком длинный корпус")
    .regex(/^[0-9A-Za-z\-\/]*$/u, "Корпус содержит недопустимые символы")
    .optional(),

  apartment: z
    .string()
    .max(10, "Слишком длинная квартира/офис")
    .regex(/^[0-9A-Za-z\-\/]*$/u, "Квартира содержит недопустимые символы")
    .optional(),

  deliveryInstructions: z
    .string()
    .max(500, "Инструкция слишком длинная")
    .optional(),

  zip: z
    .string()
    .nonempty("ZIP код не может быть пустым")
    .max(20, "ZIP слишком длинный")
    .regex(/^[0-9\-\s]+$/, "ZIP содержит недопустимые символы"),
});

export type CreateAddressFormData = z.infer<typeof createAddressSchema>;
