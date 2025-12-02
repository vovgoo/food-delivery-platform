import React from "react";
import type { Control } from "react-hook-form";
import { Controller } from "react-hook-form";
import { format } from "date-fns";
import { ru } from "date-fns/locale";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { ChevronDown } from "lucide-react";
import { Button } from "@/components/ui/button";

interface DateInputProps {
  name: string;
  control: Control<any>;
  placeholder: string;
  error?: string;
}

export const DateInput: React.FC<DateInputProps> = ({ name, control, placeholder, error }) => {
  return (
    <div className="flex flex-col gap-1">
      <Controller
        control={control}
        name={name}
        defaultValue=""
        render={({ field }) => {
          const value = field.value ? new Date(field.value) : undefined;
          const displayValue = value ? format(value, "d MMMM yyyy", { locale: ru }) : "";

          return (
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-full justify-between text-left font-normal flex items-center"
                  data-empty={!field.value}
                >
                  <div className="flex items-center">
                    {displayValue || `Выберите ${placeholder.toLowerCase()}`}
                  </div>
                  <ChevronDown className="w-4 h-4 text-gray-500" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0">
                <Calendar
                  mode="single"
                  selected={value}
                  onSelect={(date) => field.onChange(date ? format(date, "yyyy-MM-dd") : "")}
                  captionLayout="dropdown"
                  locale={ru}
                />
              </PopoverContent>
            </Popover>
          );
        }}
      />
      <p className="text-red-500 h-4 text-sm">{error}</p>
    </div>
  );
};