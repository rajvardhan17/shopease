import { Canvas } from '@react-three/fiber';
import { OrbitControls, Environment, PerspectiveCamera } from '@react-three/drei';
import { TShirt3D } from './TShirt3D';
import { Shoe3D } from './Shoe3D';
import { Cap3D } from './Cap3D';

export const ProductScene3D = () => {
  return (
    <div className="w-full h-48 md:h-64">
      <Canvas>
        <PerspectiveCamera makeDefault position={[0, 0, 5]} />
        <ambientLight intensity={0.6} />
        <directionalLight position={[5, 5, 5]} intensity={1.2} />
        <pointLight position={[-5, -5, -5]} intensity={0.5} color="#ec4899" />
        
        <TShirt3D position={[0, 0, 0]} color="#8b5cf6" speed={1.2} />
        <Shoe3D position={[2.5, 0, 0]} color="#06b6d4" speed={1} />
        <Cap3D position={[-2.5, 0, 0]} color="#ec4899" speed={0.9} />
        
        <Environment preset="sunset" />
        <OrbitControls 
          enableZoom={false} 
          enablePan={false}
          autoRotate
          autoRotateSpeed={2.5}
        />
      </Canvas>
    </div>
  );
};
