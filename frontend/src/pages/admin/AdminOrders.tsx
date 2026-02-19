import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Search,
  Eye,
  Package,
  Truck,
  CheckCircle,
  XCircle,
  Download,
  Filter
} from "lucide-react";
import { toast } from "sonner";

const mockOrders = [
  {
    id: "ORD-2024-001",
    customer: "John Doe",
    email: "john@example.com",
    total: 149.99,
    status: "pending",
    date: "2024-01-15",
    items: [
      { name: "Classic T-Shirt", quantity: 2, price: 19.99 },
      { name: "Running Shoes", quantity: 1, price: 89.99 }
    ],
    shipping: {
      address: "123 Main St, City, State 12345",
      method: "Standard Shipping"
    }
  },
  {
    id: "ORD-2024-002",
    customer: "Jane Smith",
    email: "jane@example.com",
    total: 89.50,
    status: "processing",
    date: "2024-01-16",
    items: [
      { name: "Denim Jacket", quantity: 1, price: 79.99 }
    ],
    shipping: {
      address: "456 Oak Ave, City, State 67890",
      method: "Express Shipping"
    }
  },
  {
    id: "ORD-2024-003",
    customer: "Mike Johnson",
    email: "mike@example.com",
    total: 199.99,
    status: "shipped",
    date: "2024-01-14",
    items: [
      { name: "Leather Belt", quantity: 1, price: 29.99 },
      { name: "Running Shoes", quantity: 2, price: 89.99 }
    ],
    shipping: {
      address: "789 Pine Rd, City, State 54321",
      method: "Standard Shipping"
    }
  },
  {
    id: "ORD-2024-004",
    customer: "Sarah Wilson",
    email: "sarah@example.com",
    total: 79.99,
    status: "delivered",
    date: "2024-01-12",
    items: [
      { name: "Classic T-Shirt", quantity: 4, price: 19.99 }
    ],
    shipping: {
      address: "321 Elm St, City, State 98765",
      method: "Express Shipping"
    }
  }
];

