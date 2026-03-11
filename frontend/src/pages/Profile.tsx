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
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [orders, setOrders] = useState<any[]>([]);
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

      if (!res.ok) {
        navigate("/login");
        return;
      }

      const data = await res.json();

      if (data.success) {
        setProfile(data.user);
      } else {
        navigate("/login");
      }

    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleSaveProfile = async () => {

    if (newPassword && newPassword !== confirmPassword) {
      toast({
        title: "Password mismatch",
        description: "New password and confirm password must match",
        variant: "destructive"
      });
      return;
    }

    try {

      const payload = {
        ...profile,
        password: newPassword ? newPassword : undefined
      };

      const res = await fetch(`${BACKEND_URL}/api/session`, {
        method: "PUT",
        credentials: "include",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });

      const data = await res.json();

      if (data.success) {

        setProfile(data.user);
        setIsEditing(false);
        setNewPassword("");
        setConfirmPassword("");

        toast({
          title: "Profile Updated",
          description: "Your details were updated successfully"
        });

      }

    } catch (error) {
      console.error(error);
    }
  };

  const handleLogout = async () => {

    try {

      await fetch(`${BACKEND_URL}/api/logout`, {
        method: "POST",
        credentials: "include"
      });

      toast({
        title: "Logged Out"
      });

      navigate("/login");

    } catch (error) {
      console.error(error);
    }
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

      {/* Header */}

      <div className="flex items-center gap-4 mb-8">

        <Button
          variant="ghost"
          size="icon"
          onClick={() => navigate("/home")}
        >
          <ArrowLeft />
        </Button>

        <div>
          <h1 className="text-2xl font-bold">My Profile</h1>
          <p className="text-sm text-muted-foreground">
            Manage your account details
          </p>
        </div>

      </div>

      <Tabs defaultValue="info">

        <TabsList className="grid grid-cols-3 w-full mb-6">
          <TabsTrigger value="info">Profile</TabsTrigger>
          <TabsTrigger value="orders">Orders</TabsTrigger>
          <TabsTrigger value="settings">Settings</TabsTrigger>
        </TabsList>

        {/* PROFILE TAB */}

        <TabsContent value="info">

          <Card>

            <CardHeader className="flex flex-row justify-between items-center">

              <div>
                <CardTitle>Personal Information</CardTitle>
                <CardDescription>
                  Update your account details
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
                value={profile.userId}
                disabled
                placeholder="User ID"
              />

              <Input
                placeholder="Full Name"
                value={profile.fullName || ""}
                disabled={!isEditing}
                onChange={(e) =>
                  setProfile({
                    ...profile,
                    fullName: e.target.value
                  })
                }
              />

              <Input
                placeholder="Email"
                value={profile.email || ""}
                disabled={!isEditing}
                onChange={(e) =>
                  setProfile({
                    ...profile,
                    email: e.target.value
                  })
                }
              />

              <Input
                placeholder="Phone"
                value={profile.phone || ""}
                disabled={!isEditing}
                onChange={(e) =>
                  setProfile({
                    ...profile,
                    phone: e.target.value
                  })
                }
              />

              <div className="flex items-center gap-2">
                <span>Account Type:</span>
                <Badge>
                  {profile.admin ? "Admin" : "User"}
                </Badge>
              </div>

              {/* PASSWORD CHANGE */}

              {isEditing && (

                <>

                  <Input
                    type="password"
                    placeholder="New Password"
                    value={newPassword}
                    onChange={(e) =>
                      setNewPassword(e.target.value)
                    }
                  />

                  <Input
                    type="password"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={(e) =>
                      setConfirmPassword(e.target.value)
                    }
                  />

                </>

              )}

            </CardContent>

          </Card>

        </TabsContent>

        {/* ORDERS TAB */}

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

        {/* SETTINGS TAB */}

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