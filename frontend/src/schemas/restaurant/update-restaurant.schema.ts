import { z } from "zod";
import { restaurantCreateSchema } from "./create-restaurant.schema";

export const restaurantUpdateSchema = restaurantCreateSchema.extend({
  status: z.enum(["ACTIVE", "INACTIVE"]),
});

export type RestaurantUpdateFormData = z.infer<typeof restaurantUpdateSchema>;