import React from 'react';
import { Controller } from 'react-hook-form';
import type { Control } from 'react-hook-form';
import { Textarea as UITextarea } from '@/components/ui/textarea';

interface TextareaInputProps {
  name: string;
  control: Control<any>;
  placeholder?: string;
  rows?: number;
  error?: string;
}

export const TextareaInput: React.FC<TextareaInputProps> = ({
  name,
  control,
  placeholder = '',
  rows = 3,
  error,
}) => (
  <div className="flex flex-col gap-1 w-full">
    <Controller
      control={control}
      name={name}
      defaultValue=""
      render={({ field }) => (
        <UITextarea
          {...field}
          placeholder={placeholder}
          rows={rows}
          className="w-full max-w-full resize-none overflow-y-auto overflow-x-hidden whitespace-pre-wrap"
        />
      )}
    />
    <p className="text-red-500 h-4 text-sm">{error}</p>
  </div>
);
