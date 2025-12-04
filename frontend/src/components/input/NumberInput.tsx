import React from 'react';
import { Controller } from 'react-hook-form';
import type { Control } from 'react-hook-form';
import { Input as UIInput } from '@/components/ui/input';

interface NumberInputProps {
  name: string;
  control: Control<any>;
  placeholder?: string;
  error?: string;
}

export const NumberInput: React.FC<NumberInputProps> = ({ name, control, placeholder, error }) => (
  <div className="flex flex-col gap-1">
    <Controller
      control={control}
      name={name}
      defaultValue={undefined}
      render={({ field: { onChange, value, ...restField } }) => (
        <UIInput
          {...restField}
          placeholder={placeholder}
          type="number"
          step="0.01"
          value={value ?? ''}
          onChange={(e) => {
            const val = e.target.value;
            onChange(val === '' ? undefined : parseFloat(val));
          }}
        />
      )}
    />
    <p className="text-red-500 h-4 text-sm">{error}</p>
  </div>
);
