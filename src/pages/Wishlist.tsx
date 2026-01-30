import { useState } from "react";
import { Heart, ShoppingBag, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { toast } from "sonner";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import jacketImage from "@/assets/jacket-leather.jpg";
import sneakerImage from "@/assets/sneaker-white.jpg";
import dressImage from "@/assets/dress-silk.jpg";
import beltImage from "@/assets/belt-product.jpg";

const mockWishlistItems = [
  {
    id: 1,
    name: "Premium Leather Jacket",
    price: 299.99,
    originalPrice: 399.99,
    image: jacketImage,
    category: "Outerwear",
    inStock: true
  },
  {
    id: 2,
    name: "Designer Sneakers",
    price: 179.99,
    originalPrice: 199.99,
    image: sneakerImage,
    category: "Shoes",
    inStock: true
  },
  {
    id: 3,
    name: "Silk Dress",
    price: 149.99,
    originalPrice: null,
    image: dressImage,
    category: "Women",
    inStock: false
  },
  {
    id: 4,
    name: "Leather Belt",
    price: 89.99,
    originalPrice: 119.99,
    image: beltImage,
    category: "Accessories",
    inStock: true
  }
];

const Wishlist = () => {
  const [wishlistItems, setWishlistItems] = useState(mockWishlistItems);

  const removeFromWishlist = (id: number) => {
    setWishlistItems(items => items.filter(item => item.id !== id));
    toast.success("Item removed from wishlist");
  };

  const addToBag = (item: any) => {
    toast.success(`${item.name} added to shopping bag!`);
  };

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <main className="container mx-auto px-4 py-8 pt-24">
        <div className="max-w-6xl mx-auto">
          <div className="flex items-center gap-3 mb-8">
            <Heart className="w-8 h-8 text-red-500 fill-current" />
            <h1 className="text-3xl font-bold">My Wishlist</h1>
            <span className="text-muted-foreground">({wishlistItems.length} items)</span>
          </div>
          
          {wishlistItems.length === 0 ? (
            <div className="text-center py-12">
              <Heart className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
              <h3 className="text-xl font-semibold mb-2">Your wishlist is empty</h3>
              <p className="text-muted-foreground mb-4">
                Save items you love for later by clicking the heart icon
              </p>
              <Button>Continue Shopping</Button>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {wishlistItems.map((item) => (
                <Card key={item.id} className="group overflow-hidden hover:shadow-lg transition-shadow">
                  <div className="relative">
                    <img
                      src={item.image}
                      alt={item.name}
                      className="w-full h-64 object-cover"
                    />
                    <Button
                      variant="ghost"
                      size="icon"
                      className="absolute top-2 right-2 bg-white/80 hover:bg-white"
                      onClick={() => removeFromWishlist(item.id)}
                    >
                      <X className="w-4 h-4" />
                    </Button>
                    {!item.inStock && (
                      <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                        <span className="text-white font-semibold">Out of Stock</span>
                      </div>
                    )}
                  </div>
                  
                  <CardContent className="p-4">
                    <div className="mb-2">
                      <span className="text-xs text-muted-foreground uppercase tracking-wide">
                        {item.category}
                      </span>
                    </div>
                    
                    <h3 className="font-semibold mb-2 line-clamp-2">{item.name}</h3>
                    
                    <div className="flex items-center gap-2 mb-4">
                      <span className="font-bold text-lg">${item.price}</span>
                      {item.originalPrice && (
                        <span className="text-sm text-muted-foreground line-through">
                          ${item.originalPrice}
                        </span>
                      )}
                    </div>
                    
                    <Button
                      className="w-full"
                      onClick={() => addToBag(item)}
                      disabled={!item.inStock}
                    >
                      <ShoppingBag className="w-4 h-4 mr-2" />
                      {item.inStock ? "Add to Bag" : "Out of Stock"}
                    </Button>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </main>
      
      <Footer />
    </div>
  );
};

export default Wishlist;