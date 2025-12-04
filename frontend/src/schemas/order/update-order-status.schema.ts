import { z } from "zod";

export const orderStatusEnum = z.enum([
  "CREATED",
  "CONFIRMED",
  "PREPARING",
  "READY",
  "DELIVERING",
  "COMPLETED",
  "CANCELLED",
]);

export const updateOrderStatusSchema = z.object({
  status: orderStatusEnum,
});

export type UpdateOrderStatusFormData = z.infer<typeof updateOrderStatusSchema>;
