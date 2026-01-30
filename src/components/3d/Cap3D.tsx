import { useRef } from 'react';
import { useFrame } from '@react-three/fiber';
import { Group } from 'three';

interface Cap3DProps {
  position?: [number, number, number];
  color?: string;
  speed?: number;
}

export const Cap3D = ({ 
  position = [0, 0, 0], 
  color = "#8b5cf6",
  speed = 1
}: Cap3DProps) => {
  const meshRef = useRef<Group>(null);

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.y = state.clock.getElapsedTime() * 0.5 * speed;
      meshRef.current.position.y = position[1] + Math.cos(state.clock.getElapsedTime() * speed) * 0.15;
    }
  });

  return (
    <group ref={meshRef} position={position}>
      {/* Crown */}
      <mesh position={[0, 0.2, 0]}>
        <sphereGeometry args={[0.5, 16, 16, 0, Math.PI * 2, 0, Math.PI / 2]} />
        <meshStandardMaterial color={color} metalness={0.3} roughness={0.6} />
      </mesh>
      
      {/* Visor */}
      <mesh position={[0, 0, 0.4]} rotation={[-0.2, 0, 0]}>
        <cylinderGeometry args={[0.5, 0.6, 0.05, 32, 1, false, 0, Math.PI]} />
        <meshStandardMaterial color={color} metalness={0.4} roughness={0.5} />
      </mesh>
      
      {/* Button on top */}
      <mesh position={[0, 0.5, 0]}>
        <sphereGeometry args={[0.08, 8, 8]} />
        <meshStandardMaterial color="#1f2937" metalness={0.6} roughness={0.3} />
      </mesh>
    </group>
  );
};
