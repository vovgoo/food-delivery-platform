import React, { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { userService, type UserResponse } from '@/api';
import { useQuery } from '@tanstack/react-query';
import { AppRoutes } from '@/routes';
import { ProfileInfoCard } from './ProfileInfoCard';
import { ProfileSidebar } from './ProfileSidebar';

export const ProfileLayout: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) navigate(AppRoutes.MAIN);
  }, [navigate]);

  const { data: user, isPending } = useQuery<UserResponse>({
    queryKey: ['me'],
    queryFn: () => userService.me(),
  });

  return (
    <div className="flex min-h-screen my-10 gap-x-10">
      <ProfileSidebar />
      <main className="flex flex-col flex-1 gap-6">
        <ProfileInfoCard user={user} isPending={isPending} />
        <Outlet />
      </main>
    </div>
  );
};
