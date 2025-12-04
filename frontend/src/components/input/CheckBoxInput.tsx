import React from "react";
import { Controller } from "react-hook-form";
import type { Control } from "react-hook-form";
import { Checkbox } from "@/components/ui/checkbox";

interface CheckboxInputProps {
  name: string;
  control: Control<any>;
  label?: string;
  error?: string;
}

export const CheckboxInput: React.FC<CheckboxInputProps> = ({
  name,
  control,
  label,
  error,
}) => (
  <div className="flex flex-col gap-1">
    <Controller
      control={control}
      name={name}
      defaultValue={false}
      render={({ field }) => (
        <label className="flex items-center gap-2 cursor-pointer">
          <Checkbox
            checked={field.value}
            onCheckedChange={(checked) => field.onChange(checked)}
          />
          {label && <span className="text-sm">{label}</span>}
        </label>
      )}
    />
    <p className="text-red-500 h-4 text-sm">{error}</p>
  </div>
);
