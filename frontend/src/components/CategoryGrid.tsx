import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import shoesImage from "@/assets/shoes-product.jpg";
import shirtImage from "@/assets/shirt-product.jpg";
import beltImage from "@/assets/belt-product.jpg";

const categories = [
  {
    name: "T-Shirts",
    href: "/men",
    image: shirtImage,
    description: "Premium cotton tees",
    color: "from-blue-500/20 to-purple-500/20"
  },
  {
    name: "Shoes",
    href: "/shoes", 
    image: shoesImage,
    description: "Comfort meets style",
    color: "from-green-500/20 to-blue-500/20"
  },
  {
    name: "Accessories",
    href: "/accessories",
    image: beltImage, 
    description: "Complete your look",
    color: "from-orange-500/20 to-red-500/20"
  }
];

const CategoryGrid = () => {
  return (
    <section className="py-20 bg-gradient-to-b from-background to-muted/20">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8">
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.8 }}
          className="text-center mb-16"
        >
          <h2 className="text-4xl lg:text-5xl font-bold tracking-tight text-foreground mb-4">
            Shop by Category
          </h2>
          <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
            Explore our curated collections designed for every occasion
          </p>
        </motion.div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {categories.map((category, index) => (
            <motion.div
              key={category.name}
              initial={{ opacity: 0, y: 50 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.6, delay: index * 0.2 }}
              whileHover={{ y: -10 }}
              className="group relative overflow-hidden rounded-2xl bg-gradient-card shadow-soft hover:shadow-large transition-all duration-500"
            >
              <Link to={category.href} className="block">
                <div className="aspect-[4/5] relative overflow-hidden">
                  <div className={`absolute inset-0 bg-gradient-to-br ${category.color} opacity-60 group-hover:opacity-40 transition-opacity duration-500`} />
                  
                  <img
                    src={category.image}
                    alt={category.name}
                    className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
                  />
                  
                  <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent" />
                  
                  <div className="absolute bottom-0 left-0 right-0 p-8">
                    <motion.h3 
                      className="text-2xl font-bold text-white mb-2"
                      whileHover={{ scale: 1.05 }}
                    >
                      {category.name}
                    </motion.h3>
                    <p className="text-white/80 mb-4">
                      {category.description}
                    </p>
                    
                    <motion.div
                      className="inline-flex items-center text-white font-medium"
                      whileHover={{ x: 5 }}
                    >
                      Shop Now 
                      <motion.svg
                        className="ml-2 w-4 h-4"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                        whileHover={{ x: 3 }}
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M9 5l7 7-7 7"
                        />
                      </motion.svg>
                    </motion.div>
                  </div>
                </div>
              </Link>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default CategoryGrid;
