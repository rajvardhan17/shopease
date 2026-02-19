import { motion } from "framer-motion";
import { Heart } from "lucide-react";
import { Link } from "react-router-dom";

const Footer = () => {
  const footerLinks: Record<string, { name: string; href: string }[]> = {
    Shop: [
      { name: "Men", href: "/men" },
      { name: "Women", href: "/women" },
      { name: "Kids", href: "/kids" },
      { name: "T-Shirts", href: "/men" },
      { name: "Shoes", href: "/shoes" },
      { name: "Accessories", href: "/accessories" },
    ],
    Support: [
      { name: "Help Center", href: "/help-center" },
      { name: "Size Guide", href: "/size-guide" },
      { name: "Returns", href: "/returns" },
      { name: "Shipping", href: "/shipping" },
      { name: "Contact Us", href: "/contact" },
    ],
    Company: [
      { name: "About Us", href: "/about" },
      { name: "Careers", href: "/careers" },
      { name: "Press", href: "/press" },
      { name: "Sustainability", href: "/sustainability" },
      { name: "Terms", href: "/terms" },
    ],
    Connect: [
      { name: "Instagram", href: "https://instagram.com" },
      { name: "Twitter", href: "https://twitter.com" },
      { name: "Facebook", href: "https://facebook.com" },
      { name: "YouTube", href: "https://youtube.com" },
      { name: "Newsletter", href: "/newsletter" },
    ],
  };

  return (
    <footer className="bg-primary text-primary-foreground">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-8">
          {/* Brand Section */}
          <div className="lg:col-span-2">
            <motion.h3
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              className="text-3xl font-bold mb-4"
            >
              ShopEase
            </motion.h3>
            <motion.p
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: 0.1 }}
              className="text-primary-foreground/80 mb-6 max-w-md"
            >
              Redefining fashion with premium quality and innovative design. 
              Experience the future of shopping with our immersive 3D technology.
            </motion.p>
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: 0.2 }}
              className="flex items-center space-x-4"
            >
              <div className="flex items-center space-x-2">
                <div className="w-2 h-2 bg-green-400 rounded-full animate-pulse" />
                <span className="text-sm">24/7 Support</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-2 h-2 bg-blue-400 rounded-full animate-pulse" />
                <span className="text-sm">Free Shipping</span>
              </div>
            </motion.div>
          </div>

          {/* Links Sections */}
          {Object.entries(footerLinks).map(([category, links], index) => (
            <motion.div
              key={category}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: 0.1 * (index + 1) }}
            >
              <h4 className="font-semibold mb-4">{category}</h4>
              <ul className="space-y-2">
                {links.map((link) => (
                  <li key={link.name}>
                    {link.href.startsWith("http") ? (
                      <motion.a
                        href={link.href}
                        target="_blank"
                        rel="noopener noreferrer"
                        whileHover={{ x: 5 }}
                        className="text-primary-foreground/70 hover:text-primary-foreground transition-colors duration-200"
                      >
                        {link.name}
                      </motion.a>
                    ) : (
                      <motion.div whileHover={{ x: 5 }}>
                        <Link
                          to={link.href}
                          className="text-primary-foreground/70 hover:text-primary-foreground transition-colors duration-200"
                        >
                          {link.name}
                        </Link>
                      </motion.div>
                    )}
                  </li>
                ))}
              </ul>
            </motion.div>
          ))}
        </div>

        {/* Bottom Section */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.5 }}
          className="border-t border-primary-foreground/20 mt-12 pt-8 flex flex-col md:flex-row justify-between items-center"
        >
          <div className="flex items-center space-x-2 mb-4 md:mb-0">
            <span className="text-primary-foreground/70">© 2025 ShopEase. Made with</span>
            <Heart className="w-4 h-4 text-red-400" fill="currentColor" aria-label="love" />
            <span className="text-primary-foreground/70">for fashion lovers</span>
          </div>
          
          <div className="flex space-x-6">
            <motion.a
              href="/privacy-policy"
              whileHover={{ scale: 1.1 }}
              className="text-primary-foreground/70 hover:text-primary-foreground transition-colors"
            >
              Privacy Policy
            </motion.a>
            <motion.a
              href="/terms-of-service"
              whileHover={{ scale: 1.1 }}
              className="text-primary-foreground/70 hover:text-primary-foreground transition-colors"
            >
              Terms of Service
            </motion.a>
          </div>
        </motion.div>
      </div>
    </footer>
  );
};

export default Footer;
