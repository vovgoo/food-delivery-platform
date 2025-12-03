import React, { useState } from "react";
import { toast } from "sonner";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { type DishCreateRequest, type RestaurantResponse } from "@/api";
import { dishService } from "@/api/services/dish/dish.service";

import { Form, FormField, FormItem, FormControl } from "@/components/ui/form";
import { TextInput } from "@/components/input/TextInput";
import { TextareaInput } from "@/components/input/TextareaInput";
import { NumberInput } from "@/components/input/NumberInput";
import { CheckboxInput } from "@/components/input/CheckBoxInput";
import { SpinnerButton } from "@/components/button/SpinnerButton";
import { Skeleton } from "@/components/ui/skeleton";

import {
  AlertDialog,
  AlertDialogTrigger,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogCancel,
  AlertDialogAction,
} from "@/components/ui/alert-dialog";

import { Button } from "@/components/ui/button";
import { PlusIcon } from "lucide-react";
import { dishCreateSchema, type DishCreateFormData } from "@/schemas/dish/create-dish.schema";

interface CreateDishFormDialogProps {
  restaurant?: RestaurantResponse;
  isPending: boolean;
}

export const CreateDishFormDialog: React.FC<CreateDishFormDialogProps> = ({ restaurant, isPending }) => {
  const queryClient = useQueryClient();
  const [open, setOpen] = useState(false);

  const form = useForm<DishCreateFormData>({
    resolver: zodResolver(dishCreateSchema),
    defaultValues: {
      name: "",
      description: "",
      portionInGrams: undefined,
      proteins: undefined,
      fats: undefined,
      carbohydrates: undefined,
      spicy: false,
      vegan: false,
      vegetarian: false,
      price: undefined,
    },
  });

  const mutation = useMutation({
    mutationFn: (payload: DishCreateRequest) => {
        if (!restaurant) throw new Error("Restaurant не выбран");
        return dishService.create(restaurant.id, payload);
    },
    onSuccess: () => {
        if (!restaurant) return;
        toast.success("Блюдо успешно создано!");
        queryClient.invalidateQueries({ queryKey: ["admin-dishes"] });
        form.reset();
        setOpen(false);
    },
    onError: (err: any) => {
        const { statusCode, body } = err.response?.data || {};
        if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
            form.setError(fieldError.field as keyof DishCreateFormData, {
            message: fieldError.messages.join(", "),
            });
        });
        } else {
        toast.error(typeof body === "string" ? body : "Произошла ошибка сервера");
        }
    },
    });

  const onSubmit = (data: DishCreateFormData) => {
    const payload: DishCreateRequest = {
      name: data.name,
      description: data.description || undefined,
      portionInGrams: data.portionInGrams !== undefined ? parseFloat(Number(data.portionInGrams).toFixed(2)) : undefined,
      proteins: data.proteins !== undefined ? parseFloat(Number(data.proteins).toFixed(2)) : undefined,
      fats: data.fats !== undefined ? parseFloat(Number(data.fats).toFixed(2)) : undefined,
      carbohydrates: data.carbohydrates !== undefined ? parseFloat(Number(data.carbohydrates).toFixed(2)) : undefined,
      spicy: data.spicy,
      vegan: data.vegan,
      vegetarian: data.vegetarian,
      price: data.price !== undefined ? parseFloat(Number(data.price).toFixed(2)) : 0,
    };
    mutation.mutate(payload);
  };

  return (
    <AlertDialog open={open} onOpenChange={setOpen}>
      <AlertDialogTrigger asChild>
        {isPending ? (
          <Skeleton className="w-40 h-10 rounded-md" />
        ) : (
          <Button variant="default" className="flex items-center gap-2">
            <PlusIcon className="w-4 h-4" />
            Добавить блюдо
          </Button>
        )}
      </AlertDialogTrigger>

      <AlertDialogContent className="sm:max-w-none w-[700px]">
        <AlertDialogHeader>
          <AlertDialogTitle>Добавление нового блюда</AlertDialogTitle>
          <AlertDialogDescription>
            Заполните форму ниже, чтобы добавить новое блюдо для ресторана <strong>{restaurant?.name}</strong>.
          </AlertDialogDescription>
        </AlertDialogHeader>

        <Form {...form}>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <FormField control={form.control} name="name" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <TextInput
                    name={field.name}
                    control={form.control}
                    placeholder="Название блюда"
                    error={form.formState.errors.name?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="description" render={({ field }) => (
              <FormItem className="col-span-full">
                <FormControl>
                  <TextareaInput
                    name={field.name}
                    control={form.control}
                    placeholder="Описание блюда"
                    rows={4}
                    error={form.formState.errors.description?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="portionInGrams" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <NumberInput
                    name={field.name}
                    control={form.control}
                    placeholder="Вес в граммах"
                    error={form.formState.errors.portionInGrams?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="proteins" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <NumberInput
                    name={field.name}
                    control={form.control}
                    placeholder="Белки"
                    error={form.formState.errors.proteins?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="fats" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <NumberInput
                    name={field.name}
                    control={form.control}
                    placeholder="Жиры"
                    error={form.formState.errors.fats?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="carbohydrates" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <NumberInput
                    name={field.name}
                    control={form.control}
                    placeholder="Углеводы"
                    error={form.formState.errors.carbohydrates?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="spicy" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <CheckboxInput
                    name={field.name}
                    control={form.control}
                    label="Острое"
                    error={form.formState.errors.spicy?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="vegan" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <CheckboxInput
                    name={field.name}
                    control={form.control}
                    label="Веган"
                    error={form.formState.errors.vegan?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="vegetarian" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <CheckboxInput
                    name={field.name}
                    control={form.control}
                    label="Вегетарианец"
                    error={form.formState.errors.vegetarian?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
            
            <FormField control={form.control} name="price" render={({ field }) => (
              <FormItem>
                <FormControl>
                  <NumberInput
                    name={field.name}
                    control={form.control}
                    placeholder="Цена"
                    error={form.formState.errors.price?.message}
                  />
                </FormControl>
              </FormItem>
            )} />
          </div>
        </Form>

        <AlertDialogFooter className="mt-4 flex justify-end gap-2">
          <AlertDialogCancel>Отмена</AlertDialogCancel>
          <AlertDialogAction asChild>
            <SpinnerButton
              text="Добавить блюдо"
              loadingText="Сохраняем..."
              isLoading={mutation.isPending}
              onClick={form.handleSubmit(onSubmit)}
            />
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
