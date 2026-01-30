import { useState, useEffect } from "react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { motion } from "framer-motion";
import { getAllProducts, Product as APIProduct } from "@/api";
import ProductCard3D from "@/components/ProductCard3D";

interface Product extends APIProduct {
  categoryId: string;
  price: number;
  description: string;
  shortDescription: string;
  variants?: Variant[];
}

interface Variant {
  variantId: string;
  imageUrl: string;
  additionalPrice: number;
}

const ACCESSORY_CATEGORY_ID = "691eee40-cdf1-11f0-9253-00ff80bd5d30"; // Example UUID from your DB

const Accessories = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [filter, setFilter] = useState("all");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAccessories = async () => {
      try {
        setLoading(true);
        const data = await getAllProducts();

        const productList: Product[] = Array.isArray(data?.products)
          ? data.products
          : [];

        // Filter by category
        const onlyAccessories = productList.filter(
          (p) => p.categoryId === ACCESSORY_CATEGORY_ID
        );

        // Map variants if available
        const productsWithVariants = await Promise.all(
          onlyAccessories.map(async (product) => {
            const res = await fetch(
              `http://localhost:8081/shop-ease-api/api/products/${product.id}/variants`
            );
            const json = await res.json();
            return { ...product, variants: json.variants || [] };
          })
        );

        setProducts(productsWithVariants);
      } catch (err) {
        console.error("❌ Failed to fetch accessories:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchAccessories();
  }, []);

  const filteredProducts =
    filter === "all"
      ? products
      : products.filter((product) =>
          filter === "jewelry"
            ? product.title.toLowerCase().includes("necklace") ||
              product.title.toLowerCase().includes("earrings")
            : filter === "bags"
            ? product.title.toLowerCase().includes("bag") ||
              product.title.toLowerCase().includes("wallet")
            : filter === "watches"
            ? product.title.toLowerCase().includes("watch")
            : product.title.toLowerCase().includes(filter)
        );

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main className="pt-20">
        {/* Hero Section */}
        <section className="relative py-20 bg-gradient-to-br from-amber-100 to-orange-100 dark:from-amber-900/20 dark:to-orange-900/20">
          <div className="container mx-auto px-6 text-center">
            <motion.h1
              className="text-5xl font-bold mb-6 text-foreground"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
            >
              Accessories Collection
            </motion.h1>
            <motion.p
              className="text-xl text-muted-foreground max-w-2xl mx-auto"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.2 }}
            >
              Complete your look with premium luxury accessories crafted to elevate every outfit.
            </motion.p>
          </div>
        </section>

        {/* Filter Buttons */}
        <section className="py-8 bg-card">
          <div className="container mx-auto px-6">
            <div className="flex justify-center flex-wrap gap-4">
              {[
                { key: "all", label: "All Items" },
                { key: "watches", label: "Watches" },
                { key: "bags", label: "Bags & Wallets" },
                { key: "jewelry", label: "Jewelry" },
                { key: "belt", label: "Belts" },
                { key: "sunglasses", label: "Sunglasses" },
              ].map((filterOption) => (
                <motion.button
                  key={filterOption.key}
                  onClick={() => setFilter(filterOption.key)}
                  className={`px-6 py-3 rounded-lg transition-all ${
                    filter === filterOption.key
                      ? "bg-primary text-primary-foreground"
                      : "bg-secondary text-secondary-foreground hover:bg-secondary/80"
                  }`}
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                >
                  {filterOption.label}
                </motion.button>
              ))}
            </div>
          </div>
        </section>

        {/* Product Grid */}
        <section className="py-16">
          <div className="container mx-auto px-6">
            {loading ? (
              <p className="text-center text-xl text-muted-foreground">
                Loading accessories...
              </p>
            ) : products.length === 0 ? (
              <p className="text-center text-xl text-muted-foreground">
                No accessories found in the database.
              </p>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
                {filteredProducts.map((product, index) => (
                  <motion.div
                    key={product.id}
                    initial={{ opacity: 0, y: 30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: index * 0.1 }}
                  >
                    <ProductCard3D product={product} />
                  </motion.div>
                ))}
              </div>
            )}
          </div>
        </section>
      </main>
      <Footer />
    </div>
  );
};

export default Accessories;
