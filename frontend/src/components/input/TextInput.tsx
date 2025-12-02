import React from "react";
import { Controller } from "react-hook-form";
import type { Control } from "react-hook-form";
import { Input as UIInput } from "@/components/ui/input";

interface TextInputProps {
  name: string;
  control: Control<any>;
  placeholder: string;
  type?: string;
  error?: string;
}

export const TextInput: React.FC<TextInputProps> = ({
  name,
  control,
  placeholder,
  type = "text",
  error,
}) => (
  <div className="flex flex-col gap-1">
    <Controller
      control={control}
      name={name}
      defaultValue=""
      render={({ field }) => <UIInput {...field} placeholder={placeholder} type={type} />}
    />
    <p className="text-red-500 h-4 text-sm">{error}</p>
  </div>
);
