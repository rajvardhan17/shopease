import axios from "axios";

// ===== Base Axios setup =====
const api = axios.create({
  baseURL: "http://localhost:8081/shop-ease-api", // Update to your backend URL
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // Required if your backend uses HttpSession cookies
});

// ===== Types =====
export interface LoginData {
  email: string;
  password: string;
}

export interface RegisterData {
  fullName: string;
  email: string;
  password: string;
  phone?: string;
}

export interface Product {
  id: number;
  title: string;
  shortDescription: string;
  description: string;
  category: string;
  metadata?: any;
}

export interface ProductVariant {
  id?: number;
  productId: number;
  size?: string;
  color?: string;
  price: number;
  stock?: number;
}

export interface WishListItem {
  id: number;
  productId: number;
  userId: string;
}

export interface Order {
  id: number;
  userId: string;
  products: ProductVariant[];
  totalAmount: number;
  status: string;
  createdAt: string;
}

// ===== Auth APIs =====
export const registerUser = async (data: RegisterData) => {
  try {
    const res = await api.post("/api/user/signup", data);
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Registration failed" };
  }
};

export const login = async (data: LoginData) => {
  try {
    const res = await api.post("/api/login", data);
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Login failed" };
  }
};

export const logout = async () => {
  try {
    const res = await api.post("/api/logout");
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Logout failed" };
  }
};

export const getSession = async () => {
  try {
    const res = await api.get("/api/session");
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Unable to get session" };
  }
};

// ===== Product APIs =====
export const getAllProducts = async () => {
  try {
    const res = await api.get("/api/products");
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Failed to fetch products" };
  }
};

export const getProductById = async (id: number) => {
  try {
    const res = await api.get(`/api/products/${id}`);
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Failed to fetch product" };
  }
};

// ===== Wishlist APIs =====
export const getWishlist = async () => {
  try {
    const res = await api.get("/api/wishlist");
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Failed to fetch wishlist" };
  }
};

export const addToWishlist = async (productId: number) => {
  try {
    const res = await api.post("/api/wishlist", { productId });
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Failed to add to wishlist" };
  }
};

export const removeFromWishlist = async (itemId: number) => {
  try {
    const res = await api.delete(`/api/wishlist/${itemId}`);
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Failed to remove from wishlist" };
  }
};

// ===== Search API =====
export const searchProducts = async (query: string) => {
  try {
    const res = await api.get(`/api/search?q=${encodeURIComponent(query)}`);
    return res.data;
  } catch (err: any) {
    throw err.response?.data || { message: "Search failed" };
  }
};
