import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Switch } from "@/components/ui/switch";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
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
  Settings,
  Store,
  CreditCard,
  Truck,
  Users,
  Shield,
  Bell,
  Upload,
  Plus,
  Edit,
  Trash2
} from "lucide-react";
import { toast } from "sonner";

const mockAdminUsers = [
  { id: 1, name: "John Admin", email: "john@admin.com", role: "super_admin", status: "active", lastLogin: "2024-01-15" },
  { id: 2, name: "Jane Manager", email: "jane@admin.com", role: "manager", status: "active", lastLogin: "2024-01-14" },
  { id: 3, name: "Mike Support", email: "mike@admin.com", role: "support", status: "inactive", lastLogin: "2024-01-10" },
];

const AdminSettings = () => {
  const [adminUsers, setAdminUsers] = useState(mockAdminUsers);
  const [isAddUserDialogOpen, setIsAddUserDialogOpen] = useState(false);
  
  const [storeSettings, setStoreSettings] = useState({
    storeName: "Fashion Store",
    storeDescription: "Premium fashion and lifestyle products",
    contactEmail: "contact@fashionstore.com",
    contactPhone: "+1 (555) 123-4567",
    address: "123 Fashion St, Style City, FC 12345",
    currency: "USD",
    timezone: "America/New_York",
    logo: ""
  });

  const [newUser, setNewUser] = useState({
    name: "",
    email: "",
    role: "support"
  });

  const handleStoreSettingsChange = (field: string, value: string) => {
    setStoreSettings(prev => ({ ...prev, [field]: value }));
  };

  const handleSaveStoreSettings = () => {
    toast.success("Store settings updated successfully");
  };

  const handleAddUser = () => {
    const user = {
      id: adminUsers.length + 1,
      name: newUser.name,
      email: newUser.email,
      role: newUser.role,
      status: "active",
      lastLogin: "Never"
    };
    
    setAdminUsers([...adminUsers, user]);
    setNewUser({ name: "", email: "", role: "support" });
    setIsAddUserDialogOpen(false);
    toast.success("Admin user added successfully");
  };

  const handleDeleteUser = (id: number) => {
    setAdminUsers(adminUsers.filter(user => user.id !== id));
    toast.success("Admin user removed successfully");
  };

  const getRoleColor = (role: string) => {
    switch (role) {
      case "super_admin": return "default";
      case "manager": return "secondary";
      case "support": return "outline";
      default: return "outline";
    }
  };

  const getStatusColor = (status: string) => {
    return status === "active" ? "default" : "destructive";
  };

  return (
    <div className="space-y-8">
      <div>
        <h2 className="text-3xl font-bold tracking-tight">Settings</h2>
        <p className="text-muted-foreground">Manage your store configuration and admin users</p>
      </div>

      <Tabs defaultValue="store" className="space-y-4">
        <TabsList className="grid w-full grid-cols-5">
          <TabsTrigger value="store">Store</TabsTrigger>
          <TabsTrigger value="payments">Payments</TabsTrigger>
          <TabsTrigger value="shipping">Shipping</TabsTrigger>
          <TabsTrigger value="users">Admin Users</TabsTrigger>
          <TabsTrigger value="security">Security</TabsTrigger>
        </TabsList>

        {/* Store Settings */}
        <TabsContent value="store" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Store className="h-5 w-5" />
                Store Information
              </CardTitle>
              <CardDescription>Basic information about your store</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="storeName">Store Name</Label>
                  <Input
                    id="storeName"
                    value={storeSettings.storeName}
                    onChange={(e) => handleStoreSettingsChange("storeName", e.target.value)}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="currency">Currency</Label>
                  <Select value={storeSettings.currency} onValueChange={(value) => handleStoreSettingsChange("currency", value)}>
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="USD">USD - US Dollar</SelectItem>
                      <SelectItem value="EUR">EUR - Euro</SelectItem>
                      <SelectItem value="GBP">GBP - British Pound</SelectItem>
                      <SelectItem value="CAD">CAD - Canadian Dollar</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="storeDescription">Store Description</Label>
                <Textarea
                  id="storeDescription"
                  value={storeSettings.storeDescription}
                  onChange={(e) => handleStoreSettingsChange("storeDescription", e.target.value)}
                  rows={3}
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="contactEmail">Contact Email</Label>
                  <Input
                    id="contactEmail"
                    type="email"
                    value={storeSettings.contactEmail}
                    onChange={(e) => handleStoreSettingsChange("contactEmail", e.target.value)}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="contactPhone">Contact Phone</Label>
                  <Input
                    id="contactPhone"
                    value={storeSettings.contactPhone}
                    onChange={(e) => handleStoreSettingsChange("contactPhone", e.target.value)}
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="address">Store Address</Label>
                <Textarea
                  id="address"
                  value={storeSettings.address}
                  onChange={(e) => handleStoreSettingsChange("address", e.target.value)}
                  rows={2}
                />
              </div>

              <div className="space-y-2">
                <Label>Store Logo</Label>
                <div className="border-2 border-dashed border-muted-foreground/25 rounded-lg p-6 text-center">
                  <Upload className="mx-auto h-8 w-8 text-muted-foreground mb-2" />
                  <p className="text-sm text-muted-foreground">
                    Click to upload logo or drag and drop
                  </p>
                  <p className="text-xs text-muted-foreground mt-1">
                    PNG, JPG, SVG up to 2MB
                  </p>
                </div>
              </div>

              <Button onClick={handleSaveStoreSettings}>Save Store Settings</Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Payment Settings */}
        <TabsContent value="payments" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CreditCard className="h-5 w-5" />
                Payment Configuration
              </CardTitle>
              <CardDescription>Configure payment methods and processing</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">Stripe Integration</h4>
                    <p className="text-sm text-muted-foreground">Accept credit card payments</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">PayPal Integration</h4>
                    <p className="text-sm text-muted-foreground">Accept PayPal payments</p>
                  </div>
                  <Switch />
                </div>
                
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">Apple Pay</h4>
                    <p className="text-sm text-muted-foreground">Enable Apple Pay checkout</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">Google Pay</h4>
                    <p className="text-sm text-muted-foreground">Enable Google Pay checkout</p>
                  </div>
                  <Switch defaultChecked />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4 pt-4 border-t">
                <div className="space-y-2">
                  <Label htmlFor="taxRate">Tax Rate (%)</Label>
                  <Input id="taxRate" type="number" placeholder="8.25" />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="processingFee">Processing Fee (%)</Label>
                  <Input id="processingFee" type="number" placeholder="2.9" />
                </div>
              </div>

              <Button>Save Payment Settings</Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Shipping Settings */}
        <TabsContent value="shipping" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Truck className="h-5 w-5" />
                Shipping Configuration
              </CardTitle>
              <CardDescription>Configure shipping methods and rates</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">Free Shipping</h4>
                    <p className="text-sm text-muted-foreground">Offer free shipping on orders</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="freeShippingThreshold">Free Shipping Threshold ($)</Label>
                    <Input id="freeShippingThreshold" type="number" placeholder="75.00" />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="standardShipping">Standard Shipping Rate ($)</Label>
                    <Input id="standardShipping" type="number" placeholder="9.99" />
                  </div>
                </div>
                
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="expressShipping">Express Shipping Rate ($)</Label>
                    <Input id="expressShipping" type="number" placeholder="19.99" />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="handlingFee">Handling Fee ($)</Label>
                    <Input id="handlingFee" type="number" placeholder="2.50" />
                  </div>
                </div>
              </div>

              <div className="space-y-4 pt-4 border-t">
                <h4 className="font-medium">Shipping Zones</h4>
                <div className="space-y-2">
                  <div className="flex items-center justify-between p-3 bg-muted rounded-lg">
                    <span>Domestic (US)</span>
                    <Badge variant="default">Active</Badge>
                  </div>
                  <div className="flex items-center justify-between p-3 bg-muted rounded-lg">
                    <span>International</span>
                    <Badge variant="outline">Inactive</Badge>
                  </div>
                </div>
              </div>

              <Button>Save Shipping Settings</Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Admin Users */}
        <TabsContent value="users" className="space-y-4">
          <Card>
            <CardHeader>
              <div className="flex justify-between items-center">
                <div>
                  <CardTitle className="flex items-center gap-2">
                    <Users className="h-5 w-5" />
                    Admin Users
                  </CardTitle>
                  <CardDescription>Manage admin access and permissions</CardDescription>
                </div>
                
                <Dialog open={isAddUserDialogOpen} onOpenChange={setIsAddUserDialogOpen}>
                  <DialogTrigger asChild>
                    <Button>
                      <Plus className="mr-2 h-4 w-4" />
                      Add Admin
                    </Button>
                  </DialogTrigger>
                  <DialogContent>
                    <DialogHeader>
                      <DialogTitle>Add New Admin User</DialogTitle>
                      <DialogDescription>
                        Create a new admin user account
                      </DialogDescription>
                    </DialogHeader>
                    
                    <div className="grid gap-4 py-4">
                      <div className="space-y-2">
                        <Label htmlFor="newUserName">Full Name</Label>
                        <Input
                          id="newUserName"
                          value={newUser.name}
                          onChange={(e) => setNewUser({...newUser, name: e.target.value})}
                          placeholder="Enter full name"
                        />
                      </div>
                      
                      <div className="space-y-2">
                        <Label htmlFor="newUserEmail">Email</Label>
                        <Input
                          id="newUserEmail"
                          type="email"
                          value={newUser.email}
                          onChange={(e) => setNewUser({...newUser, email: e.target.value})}
                          placeholder="Enter email address"
                        />
                      </div>
                      
                      <div className="space-y-2">
                        <Label htmlFor="newUserRole">Role</Label>
                        <Select value={newUser.role} onValueChange={(value) => setNewUser({...newUser, role: value})}>
                          <SelectTrigger>
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="super_admin">Super Admin</SelectItem>
                            <SelectItem value="manager">Manager</SelectItem>
                            <SelectItem value="support">Support</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                    
                    <div className="flex justify-end gap-2">
                      <Button variant="outline" onClick={() => setIsAddUserDialogOpen(false)}>
                        Cancel
                      </Button>
                      <Button onClick={handleAddUser}>Add User</Button>
                    </div>
                  </DialogContent>
                </Dialog>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Name</TableHead>
                    <TableHead>Email</TableHead>
                    <TableHead>Role</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Last Login</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {adminUsers.map((user) => (
                    <TableRow key={user.id}>
                      <TableCell className="font-medium">{user.name}</TableCell>
                      <TableCell>{user.email}</TableCell>
                      <TableCell>
                        <Badge variant={getRoleColor(user.role)}>
                          {user.role.replace("_", " ")}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <Badge variant={getStatusColor(user.status)}>
                          {user.status}
                        </Badge>
                      </TableCell>
                      <TableCell>{user.lastLogin}</TableCell>
                      <TableCell className="text-right">
                        <div className="flex justify-end space-x-2">
                          <Button variant="ghost" size="icon">
                            <Edit className="h-4 w-4" />
                          </Button>
                          <Button 
                            variant="ghost" 
                            size="icon"
                            onClick={() => handleDeleteUser(user.id)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Security Settings */}
        <TabsContent value="security" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Shield className="h-5 w-5" />
                Security Settings
              </CardTitle>
              <CardDescription>Configure security and notification preferences</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">Two-Factor Authentication</h4>
                    <p className="text-sm text-muted-foreground">Require 2FA for admin login</p>
                  </div>
                  <Switch />
                </div>
                
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">Login Notifications</h4>
                    <p className="text-sm text-muted-foreground">Email notifications for admin logins</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">Order Notifications</h4>
                    <p className="text-sm text-muted-foreground">Email notifications for new orders</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-medium">Low Stock Alerts</h4>
                    <p className="text-sm text-muted-foreground">Notifications when products are low in stock</p>
                  </div>
                  <Switch defaultChecked />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4 pt-4 border-t">
                <div className="space-y-2">
                  <Label htmlFor="sessionTimeout">Session Timeout (minutes)</Label>
                  <Input id="sessionTimeout" type="number" placeholder="60" />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="maxLoginAttempts">Max Login Attempts</Label>
                  <Input id="maxLoginAttempts" type="number" placeholder="5" />
                </div>
              </div>

              <Button>Save Security Settings</Button>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
};

export default AdminSettings;