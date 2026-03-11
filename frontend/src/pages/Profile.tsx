import { useState, useEffect } from "react";
import { ArrowLeft, Save, LogOut } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
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

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {

      const res = await fetch(`${BACKEND_URL}/api/session`, {
        credentials: "include"
      });

      const data = await res.json();

      if (data.success) {
        setProfile(data.user);
      } else {
        navigate("/login");
      }

    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSaveProfile = async () => {

    try {

      const res = await fetch(`${BACKEND_URL}/api/session`, {
        method: "PUT",
        credentials: "include",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(profile)
      });

      const data = await res.json();

      if (data.success) {

        toast({
          title: "Profile Updated",
          description: "Your profile has been updated successfully."
        });

        setIsEditing(false);

      }

    } catch (err) {
      console.error(err);
    }
  };

  const handleLogout = () => {

    toast({
      title: "Logged Out",
      description: "You have been logged out."
    });

    navigate("/login");
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        Loading profile...
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="flex justify-center items-center h-screen">
        Failed to load profile
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-10">

      {/* HEADER */}
      <div className="flex items-center gap-4 mb-8">

        <Button variant="ghost" size="icon" onClick={() => navigate("/home")}>
          <ArrowLeft />
        </Button>

        <div>
          <h1 className="text-2xl font-bold">My Profile</h1>
          <p className="text-muted-foreground text-sm">
            Manage your account
          </p>
        </div>

      </div>

      <Tabs defaultValue="info">

        <TabsList className="grid grid-cols-3 w-full mb-6">
          <TabsTrigger value="info">Info</TabsTrigger>
          <TabsTrigger value="orders">Orders</TabsTrigger>
          <TabsTrigger value="settings">Settings</TabsTrigger>
        </TabsList>

        {/* INFO TAB */}
        <TabsContent value="info">

          <Card>

            <CardHeader className="flex flex-row justify-between items-center">

              <div>
                <CardTitle>Personal Information</CardTitle>
                <CardDescription>
                  Update your details
                </CardDescription>
              </div>

              {!isEditing ? (
                <Button onClick={() => setIsEditing(true)}>
                  Edit
                </Button>
              ) : (
                <Button onClick={handleSaveProfile}>
                  <Save className="h-4 w-4 mr-2" />
                  Save
                </Button>
              )}

            </CardHeader>

            <CardContent className="space-y-4">

              <Input
                placeholder="Full Name"
                value={profile.fullName || ""}
                disabled={!isEditing}
                onChange={(e) =>
                  setProfile({ ...profile, fullName: e.target.value })
                }
              />

              <Input
                placeholder="Email"
                value={profile.email || ""}
                disabled={!isEditing}
                onChange={(e) =>
                  setProfile({ ...profile, email: e.target.value })
                }
              />

              <Input
                placeholder="Phone"
                value={profile.phone || ""}
                disabled={!isEditing}
                onChange={(e) =>
                  setProfile({ ...profile, phone: e.target.value })
                }
              />

            </CardContent>

          </Card>

        </TabsContent>

        {/* ORDERS */}
        <TabsContent value="orders">

          <Card>

            <CardHeader>
              <CardTitle>Your Orders</CardTitle>
            </CardHeader>

            <CardContent>

              {orders.length === 0 ? (
                <p>No orders found</p>
              ) : (
                orders.map((order) => (

                  <div
                    key={order.id}
                    className="border p-4 rounded-lg flex justify-between mb-3"
                  >

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

                ))
              )}

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
                <LogOut className="h-4 w-4 mr-2" />
                Logout
              </Button>

            </CardContent>

          </Card>

        </TabsContent>

      </Tabs>

    </div>
  );
};

export default Profile;