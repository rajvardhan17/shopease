import { useRef } from 'react';
import { useFrame } from '@react-three/fiber';
import { Group } from 'three';

interface Shoe3DProps {
  position?: [number, number, number];
  color?: string;
  speed?: number;
}

export const Shoe3D = ({ 
  position = [0, 0, 0], 
  color = "#06b6d4",
  speed = 1
}: Shoe3DProps) => {
  const meshRef = useRef<Group>(null);

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.y = state.clock.getElapsedTime() * 0.6 * speed;
      meshRef.current.position.y = position[1] + Math.cos(state.clock.getElapsedTime() * speed) * 0.2;
    }
  });

  return (
    <group ref={meshRef} position={position}>
      {/* Sole */}
      <mesh position={[0, -0.3, 0]}>
        <boxGeometry args={[0.8, 0.2, 1.2]} />
        <meshStandardMaterial color="#2d3748" metalness={0.6} roughness={0.3} />
      </mesh>
      
      {/* Upper shoe body */}
      <mesh position={[0, 0, 0]} rotation={[0.2, 0, 0]}>
        <boxGeometry args={[0.7, 0.6, 1]} />
        <meshStandardMaterial color={color} metalness={0.4} roughness={0.5} />
      </mesh>
      
      {/* Toe cap */}
      <mesh position={[0, -0.1, 0.5]}>
        <sphereGeometry args={[0.35, 16, 16]} />
        <meshStandardMaterial color={color} metalness={0.4} roughness={0.5} />
      </mesh>
      
      {/* Laces area */}
      <mesh position={[0, 0.2, 0.2]}>
        <boxGeometry args={[0.5, 0.3, 0.4]} />
        <meshStandardMaterial color={color} metalness={0.3} roughness={0.6} />
      </mesh>
    </group>
  );
};
