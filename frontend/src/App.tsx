import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import MainPage from "./pages/MainPage";
import RestaurantPage from "./pages/RestaurantPage";
import ProfilePage from "./pages/ProfilePage";
import ChangeUserEmailPage from "./pages/ChangeUserEmailPage";
import ChangeUserPhonePage from "./pages/ChangeUserPhonePage";
import CreateOrderPage from "./pages/CreateOrderPage";

import SignInPage from "./pages/SignInPage";
import SignUpPage from "./pages/SignUpPage";
import ConfirmSignUpPage from "./pages/ConfirmSignUpPage";

import AdminDashboardPage from "./pages/AdminDashboardPage";
import AdminRestaurantsPage from "./pages/AdminRestaurantsPage";
import AdminDishesPage from "./pages/AdminDishesPage";
import AdminOrdersPage from "./pages/AdminOrdersPage";

import { AppRoutes } from "./routes";


const App: React.FC = () => {
  return (
    <Routes>
      <Route path={AppRoutes.MAIN} element={<MainPage />} />
      <Route path={AppRoutes.RESTAURANT} element={<RestaurantPage />} />
      <Route path={AppRoutes.PROFILE} element={<ProfilePage />} />
      <Route path={AppRoutes.CHANGE_EMAIL} element={<ChangeUserEmailPage />} />
      <Route path={AppRoutes.CHANGE_PHONE} element={<ChangeUserPhonePage />} />
      <Route path={AppRoutes.CREATE_ORDER} element={<CreateOrderPage />} />
      <Route path={AppRoutes.ADMIN_DASHBOARD} element={<AdminDashboardPage />} />
      <Route path={AppRoutes.ADMIN_RESTAURANTS} element={<AdminRestaurantsPage />} />
      <Route path={AppRoutes.ADMIN_DISHES} element={<AdminDishesPage />} />
      <Route path={AppRoutes.ADMIN_ORDERS} element={<AdminOrdersPage />} />
      <Route path={AppRoutes.SIGN_IN} element={<SignInPage />} />
      <Route path={AppRoutes.SIGN_UP} element={<SignUpPage />} />
      <Route path={AppRoutes.CONFIRM_SIGN_UP} element={<ConfirmSignUpPage />} />
      <Route path="*" element={<Navigate to={AppRoutes.MAIN} />} />
    </Routes>
  );
};

export default App;
