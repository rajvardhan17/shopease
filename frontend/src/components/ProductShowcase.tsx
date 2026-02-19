import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import ProductCard3D from "./ProductCard3D";
import { Button } from "@/components/ui/button";

interface Product {
  id: string;
  name: string;
  price: number;
  image?: string;
  category: string;
}

interface ProductShowcaseProps {
  title: string;
  products: Product[];
  viewAllLink?: string;
}

const ProductShowcase = ({ title, products, viewAllLink }: ProductShowcaseProps) => {
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 30 },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: 0.6,
      },
    },
  };

  return (
    <section className="py-20 bg-background">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8">
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.8 }}
          className="text-center mb-16"
        >
          <h2 className="text-4xl lg:text-5xl font-bold tracking-tight text-foreground mb-4">
            {title}
          </h2>
          <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
            Discover our premium collection crafted with precision and style
          </p>
        </motion.div>

        <motion.div
          variants={containerVariants}
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
          className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8 mb-12"
        >
          {products.slice(0, 8).map((product, index) => (
            <motion.div key={product.id} variants={itemVariants}>
              <ProductCard3D product={product} />
            </motion.div>
          ))}
        </motion.div>

        {viewAllLink && (
          <motion.div
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            viewport={{ once: true }}
            transition={{ delay: 0.5, duration: 0.6 }}
            className="text-center"
          >
            <Button
              variant="outline"
              size="lg"
              className="apple-button border-2 px-8 py-4"
              asChild
            >
              <Link to={viewAllLink}>
                View All {title}
              </Link>
            </Button>
          </motion.div>
        )}
      </div>
    </section>
  );
};

export default ProductShowcase;
