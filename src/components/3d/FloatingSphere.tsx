import { useRef } from 'react';
import { useFrame } from '@react-three/fiber';
import { Mesh } from 'three';
import { Sphere } from '@react-three/drei';

interface FloatingSphereProps {
  position?: [number, number, number];
  color?: string;
  speed?: number;
}

export const FloatingSphere = ({ 
  position = [0, 0, 0], 
  color = "#06b6d4",
  speed = 1
}: FloatingSphereProps) => {
  const meshRef = useRef<Mesh>(null);

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.x = state.clock.getElapsedTime() * 0.2 * speed;
      meshRef.current.rotation.y = state.clock.getElapsedTime() * 0.4 * speed;
      meshRef.current.position.y = position[1] + Math.cos(state.clock.getElapsedTime() * speed) * 0.4;
    }
  });

  return (
    <Sphere
      ref={meshRef}
      args={[0.7, 32, 32]}
      position={position}
    >
      <meshStandardMaterial color={color} metalness={0.6} roughness={0.1} />
    </Sphere>
  );
};
