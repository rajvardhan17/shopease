import React from "react";

interface Product {
  id?: string;
  name?: string;
  price?: number;
  image?: string;
  category?: string;
  brand?: string;
  sizes?: string[];
}

interface Props {
  product?: Product;
}

const ProductCard3D: React.FC<Props> = ({ product }) => {
  // Safe defaults
  const {
    id = "0",
    name = "Unnamed Product",
    price = 0,
    image = "/default-shoe.jpg",
    category = "",
    brand = "",
    sizes = [],
  } = product || {};

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden">
      <img
        src={image}
        alt={name}
        className="w-full h-48 object-cover"
      />
      <div className="p-4">
        <h3 className="text-lg font-semibold">{name}</h3>
        {brand && <p className="text-sm text-muted-foreground">{brand}</p>}
        <p className="font-bold text-xl">${price}</p>
        {sizes.length > 0 && (
          <div className="flex gap-1 mt-2 flex-wrap">
            {sizes.map((size) => (
              <span key={size} className="text-xs border px-2 py-1 rounded">{size}</span>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default ProductCard3D;