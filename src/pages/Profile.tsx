import { useState } from "react";
import { motion } from "framer-motion";
import { User, Package, MapPin, Settings, Camera, Save, ArrowLeft, LogOut } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";

const Profile = () => {
  const navigate = useNavigate();
  const { toast } = useToast();
  const [isEditing, setIsEditing] = useState(false);
  
  const [profile, setProfile] = useState({
    firstName: "John",
    lastName: "Doe",
    email: "john.doe@example.com",
    phone: "+1 (555) 123-4567",
    avatar: "",
  });

  const mockOrders = [
    {
      id: "ORD-001",
      date: "2024-01-15",
      status: "Delivered",
      total: 299.99,
      items: 3,
    },
    {
      id: "ORD-002",
      date: "2024-01-10",
      status: "Shipped",
      total: 149.50,
      items: 2,
    },
    {
      id: "ORD-003",
      date: "2024-01-05",
      status: "Processing",
      total: 89.99,
      items: 1,
    },
  ];

  const mockAddresses = [
    {
      id: 1,
      type: "Home",
      name: "John Doe",
      street: "123 Main Street",
      city: "New York",
      state: "NY",
      zip: "10001",
      isDefault: true,
    },
    {
      id: 2,
      type: "Work",
      name: "John Doe",
      street: "456 Business Ave",
      city: "New York",
      state: "NY",
      zip: "10002",
      isDefault: false,
    },
  ];

  const handleSaveProfile = () => {
    toast({
      title: "Profile Updated",
      description: "Your profile information has been saved successfully.",
    });
    setIsEditing(false);
  };

  const handleLogout = () => {
    toast({
      title: "Logged Out",
      description: "You have been successfully logged out.",
    });
    setTimeout(() => {
      navigate("/");
    }, 1000);
  };

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case "delivered":
        return "bg-green-500/10 text-green-700 border-green-500/20";
      case "shipped":
        return "bg-blue-500/10 text-blue-700 border-blue-500/20";
      case "processing":
        return "bg-yellow-500/10 text-yellow-700 border-yellow-500/20";
      default:
        return "bg-muted text-muted-foreground";
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-primary/5 to-secondary/5">
      {/* Header */}
      <div className="bg-background/80 backdrop-blur-sm border-b sticky top-0 z-10">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              size="icon"
              onClick={() => navigate("/home")}
            >
              <ArrowLeft className="h-5 w-5" />
            </Button>
            <div>
              <h1 className="text-2xl font-bold">My Profile</h1>
              <p className="text-sm text-muted-foreground">Manage your account settings</p>
            </div>
          </div>
        </div>
      </div>

      {/* Profile Content */}
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* Sidebar */}
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            className="lg:col-span-1"
          >
            <Card>
              <CardContent className="p-6">
                <div className="flex flex-col items-center text-center space-y-4">
                  <div className="relative group">
                    <Avatar className="h-24 w-24">
                      <AvatarImage src={profile.avatar} />
                      <AvatarFallback className="text-2xl bg-primary/10 text-primary">
                        {profile.firstName[0]}{profile.lastName[0]}
                      </AvatarFallback>
                    </Avatar>
                    <Button
                      size="icon"
                      variant="secondary"
                      className="absolute bottom-0 right-0 h-8 w-8 rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
                    >
                      <Camera className="h-4 w-4" />
                    </Button>
                  </div>
                  <div>
                    <h3 className="font-semibold text-lg">
                      {profile.firstName} {profile.lastName}
                    </h3>
                    <p className="text-sm text-muted-foreground">{profile.email}</p>
                  </div>
                  <Badge variant="secondary" className="bg-primary/10 text-primary">
                    Premium Member
                  </Badge>
                </div>

                <Separator className="my-6" />

                <div className="space-y-2">
                  <div className="flex justify-between text-sm">
                    <span className="text-muted-foreground">Total Orders</span>
                    <span className="font-semibold">{mockOrders.length}</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-muted-foreground">Wishlist Items</span>
                    <span className="font-semibold">12</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-muted-foreground">Member Since</span>
                    <span className="font-semibold">2023</span>
                  </div>
                </div>
              </CardContent>
            </Card>
          </motion.div>

          {/* Main Content */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
            className="lg:col-span-3"
          >
            <Tabs defaultValue="info" className="w-full">
              <TabsList className="grid w-full grid-cols-4">
                <TabsTrigger value="info" className="gap-2">
                  <User className="h-4 w-4" />
                  <span className="hidden sm:inline">Personal Info</span>
                </TabsTrigger>
                <TabsTrigger value="orders" className="gap-2">
                  <Package className="h-4 w-4" />
                  <span className="hidden sm:inline">Orders</span>
                </TabsTrigger>
                <TabsTrigger value="addresses" className="gap-2">
                  <MapPin className="h-4 w-4" />
                  <span className="hidden sm:inline">Addresses</span>
                </TabsTrigger>
                <TabsTrigger value="settings" className="gap-2">
                  <Settings className="h-4 w-4" />
                  <span className="hidden sm:inline">Settings</span>
                </TabsTrigger>
              </TabsList>

              {/* Personal Info Tab */}
              <TabsContent value="info">
                <Card>
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <div>
                        <CardTitle>Personal Information</CardTitle>
                        <CardDescription>Update your personal details</CardDescription>
                      </div>
                      {!isEditing ? (
                        <Button onClick={() => setIsEditing(true)} variant="outline">
                          Edit Profile
                        </Button>
                      ) : (
                        <div className="flex gap-2">
                          <Button onClick={() => setIsEditing(false)} variant="outline">
                            Cancel
                          </Button>
                          <Button onClick={handleSaveProfile} className="gap-2">
                            <Save className="h-4 w-4" />
                            Save Changes
                          </Button>
                        </div>
                      )}
                    </div>
                  </CardHeader>
                  <CardContent className="space-y-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div className="space-y-2">
                        <Label htmlFor="firstName">First Name</Label>
                        <Input
                          id="firstName"
                          value={profile.firstName}
                          onChange={(e) => setProfile({ ...profile, firstName: e.target.value })}
                          disabled={!isEditing}
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="lastName">Last Name</Label>
                        <Input
                          id="lastName"
                          value={profile.lastName}
                          onChange={(e) => setProfile({ ...profile, lastName: e.target.value })}
                          disabled={!isEditing}
                        />
                      </div>
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="email">Email Address</Label>
                      <Input
                        id="email"
                        type="email"
                        value={profile.email}
                        onChange={(e) => setProfile({ ...profile, email: e.target.value })}
                        disabled={!isEditing}
                      />
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="phone">Phone Number</Label>
                      <Input
                        id="phone"
                        type="tel"
                        value={profile.phone}
                        onChange={(e) => setProfile({ ...profile, phone: e.target.value })}
                        disabled={!isEditing}
                      />
                    </div>
                  </CardContent>
                </Card>
              </TabsContent>

              {/* Orders Tab */}
              <TabsContent value="orders">
                <Card>
                  <CardHeader>
                    <CardTitle>Order History</CardTitle>
                    <CardDescription>View and track your orders</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      {mockOrders.map((order) => (
                        <motion.div
                          key={order.id}
                          whileHover={{ scale: 1.01 }}
                          className="border rounded-lg p-4 hover:border-primary/50 transition-colors"
                        >
                          <div className="flex items-center justify-between">
                            <div className="space-y-1">
                              <div className="flex items-center gap-3">
                                <h4 className="font-semibold">{order.id}</h4>
                                <Badge className={getStatusColor(order.status)}>
                                  {order.status}
                                </Badge>
                              </div>
                              <p className="text-sm text-muted-foreground">
                                Ordered on {new Date(order.date).toLocaleDateString()}
                              </p>
                              <p className="text-sm">
                                {order.items} item{order.items > 1 ? "s" : ""}
                              </p>
                            </div>
                            <div className="text-right space-y-2">
                              <p className="text-lg font-bold">${order.total.toFixed(2)}</p>
                              <Button variant="outline" size="sm">
                                View Details
                              </Button>
                            </div>
                          </div>
                        </motion.div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              </TabsContent>

              {/* Addresses Tab */}
              <TabsContent value="addresses">
                <Card>
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <div>
                        <CardTitle>Saved Addresses</CardTitle>
                        <CardDescription>Manage your delivery addresses</CardDescription>
                      </div>
                      <Button>Add New Address</Button>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      {mockAddresses.map((address) => (
                        <motion.div
                          key={address.id}
                          whileHover={{ scale: 1.02 }}
                          className="border rounded-lg p-4 space-y-3 hover:border-primary/50 transition-colors"
                        >
                          <div className="flex items-start justify-between">
                            <div>
                              <div className="flex items-center gap-2">
                                <h4 className="font-semibold">{address.type}</h4>
                                {address.isDefault && (
                                  <Badge variant="secondary" className="text-xs">
                                    Default
                                  </Badge>
                                )}
                              </div>
                              <p className="text-sm font-medium mt-2">{address.name}</p>
                            </div>
                            <Button variant="ghost" size="sm">
                              Edit
                            </Button>
                          </div>
                          <div className="text-sm text-muted-foreground space-y-1">
                            <p>{address.street}</p>
                            <p>
                              {address.city}, {address.state} {address.zip}
                            </p>
                          </div>
                        </motion.div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              </TabsContent>

              {/* Settings Tab */}
              <TabsContent value="settings">
                <Card>
                  <CardHeader>
                    <CardTitle>Account Settings</CardTitle>
                    <CardDescription>Manage your account preferences</CardDescription>
                  </CardHeader>
                  <CardContent className="space-y-6">
                    <div className="space-y-4">
                      <div>
                        <h4 className="font-semibold mb-3">Password</h4>
                        <Button variant="outline">Change Password</Button>
                      </div>

                      <Separator />

                      <div>
                        <h4 className="font-semibold mb-3">Session</h4>
                        <Button 
                          variant="outline" 
                          className="w-full flex items-center gap-2"
                          onClick={handleLogout}
                        >
                          <LogOut className="h-4 w-4" />
                          Logout
                        </Button>
                      </div>

                      <Separator />

                      <div>
                        <h4 className="font-semibold mb-3">Notifications</h4>
                        <div className="space-y-3">
                          <label className="flex items-center justify-between cursor-pointer">
                            <span className="text-sm">Email notifications</span>
                            <input type="checkbox" defaultChecked className="toggle" />
                          </label>
                          <label className="flex items-center justify-between cursor-pointer">
                            <span className="text-sm">Order updates</span>
                            <input type="checkbox" defaultChecked className="toggle" />
                          </label>
                          <label className="flex items-center justify-between cursor-pointer">
                            <span className="text-sm">Promotional emails</span>
                            <input type="checkbox" className="toggle" />
                          </label>
                        </div>
                      </div>

                      <Separator />

                      <div>
                        <h4 className="font-semibold mb-3 text-destructive">Danger Zone</h4>
                        <Button variant="destructive" className="w-full">
                          Delete Account
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </TabsContent>
            </Tabs>
          </motion.div>
        </div>
      </div>
    </div>
  );
};

export default Profile;