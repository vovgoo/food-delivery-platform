import React from "react";
import { Routes, Route, Navigate, useLocation } from "react-router-dom";
import MainPage from "./pages/MainPage";
import RestaurantPage from "./pages/RestaurantPage";
import ChangeUserEmailPage from "./pages/profile/ConfirmChangeEmailPage";
import ChangeUserPhonePage from "./pages/profile/ConfirmChangePhonePage";
import CreateOrderPage from "./pages/CreateOrderPage";

import SignInPage from "./pages/auth/SignInPage";
import SignUpPage from "./pages/auth/SignUpPage";
import ConfirmSignUpPage from "./pages/auth/ConfirmSignUpPage";

import AdminDashboardPage from "./pages/AdminDashboardPage";
import AdminRestaurantsPage from "./pages/AdminRestaurantsPage";
import AdminDishesPage from "./pages/AdminDishesPage";
import AdminOrdersPage from "./pages/AdminOrdersPage";

import { AppRoutes } from "./routes";
import UserDeactivatePage from "./pages/UserDeactivatePage";
import UserBlockedPage from "./pages/UserBlockedPage";
import Header from "./features/common/Header";
import Footer from "./features/common/Footer";
import { Toaster } from "sonner";
import { ProfileLayout } from "./features/profile/ProfileLayout";
import ProfileOrdersPage from "./pages/profile/ProfileOrdersPage";
import ProfileAddressesPage from "./pages/profile/ProfileAddressesPage";
import ProfileSettingsPage from "./pages/profile/ProfileSettingsPage";

const App: React.FC = () => {
  const location = useLocation();

  const noHeaderFooterPages: string[] = [
    AppRoutes.USER_DEACTIVATE,
    AppRoutes.USER_BLOCKED
  ];

  const hideHeaderFooter = noHeaderFooterPages.includes(location.pathname);

  return (
    <div className="bg-[#dfdfdf] w-full flex justify-center">
      <Toaster position="bottom-right"/>
      <div className="min-h-dvh max-w-[1400px] w-full flex flex-col justify-between">
        {!hideHeaderFooter && <Header/>}

        <Routes>
          <Route path={AppRoutes.MAIN} element={<MainPage />} />

          <Route path={AppRoutes.USER_DEACTIVATE} element={<UserDeactivatePage />} />
          <Route path={AppRoutes.USER_BLOCKED} element={<UserBlockedPage />} />
          
          <Route path={AppRoutes.SIGN_IN} element={<SignInPage />} />
          <Route path={AppRoutes.SIGN_UP} element={<SignUpPage />} />
          <Route path={AppRoutes.CONFIRM_SIGN_UP} element={<ConfirmSignUpPage />} />

          <Route path={AppRoutes.PROFILE} element={<ProfileLayout />}>
            <Route path={AppRoutes.PROFILE_ORDERS} element={<ProfileOrdersPage />} />
            <Route path={AppRoutes.PROFILE_ADDRESSES} element={<ProfileAddressesPage />} />
            <Route path={AppRoutes.PROFILE_SETTINGS} element={<ProfileSettingsPage />} />
            <Route index element={<Navigate to={AppRoutes.PROFILE_ORDERS} replace />} />
          </Route>
          <Route path={AppRoutes.CHANGE_EMAIL} element={<ChangeUserEmailPage />} />
          <Route path={AppRoutes.CHANGE_PHONE} element={<ChangeUserPhonePage />} />
          
          <Route path={AppRoutes.RESTAURANT} element={<RestaurantPage />} />
          <Route path={AppRoutes.CREATE_ORDER} element={<CreateOrderPage />} />
          <Route path={AppRoutes.ADMIN_DASHBOARD} element={<AdminDashboardPage />} />
          <Route path={AppRoutes.ADMIN_RESTAURANTS} element={<AdminRestaurantsPage />} />
          <Route path={AppRoutes.ADMIN_DISHES} element={<AdminDishesPage />} />
          <Route path={AppRoutes.ADMIN_ORDERS} element={<AdminOrdersPage />} />
          <Route path="*" element={<Navigate to={AppRoutes.MAIN} />} />
        </Routes>

        {!hideHeaderFooter && <Footer/>}
      </div>
    </div>
  );
};

export default App;
