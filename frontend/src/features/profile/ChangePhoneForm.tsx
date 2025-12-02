import React from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";

import { changePhoneSchema, type ChangePhoneFormData } from "@/schemas";
import { userService, type ChangePhoneRequest } from "@/api";

import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent
} from "@/components/ui/card";

import { Form, FormField, FormItem, FormControl } from "@/components/ui/form";
import { SpinnerButton } from "@/components/button/SpinnerButton";
import { useNavigate } from "react-router-dom";
import { AppRoutes } from "@/routes";
import { PhoneInput } from "@/components/input/PhoneInput";

export const ChangePhoneForm: React.FC = () => {
  const navigate = useNavigate();
  const form = useForm<ChangePhoneFormData>({
    resolver: zodResolver(changePhoneSchema),
    defaultValues: { phone: "" },
  });

  const mutation = useMutation({
    mutationFn: (payload: ChangePhoneRequest) => userService.changePhone(payload),
    onSuccess: () => {
      toast.success("На ваш телефон был отправлен код. Подтвердите его что бы сменить номер!");
      navigate(AppRoutes.CHANGE_PHONE)
      form.reset();
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach(
          (fieldError: { field: string; messages: string[] }) => {
            form.setError(fieldError.field as keyof ChangePhoneFormData, {
              message: fieldError.messages.join(", "),
            });
          }
        );
      } else {
        toast.error(typeof body === "string" ? body : "Произошла ошибка сервера");
      }
    },
  });

  const onSubmit = (data: ChangePhoneFormData) => {
    mutation.mutate({ phone: data.phone });
  };

  return (
    <Card className="w-full max-w-[1300px]">
      <CardHeader>
        <CardTitle>Смена телефона</CardTitle>
        <CardDescription>Введите новый номер телефона</CardDescription>
      </CardHeader>

      <CardContent>
        <Form {...form}>
          <div className="flex flex-col gap-5">
            <FormField
              control={form.control}
              name="phone"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <PhoneInput
                      name={field.name}
                      control={form.control}
                      placeholder="Новый телефон"
                      error={form.formState.errors.phone?.message}
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <SpinnerButton
              text="Сменить телефон"
              loadingText="Сохраняем..."
              isLoading={mutation.isPending}
              onClick={form.handleSubmit(onSubmit)}
            />
          </div>
        </Form>
      </CardContent>
    </Card>
  );
};
