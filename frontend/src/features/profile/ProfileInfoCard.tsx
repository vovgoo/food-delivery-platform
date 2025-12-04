import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Skeleton } from '@/components/ui/skeleton';
import { UserIcon, MailIcon, PhoneIcon, CalendarIcon, ClockIcon } from 'lucide-react';
import { type UserResponse } from '@/api';

interface ProfileInfoCardProps {
  user?: UserResponse;
  isPending: boolean;
}

export const ProfileInfoCard: React.FC<ProfileInfoCardProps> = ({ user, isPending }) => {
  const formatDate = (dateString?: string) => {
    if (!dateString) return undefined;

    return new Date(dateString).toLocaleDateString('ru-RU', {
      day: 'numeric',
      month: 'long',
      year: 'numeric',
    });
  };
  const Field = ({ icon: Icon, label, value }: { icon: any; label: string; value?: string }) => (
    <div className="flex flex-col text-sm sm:flex-row sm:items-center sm:justify-between gap-1 sm:gap-3 p-2 rounded-lg ">
      <div className="flex items-center gap-2 text-gray-600 whitespace-nowrap">
        <Icon className="w-4 h-4" />
        <span className="font-medium">{label}</span>
      </div>

      {isPending ? (
        <Skeleton className="h-4 w-40 bg-gray-400 rounded-xs" />
      ) : (
        <span className="text-black break-all">{value || '-'}</span>
      )}
    </div>
  );

  return (
    <Card className="w-full shadow-sm border border-gray-200">
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-lg">
          <UserIcon className="w-5 h-5 text-gray-700" />
          Информация профиля
        </CardTitle>
      </CardHeader>

      <CardContent className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <Field icon={UserIcon} label="Полное имя" value={user?.fullName} />
        <Field icon={MailIcon} label="Email" value={user?.email} />

        <Field icon={PhoneIcon} label="Телефон" value={user?.phone} />
        <Field icon={CalendarIcon} label="Дата рождения" value={formatDate(user?.birthDate)} />

        <Field
          icon={ClockIcon}
          label="Дата регистрации"
          value={user ? formatDate(user.createdAt) : undefined}
        />
      </CardContent>
    </Card>
  );
};
