import { useEffect, useMemo, useState } from "react";
import { Minus, Plus, Trash2 } from "lucide-react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  getCartItems,
  removeCartItem,
  type CartItem,
  updateCartItemQuantity,
} from "@/lib/cart";

const Cart = () => {
  const [items, setItems] = useState<CartItem[]>([]);

  const refreshCart = () => {
    setItems(getCartItems());
  };

  useEffect(() => {
    refreshCart();
  }, []);

  const subtotal = useMemo(
    () => items.reduce((sum, item) => sum + item.price * item.quantity, 0),
    [items]
  );

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main className="container mx-auto px-4 py-8 pt-24">
        <div className="max-w-5xl mx-auto">
          <h1 className="text-3xl font-bold mb-6">Shopping Cart</h1>

          {items.length === 0 ? (
            <Card>
              <CardContent className="py-10 text-center text-muted-foreground">
                Your cart is empty.
              </CardContent>
            </Card>
          ) : (
            <div className="space-y-4">
              {items.map((item, index) => (
                <Card key={`${item.id}-${item.selectedSize}-${item.selectedColor}-${index}`}>
                  <CardContent className="p-4 md:p-6">
                    <div className="flex flex-col gap-4 md:flex-row md:items-center">
                      <div className="w-full md:w-28 h-28 rounded-md overflow-hidden bg-muted flex-shrink-0">
                        {item.image ? (
                          <img
                            src={item.image}
                            alt={item.name}
                            className="h-full w-full object-cover"
                          />
                        ) : (
                          <div className="h-full w-full flex items-center justify-center text-xs text-muted-foreground">
                            No image
                          </div>
                        )}
                      </div>

                      <div className="flex-1">
                        <h2 className="text-lg font-semibold">{item.name}</h2>
                        <p className="text-sm text-muted-foreground mt-1">
                          Size: {item.selectedSize || "-"} | Color: {item.selectedColor || "-"}
                        </p>
                        <p className="font-semibold mt-2">₹{item.price.toFixed(2)}</p>
                      </div>

                      <div className="flex items-center gap-2">
                        <Button
                          variant="outline"
                          size="icon"
                          onClick={() => {
                            updateCartItemQuantity(index, item.quantity - 1);
                            refreshCart();
                          }}
                        >
                          <Minus className="h-4 w-4" />
                        </Button>
                        <span className="w-8 text-center">{item.quantity}</span>
                        <Button
                          variant="outline"
                          size="icon"
                          onClick={() => {
                            updateCartItemQuantity(index, item.quantity + 1);
                            refreshCart();
                          }}
                        >
                          <Plus className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => {
                            removeCartItem(index);
                            refreshCart();
                          }}
                        >
                          <Trash2 className="h-4 w-4 text-red-500" />
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}

              <Card>
                <CardContent className="p-6">
                  <div className="flex items-center justify-between text-lg font-semibold">
                    <span>Subtotal</span>
                    <span>₹{subtotal.toFixed(2)}</span>
                  </div>
                </CardContent>
              </Card>
            </div>
          )}
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default Cart;
