import React from "react";
import { Link } from "react-router-dom";
import { Button as UIButton } from "@/components/ui/button";

interface LinkButtonProps {
  text: string;
  to: string;
}

export const LinkButton: React.FC<LinkButtonProps> = ({ text, to }) => {
  return (
    <Link to={to}>
      <UIButton variant="outline" className="w-full">
        {text}
      </UIButton>
    </Link>
  );
};
