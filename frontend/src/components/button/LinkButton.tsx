import React from 'react';
import { Link } from 'react-router-dom';
import { Button as UIButton } from '@/components/ui/button';

interface LinkButtonProps {
  text: string;
  to: string;
  icon?: React.ReactNode;
}

export const LinkButton: React.FC<LinkButtonProps> = ({ text, to, icon }) => {
  return (
    <Link to={to}>
      <UIButton variant="outline" className="w-full cursor-pointer">
        {icon && <span className="flex items-center">{icon}</span>}
        {text}
      </UIButton>
    </Link>
  );
};
