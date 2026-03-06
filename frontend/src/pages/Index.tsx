import { useEffect, useState } from "react";
import Header from "@/components/Header";
import Hero3D from "@/components/Hero3D";
import CategoryGrid from "@/components/CategoryGrid";
import ProductShowcase from "@/components/ProductShowcase";
import Footer from "@/components/Footer";

const Index = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  const BACKEND_URL = "https://shopease-production-acc0.up.railway.app";

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await fetch(`${BACKEND_URL}/api/products/random`);
        const data = await res.json();

        console.log("API Response:", data);

        if (data?.products) {
          setProducts(data.products);
        } else if (Array.isArray(data)) {
          setProducts(data);
        }
      } catch (err) {
        console.error("Error fetching products:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Normalize category names (handles T-Shirts, Accessories, etc.)
  const normalize = (str) =>
    str?.toLowerCase().replace(/[\s-]/g, "");

  const tshirts = products.filter(
    (p) => normalize(p.category) === "t-shirt"
  );

  const shoes = products.filter(
    (p) => normalize(p.category) === "shoes"
  );

  const shirts = products.filter(
    (p) => normalize(p.category) === "shirts"
  );

  const accessories = products.filter(
    (p) => normalize(p.category) === "accessories"
  );

  console.log("Products:", products);

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main>
        <Hero3D />
        <CategoryGrid />

        {loading ? (
          <div className="text-center py-10">
            Loading products...
          </div>
        ) : (
          <>
            <ProductShowcase
              title="Featured T-Shirts"
              products={tshirts}
              viewAllLink="/men"
            />

            <ProductShowcase
              title="Premium Shoes"
              products={shoes}
              viewAllLink="/shoes"
            />

            <ProductShowcase
              title="Essential Shirts"
              products={shirts}
              viewAllLink="/men"
            />

            <ProductShowcase
              title="Luxury Accessories"
              products={accessories}
              viewAllLink="/accessories"
            />
          </>
        )}
      </main>
      <Footer />
    </div>
  );
};

export default Index;