import React from 'react';
import { Controller } from 'react-hook-form';
import type { Control } from 'react-hook-form';
import {
  Select,
  SelectTrigger,
  SelectContent,
  SelectItem,
  SelectValue,
} from '@/components/ui/select';

interface SelectInputProps {
  name: string;
  control: Control<any>;
  options: { value: string; label: string }[];
  placeholder?: string;
  error?: string;
}

export const SelectInput: React.FC<SelectInputProps> = ({
  name,
  control,
  options,
  placeholder = 'Выберите...',
  error,
}) => (
  <div className="flex flex-col gap-1 w-full">
    <Controller
      control={control}
      name={name}
      defaultValue=""
      render={({ field }) => (
        <Select value={field.value} onValueChange={field.onChange}>
          <SelectTrigger className="w-full">
            <SelectValue placeholder={placeholder} />
          </SelectTrigger>

          <SelectContent>
            {options.map((opt) => (
              <SelectItem key={opt.value} value={opt.value}>
                {opt.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      )}
    />

    <p className="text-red-500 h-4 text-sm">{error}</p>
  </div>
);
