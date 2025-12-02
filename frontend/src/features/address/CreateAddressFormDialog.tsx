import React, { useState } from "react";
import { toast } from "sonner";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";

import { addressService, type CreateAddressRequest } from "@/api";
import { AppRoutes } from "@/routes";
import { createAddressSchema, type CreateAddressFormData } from "@/schemas";

import { Form, FormField, FormItem, FormControl } from "@/components/ui/form";
import { Textarea } from "@/components/ui/textarea";
import { TextInput } from "@/components/input/TextInput";
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
  AlertDialogAction
} from "@/components/ui/alert-dialog";

import { Button } from "@/components/ui/button";
import { PlusIcon } from "lucide-react";

export const CreateAddressFormDialog: React.FC = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [open, setOpen] = useState(false); 

  const form = useForm<CreateAddressFormData>({
    resolver: zodResolver(createAddressSchema),
    defaultValues: {
      country: "",
      state: "",
      city: "",
      street: "",
      house: "",
      building: "",
      apartment: "",
      deliveryInstructions: "",
      zip: "",
    },
  });

  const mutation = useMutation({
    mutationFn: (payload: CreateAddressRequest) => addressService.create(payload),
    onSuccess: () => {
      toast.success("Адрес успешно создан!");
      queryClient.invalidateQueries({ queryKey: ["me"] });
      queryClient.invalidateQueries({ queryKey: ["addresses"] });
      form.reset();
      setOpen(false);
      navigate(AppRoutes.PROFILE_ADDRESSES);
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach(
          (fieldError: { field: string; messages: string[] }) => {
            form.setError(fieldError.field as keyof CreateAddressFormData, {
              message: fieldError.messages.join(", "),
            });
          }
        );
      } else {
        toast.error(typeof body === "string" ? body : "Произошла ошибка сервера");
      }
    },
  });

  const onSubmit = (data: CreateAddressFormData) => {
    const payload: CreateAddressRequest = {
      country: data.country,
      state: data.state,
      city: data.city,
      street: data.street,
      house: data.house,
      building: data.building || undefined,
      apartment: data.apartment || undefined,
      deliveryInstructions: data.deliveryInstructions || undefined,
      zip: data.zip,
    };
    mutation.mutate(payload);
  };

  return (
    <AlertDialog open={open} onOpenChange={setOpen}>
      <AlertDialogTrigger asChild>
        <Button variant="default" className="flex items-center gap-2">
          <PlusIcon className="w-4 h-4" />
          Добавить адрес
        </Button>
      </AlertDialogTrigger>

      <AlertDialogContent className="sm:max-w-none w-[900px]">
        <AlertDialogHeader>
          <AlertDialogTitle>Добавление нового адреса</AlertDialogTitle>
          <AlertDialogDescription>
            Заполните форму ниже, чтобы добавить новый адрес доставки.
          </AlertDialogDescription>
        </AlertDialogHeader>

        <Form {...form}>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {[
              { name: "country", placeholder: "Страна" },
              { name: "state", placeholder: "Штат/Регион" },
              { name: "city", placeholder: "Город" },
              { name: "street", placeholder: "Улица" },
              { name: "house", placeholder: "Дом" },
              { name: "building", placeholder: "Корпус (необязательно)" },
              { name: "apartment", placeholder: "Квартира/офис (необязательно)" },
              { name: "zip", placeholder: "ZIP код" },
            ].map((field) => (
              <FormField
                key={field.name}
                control={form.control}
                name={field.name as keyof CreateAddressFormData}
                render={({ field: f }) => (
                  <FormItem>
                    <FormControl>
                      <TextInput
                        name={f.name}
                        control={form.control}
                        placeholder={field.placeholder}
                        error={form.formState.errors[f.name]?.message}
                      />
                    </FormControl>
                  </FormItem>
                )}
              />
            ))}
          </div>

          <FormField
            control={form.control}
            name="deliveryInstructions"
            render={({ field }) => (
              <FormItem className="col-span-full">
                <FormControl>
                  <Textarea
                    {...field}
                    placeholder="Инструкция для курьера (необязательно)"
                    className="resize-none"
                    rows={3}
                  />
                </FormControl>
              </FormItem>
            )}
          />
        </Form>

        <AlertDialogFooter className="mt-4 flex justify-end gap-2">
          <AlertDialogCancel>Отмена</AlertDialogCancel>
          <AlertDialogAction asChild>
            <SpinnerButton 
              text="Добавить адрес" 
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
