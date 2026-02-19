import { useRef } from 'react';
import { useFrame } from '@react-three/fiber';
import { Group } from 'three';

interface ShoppingBag3DProps {
  position?: [number, number, number];
  color?: string;
  speed?: number;
}

export const ShoppingBag3D = ({ 
  position = [0, 0, 0], 
  color = "#ec4899",
  speed = 1
}: ShoppingBag3DProps) => {
  const meshRef = useRef<Group>(null);

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.y = state.clock.getElapsedTime() * 0.4 * speed;
      meshRef.current.position.y = position[1] + Math.sin(state.clock.getElapsedTime() * speed) * 0.25;
    }
  });

  return (
    <group ref={meshRef} position={position}>
      {/* Main bag body */}
      <mesh position={[0, 0, 0]}>
        <boxGeometry args={[1, 1.2, 0.6]} />
        <meshStandardMaterial color={color} metalness={0.2} roughness={0.7} />
      </mesh>
      
      {/* Handle left */}
      <mesh position={[-0.3, 0.8, 0]}>
        <torusGeometry args={[0.15, 0.05, 8, 16, Math.PI]} />
        <meshStandardMaterial color={color} metalness={0.3} roughness={0.6} />
      </mesh>
      
      {/* Handle right */}
      <mesh position={[0.3, 0.8, 0]}>
        <torusGeometry args={[0.15, 0.05, 8, 16, Math.PI]} />
        <meshStandardMaterial color={color} metalness={0.3} roughness={0.6} />
      </mesh>
      
      {/* Bottom detail */}
      <mesh position={[0, -0.6, 0]}>
        <boxGeometry args={[1.05, 0.05, 0.65]} />
        <meshStandardMaterial color="#1f2937" metalness={0.5} roughness={0.5} />
      </mesh>
    </group>
  );
};
