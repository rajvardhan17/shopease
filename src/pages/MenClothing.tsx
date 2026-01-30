import { useState } from "react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import ProductCard3D from "@/components/ProductCard3D";
import { motion } from "framer-motion";
import heroTshirtImage from "@/assets/hero-tshirt.jpg";
import shirtImage from "@/assets/shirt-white.jpg";
import hoodieImage from "@/assets/hoodie-black.jpg";
import jeansImage from "@/assets/jeans-blue.jpg";
import sneakerImage from "@/assets/sneaker-white.jpg";

const menProducts = [
  { id: "m1", name: "Classic Polo Shirt", price: 2999, category: "shirt", image: shirtImage },
  { id: "m2", name: "Denim Jacket", price: 5999, category: "shirt", image: jeansImage },
  { id: "m3", name: "Formal White Shirt", price: 3499, category: "shirt", image: shirtImage },
  { id: "m4", name: "Casual T-Shirt", price: 1999, category: "tshirt", image: heroTshirtImage },
  { id: "m5", name: "Premium Cotton Tee", price: 2499, category: "tshirt", image: heroTshirtImage },
  { id: "m6", name: "Sports Sneakers", price: 7999, category: "shoe", image: sneakerImage },
  { id: "m7", name: "Leather Dress Shoes", price: 12999, category: "shoe", image: sneakerImage },
  { id: "m8", name: "Running Shoes", price: 8999, category: "shoe", image: sneakerImage },
  { id: "m9", name: "Graphic Print Tee", price: 2299, category: "tshirt", image: heroTshirtImage },
  { id: "m10", name: "Casual Button Down", price: 3999, category: "shirt", image: shirtImage },
  { id: "m11", name: "Designer Hoodie", price: 4999, category: "tshirt", image: hoodieImage },
  { id: "m12", name: "Canvas Sneakers", price: 5999, category: "shoe", image: sneakerImage },
];

const MenClothing = () => {
  const [filter, setFilter] = useState("all");

  const filteredProducts = filter === "all" 
    ? menProducts 
    : menProducts.filter(product => product.category === filter);

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <main className="pt-20">
        {/* Hero Section */}
        <section className="relative py-20 bg-gradient-to-br from-primary/10 to-secondary/10">
          <div className="container mx-auto px-6 text-center">
            <motion.h1 
              className="text-5xl font-bold mb-6 text-foreground"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
            >
              Men's Collection
            </motion.h1>
            <motion.p 
              className="text-xl text-muted-foreground max-w-2xl mx-auto"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.2 }}
            >
              Discover premium quality clothing and footwear designed for the modern man
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
                { key: "shirt", label: "Shirts" },
                { key: "shoe", label: "Shoes" }
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
          </div>
        </section>
      </main>

      <Footer />
    </div>
  );
};

export default MenClothing;