import React, { useState } from "react";
import type { Control } from "react-hook-form";
import { Controller } from "react-hook-form";
import { Input } from "@/components/ui/input";
import { Eye, EyeOff } from "lucide-react";

interface PasswordInputProps {
  name: string;
  control: Control<any>;
  placeholder?: string;
  error?: string;
}

export const PasswordInput: React.FC<PasswordInputProps> = ({ name, control, placeholder, error }) => {
  const [show, setShow] = useState(false);

  return (
    <div className="flex flex-col gap-1 w-full">
      <Controller
        control={control}
        name={name}
        defaultValue=""
        render={({ field }) => (
          <div className="relative w-full">
            <Input
              {...field}
              type={show ? "text" : "password"}
              placeholder={placeholder}
              className="pr-10"
            />
            <button
              type="button"
              onClick={() => setShow(!show)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500"
            >
              {show ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
            </button>
          </div>
        )}
      />
      <p className="text-red-500 h-4 text-sm">{error}</p>
    </div>
  );
};
