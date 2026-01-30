import { Canvas } from '@react-three/fiber';
import { OrbitControls, Environment, PerspectiveCamera } from '@react-three/drei';
import { TShirt3D } from './TShirt3D';
import { Shoe3D } from './Shoe3D';
import { ShoppingBag3D } from './ShoppingBag3D';
import { Cap3D } from './Cap3D';

export const AuthScene3D = () => {
  return (
    <div className="w-full h-64 md:h-96">
      <Canvas className="rounded-2xl">
        <PerspectiveCamera makeDefault position={[0, 0, 6]} />
        <ambientLight intensity={0.6} />
        <directionalLight position={[10, 10, 5]} intensity={1.2} />
        <pointLight position={[-10, -10, -5]} intensity={0.6} color="#8b5cf6" />
        <spotLight position={[5, 5, 5]} angle={0.3} intensity={0.8} color="#06b6d4" />
        
        <TShirt3D position={[-2, 0.5, 0]} color="#8b5cf6" speed={0.8} />
        <Shoe3D position={[2, -0.5, 0]} color="#06b6d4" speed={1.1} />
        <ShoppingBag3D position={[0, 1.5, -1]} color="#ec4899" speed={0.9} />
        <Cap3D position={[-1, -1.5, 0]} color="#8b5cf6" speed={1.2} />
        
        <Environment preset="city" />
        <OrbitControls 
          enableZoom={false} 
          enablePan={false}
          autoRotate
          autoRotateSpeed={1.5}
        />
      </Canvas>
    </div>
  );
};
