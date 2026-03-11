import { useState, useEffect } from "react";
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

interface Order {
  id: string;
  date: string;
  status: string;
  total: number;
  items: number;
}

interface Address {
  id: number;
  type: string;
  name: string;
  street: string;
  city: string;
  state: string;
  zip: string;
  isDefault: boolean;
}

const Profile = () => {
  const navigate = useNavigate();
  const { toast } = useToast();

  const [isEditing, setIsEditing] = useState(false);

  const [profile, setProfile] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    avatar: "",
  });

  // Load profile from localStorage
  useEffect(() => {
    const storedUser = localStorage.getItem("user");

    if (storedUser) {
      const user = JSON.parse(storedUser);

      setProfile({
        firstName: user.firstName || "",
        lastName: user.lastName || "",
        email: user.email || "",
        phone: user.phone || "",
        avatar: user.avatar || "",
      });
    }
  }, []);

  const mockOrders: Order[] = [
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
      total: 149.5,
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

  const mockAddresses: Address[] = [
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
  ];

  const handleSaveProfile = () => {
    localStorage.setItem("user", JSON.stringify(profile));

    toast({
      title: "Profile Updated",
      description: "Your profile information has been saved successfully.",
    });

    setIsEditing(false);
  };

  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");

    toast({
      title: "Logged Out",
      description: "You have been successfully logged out.",
    });

    setTimeout(() => {
      navigate("/login");
    }, 800);
  };

  const handleAvatarUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];

    if (!file) return;

    const reader = new FileReader();

    reader.onload = () => {
      setProfile({ ...profile, avatar: reader.result as string });
    };

    reader.readAsDataURL(file);
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
            <Button variant="ghost" size="icon" onClick={() => navigate("/home")}>
              <ArrowLeft className="h-5 w-5" />
            </Button>

            <div>
              <h1 className="text-2xl font-bold">My Profile</h1>
              <p className="text-sm text-muted-foreground">
                Manage your account settings
              </p>
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
                        {profile.firstName?.[0]}
                        {profile.lastName?.[0]}
                      </AvatarFallback>
                    </Avatar>

                    <label className="absolute bottom-0 right-0 cursor-pointer">
                      <input
                        type="file"
                        accept="image/*"
                        hidden
                        onChange={handleAvatarUpload}
                      />
                      <div className="bg-primary text-white rounded-full p-2">
                        <Camera className="h-4 w-4" />
                      </div>
                    </label>

                  </div>

                  <div>
                    <h3 className="font-semibold text-lg">
                      {profile.firstName} {profile.lastName}
                    </h3>
                    <p className="text-sm text-muted-foreground">
                      {profile.email}
                    </p>
                  </div>

                  <Badge variant="secondary" className="bg-primary/10 text-primary">
                    Premium Member
                  </Badge>

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

            <Tabs defaultValue="info">

              <TabsList className="grid w-full grid-cols-4">
                <TabsTrigger value="info"><User className="h-4 w-4 mr-1"/>Info</TabsTrigger>
                <TabsTrigger value="orders"><Package className="h-4 w-4 mr-1"/>Orders</TabsTrigger>
                <TabsTrigger value="addresses"><MapPin className="h-4 w-4 mr-1"/>Addresses</TabsTrigger>
                <TabsTrigger value="settings"><Settings className="h-4 w-4 mr-1"/>Settings</TabsTrigger>
              </TabsList>

              {/* Personal Info */}
              <TabsContent value="info">
                <Card>

                  <CardHeader>
                    <div className="flex justify-between items-center">
                      <CardTitle>Personal Information</CardTitle>

                      {!isEditing ? (
                        <Button onClick={() => setIsEditing(true)}>Edit</Button>
                      ) : (
                        <div className="flex gap-2">
                          <Button variant="outline" onClick={() => setIsEditing(false)}>Cancel</Button>
                          <Button onClick={handleSaveProfile}>
                            <Save className="h-4 w-4 mr-2"/>Save
                          </Button>
                        </div>
                      )}
                    </div>
                  </CardHeader>

                  <CardContent className="space-y-4">

                    <div className="grid md:grid-cols-2 gap-4">

                      <div>
                        <Label>First Name</Label>
                        <Input
                          value={profile.firstName}
                          disabled={!isEditing}
                          onChange={(e)=>setProfile({...profile,firstName:e.target.value})}
                        />
                      </div>

                      <div>
                        <Label>Last Name</Label>
                        <Input
                          value={profile.lastName}
                          disabled={!isEditing}
                          onChange={(e)=>setProfile({...profile,lastName:e.target.value})}
                        />
                      </div>

                    </div>

                    <div>
                      <Label>Email</Label>
                      <Input value={profile.email} disabled />
                    </div>

                    <div>
                      <Label>Phone</Label>
                      <Input
                        value={profile.phone}
                        disabled={!isEditing}
                        onChange={(e)=>setProfile({...profile,phone:e.target.value})}
                      />
                    </div>

                  </CardContent>

                </Card>
              </TabsContent>

              {/* Orders */}
              <TabsContent value="orders">
                <Card>
                  <CardHeader>
                    <CardTitle>Order History</CardTitle>
                  </CardHeader>

                  <CardContent className="space-y-4">
                    {mockOrders.map((order)=>(
                      <div key={order.id} className="border rounded-lg p-4 flex justify-between">
                        <div>
                          <p className="font-semibold">{order.id}</p>
                          <p className="text-sm text-muted-foreground">
                            {new Date(order.date).toLocaleDateString()}
                          </p>
                        </div>

                        <div className="text-right">
                          <Badge className={getStatusColor(order.status)}>
                            {order.status}
                          </Badge>
                          <p className="font-bold">${order.total}</p>
                        </div>
                      </div>
                    ))}
                  </CardContent>

                </Card>
              </TabsContent>

              {/* Addresses */}
              <TabsContent value="addresses">
                <Card>
                  <CardHeader>
                    <CardTitle>Saved Addresses</CardTitle>
                  </CardHeader>

                  <CardContent>
                    {mockAddresses.map((address)=>(
                      <div key={address.id} className="border p-4 rounded-lg">
                        <p className="font-semibold">{address.type}</p>
                        <p className="text-sm">{address.street}</p>
                        <p className="text-sm">{address.city}, {address.state}</p>
                      </div>
                    ))}
                  </CardContent>

                </Card>
              </TabsContent>

              {/* Settings */}
              <TabsContent value="settings">
                <Card>
                  <CardHeader>
                    <CardTitle>Account Settings</CardTitle>
                  </CardHeader>

                  <CardContent>
                    <Button
                      variant="outline"
                      className="w-full flex gap-2"
                      onClick={handleLogout}
                    >
                      <LogOut className="h-4 w-4"/>
                      Logout
                    </Button>
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