export const AppRoutes = {
  MAIN: '/',

  SIGN_IN: '/auth/signIn',
  SIGN_UP: '/auth/signUp',
  CONFIRM_SIGN_UP: '/auth/confirm/signUp',

  PROFILE: '/profile',
  PROFILE_ORDERS: '/profile/orders',
  PROFILE_ADDRESSES: '/profile/addresses',
  PROFILE_SETTINGS: '/profile/settings',
  CHANGE_EMAIL: '/profile/confirm/change-email',
  CHANGE_PHONE: '/profile/confirm/change-phone',


  RESTAURANT: '/restaurant/:id',
  CREATE_ORDER: '/create-order',
  USER_DEACTIVATE: '/error/diactivate',
  USER_BLOCKED: '/error/blocked',

  ADMIN_DASHBOARD: '/admin',
  ADMIN_RESTAURANTS: '/admin/restaurants',
  ADMIN_DISHES: '/admin/dishes',
  ADMIN_ORDERS: '/admin/orders',
} as const;