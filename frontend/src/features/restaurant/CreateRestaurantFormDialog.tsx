import React, { useState } from "react";
import { toast } from "sonner";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";

import { type RestaurantCreateRequest } from "@/api";
import { AppRoutes } from "@/routes";

import { Form, FormField, FormItem, FormControl } from "@/components/ui/form";
import { TextInput } from "@/components/input/TextInput";
import { TextareaInput } from "@/components/input/TextareaInput";
import { PhoneInput } from "@/components/input/PhoneInput";
import { SpinnerButton } from "@/components/button/SpinnerButton";

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
import { restaurantCreateSchema, type RestaurantCreateFormData } from "@/schemas/restaurant/create-restaurant.schema";
import { restaurantService } from "@/api/services/restaurant/restaurant.service";
import { CheckboxInput } from "@/components/input/CheckBoxInput";
import { TimeInput } from "@/components/input/TimeInput";

export const CreateRestaurantFormDialog: React.FC = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [open, setOpen] = useState(false);

  const form = useForm<RestaurantCreateFormData>({
    resolver: zodResolver(restaurantCreateSchema),
    defaultValues: {
      name: "",
      description: "",
      cuisine: "",
      address: "",
      website: "",
      phone: "",
      openingTime: "",
      closingTime: "",
      deliveryAvailable: false,
      parkingAvailable: false,
    },
  });

  const mutation = useMutation({
    mutationFn: (payload: RestaurantCreateRequest) => restaurantService.create(payload),
    onSuccess: () => {
      toast.success("Ресторан успешно создан!");
      queryClient.invalidateQueries({ queryKey: ["admin-restaurants"] });
      form.reset();
      setOpen(false);
      navigate(AppRoutes.ADMIN_RESTAURANTS);
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
          form.setError(fieldError.field as keyof RestaurantCreateFormData, {
            message: fieldError.messages.join(", "),
          });
        });
      } else {
        toast.error(typeof body === "string" ? body : "Произошла ошибка сервера");
      }
    },
  });

  const onSubmit = (data: RestaurantCreateFormData) => {
    const payload: RestaurantCreateRequest = {
      name: data.name,
      description: data.description || undefined,
      cuisine: data.cuisine,
      address: data.address,
      website: data.website || undefined,
      phone: data.phone,
      openingTime: data.openingTime,
      closingTime: data.closingTime,
      deliveryAvailable: data.deliveryAvailable,
      parkingAvailable: data.parkingAvailable,
    };
    mutation.mutate(payload);
  };

  return (
    <AlertDialog open={open} onOpenChange={setOpen}>
      <AlertDialogTrigger asChild>
        <Button variant="default" className="flex items-center gap-2">
          <PlusIcon className="w-4 h-4" />
          Добавить ресторан
        </Button>
      </AlertDialogTrigger>

      <AlertDialogContent className="sm:max-w-none w-[900px]">
        <AlertDialogHeader>
          <AlertDialogTitle>Добавление нового ресторана</AlertDialogTitle>
          <AlertDialogDescription>
            Заполните форму ниже, чтобы добавить новый ресторан.
          </AlertDialogDescription>
        </AlertDialogHeader>

        <Form {...form}>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <TextInput name={field.name} control={form.control} placeholder="Название ресторана" error={form.formState.errors.name?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="cuisine"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <TextInput name={field.name} control={form.control} placeholder="Кухня" error={form.formState.errors.cuisine?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="address"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <TextInput name={field.name} control={form.control} placeholder="Адрес" error={form.formState.errors.address?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="website"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <TextInput name={field.name} control={form.control} placeholder="Сайт (необязательно)" error={form.formState.errors.website?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="phone"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <PhoneInput name={field.name} control={form.control} error={form.formState.errors.phone?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem className="col-span-full">
                  <FormControl>
                    <TextareaInput name={field.name} control={form.control} placeholder="Описание ресторана" rows={4} error={form.formState.errors.description?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="openingTime"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <TimeInput name={field.name} control={form.control} placeholder="Время открытия" error={form.formState.errors.openingTime?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="closingTime"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <TimeInput name={field.name} control={form.control} placeholder="Время закрытия" error={form.formState.errors.closingTime?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="deliveryAvailable"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <CheckboxInput name={field.name} control={form.control} label="Доступна доставка" error={form.formState.errors.deliveryAvailable?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="parkingAvailable"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <CheckboxInput name={field.name} control={form.control} label="Есть парковка" error={form.formState.errors.parkingAvailable?.message} />
                  </FormControl>
                </FormItem>
              )}
            />
          </div>
        </Form>

        <AlertDialogFooter className="mt-4 flex justify-end gap-2">
          <AlertDialogCancel>Отмена</AlertDialogCancel>
          <AlertDialogAction asChild>
            <SpinnerButton text="Добавить ресторан" loadingText="Сохраняем..." isLoading={mutation.isPending} onClick={form.handleSubmit(onSubmit)} />
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
