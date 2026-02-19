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
  Mail,
  Phone,
  MapPin,
  Calendar,
  DollarSign,
  ShoppingBag,
  Filter
} from "lucide-react";

const mockCustomers = [
  {
    id: 1,
    name: "John Doe",
    email: "john@example.com",
    phone: "+1 (555) 123-4567",
    address: "123 Main St, City, State 12345",
    joinDate: "2023-06-15",
    totalOrders: 12,
    totalSpent: 1249.88,
    status: "active",
    lastOrder: "2024-01-15",
    orderHistory: [
      { id: "ORD-001", date: "2024-01-15", total: 149.99, status: "completed" },
      { id: "ORD-002", date: "2024-01-10", total: 89.50, status: "completed" },
      { id: "ORD-003", date: "2024-01-05", total: 199.99, status: "shipped" }
    ]
  },
  {
    id: 2,
    name: "Jane Smith",
    email: "jane@example.com",
    phone: "+1 (555) 987-6543",
    address: "456 Oak Ave, City, State 67890",
    joinDate: "2023-08-22",
    totalOrders: 8,
    totalSpent: 756.42,
    status: "active",
    lastOrder: "2024-01-16",
    orderHistory: [
      { id: "ORD-004", date: "2024-01-16", total: 79.99, status: "processing" },
      { id: "ORD-005", date: "2024-01-08", total: 129.99, status: "completed" }
    ]
  },
  {
    id: 3,
    name: "Mike Johnson",
    email: "mike@example.com",
    phone: "+1 (555) 456-7890",
    address: "789 Pine Rd, City, State 54321",
    joinDate: "2023-04-10",
    totalOrders: 15,
    totalSpent: 2156.78,
    status: "vip",
    lastOrder: "2024-01-14",
    orderHistory: [
      { id: "ORD-006", date: "2024-01-14", total: 299.99, status: "delivered" },
      { id: "ORD-007", date: "2024-01-12", total: 189.99, status: "completed" }
    ]
  },
  {
    id: 4,
    name: "Sarah Wilson",
    email: "sarah@example.com",
    phone: "+1 (555) 321-0987",
    address: "321 Elm St, City, State 98765",
    joinDate: "2023-11-30",
    totalOrders: 3,
    totalSpent: 234.56,
    status: "new",
    lastOrder: "2024-01-12",
    orderHistory: [
      { id: "ORD-008", date: "2024-01-12", total: 79.99, status: "delivered" }
    ]
  }
];

