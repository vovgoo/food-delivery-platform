import { z } from "zod";
import { dishCreateSchema } from "./create-dish.schema";

export const dishUpdateSchema = dishCreateSchema.extend({
  status: z.enum(["AVAILABLE", "TEMPORARY_UNAVAILABLE"]),
});

export type DishUpdateFormData = z.infer<typeof dishUpdateSchema>;