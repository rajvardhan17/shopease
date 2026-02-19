import Header from "@/components/Header";
import Hero3D from "@/components/Hero3D";
import CategoryGrid from "@/components/CategoryGrid";
import ProductShowcase from "@/components/ProductShowcase";
import Footer from "@/components/Footer";

// Sample product data
const sampleProducts = {
  tshirts: [
    { id: "1", name: "Premium Cotton Tee", price: 2499, category: "tshirt" },
    { id: "2", name: "Organic Basic Tee", price: 1999, category: "tshirt" },
    { id: "3", name: "Designer Graphic Tee", price: 3499, category: "tshirt" },
    { id: "4", name: "Vintage Style Tee", price: 2799, category: "tshirt" },
    { id: "5", name: "Modern Fit Tee", price: 2299, category: "tshirt" },
    { id: "6", name: "Eco-Friendly Tee", price: 2699, category: "tshirt" },
    { id: "7", name: "Classic White Tee", price: 1899, category: "tshirt" },
    { id: "8", name: "Luxury Cotton Tee", price: 3999, category: "tshirt" },
  ],
  shoes: [
    { id: "9", name: "Air Comfort Sneakers", price: 8999, category: "shoe" },
    { id: "10", name: "Urban Street Shoes", price: 7499, category: "shoe" },
    { id: "11", name: "Premium Runners", price: 12999, category: "shoe" },
    { id: "12", name: "Classic Leather Shoes", price: 15999, category: "shoe" },
  ],
  shirts: [
    { id: "13", name: "Formal White Shirt", price: 4999, category: "shirt" },
    { id: "14", name: "Casual Blue Shirt", price: 3499, category: "shirt" },
    { id: "15", name: "Designer Print Shirt", price: 5999, category: "shirt" },
    { id: "16", name: "Linen Summer Shirt", price: 4499, category: "shirt" },
  ],
  accessories: [
    { id: "17", name: "Leather Belt", price: 2999, category: "accessory" },
    { id: "18", name: "Designer Watch", price: 24999, category: "accessory" },
    { id: "19", name: "Premium Wallet", price: 3999, category: "accessory" },
    { id: "20", name: "Stylish Sunglasses", price: 4999, category: "accessory" },
  ]
};

const Index = () => {
  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main>
        <Hero3D />
        <CategoryGrid />
        <ProductShowcase
          title="Featured T-Shirts"
          products={sampleProducts.tshirts}
          viewAllLink="/men"
        />
        <ProductShowcase
          title="Premium Shoes"
          products={sampleProducts.shoes}
          viewAllLink="/shoes"
        />
        <ProductShowcase
          title="Essential Shirts"
          products={sampleProducts.shirts}
          viewAllLink="/men"
        />
        <ProductShowcase
          title="Luxury Accessories"
          products={sampleProducts.accessories}
          viewAllLink="/accessories"
        />
      </main>
      <Footer />
    </div>
  );
};

export default Index;
