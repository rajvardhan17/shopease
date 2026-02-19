import { useParams, useNavigate, useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import { Canvas } from "@react-three/fiber";
import { Box } from "@react-three/drei";
import { motion } from "framer-motion";
import { ArrowLeft, Heart, Share2, Star, Plus, Minus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Separator } from "@/components/ui/separator";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import ProductCard3D from "@/components/ProductCard3D";
import { useToast } from "@/hooks/use-toast";
import { addCartItem } from "@/lib/cart";
import heroTshirtImage from "@/assets/hero-tshirt.jpg";
import sneakerImage from "@/assets/sneaker-white.jpg";
import shirtImage from "@/assets/shirt-product.jpg";
import beltImage from "@/assets/belt-product.jpg";
import watchImage from "@/assets/watch-silver.jpg";

// 3D Product Display
const Product3DDetail = ({ color = "#ffffff" }) => {
  return (
    <Box args={[2, 2.4, 0.2]} rotation={[0, Math.PI / 4, 0]}>
      <meshStandardMaterial color={color} />
    </Box>
  );
};

// Sample product data - replace with API calls
const allProducts = {
  tshirts: [
    { id: "1", name: "Classic Cotton T-Shirt", price: 899, category: "tshirt", image: heroTshirtImage },
    { id: "2", name: "Premium Polo Shirt", price: 1299, category: "tshirt", image: heroTshirtImage },
    { id: "3", name: "Graphic Print Tee", price: 1099, category: "tshirt", image: heroTshirtImage },
    { id: "4", name: "V-Neck Basic Tee", price: 799, category: "tshirt", image: heroTshirtImage },
  ],
  shoes: [
    { id: "5", name: "Running Sneakers", price: 2999, category: "shoe", image: sneakerImage },
    { id: "6", name: "Casual Loafers", price: 2499, category: "shoe", image: sneakerImage },
    { id: "7", name: "Sport Sandals", price: 1899, category: "shoe", image: sneakerImage },
    { id: "8", name: "Canvas Shoes", price: 1599, category: "shoe", image: sneakerImage },
  ],
  shirts: [
    { id: "9", name: "Formal White Shirt", price: 1599, category: "shirt", image: shirtImage },
    { id: "10", name: "Casual Check Shirt", price: 1399, category: "shirt", image: shirtImage },
    { id: "11", name: "Denim Shirt", price: 1899, category: "shirt", image: shirtImage },
    { id: "12", name: "Linen Summer Shirt", price: 1699, category: "shirt", image: shirtImage },
  ],
  accessories: [
    { id: "13", name: "Leather Belt", price: 999, category: "accessory", image: beltImage },
    { id: "14", name: "Classic Watch", price: 4999, category: "accessory", image: watchImage },
    { id: "15", name: "Leather Wallet", price: 1299, category: "accessory", image: beltImage },
    { id: "16", name: "Sunglasses", price: 1899, category: "accessory", image: watchImage },
  ]
};

const ProductDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const { toast } = useToast();
  const [quantity, setQuantity] = useState(1);
  const [selectedSize, setSelectedSize] = useState("M");
  const [selectedColor, setSelectedColor] = useState("Blue");
  const [isWishlisted, setIsWishlisted] = useState(false);
  const optionsRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const shouldScrollToOptions = Boolean(
      (location.state as { scrollToOptions?: boolean } | null)?.scrollToOptions
    );

    if (shouldScrollToOptions) {
      optionsRef.current?.scrollIntoView({ behavior: "smooth", block: "start" });
      return;
    }

    window.scrollTo({ top: 0, left: 0, behavior: "auto" });
  }, [id, location.state]);

  // Find the product - in real app, this would be an API call
  const findProduct = () => {
    const allProductsList = [
      ...allProducts.tshirts,
      ...allProducts.shoes,
      ...allProducts.shirts,
      ...allProducts.accessories
    ];
    return allProductsList.find(p => p.id === id) || allProducts.tshirts[0];
  };

  const product = findProduct();
  
  // Get similar products based on category
  const getSimilarProducts = () => {
    const categoryKey = product.category === "tshirt" ? "tshirts" : 
                       product.category === "shoe" ? "shoes" :
                       product.category === "shirt" ? "shirts" : "accessories";
    return allProducts[categoryKey].filter(p => p.id !== product.id).slice(0, 4);
  };

  const similarProducts = getSimilarProducts();

  const handleAddToCart = () => {
    addCartItem({
      id: product.id,
      name: product.name,
      price: product.price,
      image: product.image,
      quantity,
      selectedSize,
      selectedColor,
    });

    toast({
      title: "Added to Cart",
      description: `${product.name} (${selectedSize}, ${selectedColor}) x${quantity} added to cart`,
    });
  };

  const handleWishlist = () => {
    setIsWishlisted(!isWishlisted);
    toast({
      title: isWishlisted ? "Removed from Wishlist" : "Added to Wishlist",
      description: `${product.name} ${isWishlisted ? "removed from" : "added to"} your wishlist`,
    });
  };

  const productColor = product.category === "tshirt" ? "#4F46E5" : 
                      product.category === "shoe" ? "#059669" : 
                      product.category === "shirt" ? "#DC2626" : "#7C3AED";

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <main className="container mx-auto px-4 py-8">
        {/* Back Button */}
        <Button 
          variant="ghost" 
          onClick={() => navigate(-1)}
          className="mb-6"
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          Back
        </Button>

        <div className="grid lg:grid-cols-2 gap-12">
          {/* Product Image/3D View */}
          <motion.div 
            className="aspect-square bg-gradient-card rounded-2xl p-8"
            initial={{ opacity: 0, x: -30 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6 }}
          >
            <Canvas camera={{ position: [0, 0, 6], fov: 50 }}>
              <ambientLight intensity={0.6} />
              <pointLight position={[10, 10, 10]} intensity={0.8} />
              <pointLight position={[-10, -10, -10]} intensity={0.3} />
              <Product3DDetail color={productColor} />
            </Canvas>
          </motion.div>

          {/* Product Details */}
          <motion.div 
            className="space-y-6"
            initial={{ opacity: 0, x: 30 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
          >
            <div>
              <div className="flex items-center gap-2 mb-2">
                <Badge variant="secondary">{product.category}</Badge>
                <div className="flex items-center gap-1">
                  {[...Array(5)].map((_, i) => (
                    <Star key={i} className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                  ))}
                  <span className="text-sm text-muted-foreground ml-1">(4.8)</span>
                </div>
              </div>
              
              <h1 className="text-3xl font-bold text-foreground mb-2">{product.name}</h1>
              <p className="text-4xl font-bold text-primary">₹{product.price}</p>
            </div>

            {/* Product Options */}
            <div className="space-y-4" ref={optionsRef}>
              {/* Size Selection */}
              <div>
                <h3 className="font-semibold mb-2">Size</h3>
                <div className="flex gap-2">
                  {["S", "M", "L", "XL"].map((size) => (
                    <Button
                      key={size}
                      variant={selectedSize === size ? "default" : "outline"}
                      size="sm"
                      onClick={() => setSelectedSize(size)}
                    >
                      {size}
                    </Button>
                  ))}
                </div>
              </div>

              {/* Color Selection */}
              <div>
                <h3 className="font-semibold mb-2">Color</h3>
                <div className="flex gap-2">
                  {["Blue", "Black", "White", "Red"].map((color) => (
                    <Button
                      key={color}
                      variant={selectedColor === color ? "default" : "outline"}
                      size="sm"
                      onClick={() => setSelectedColor(color)}
                    >
                      {color}
                    </Button>
                  ))}
                </div>
              </div>

              {/* Quantity */}
              <div>
                <h3 className="font-semibold mb-2">Quantity</h3>
                <div className="flex items-center gap-3">
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  >
                    <Minus className="w-4 h-4" />
                  </Button>
                  <span className="w-12 text-center font-semibold">{quantity}</span>
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => setQuantity(quantity + 1)}
                  >
                    <Plus className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-3">
              <Button 
                className="flex-1" 
                size="lg"
                onClick={handleAddToCart}
              >
                Add to Cart
              </Button>
              <Button
                variant={isWishlisted ? "default" : "outline"}
                size="lg"
                onClick={handleWishlist}
              >
                <Heart className={`w-5 h-5 ${isWishlisted ? "fill-current" : ""}`} />
              </Button>
              <Button variant="outline" size="lg">
                <Share2 className="w-5 h-5" />
              </Button>
            </div>

            {/* Product Info Tabs */}
            <Tabs defaultValue="description" className="mt-8">
              <TabsList className="grid w-full grid-cols-3">
                <TabsTrigger value="description">Description</TabsTrigger>
                <TabsTrigger value="specifications">Specs</TabsTrigger>
                <TabsTrigger value="reviews">Reviews</TabsTrigger>
              </TabsList>
              
              <TabsContent value="description" className="mt-4">
                <p className="text-muted-foreground leading-relaxed">
                  This premium {product.name.toLowerCase()} is crafted with the finest materials 
                  and attention to detail. Perfect for everyday wear with superior comfort and style. 
                  Made from high-quality fabric that ensures durability and a perfect fit.
                </p>
              </TabsContent>
              
              <TabsContent value="specifications" className="mt-4">
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span className="font-medium">Material:</span>
                    <span className="text-muted-foreground">100% Cotton</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-medium">Care:</span>
                    <span className="text-muted-foreground">Machine Wash</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-medium">Origin:</span>
                    <span className="text-muted-foreground">India</span>
                  </div>
                </div>
              </TabsContent>
              
              <TabsContent value="reviews" className="mt-4">
                <div className="space-y-4">
                  <div className="border-l-4 border-primary pl-4">
                    <div className="flex items-center gap-2 mb-1">
                      <span className="font-semibold">Rahul K.</span>
                      <div className="flex">
                        {[...Array(5)].map((_, i) => (
                          <Star key={i} className="w-3 h-3 fill-yellow-400 text-yellow-400" />
                        ))}
                      </div>
                    </div>
                    <p className="text-sm text-muted-foreground">Great quality and perfect fit!</p>
                  </div>
                </div>
              </TabsContent>
            </Tabs>
          </motion.div>
        </div>

        {/* Similar Products */}
        <Separator className="my-12" />
        
        <motion.section
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.4 }}
        >
          <h2 className="text-2xl font-bold text-foreground mb-8">Similar Products</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {similarProducts.map((similarProduct) => (
              <ProductCard3D 
                key={similarProduct.id} 
                product={similarProduct} 
              />
            ))}
          </div>
        </motion.section>
      </main>

      <Footer />
    </div>
  );
};

export default ProductDetail;
