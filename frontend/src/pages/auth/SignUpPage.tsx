import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AppRoutes } from '@/routes';
import { SignUpForm } from '@/features';

export const SignUpPage: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      navigate(AppRoutes.MAIN);
    }
  }, [navigate]);

  return (
    <div className="flex w-full justify-center">
      <SignUpForm />
    </div>
  );
};
