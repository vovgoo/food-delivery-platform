import React from "react";
import { toast } from "sonner";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";

import { authService, type SignInRequest } from "@/api";
import { AppRoutes } from "@/routes";
import { signInSchema, type SignInFormData } from "@/schemas";

import { Form, FormField, FormItem, FormControl } from "@/components/ui/form";
import { PasswordInput } from "@/components/input/PasswordInput";
import { SpinnerButton } from "@/components/button/SpinnerButton";

import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent
} from "@/components/ui/card";
import { LinkButton } from "@/components/button/LinkButton";
import { PhoneInput } from "@/components/input/PhoneInput";

export const SignInForm: React.FC = () => {
  const navigate = useNavigate();

  const form = useForm<SignInFormData>({
    resolver: zodResolver(signInSchema),
    defaultValues: {
      phone: "",
      password: "",
    },
  });

  const mutation = useMutation({
    mutationFn: (payload: SignInRequest) => authService.signIn(payload),
    onSuccess: (data) => {
      localStorage.setItem("accessToken", data.accessToken);
      navigate(AppRoutes.MAIN);
      toast.success("Вы успешно авторизовались!");
    },
    onError: (err: any) => {
      const { statusCode, body } = err.response?.data || {};
      if (statusCode === 400 && body?.errors) {
        body.errors.forEach((fieldError: { field: string; messages: string[] }) => {
          form.setError(fieldError.field as keyof SignInFormData, {
            message: fieldError.messages.join(", "),
          });
        });
      } else {
        toast.error(typeof body === "string" ? body : "Произошла ошибка сервера");
      }
    },
  });

  const onSubmit = (data: SignInFormData) => {
    const payload: SignInRequest = {
      phone: data.phone.replace(/[^\d+]/g, ""),
      password: data.password,
    };
    mutation.mutate(payload);
  };

  return (
    <Card className="w-full max-w-[500px] mx-auto">
      <CardHeader>
        <CardTitle>Вход</CardTitle>
        <CardDescription>Введите ваш телефон и пароль</CardDescription>
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
                      placeholder="Tелефон"
                      error={form.formState.errors.phone?.message}
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <PasswordInput
                      name={field.name}
                      control={form.control}
                      placeholder="Пароль"
                      error={form.formState.errors.password?.message}
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <SpinnerButton
              text="Войти"
              loadingText="Вход..."
              isLoading={mutation.isPending}
              onClick={form.handleSubmit(onSubmit)}
            />

            <LinkButton
              text="Нет аккаунта? Зарегистрироваться"
              to={AppRoutes.SIGN_UP}
            />
          </div>
        </Form>
      </CardContent>
    </Card>
  );
};
