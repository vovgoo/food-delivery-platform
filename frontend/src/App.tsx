import React from 'react';
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { Toaster } from 'sonner';
import { AppRoutes } from './routes';
import {
  AdminDishPage,
  AdminOrdersPage,
  AdminRestaurantPage,
  AdminRestaurantsPage,
  CartPage,
  ConfirmChangeEmailPage,
  ConfirmChangePhonePage,
  ConfirmSignUpPage,
  MainPage,
  ProfileAddressesPage,
  ProfileOrderDetailsPage,
  ProfileOrdersPage,
  ProfileSettingsPage,
  RestaurantPage,
  SignInPage,
  SignUpPage,
  UserBlockedPage,
  UserDeactivatePage,
} from './pages';
import { DishPage } from './pages/dish';
import { AdminLayout, Header, ProfileLayout } from './features';
import { Footer } from 'react-day-picker';

const App: React.FC = () => {
  const location = useLocation();

  const noHeaderFooterPages: string[] = [AppRoutes.USER_DEACTIVATE, AppRoutes.USER_BLOCKED];

  const hideHeaderFooter = noHeaderFooterPages.includes(location.pathname);

  return (
    <div className="bg-[#dfdfdf] w-full flex justify-center">
      <Toaster position="bottom-right" />
      <div className="min-h-dvh max-w-[1400px] w-full flex flex-col justify-between">
        {!hideHeaderFooter && <Header />}

        <Routes>
          <Route path={AppRoutes.USER_DEACTIVATE} element={<UserDeactivatePage />} />
          <Route path={AppRoutes.USER_BLOCKED} element={<UserBlockedPage />} />

          <Route path={AppRoutes.MAIN} element={<MainPage />} />
          <Route path={AppRoutes.RESTAURANT} element={<RestaurantPage />} />
          <Route path={AppRoutes.DISH} element={<DishPage />} />
          <Route path={AppRoutes.CART} element={<CartPage />} />

          <Route path={AppRoutes.SIGN_IN} element={<SignInPage />} />
          <Route path={AppRoutes.SIGN_UP} element={<SignUpPage />} />
          <Route path={AppRoutes.CONFIRM_SIGN_UP} element={<ConfirmSignUpPage />} />

          <Route path={AppRoutes.PROFILE} element={<ProfileLayout />}>
            <Route path={AppRoutes.PROFILE_ORDERS} element={<ProfileOrdersPage />} />
            <Route path={AppRoutes.PROFILE_ORDER_DETAILS} element={<ProfileOrderDetailsPage />} />
            <Route path={AppRoutes.PROFILE_ADDRESSES} element={<ProfileAddressesPage />} />
            <Route path={AppRoutes.PROFILE_SETTINGS} element={<ProfileSettingsPage />} />
            <Route index element={<Navigate to={AppRoutes.PROFILE_ORDERS} replace />} />
          </Route>
          <Route path={AppRoutes.CHANGE_EMAIL} element={<ConfirmChangeEmailPage />} />
          <Route path={AppRoutes.CHANGE_PHONE} element={<ConfirmChangePhonePage />} />

          <Route path={AppRoutes.ADMIN_DASHBOARD} element={<AdminLayout />}>
            <Route path={AppRoutes.ADMIN_RESTAURANTS} element={<AdminRestaurantsPage />} />
            <Route path={AppRoutes.ADMIN_RESTAURANT} element={<AdminRestaurantPage />} />
            <Route path={AppRoutes.ADMIN_DISH} element={<AdminDishPage />} />
            <Route path={AppRoutes.ADMIN_ORDERS} element={<AdminOrdersPage />} />
            <Route index element={<Navigate to={AppRoutes.ADMIN_RESTAURANTS} replace />} />
          </Route>

          <Route path="*" element={<Navigate to={AppRoutes.MAIN} />} />
        </Routes>

        {!hideHeaderFooter && <Footer />}
      </div>
    </div>
  );
};

export default App;
