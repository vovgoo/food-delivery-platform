import React from 'react';
import { Controller } from 'react-hook-form';
import type { Control } from 'react-hook-form';
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSeparator,
  InputOTPSlot,
} from '@/components/ui/input-otp';

interface OtpInputProps {
  name: string;
  control: Control<any>;
  maxLength?: number;
  error?: string;
}

export const OtpInput: React.FC<OtpInputProps> = ({ name, control, maxLength = 6, error }) => (
  <div className="flex flex-col gap-1">
    <Controller
      control={control}
      name={name}
      defaultValue=""
      render={({ field }) => (
        <InputOTP maxLength={maxLength} value={field.value || ''} onChange={field.onChange}>
          <InputOTPGroup>
            {Array.from({ length: 3 }).map((_, i) => (
              <InputOTPSlot className="bg-white" key={i} index={i} />
            ))}
          </InputOTPGroup>
          <InputOTPSeparator />
          <InputOTPGroup>
            {Array.from({ length: 3 }).map((_, i) => (
              <InputOTPSlot className="bg-white" key={i + 3} index={i + 3} />
            ))}
          </InputOTPGroup>
        </InputOTP>
      )}
    />
    <p className="text-red-500 h-4 text-sm">{error}</p>
  </div>
);
