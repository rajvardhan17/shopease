import { useState } from "react";
import { Search, Package, Truck, CheckCircle } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

const mockOrders = [
  {
    id: "ORD-2024-001",
    date: "2024-01-15",
    status: "delivered",
    total: 149.99,
    items: [
      { name: "Premium T-Shirt", quantity: 2, price: 49.99 },
      { name: "Classic Jeans", quantity: 1, price: 89.99 }
    ],
    tracking: "TRK123456789",
    estimatedDelivery: "2024-01-20"
  },
  {
    id: "ORD-2024-002",
    date: "2024-01-20",
    status: "shipped",
    total: 79.99,
    items: [
      { name: "Running Shoes", quantity: 1, price: 79.99 }
    ],
    tracking: "TRK987654321",
    estimatedDelivery: "2024-01-25"
  },
  {
    id: "ORD-2024-003",
    date: "2024-01-22",
    status: "processing",
    total: 199.99,
    items: [
      { name: "Winter Jacket", quantity: 1, price: 199.99 }
    ],
    tracking: null,
    estimatedDelivery: "2024-01-30"
  }
];

const statusColors = {
  processing: "bg-yellow-500",
  shipped: "bg-blue-500",
  delivered: "bg-green-500",
  cancelled: "bg-red-500"
};

const statusIcons = {
  processing: Package,
  shipped: Truck,
  delivered: CheckCircle,
  cancelled: Package
};

const Orders = () => {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredOrders = mockOrders.filter(order =>
    order.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
    order.tracking?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <main className="container mx-auto px-4 py-8 pt-24">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-3xl font-bold mb-8">Order Tracking</h1>
          
          <div className="mb-6">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                placeholder="Search by order ID or tracking number..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>

          <div className="space-y-6">
            {filteredOrders.map((order) => {
              const StatusIcon = statusIcons[order.status as keyof typeof statusIcons];
              
              return (
                <Card key={order.id} className="overflow-hidden">
                  <CardHeader className="pb-4">
                    <div className="flex justify-between items-start">
                      <div>
                        <CardTitle className="text-xl">{order.id}</CardTitle>
                        <p className="text-muted-foreground">Ordered on {order.date}</p>
                      </div>
                      <div className="text-right">
                        <div className="flex items-center gap-2 mb-2">
                          <StatusIcon className="w-4 h-4" />
                          <Badge className={`${statusColors[order.status as keyof typeof statusColors]} text-white`}>
                            {order.status.charAt(0).toUpperCase() + order.status.slice(1)}
                          </Badge>
                        </div>
                        <p className="font-semibold">${order.total}</p>
                      </div>
                    </div>
                  </CardHeader>
                  
                  <CardContent className="space-y-4">
                    <div>
                      <h4 className="font-semibold mb-2">Items:</h4>
                      <div className="space-y-1">
                        {order.items.map((item, index) => (
                          <div key={index} className="flex justify-between text-sm">
                            <span>{item.name} (x{item.quantity})</span>
                            <span>${item.price}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                    
                    {order.tracking && (
                      <div className="border-t pt-4">
                        <div className="flex justify-between text-sm">
                          <span className="font-medium">Tracking Number:</span>
                          <span className="font-mono">{order.tracking}</span>
                        </div>
                        <div className="flex justify-between text-sm mt-1">
                          <span className="font-medium">Estimated Delivery:</span>
                          <span>{order.estimatedDelivery}</span>
                        </div>
                      </div>
                    )}
                  </CardContent>
                </Card>
              );
            })}
          </div>
          
          {filteredOrders.length === 0 && (
            <div className="text-center py-12">
              <Package className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
              <h3 className="text-xl font-semibold mb-2">No orders found</h3>
              <p className="text-muted-foreground">
                {searchTerm ? "Try adjusting your search terms" : "You haven't placed any orders yet"}
              </p>
            </div>
          )}
        </div>
      </main>
      
      <Footer />
    </div>
  );
};

export default Orders;