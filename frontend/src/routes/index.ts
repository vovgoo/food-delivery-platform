export const AppRoutes = {
  MAIN: '/',
  RESTAURANT: '/restaurant/:id',
  CREATE_ORDER: '/create-order',

  PROFILE: '/profile',
  USER_DEACTIVATE: '/error/diactivate',
  USER_BLOCKED: '/error/blocked',
  CHANGE_EMAIL: '/profile/change-email',
  CHANGE_PHONE: '/profile/change-phone',

  SIGN_IN: '/auth/signIn',
  SIGN_UP: '/auth/signUp',
  CONFIRM_SIGN_UP: '/auth/confirm/signUp',

  ADMIN_DASHBOARD: '/admin',
  ADMIN_RESTAURANTS: '/admin/restaurants',
  ADMIN_DISHES: '/admin/dishes',
  ADMIN_ORDERS: '/admin/orders',
} as const;