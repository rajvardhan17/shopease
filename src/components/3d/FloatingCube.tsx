import { useRef } from 'react';
import { useFrame } from '@react-three/fiber';
import { Mesh } from 'three';
import { RoundedBox } from '@react-three/drei';

interface FloatingCubeProps {
  position?: [number, number, number];
  color?: string;
  speed?: number;
}

export const FloatingCube = ({ 
  position = [0, 0, 0], 
  color = "#8b5cf6",
  speed = 1
}: FloatingCubeProps) => {
  const meshRef = useRef<Mesh>(null);

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.x = state.clock.getElapsedTime() * 0.3 * speed;
      meshRef.current.rotation.y = state.clock.getElapsedTime() * 0.5 * speed;
      meshRef.current.position.y = position[1] + Math.sin(state.clock.getElapsedTime() * speed) * 0.3;
    }
  });

  return (
    <RoundedBox
      ref={meshRef}
      args={[1, 1, 1]}
      position={position}
      radius={0.1}
      smoothness={4}
    >
      <meshStandardMaterial color={color} metalness={0.5} roughness={0.2} />
    </RoundedBox>
  );
};
