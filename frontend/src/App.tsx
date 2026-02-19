import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";

// Pages
import Index from "./pages/Index";
import NotFound from "./pages/NotFound";
import MenClothing from "./pages/MenClothing";
import WomenClothing from "./pages/WomenClothing";
import KidsClothing from "./pages/KidsClothing";
import Accessories from "./pages/Accessories";
import Orders from "./pages/Orders";
import Wishlist from "./pages/Wishlist";
import Payments from "./pages/Payments";
import Shoes from "./pages/Shoes";
import ProductDetail from "./pages/ProductDetail";
import Cart from "./pages/Cart";
import Login from "./pages/Login";
import Register from "./pages/Register";

// Admin Pages
import AdminLayout from "./components/admin/AdminLayout";
import AdminDashboard from "./pages/admin/AdminDashboard";
import AdminProducts from "./pages/admin/AdminProducts";
import AdminOrders from "./pages/admin/AdminOrders";
import AdminCustomers from "./pages/admin/AdminCustomers";
import AdminAnalytics from "./pages/admin/AdminAnalytics";
import AdminSettings from "./pages/admin/AdminSettings";

// Profile & Address management
import Profile from "./pages/Profile";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Index />} />
          <Route path="/register" element={<Register />} />

          {/* User Routes */}
          <Route path="/home" element={<Index />} />
          <Route path="/men" element={<MenClothing />} />
          <Route path="/tshirts" element={<MenClothing />} />
          <Route path="/shirts" element={<MenClothing />} />
          <Route path="/women" element={<WomenClothing />} />
          <Route path="/kids" element={<KidsClothing />} />
          <Route path="/accessories" element={<Accessories />} />
          <Route path="/shoes" element={<Shoes />} />
          <Route path="/orders" element={<Orders />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/wishlist" element={<Wishlist />} />
          <Route path="/payments" element={<Payments />} />
          <Route path="/user" element={<Profile />} />
          <Route path="/product/:id" element={<ProductDetail />} />

          {/* Admin Routes */}
          <Route path="/admin" element={<AdminLayout />}>
            <Route index element={<AdminDashboard />} />
            <Route path="products" element={<AdminProducts />} />
            <Route path="orders" element={<AdminOrders />} />
            <Route path="customers" element={<AdminCustomers />} />
            <Route path="analytics" element={<AdminAnalytics />} />
            <Route path="settings" element={<AdminSettings />} />
          </Route>

          {/* Catch-all 404 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
