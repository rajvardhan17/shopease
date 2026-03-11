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

const BACKEND_URL = "https://shopease-production-acc0.up.railway.app";

const Profile = () => {
  const navigate = useNavigate();
  const { toast } = useToast();

  const [profile, setProfile] = useState<any>(null);
  const [orders, setOrders] = useState<any[]>([]);
  const [addresses, setAddresses] = useState<any[]>([]);
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("token");

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const res = await fetch(`${BACKEND_URL}/api/session`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const data = await res.json();

      setProfile(data.user);
      setOrders(data.orders || []);
      setAddresses(data.addresses || []);
    } catch (error) {
      console.error("Profile fetch error:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSaveProfile = async () => {
    try {
      await fetch(`${BACKEND_URL}/api/session`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(profile),
      });

      toast({
        title: "Profile Updated",
        description: "Your profile information has been saved successfully.",
      });

      setIsEditing(false);
    } catch (error) {
      console.error(error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");

    toast({
      title: "Logged Out",
      description: "You have been successfully logged out.",
    });

    navigate("/login");
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen">
        Loading profile...
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="flex items-center justify-center h-screen">
        Failed to load profile
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-primary/5 to-secondary/5">

      {/* Header */}
      <div className="bg-background/80 backdrop-blur-sm border-b sticky top-0 z-10">
        <div className="container mx-auto px-4 py-4 flex items-center gap-4">
          <Button variant="ghost" size="icon" onClick={() => navigate("/home")}>
            <ArrowLeft className="h-5 w-5" />
          </Button>

          <div>
            <h1 className="text-2xl font-bold">My Profile</h1>
            <p className="text-sm text-muted-foreground">Manage your account settings</p>
          </div>
        </div>
      </div>

      <div className="container mx-auto px-4 py-8 grid grid-cols-1 lg:grid-cols-4 gap-6">

        {/* Sidebar */}
        <Card className="lg:col-span-1">
          <CardContent className="p-6 text-center space-y-4">

            <Avatar className="h-24 w-24 mx-auto">
              <AvatarImage src={profile.avatar} />
              <AvatarFallback className="text-2xl">
                {profile.firstName?.[0]}{profile.lastName?.[0]}
              </AvatarFallback>
            </Avatar>

            <div>
              <h3 className="font-semibold text-lg">
                {profile.firstName} {profile.lastName}
              </h3>
              <p className="text-sm text-muted-foreground">{profile.email}</p>
            </div>

            <Separator />

            <div className="flex justify-between text-sm">
              <span>Total Orders</span>
              <span className="font-semibold">{orders.length}</span>
            </div>

          </CardContent>
        </Card>

        {/* Main */}
        <div className="lg:col-span-3">

          <Tabs defaultValue="info">

            <TabsList className="grid grid-cols-4 w-full">
              <TabsTrigger value="info">Info</TabsTrigger>
              <TabsTrigger value="orders">Orders</TabsTrigger>
              <TabsTrigger value="addresses">Addresses</TabsTrigger>
              <TabsTrigger value="settings">Settings</TabsTrigger>
            </TabsList>

            {/* INFO */}
            <TabsContent value="info">
              <Card>
                <CardHeader className="flex flex-row justify-between items-center">
                  <div>
                    <CardTitle>Personal Information</CardTitle>
                    <CardDescription>Update your personal details</CardDescription>
                  </div>

                  {!isEditing ? (
                    <Button onClick={() => setIsEditing(true)}>Edit</Button>
                  ) : (
                    <Button onClick={handleSaveProfile}>
                      <Save className="h-4 w-4 mr-2"/>
                      Save
                    </Button>
                  )}
                </CardHeader>

                <CardContent className="space-y-4">

                  <Input
                    value={profile.firstName}
                    disabled={!isEditing}
                    onChange={(e) =>
                      setProfile({ ...profile, firstName: e.target.value })
                    }
                  />

                  <Input
                    value={profile.lastName}
                    disabled={!isEditing}
                    onChange={(e) =>
                      setProfile({ ...profile, lastName: e.target.value })
                    }
                  />

                  <Input
                    value={profile.email}
                    disabled={!isEditing}
                    onChange={(e) =>
                      setProfile({ ...profile, email: e.target.value })
                    }
                  />

                </CardContent>
              </Card>
            </TabsContent>

            {/* ORDERS */}
            <TabsContent value="orders">
              <Card>
                <CardHeader>
                  <CardTitle>Orders</CardTitle>
                </CardHeader>

                <CardContent className="space-y-3">

                  {orders.length === 0 && (
                    <p>No orders found</p>
                  )}

                  {orders.map((order) => (
                    <div key={order.id} className="border p-4 rounded-lg flex justify-between">

                      <div>
                        <p className="font-semibold">{order.id}</p>
                        <p className="text-sm text-muted-foreground">
                          {new Date(order.date).toLocaleDateString()}
                        </p>
                      </div>

                      <div className="text-right">
                        <p className="font-bold">₹{order.total}</p>
                        <Badge>{order.status}</Badge>
                      </div>

                    </div>
                  ))}

                </CardContent>
              </Card>
            </TabsContent>

            {/* ADDRESSES */}
            <TabsContent value="addresses">
              <Card>
                <CardHeader>
                  <CardTitle>Saved Addresses</CardTitle>
                </CardHeader>

                <CardContent className="space-y-3">

                  {addresses.length === 0 && <p>No addresses saved</p>}

                  {addresses.map((addr) => (
                    <div key={addr.id} className="border p-4 rounded-lg">

                      <p className="font-semibold">{addr.name}</p>
                      <p>{addr.street}</p>
                      <p>{addr.city} {addr.state}</p>

                    </div>
                  ))}

                </CardContent>
              </Card>
            </TabsContent>

            {/* SETTINGS */}
            <TabsContent value="settings">
              <Card>
                <CardHeader>
                  <CardTitle>Account</CardTitle>
                </CardHeader>

                <CardContent>
                  <Button
                    variant="outline"
                    className="w-full"
                    onClick={handleLogout}
                  >
                    <LogOut className="h-4 w-4 mr-2"/>
                    Logout
                  </Button>
                </CardContent>
              </Card>
            </TabsContent>

          </Tabs>

        </div>
      </div>
    </div>
  );
};

export default Profile;