import React from "react";
import { Controller } from "react-hook-form";
import type { Control } from "react-hook-form";
import { Input as UIInput } from "@/components/ui/input";
import { SearchIcon } from "lucide-react";

interface SearchInputProps {
  name: string;
  control: Control<any>;
  placeholder?: string;
}

export const SearchInput: React.FC<SearchInputProps> = ({
  name,
  control,
  placeholder = "Поиск...",
}) => (
  <div className="flex flex-col gap-1 relative">
    <Controller
      control={control}
      name={name}
      defaultValue=""
      render={({ field }) => (
        <UIInput
          {...field}
          placeholder={placeholder}
          className="pr-10 bg-white"
          autoComplete="off"
        />
      )}
    />
    <SearchIcon className="w-4 h-4 absolute right-2 top-1/2 -translate-y-1/2 text-gray-400" />
  </div>
);
