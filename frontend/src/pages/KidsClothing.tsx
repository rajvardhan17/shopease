import { useState } from "react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import ProductCard3D from "@/components/ProductCard3D";
import { motion } from "framer-motion";
import heroTshirtImage from "@/assets/hero-tshirt.jpg";
import dressImage from "@/assets/dress-red.jpg";
import sneakerImage from "@/assets/sneaker-white.jpg";
import shirtImage from "@/assets/shirt-white.jpg";

const kidsProducts = [
  { id: "k1", name: "Rainbow T-Shirt", price: 1499, category: "tshirt", image: heroTshirtImage },
  { id: "k2", name: "Superhero Costume", price: 2999, category: "shirt", image: shirtImage },
  { id: "k3", name: "Cartoon Print Tee", price: 1299, category: "tshirt", image: heroTshirtImage },
  { id: "k4", name: "School Uniform Shirt", price: 1899, category: "shirt", image: shirtImage },
  { id: "k5", name: "Dinosaur T-Shirt", price: 1599, category: "tshirt", image: heroTshirtImage },
  { id: "k6", name: "Light-Up Sneakers", price: 3999, category: "shoe", image: sneakerImage },
  { id: "k7", name: "School Shoes", price: 2999, category: "shoe", image: sneakerImage },
  { id: "k8", name: "Character Sandals", price: 1999, category: "shoe", image: sneakerImage },
  { id: "k9", name: "Princess Dress", price: 3499, category: "shirt", image: dressImage },
  { id: "k10", name: "Sports Jersey", price: 2299, category: "tshirt", image: heroTshirtImage },
  { id: "k11", name: "Party Dress", price: 4999, category: "shirt", image: dressImage },
  { id: "k12", name: "Canvas Shoes", price: 2499, category: "shoe", image: sneakerImage },
];

const KidsClothing = () => {
  const [filter, setFilter] = useState("all");

  const filteredProducts = filter === "all" 
    ? kidsProducts 
    : kidsProducts.filter(product => product.category === filter);

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
                { key: "shirt", label: "Dresses & Shirts" },
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

export default KidsClothing;