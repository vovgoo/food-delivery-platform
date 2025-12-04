import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

export interface CartDish {
  id: string;
  quantity: number;
}

export interface RestaurantCart {
  restaurantId: string;
  items: CartDish[];
}

export interface CartState {
  carts: Record<string, RestaurantCart>;
}

const initialState: CartState = JSON.parse(localStorage.getItem("cart") || "{}")?.carts
  ? { carts: JSON.parse(localStorage.getItem("cart")!).carts }
  : { carts: {} };

const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    addItem: (state, action: PayloadAction<{ restaurantId: string; dishId: string; quantity?: number }>) => {
      const { restaurantId, dishId, quantity = 1 } = action.payload;
      if (!state.carts[restaurantId]) state.carts[restaurantId] = { restaurantId, items: [] };
      const existing = state.carts[restaurantId].items.find(i => i.id === dishId);
      if (existing) existing.quantity += quantity;
      else state.carts[restaurantId].items.push({ id: dishId, quantity });
      localStorage.setItem("cart", JSON.stringify(state));
    },
    removeItem: (state, action: PayloadAction<{ restaurantId: string; dishId: string }>) => {
      const { restaurantId, dishId } = action.payload;
      if (!state.carts[restaurantId]) return;
      state.carts[restaurantId].items = state.carts[restaurantId].items.filter(i => i.id !== dishId);
      localStorage.setItem("cart", JSON.stringify(state));
    },
    updateQuantity: (state, action: PayloadAction<{ restaurantId: string; dishId: string; quantity: number }>) => {
      const { restaurantId, dishId, quantity } = action.payload;
      const cart = state.carts[restaurantId];
      if (!cart) return;
      const item = cart.items.find(i => i.id === dishId);
      if (item) item.quantity = quantity;
      localStorage.setItem("cart", JSON.stringify(state));
    },
    clearCart: (state, action: PayloadAction<{ restaurantId: string }>) => {
      const { restaurantId } = action.payload;
      state.carts[restaurantId] = { restaurantId, items: [] };
      localStorage.setItem("cart", JSON.stringify(state));
    },
  }
});

export const { addItem, removeItem, updateQuantity, clearCart } = cartSlice.actions;
export default cartSlice.reducer;
