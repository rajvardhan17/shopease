import { useState } from "react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import ProductCard3D from "@/components/ProductCard3D";
import { motion } from "framer-motion";
import dressImage from "@/assets/dress-red.jpg";
import silkDressImage from "@/assets/dress-silk.jpg";
import heroTshirtImage from "@/assets/hero-tshirt.jpg";
import sneakerImage from "@/assets/sneaker-white.jpg";
import shirtImage from "@/assets/shirt-white.jpg";

const womenProducts = [
  { id: "w1", name: "Floral Summer Dress", price: 3999, category: "shirt", image: dressImage },
  { id: "w2", name: "Designer Blouse", price: 2999, category: "shirt", image: shirtImage },
  { id: "w3", name: "Casual Crop Top", price: 1899, category: "tshirt", image: heroTshirtImage },
  { id: "w4", name: "Elegant Evening Dress", price: 7999, category: "shirt", image: silkDressImage },
  { id: "w5", name: "Cotton Basic Tee", price: 1599, category: "tshirt", image: heroTshirtImage },
  { id: "w6", name: "High Heel Sandals", price: 6999, category: "shoe", image: sneakerImage },
  { id: "w7", name: "Casual Sneakers", price: 5999, category: "shoe", image: sneakerImage },
  { id: "w8", name: "Ballet Flats", price: 4999, category: "shoe", image: sneakerImage },
  { id: "w9", name: "Printed Tank Top", price: 1799, category: "tshirt", image: heroTshirtImage },
  { id: "w10", name: "Silk Blouse", price: 4999, category: "shirt", image: silkDressImage },
  { id: "w11", name: "Vintage Style Tee", price: 2199, category: "tshirt", image: heroTshirtImage },
  { id: "w12", name: "Designer Boots", price: 8999, category: "shoe", image: sneakerImage },
];

const WomenClothing = () => {
  const [filter, setFilter] = useState("all");

  const filteredProducts = filter === "all" 
    ? womenProducts 
    : womenProducts.filter(product => product.category === filter);

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <main className="pt-20">
        {/* Hero Section */}
        <section className="relative py-20 bg-gradient-to-br from-pink-100 to-purple-100 dark:from-pink-900/20 dark:to-purple-900/20">
          <div className="container mx-auto px-6 text-center">
            <motion.h1 
              className="text-5xl font-bold mb-6 text-foreground"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
            >
              Women's Collection
            </motion.h1>
            <motion.p 
              className="text-xl text-muted-foreground max-w-2xl mx-auto"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.2 }}
            >
              Embrace elegance with our curated selection of women's fashion essentials
            </motion.p>
          </div>
        </section>

        {/* Filter Section */}
        <section className="py-8 bg-card">
          <div className="container mx-auto px-6">
            <div className="flex justify-center space-x-4">
              {[
                { key: "all", label: "All Items" },
                { key: "tshirt", label: "Tops & Tees" },
                { key: "shirt", label: "Dresses & Blouses" },
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

export default WomenClothing;