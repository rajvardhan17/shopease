import { useRef } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import { motion } from "framer-motion";
import { Button } from "@/components/ui/button";
import * as THREE from "three";
import heroImage from "@/assets/hero-tshirt.jpg";

// 3D T-Shirt Model
const TShirt3D = () => {
  const groupRef = useRef<THREE.Group>(null);

  useFrame((state) => {
    if (groupRef.current) {
      groupRef.current.rotation.y = Math.sin(state.clock.elapsedTime * 0.5) * 0.3;
      groupRef.current.rotation.x = Math.sin(state.clock.elapsedTime * 0.3) * 0.1;
      groupRef.current.position.y = Math.sin(state.clock.elapsedTime * 2) * 0.1;
    }
  });

  return (
    <group ref={groupRef}>
      {/* T-Shirt Body */}
      <mesh position={[0, 0, 0]}>
        <boxGeometry args={[2, 2.5, 0.2]} />
        <meshStandardMaterial color="#4F46E5" roughness={0.2} metalness={0.1} />
      </mesh>
      
      {/* T-Shirt Sleeves */}
      <mesh position={[-1.2, 0.8, 0]} rotation={[0, 0, 0.3]}>
        <boxGeometry args={[0.8, 0.8, 0.2]} />
        <meshStandardMaterial color="#4F46E5" roughness={0.2} metalness={0.1} />
      </mesh>
      
      <mesh position={[1.2, 0.8, 0]} rotation={[0, 0, -0.3]}>
        <boxGeometry args={[0.8, 0.8, 0.2]} />
        <meshStandardMaterial color="#4F46E5" roughness={0.2} metalness={0.1} />
      </mesh>

      {/* Logo on T-Shirt - Simple geometry */}
      <mesh position={[0, 0.3, 0.11]}>
        <boxGeometry args={[0.4, 0.2, 0.02]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh>
    </group>
  );
};

const Hero3D = () => {
  return (
    <section className="relative min-h-screen flex items-center justify-center overflow-hidden bg-gradient-hero">
      {/* Background Image with Overlay */}
      <div 
        className="absolute inset-0 bg-cover bg-center bg-no-repeat opacity-20"
        style={{ backgroundImage: `url(${heroImage})` }}
      />
      <div className="absolute inset-0 bg-gradient-to-br from-background/80 to-background/60" />

      <div className="container mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
        <div className="grid lg:grid-cols-2 gap-12 items-center">
          {/* Left Content */}
          <motion.div
            initial={{ opacity: 0, x: -50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.8 }}
            className="text-center lg:text-left"
          >
            <motion.h1
              className="text-5xl lg:text-7xl font-bold tracking-tight text-foreground mb-6"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2, duration: 0.8 }}
            >
              Unleash Your
              <span className="block text-transparent bg-clip-text bg-gradient-to-r from-apple-blue to-primary">
                Style
              </span>
            </motion.h1>

            <motion.p
              className="text-xl text-muted-foreground mb-8 max-w-2xl"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.4, duration: 0.8 }}
            >
              Discover premium fashion that defines your personality. 
              Experience the future of shopping with our immersive 3D product showcase.
            </motion.p>

            <motion.div
              className="flex flex-col sm:flex-row gap-4 justify-center lg:justify-start"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.6, duration: 0.8 }}
            >
              <Button 
                size="lg" 
                className="apple-button bg-primary text-primary-foreground hover:bg-primary/90 px-8 py-4 text-lg"
              >
                Shop Collection
              </Button>
              
              <Button 
                variant="outline" 
                size="lg"
                className="apple-button border-2 px-8 py-4 text-lg"
              >
                Watch Story
              </Button>
            </motion.div>

            <motion.div
              className="flex items-center justify-center lg:justify-start gap-8 mt-12"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.8, duration: 0.8 }}
            >
              <div className="text-center">
                <div className="text-2xl font-bold text-primary">10K+</div>
                <div className="text-sm text-muted-foreground">Happy Customers</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-primary">500+</div>
                <div className="text-sm text-muted-foreground">Premium Products</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-primary">4.9★</div>
                <div className="text-sm text-muted-foreground">Customer Rating</div>
              </div>
            </motion.div>
          </motion.div>

          {/* Right 3D Content */}
          <motion.div
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.8, delay: 0.3 }}
            className="relative h-96 lg:h-[600px]"
          >
            <div className="absolute inset-0 bg-gradient-to-br from-apple-blue/5 to-primary/5 rounded-3xl" />
            <Canvas camera={{ position: [0, 0, 8], fov: 45 }}>
              <ambientLight intensity={0.5} />
              <spotLight position={[10, 10, 10]} angle={0.15} penumbra={1} intensity={1} />
              <pointLight position={[-10, -10, -10]} intensity={0.5} />
              <TShirt3D />
            </Canvas>
            
            {/* Floating Elements */}
            <motion.div
              className="absolute top-4 right-4 glass rounded-full p-4"
              animate={{ y: [0, -10, 0] }}
              transition={{ duration: 3, repeat: Infinity }}
            >
              <div className="w-4 h-4 bg-apple-blue rounded-full animate-pulse" />
            </motion.div>
            
            <motion.div
              className="absolute bottom-8 left-8 glass rounded-lg p-3"
              animate={{ y: [0, -5, 0] }}
              transition={{ duration: 4, repeat: Infinity }}
            >
              <div className="text-sm font-medium">New Arrival</div>
            </motion.div>
          </motion.div>
        </div>
      </div>
    </section>
  );
};

export default Hero3D;