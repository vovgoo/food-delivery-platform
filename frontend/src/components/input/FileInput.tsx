import React from "react";
import { Controller } from "react-hook-form";
import type { Control } from "react-hook-form";
import { Input as UIInput } from "@/components/ui/input";

interface FileInputProps {
  name: string;
  control: Control<any>;
  multiple?: boolean; 
  accept?: string; 
  error?: string;
}

export const FileInput: React.FC<FileInputProps> = ({
  name,
  control,
  multiple = false,
  accept,
  error,
}) => (
  <div className="flex flex-col gap-1">
    <Controller
      control={control}
      name={name}
      defaultValue={undefined}
      render={({ field }) => (
        <UIInput
          type="file"
          multiple={multiple}
          accept={accept}
          ref={field.ref}
          onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
            const files = e.target.files;
            field.onChange(multiple ? files : files?.[0]);
          }}
        />
      )}
    />
    <p className="text-red-500 h-4 text-sm">{error}</p>
  </div>
);

