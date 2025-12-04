import React from 'react';
import { Button } from '@/components/ui/button';
import { Spinner } from '@/components/ui/spinner';

interface SpinnerButtonProps {
  text: string;
  loadingText?: string;
  isLoading?: boolean;
  disabled?: boolean;
  onClick?: () => void;
}

export const SpinnerButton: React.FC<SpinnerButtonProps> = ({
  text,
  loadingText,
  isLoading = false,
  disabled = false,
  onClick,
}) => {
  return (
    <Button
      disabled={disabled || isLoading}
      onClick={onClick}
      className="flex items-center justify-center gap-2"
    >
      {isLoading && <Spinner className="w-4 h-4" />}
      {isLoading ? loadingText || text : text}
    </Button>
  );
};
