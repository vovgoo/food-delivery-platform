import {
  ChangeEmailForm,
  ChangePasswordForm,
  ChangePhoneForm,
  DeactivateAccountForm,
  UpdateUserProfileForm,
} from '@/features';
import React from 'react';

export const ProfileSettingsPage: React.FC = () => {
  return (
    <div className="flex flex-col gap-6">
      <div className="grid grid-cols-[3fr_7fr] gap-6">
        <div>
          <UpdateUserProfileForm />
        </div>
        <div>
          <ChangePasswordForm />
        </div>
      </div>
      <div className="grid grid-cols-[4fr_6fr] gap-6">
        <div>
          <ChangeEmailForm />
        </div>
        <div>
          <ChangePhoneForm />
        </div>
      </div>
      <div>
        <DeactivateAccountForm />
      </div>
    </div>
  );
};
