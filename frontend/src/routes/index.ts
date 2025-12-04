export const AppRoutes = {
  MAIN: '/',
  RESTAURANT: '/restaurant/:restaurantId',
  DISH: '/restaurant/:restaurantId/dish/:dishId',
  CART: '/cart',

  SIGN_IN: '/auth/signIn',
  SIGN_UP: '/auth/signUp',
  CONFIRM_SIGN_UP: '/auth/confirm/signUp',

  PROFILE: '/profile',
  PROFILE_ORDERS: '/profile/orders',
  PROFILE_ORDER_DETAILS: '/profile/orders/:orderId',
  PROFILE_ADDRESSES: '/profile/addresses',
  PROFILE_SETTINGS: '/profile/settings',
  CHANGE_EMAIL: '/profile/confirm/change-email',
  CHANGE_PHONE: '/profile/confirm/change-phone',

  USER_DEACTIVATE: '/error/diactivate',
  USER_BLOCKED: '/error/blocked',

  ADMIN_DASHBOARD: '/admin',
  ADMIN_RESTAURANTS: '/admin/restaurants',
  ADMIN_RESTAURANT: '/admin/restaurants/:restaurantId',
  ADMIN_DISH: '/admin/restaurants/:restaurantId/dishes/:dishId',
  ADMIN_ORDERS: '/admin/orders',
} as const;
