import React from "react";
import { Controller } from "react-hook-form";
import type { Control } from "react-hook-form";
import { Input } from "@/components/ui/input";

interface TimeInputProps {
  name: string;
  control: Control<any>;
  placeholder?: string;
  error?: string;
}

export const TimeInput: React.FC<TimeInputProps> = ({
  name,
  control,
  placeholder = "HH:mm",
  error,
}) => (
  <div className="flex flex-col gap-1">
    <Controller
      control={control}
      name={name}
      defaultValue=""
      render={({ field }) => (
        <Input
          {...field}
          type="time"
          step={60}
          placeholder={placeholder}
          className="bg-background"
        />
      )}
    />
    <p className="text-red-500 h-4 text-sm">{error}</p>
  </div>
);
