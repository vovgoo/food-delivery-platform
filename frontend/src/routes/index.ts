export const AppRoutes = {
  MAIN: '/',
  RESTAURANT: '/restaurant/:id',
  PROFILE: '/profile',
  CHANGE_EMAIL: '/profile/change-email',
  CHANGE_PHONE: '/profile/change-phone',
  CREATE_ORDER: '/create-order',

  SIGN_IN: '/signin',
  SIGN_UP: '/signup',
  CONFIRM_SIGN_UP: '/confirm-signup',

  ADMIN_DASHBOARD: '/admin',
  ADMIN_RESTAURANTS: '/admin/restaurants',
  ADMIN_DISHES: '/admin/dishes',
  ADMIN_ORDERS: '/admin/orders',
} as const;