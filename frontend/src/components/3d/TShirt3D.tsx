import { useRef } from 'react';
import { useFrame } from '@react-three/fiber';
import { Group } from 'three';

interface TShirt3DProps {
  position?: [number, number, number];
  color?: string;
  speed?: number;
}

export const TShirt3D = ({ 
  position = [0, 0, 0], 
  color = "#8b5cf6",
  speed = 1
}: TShirt3DProps) => {
  const meshRef = useRef<Group>(null);

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.y = state.clock.getElapsedTime() * 0.5 * speed;
      meshRef.current.position.y = position[1] + Math.sin(state.clock.getElapsedTime() * speed) * 0.2;
    }
  });

  return (
    <group ref={meshRef} position={position}>
      {/* Body */}
      <mesh position={[0, 0, 0]}>
        <boxGeometry args={[1.2, 1.4, 0.3]} />
        <meshStandardMaterial color={color} metalness={0.3} roughness={0.4} />
      </mesh>
      
      {/* Left sleeve */}
      <mesh position={[-0.75, 0.3, 0]} rotation={[0, 0, -0.3]}>
        <boxGeometry args={[0.5, 0.8, 0.3]} />
        <meshStandardMaterial color={color} metalness={0.3} roughness={0.4} />
      </mesh>
      
      {/* Right sleeve */}
      <mesh position={[0.75, 0.3, 0]} rotation={[0, 0, 0.3]}>
        <boxGeometry args={[0.5, 0.8, 0.3]} />
        <meshStandardMaterial color={color} metalness={0.3} roughness={0.4} />
      </mesh>
      
      {/* Collar */}
      <mesh position={[0, 0.7, 0]}>
        <boxGeometry args={[0.4, 0.2, 0.35]} />
        <meshStandardMaterial color={color} metalness={0.3} roughness={0.4} />
      </mesh>
    </group>
  );
};