const AdminOrders = () => {
  const [orders, setOrders] = useState(mockOrders);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("all");
  const [selectedOrder, setSelectedOrder] = useState<any>(null);

  const filteredOrders = orders.filter(order => {
    const matchesSearch = order.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         order.customer.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === "all" || order.status === filterStatus;
    return matchesSearch && matchesStatus;
  });

  const handleStatusChange = (orderId: string, newStatus: string) => {
    setOrders(orders.map(order => 
      order.id === orderId ? { ...order, status: newStatus } : order
    ));
    toast.success(`Order ${orderId} status updated to ${newStatus}`);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "pending": return "secondary";
      case "processing": return "default";
      case "shipped": return "outline";
      case "delivered": return "default";
      case "cancelled": return "destructive";
      default: return "secondary";
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "pending": return <Package className="h-4 w-4" />;
      case "processing": return <Package className="h-4 w-4" />;
      case "shipped": return <Truck className="h-4 w-4" />;
      case "delivered": return <CheckCircle className="h-4 w-4" />;
      case "cancelled": return <XCircle className="h-4 w-4" />;
      default: return <Package className="h-4 w-4" />;
    }
  };

  return (
    <div className="space-y-8">
      <div>
        <h2 className="text-3xl font-bold tracking-tight">Orders</h2>
        <p className="text-muted-foreground">Manage customer orders and track shipments</p>
      </div>

      {/* Order Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Orders</CardTitle>
            <Package className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{orders.length}</div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Pending</CardTitle>
            <Package className="h-4 w-4 text-yellow-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {orders.filter(o => o.status === "pending").length}
            </div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Shipped</CardTitle>
            <Truck className="h-4 w-4 text-blue-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {orders.filter(o => o.status === "shipped").length}
            </div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Delivered</CardTitle>
            <CheckCircle className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {orders.filter(o => o.status === "delivered").length}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle>Filter Orders</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4 items-center">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Search orders..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            
            <Select value={filterStatus} onValueChange={setFilterStatus}>
              <SelectTrigger className="w-48">
                <Filter className="mr-2 h-4 w-4" />
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Status</SelectItem>
                <SelectItem value="pending">Pending</SelectItem>
                <SelectItem value="processing">Processing</SelectItem>
                <SelectItem value="shipped">Shipped</SelectItem>
                <SelectItem value="delivered">Delivered</SelectItem>
                <SelectItem value="cancelled">Cancelled</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Orders Table */}
      <Card>
        <CardHeader>
          <CardTitle>Order List</CardTitle>
          <CardDescription>
            {filteredOrders.length} orders found
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Order ID</TableHead>
                <TableHead>Customer</TableHead>
                <TableHead>Date</TableHead>
                <TableHead>Total</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredOrders.map((order) => (
                <TableRow key={order.id}>
                  <TableCell className="font-medium">{order.id}</TableCell>
                  <TableCell>
                    <div>
                      <div className="font-medium">{order.customer}</div>
                      <div className="text-sm text-muted-foreground">{order.email}</div>
                    </div>
                  </TableCell>
                  <TableCell>{order.date}</TableCell>
                  <TableCell>${order.total}</TableCell>
                  <TableCell>
                    <Badge variant={getStatusColor(order.status)} className="flex items-center gap-1 w-fit">
                      {getStatusIcon(order.status)}
                      {order.status}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end space-x-2">
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="ghost" size="icon" onClick={() => setSelectedOrder(order)}>
                            <Eye className="h-4 w-4" />
                          </Button>
                        </DialogTrigger>
                        <DialogContent className="max-w-2xl">
                          <DialogHeader>
                            <DialogTitle>Order Details - {selectedOrder?.id}</DialogTitle>
                            <DialogDescription>
                              Complete order information and management
                            </DialogDescription>
                          </DialogHeader>
                          
                          {selectedOrder && (
                            <div className="space-y-6">
                              {/* Customer Info */}
                              <div>
                                <h4 className="font-semibold mb-2">Customer Information</h4>
                                <div className="bg-muted p-3 rounded-lg space-y-1">
                                  <p><strong>Name:</strong> {selectedOrder.customer}</p>
                                  <p><strong>Email:</strong> {selectedOrder.email}</p>
                                  <p><strong>Order Date:</strong> {selectedOrder.date}</p>
                                </div>
                              </div>

                              {/* Order Items */}
                              <div>
                                <h4 className="font-semibold mb-2">Order Items</h4>
                                <div className="space-y-2">
                                  {selectedOrder.items.map((item: any, index: number) => (
                                    <div key={index} className="flex justify-between items-center p-2 bg-muted rounded">
                                      <span>{item.name} x {item.quantity}</span>
                                      <span>${(item.price * item.quantity).toFixed(2)}</span>
                                    </div>
                                  ))}
                                  <div className="border-t pt-2 font-semibold">
                                    Total: ${selectedOrder.total}
                                  </div>
                                </div>
                              </div>

                              {/* Shipping Info */}
                              <div>
                                <h4 className="font-semibold mb-2">Shipping Information</h4>
                                <div className="bg-muted p-3 rounded-lg space-y-1">
                                  <p><strong>Address:</strong> {selectedOrder.shipping.address}</p>
                                  <p><strong>Method:</strong> {selectedOrder.shipping.method}</p>
                                </div>
                              </div>

                              {/* Status Update */}
                              <div>
                                <h4 className="font-semibold mb-2">Update Status</h4>
                                <div className="flex gap-2">
                                  <Select 
                                    value={selectedOrder.status} 
                                    onValueChange={(value) => handleStatusChange(selectedOrder.id, value)}
                                  >
                                    <SelectTrigger className="w-48">
                                      <SelectValue />
                                    </SelectTrigger>
                                    <SelectContent>
                                      <SelectItem value="pending">Pending</SelectItem>
                                      <SelectItem value="processing">Processing</SelectItem>
                                      <SelectItem value="shipped">Shipped</SelectItem>
                                      <SelectItem value="delivered">Delivered</SelectItem>
                                      <SelectItem value="cancelled">Cancelled</SelectItem>
                                    </SelectContent>
                                  </Select>
                                  <Button variant="outline">
                                    <Download className="mr-2 h-4 w-4" />
                                    Generate Invoice
                                  </Button>
                                </div>
                              </div>
                            </div>
                          )}
                        </DialogContent>
                      </Dialog>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
};

export default AdminOrders;