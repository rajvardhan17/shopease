import { useRef, useState } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import { Box } from "@react-three/drei";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import type { MouseEvent } from "react";
import * as THREE from "three";

// 3D Product Component
const Product3D = ({ color = "#ffffff" }) => {
  const meshRef = useRef<THREE.Mesh>(null);
  const [hovered, setHovered] = useState(false);

  useFrame((state, delta) => {
    if (meshRef.current) {
      meshRef.current.rotation.y += delta * 0.5;
      meshRef.current.rotation.x = Math.sin(state.clock.elapsedTime) * 0.1;
    }
  });

  return (
    <group>
      <Box
        ref={meshRef}
        args={[1, 1.2, 0.1]}
        onPointerOver={() => setHovered(true)}
        onPointerOut={() => setHovered(false)}
        scale={hovered ? 1.1 : 1}
      >
        <meshStandardMaterial color={color} />
      </Box>
    </group>
  );
};

interface ProductCard3DProps {
  product: {
    id: string;
    name: string;
    description?: string;
    price?: number;
    image?: string;
    category?: string;
  };
  className?: string;
}

const ProductCard3D = ({ product, className = "" }: ProductCard3DProps) => {
  const [isHovered, setIsHovered] = useState(false);
  const navigate = useNavigate();

  const openProduct = () => {
    navigate(`/product/${product.id}`);
    window.scrollTo({ top: 0, left: 0, behavior: "auto" });
  };

  const openProductOptions = (event: MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation();
    navigate(`/product/${product.id}`, { state: { scrollToOptions: true } });
    window.scrollTo({ top: 0, left: 0, behavior: "auto" });
  };

  return (
    <motion.div
      className={`product-card group cursor-pointer ${className}`}
      onHoverStart={() => setIsHovered(true)}
      onHoverEnd={() => setIsHovered(false)}
      whileHover={{ y: -8 }}
      initial={{ opacity: 0, y: 30 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
      onClick={openProduct}
    >
      {/* Image / 3D Canvas */}
      <div className="aspect-square bg-gradient-card rounded-xl p-4 mb-4 overflow-hidden relative">
        {product.image ? (
          <img
            src={product.image}
            alt={product.name}
            className="w-full h-full object-cover rounded-lg"
          />
        ) : (
          <Canvas camera={{ position: [0, 0, 4], fov: 50 }}>
            <ambientLight intensity={0.6} />
            <pointLight position={[10, 10, 10]} intensity={0.8} />
            <pointLight position={[-10, -10, -10]} intensity={0.3} />
            <Product3D
              color={
                product.category?.toLowerCase().includes("shirt")
                  ? "#DC2626"
                  : product.category?.toLowerCase().includes("shoe")
                  ? "#059669"
                  : "#7C3AED"
              }
            />
          </Canvas>
        )}
      </div>

      {/* Product Info */}
      <div className="p-4">
        <motion.h3
          className="font-semibold text-lg mb-1 text-foreground"
          animate={{ color: isHovered ? "#2563eb" : "#000000" }}
        >
          {product.name}
        </motion.h3>

        {product.description && (
          <p className="text-sm text-muted-foreground mb-2 line-clamp-2">
            {product.description}
          </p>
        )}

        <div className="flex items-center justify-between">
          <span className="text-2xl font-bold text-primary">
            ₹{product.price || 0}
          </span>

          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            className="apple-button text-sm bg-primary text-primary-foreground hover:bg-primary/90"
            onClick={openProductOptions}
          >
            Add to Cart
          </motion.button>
        </div>
      </div>
    </motion.div>
  );
};

export default ProductCard3D;
