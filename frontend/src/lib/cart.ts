export const CART_STORAGE_KEY = "cartItems";

export interface CartItem {
  id: string;
  name: string;
  price: number;
  image?: string;
  quantity: number;
  selectedSize?: string;
  selectedColor?: string;
}

const readCartItems = (): CartItem[] => {
  const raw = localStorage.getItem(CART_STORAGE_KEY);
  if (!raw) {
    return [];
  }

  try {
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
};

const writeCartItems = (items: CartItem[]) => {
  localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(items));
};

export const getCartItems = () => readCartItems();

export const addCartItem = (item: CartItem) => {
  const currentItems = readCartItems();
  const existingItemIndex = currentItems.findIndex(
    (cartItem) =>
      cartItem.id === item.id &&
      cartItem.selectedSize === item.selectedSize &&
      cartItem.selectedColor === item.selectedColor
  );

  if (existingItemIndex >= 0) {
    currentItems[existingItemIndex] = {
      ...currentItems[existingItemIndex],
      quantity: currentItems[existingItemIndex].quantity + item.quantity,
      image: currentItems[existingItemIndex].image || item.image,
    };
  } else {
    currentItems.push(item);
  }

  writeCartItems(currentItems);
};

export const updateCartItemQuantity = (
  itemIndex: number,
  quantity: number
) => {
  const currentItems = readCartItems();
  if (!currentItems[itemIndex]) {
    return;
  }

  if (quantity <= 0) {
    currentItems.splice(itemIndex, 1);
  } else {
    currentItems[itemIndex] = {
      ...currentItems[itemIndex],
      quantity,
    };
  }

  writeCartItems(currentItems);
};

export const removeCartItem = (itemIndex: number) => {
  const currentItems = readCartItems();
  if (!currentItems[itemIndex]) {
    return;
  }

  currentItems.splice(itemIndex, 1);
  writeCartItems(currentItems);
};
