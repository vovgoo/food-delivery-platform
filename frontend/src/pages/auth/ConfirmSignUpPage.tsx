import { ConfirmSignUpForm } from '@/features';
import { AppRoutes } from '@/routes';
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const ConfirmSignUpPage: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      navigate(AppRoutes.MAIN);
    }
  }, [navigate]);

  return (
    <div className="flex w-full justify-center">
      <ConfirmSignUpForm />
    </div>
  );
};
