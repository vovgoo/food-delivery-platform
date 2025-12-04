import { z } from "zod";

export const addOrderItemSchema = z.object({
  dishId: z.string().uuid("Неверный формат UUID для блюда"),
  quantity: z
    .number()
    .int("Количество должно быть целым числом")
    .min(1, "Количество должно быть не меньше 1"),
});

export const addPaymentSchema = z.object({
  paymentMethod: z.enum([
    "CREDIT_CARD",
    "DEBIT_CARD",
    "PAYPAL",
    "APPLE_PAY",
    "GOOGLE_PAY",
    "BANK_TRANSFER",
    "CASH_ON_DELIVERY",
  ], "Необходимо указать корректный способ оплаты"),
});

export const createOrderSchema = z.object({
  restaurantId: z.string().uuid("Неверный формат UUID для ресторана"),
  deliveryAddress: z.string().optional(),
  items: z
    .array(addOrderItemSchema)
    .nonempty("Список блюд не может быть пустым"),
  payment: addPaymentSchema,
});

export type CreateOrderFormData = z.infer<typeof createOrderSchema>;
