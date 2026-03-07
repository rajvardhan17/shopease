import { useEffect, useState } from "react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import ProductCard3D from "@/components/ProductCard3D";
import { motion } from "framer-motion";

const BACKEND_URL = "https://shopease-production-acc0.up.railway.app";

const KidsClothing = () => {
  const [products, setProducts] = useState<any[]>([]);
  const [filter, setFilter] = useState("all");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await fetch(`${BACKEND_URL}/api/products`);
        const data = await res.json();

        if (data?.products) {
          setProducts(data.products);
        } else if (Array.isArray(data)) {
          setProducts(data);
        }
      } catch (error) {
        console.error("Error fetching products:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Normalize category string
  const normalize = (str: string) =>
    str?.toLowerCase().replace(/[\s-]/g, "");

  // Map button filters to category variants
  const categoryMap: Record<string, string[]> = {
    tshirt: ["tshirt", "tshirts", "tee", "tanktop", "croptop"],
    shirt: ["shirt", "shirts", "dress", "blouse"],
    shoe: ["shoe", "shoes", "sneaker", "boots", "sandals", "flats"],
    accessories: ["accessory", "accessories", "bag", "hat", "belt"],
  };

  // Only kids products (filter based on category variants)
  const kidsProducts = products.filter((p) => {
    const cat = normalize(p.category);
    return Object.values(categoryMap).flat().includes(cat);
  });

  // Apply selected filter
  const filteredProducts =
    filter === "all"
      ? kidsProducts
      : kidsProducts.filter((p) =>
          categoryMap[filter]?.includes(normalize(p.category))
        );

  return (
    <div className="min-h-screen bg-background">
      <Header />

      <main className="pt-20">
        {/* Hero Section */}
        <section className="relative py-20 bg-gradient-to-br from-yellow-100 to-green-100 dark:from-yellow-900/20 dark:to-green-900/20">
          <div className="container mx-auto px-6 text-center">
            <motion.h1
              className="text-5xl font-bold mb-6 text-foreground"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
            >
              Kids Collection
            </motion.h1>
            <motion.p
              className="text-xl text-muted-foreground max-w-2xl mx-auto"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.2 }}
            >
              Fun, comfortable, and colorful clothing for your little ones
            </motion.p>
          </div>
        </section>

        {/* Filter Section */}
        <section className="py-8 bg-card">
          <div className="container mx-auto px-6">
            <div className="flex justify-center space-x-4">
              {[
                { key: "all", label: "All Items" },
                { key: "tshirt", label: "T-Shirts" },
                { key: "shirt", label: "Shirts & Dresses" },
                { key: "shoe", label: "Shoes" },
                { key: "accessories", label: "Accessories" },
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

        {/* Products Grid */}
        <section className="py-16">
          <div className="container mx-auto px-6">
            {loading ? (
              <div className="text-center py-20 text-lg">Loading products...</div>
            ) : filteredProducts.length === 0 ? (
              <div className="text-center py-20 text-lg">No products found</div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
                {filteredProducts.map((product, index) => (
                  <motion.div
                    key={product.id}
                    initial={{ opacity: 0, y: 30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: index * 0.05 }}
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

export default KidsClothing;