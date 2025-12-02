import React from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";

import { changeEmailSchema, type ChangeEmailFormData } from "@/schemas";
import { userService, type ChangeEmailRequest } from "@/api";

import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent
} from "@/components/ui/card";

import { Form, FormField, FormItem, FormControl } from "@/components/ui/form";
import { TextInput } from "@/components/input/TextInput";
import { SpinnerButton } from "@/components/button/SpinnerButton";

export const ChangeEmailForm: React.FC = () => {
  const form = useForm<ChangeEmailFormData>({
    resolver: zodResolver(changeEmailSchema),
    defaultValues: { email: "" },
  });

  const mutation = useMutation({
    mutationFn: (payload: ChangeEmailRequest) => userService.changeEmail(payload),
    onSuccess: () => {
      toast.success("Ваш было отправлено письмо на электронную почту, для подтверждения перейдите по ссылке в нем!");
      form.reset();
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach(
          (fieldError: { field: string; messages: string[] }) => {
            form.setError(fieldError.field as keyof ChangeEmailFormData, {
              message: fieldError.messages.join(", "),
            });
          }
        );
      } else {
        toast.error(typeof body === "string" ? body : "Произошла ошибка сервера");
      }
    },
  });

  const onSubmit = (data: ChangeEmailFormData) => {
    mutation.mutate({ email: data.email });
  };

  return (
    <Card className="w-full max-w-[1300px]">
      <CardHeader>
        <CardTitle>Смена почты</CardTitle>
        <CardDescription>Введите новый адрес электронной почты</CardDescription>
      </CardHeader>

      <CardContent>
        <Form {...form}>
          <div className="flex flex-col gap-5">
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <TextInput
                      name={field.name}
                      control={form.control}
                      placeholder="Новый email"
                      error={form.formState.errors.email?.message}
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <SpinnerButton
              text="Сменить почту"
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