const AdminCustomers = () => {
  const [customers, setCustomers] = useState(mockCustomers);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("all");
  const [selectedCustomer, setSelectedCustomer] = useState<any>(null);

  const filteredCustomers = customers.filter(customer => {
    const matchesSearch = customer.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         customer.email.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === "all" || customer.status === filterStatus;
    return matchesSearch && matchesStatus;
  });

  const getStatusColor = (status: string) => {
    switch (status) {
      case "active": return "default";
      case "vip": return "default";
      case "new": return "secondary";
      case "inactive": return "outline";
      default: return "secondary";
    }
  };

  const getCustomerStats = () => {
    return {
      total: customers.length,
      active: customers.filter(c => c.status === "active").length,
      vip: customers.filter(c => c.status === "vip").length,
      new: customers.filter(c => c.status === "new").length
    };
  };

  const stats = getCustomerStats();

  return (
    <div className="space-y-8">
      <div>
        <h2 className="text-3xl font-bold tracking-tight">Customers</h2>
        <p className="text-muted-foreground">Manage your customer base and relationships</p>
      </div>

      {/* Customer Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Customers</CardTitle>
            <ShoppingBag className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.total}</div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Active</CardTitle>
            <ShoppingBag className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.active}</div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">VIP Customers</CardTitle>
            <DollarSign className="h-4 w-4 text-yellow-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.vip}</div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">New This Month</CardTitle>
            <Calendar className="h-4 w-4 text-blue-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.new}</div>
          </CardContent>
        </Card>
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle>Filter Customers</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4 items-center">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Search customers..."
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
                <SelectItem value="all">All Customers</SelectItem>
                <SelectItem value="active">Active</SelectItem>
                <SelectItem value="vip">VIP</SelectItem>
                <SelectItem value="new">New</SelectItem>
                <SelectItem value="inactive">Inactive</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Customers Table */}
      <Card>
        <CardHeader>
          <CardTitle>Customer List</CardTitle>
          <CardDescription>
            {filteredCustomers.length} customers found
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Customer</TableHead>
                <TableHead>Join Date</TableHead>
                <TableHead>Orders</TableHead>
                <TableHead>Total Spent</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredCustomers.map((customer) => (
                <TableRow key={customer.id}>
                  <TableCell>
                    <div>
                      <div className="font-medium">{customer.name}</div>
                      <div className="text-sm text-muted-foreground">{customer.email}</div>
                    </div>
                  </TableCell>
                  <TableCell>{customer.joinDate}</TableCell>
                  <TableCell>{customer.totalOrders}</TableCell>
                  <TableCell>${customer.totalSpent.toFixed(2)}</TableCell>
                  <TableCell>
                    <Badge variant={getStatusColor(customer.status)}>
                      {customer.status.toUpperCase()}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end space-x-2">
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="ghost" size="icon" onClick={() => setSelectedCustomer(customer)}>
                            <Eye className="h-4 w-4" />
                          </Button>
                        </DialogTrigger>
                        <DialogContent className="max-w-2xl">
                          <DialogHeader>
                            <DialogTitle>Customer Profile - {selectedCustomer?.name}</DialogTitle>
                            <DialogDescription>
                              Complete customer information and order history
                            </DialogDescription>
                          </DialogHeader>
                          
                          {selectedCustomer && (
                            <div className="space-y-6">
                              {/* Customer Info */}
                              <div className="grid gap-4 md:grid-cols-2">
                                <div>
                                  <h4 className="font-semibold mb-2">Contact Information</h4>
                                  <div className="space-y-2 text-sm">
                                    <div className="flex items-center gap-2">
                                      <Mail className="h-4 w-4 text-muted-foreground" />
                                      {selectedCustomer.email}
                                    </div>
                                    <div className="flex items-center gap-2">
                                      <Phone className="h-4 w-4 text-muted-foreground" />
                                      {selectedCustomer.phone}
                                    </div>
                                    <div className="flex items-center gap-2">
                                      <MapPin className="h-4 w-4 text-muted-foreground" />
                                      {selectedCustomer.address}
                                    </div>
                                  </div>
                                </div>
                                
                                <div>
                                  <h4 className="font-semibold mb-2">Account Summary</h4>
                                  <div className="space-y-2 text-sm">
                                    <div className="flex justify-between">
                                      <span>Join Date:</span>
                                      <span>{selectedCustomer.joinDate}</span>
                                    </div>
                                    <div className="flex justify-between">
                                      <span>Total Orders:</span>
                                      <span>{selectedCustomer.totalOrders}</span>
                                    </div>
                                    <div className="flex justify-between">
                                      <span>Total Spent:</span>
                                      <span className="font-medium">${selectedCustomer.totalSpent.toFixed(2)}</span>
                                    </div>
                                    <div className="flex justify-between">
                                      <span>Status:</span>
                                      <Badge variant={getStatusColor(selectedCustomer.status)}>
                                        {selectedCustomer.status.toUpperCase()}
                                      </Badge>
                                    </div>
                                  </div>
                                </div>
                              </div>

                              {/* Order History */}
                              <div>
                                <h4 className="font-semibold mb-2">Recent Orders</h4>
                                <div className="space-y-2">
                                  {selectedCustomer.orderHistory.map((order: any) => (
                                    <div key={order.id} className="flex justify-between items-center p-3 bg-muted rounded-lg">
                                      <div>
                                        <div className="font-medium">{order.id}</div>
                                        <div className="text-sm text-muted-foreground">{order.date}</div>
                                      </div>
                                      <div className="text-right">
                                        <div className="font-medium">${order.total}</div>
                                        <Badge variant={getStatusColor(order.status)} className="text-xs">
                                          {order.status}
                                        </Badge>
                                      </div>
                                    </div>
                                  ))}
                                </div>
                              </div>

                              {/* Actions */}
                              <div className="flex gap-2 pt-4 border-t">
                                <Button variant="outline" className="flex-1">
                                  <Mail className="mr-2 h-4 w-4" />
                                  Send Email
                                </Button>
                                <Button variant="outline" className="flex-1">
                                  <Eye className="mr-2 h-4 w-4" />
                                  View All Orders
                                </Button>
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

export default AdminCustomers;