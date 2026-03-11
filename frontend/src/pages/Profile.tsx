import { useState, useEffect } from "react";
import { ArrowLeft, Save, LogOut, Plus, Trash, User, MapPin } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";

const BACKEND_URL = "https://shopease-production-acc0.up.railway.app";

const Profile = () => {

  const navigate = useNavigate();
  const { toast } = useToast();

  const [profile, setProfile] = useState<any>(null);
  const [addresses, setAddresses] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [isEditingProfile, setIsEditingProfile] = useState(false);

  // =============================
  // AUTO LOAD PROFILE
  // =============================

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {

    try {

      const res = await fetch(`${BACKEND_URL}/api/session`, {
        credentials: "include"
      });

      if (res.status === 401) {
        navigate("/login");
        return;
      }

      const data = await res.json();

      if (data.success) {
        setProfile(data.user);
      }

    } catch (err) {

      toast({
        title: "Failed to load profile",
        variant: "destructive"
      });

    } finally {
      setLoading(false);
    }
  };

  // =============================
  // SAVE PROFILE
  // =============================

  const saveProfile = async () => {

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
          title: "Profile updated successfully"
        });

        setIsEditingProfile(false);
      }

    } catch (err) {

      toast({
        title: "Failed to update profile",
        variant: "destructive"
      });

    }
  };

  // =============================
  // ADDRESS FUNCTIONS
  // =============================

  const addAddress = () => {

    setAddresses([
      ...addresses,
      {
        id: Date.now(),
        fullName: "",
        phone: "",
        street: "",
        city: "",
        state: "",
        postalCode: "",
        country: ""
      }
    ]);
  };

  const updateAddress = (id: number, field: string, value: string) => {

    setAddresses(
      addresses.map((addr) =>
        addr.id === id ? { ...addr, [field]: value } : addr
      )
    );
  };

  const deleteAddress = (id: number) => {

    setAddresses(addresses.filter((addr) => addr.id !== id));

    toast({
      title: "Address removed"
    });
  };

  // =============================
  // LOGOUT
  // =============================

  const logout = async () => {

    await fetch(`${BACKEND_URL}/api/logout`, {
      method: "POST",
      credentials: "include"
    });

    navigate("/login");
  };

  // =============================
  // LOADING STATE
  // =============================

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen text-lg">
        Loading profile...
      </div>
    );
  }

  return (

    <div className="container mx-auto px-4 py-10 max-w-4xl">

      {/* HEADER */}

      <div className="flex items-center justify-between mb-8">

        <div className="flex items-center gap-4">

          <Button variant="ghost" onClick={() => navigate("/")}>
            <ArrowLeft />
          </Button>

          <h1 className="text-2xl font-bold">My Profile</h1>

        </div>

      </div>

      {profile && (

        <Tabs defaultValue="profile">

          <TabsList className="grid grid-cols-3 mb-6">

            <TabsTrigger value="profile">
              <User className="mr-2 h-4 w-4" />
              Profile
            </TabsTrigger>

            <TabsTrigger value="addresses">
              <MapPin className="mr-2 h-4 w-4" />
              Addresses
            </TabsTrigger>

            <TabsTrigger value="settings">
              Settings
            </TabsTrigger>

          </TabsList>

          {/* PROFILE TAB */}

          <TabsContent value="profile">

            <Card>

              <CardHeader className="flex flex-row justify-between items-center">

                <CardTitle>Personal Information</CardTitle>

                {!isEditingProfile ? (

                  <Button onClick={() => setIsEditingProfile(true)}>
                    Edit
                  </Button>

                ) : (

                  <Button onClick={saveProfile}>
                    <Save className="mr-2 h-4 w-4" />
                    Save
                  </Button>

                )}

              </CardHeader>

              <CardContent className="space-y-4">

                <Input
                  placeholder="Full Name"
                  value={profile.fullName || ""}
                  disabled={!isEditingProfile}
                  onChange={(e) =>
                    setProfile({ ...profile, fullName: e.target.value })
                  }
                />

                <Input
                  placeholder="Email"
                  value={profile.email || ""}
                  disabled={!isEditingProfile}
                  onChange={(e) =>
                    setProfile({ ...profile, email: e.target.value })
                  }
                />

                <Input
                  placeholder="Phone"
                  value={profile.phone || ""}
                  disabled={!isEditingProfile}
                  onChange={(e) =>
                    setProfile({ ...profile, phone: e.target.value })
                  }
                />

              </CardContent>

            </Card>

          </TabsContent>

          {/* ADDRESS TAB */}

          <TabsContent value="addresses">

            <div className="flex justify-end mb-4">

              <Button onClick={addAddress}>
                <Plus className="mr-2 h-4 w-4" />
                Add Address
              </Button>

            </div>

            {addresses.length === 0 && (
              <p className="text-center text-gray-500">
                No addresses added yet
              </p>
            )}

            {addresses.map((addr) => (

              <Card key={addr.id} className="mb-4">

                <CardContent className="space-y-3 pt-6">

                  <Input
                    placeholder="Full Name"
                    value={addr.fullName}
                    onChange={(e) =>
                      updateAddress(addr.id, "fullName", e.target.value)
                    }
                  />

                  <Input
                    placeholder="Phone"
                    value={addr.phone}
                    onChange={(e) =>
                      updateAddress(addr.id, "phone", e.target.value)
                    }
                  />

                  <Input
                    placeholder="Street"
                    value={addr.street}
                    onChange={(e) =>
                      updateAddress(addr.id, "street", e.target.value)
                    }
                  />

                  <div className="grid grid-cols-2 gap-3">

                    <Input
                      placeholder="City"
                      value={addr.city}
                      onChange={(e) =>
                        updateAddress(addr.id, "city", e.target.value)
                      }
                    />

                    <Input
                      placeholder="State"
                      value={addr.state}
                      onChange={(e) =>
                        updateAddress(addr.id, "state", e.target.value)
                      }
                    />

                  </div>

                  <div className="grid grid-cols-2 gap-3">

                    <Input
                      placeholder="Postal Code"
                      value={addr.postalCode}
                      onChange={(e) =>
                        updateAddress(addr.id, "postalCode", e.target.value)
                      }
                    />

                    <Input
                      placeholder="Country"
                      value={addr.country}
                      onChange={(e) =>
                        updateAddress(addr.id, "country", e.target.value)
                      }
                    />

                  </div>

                  <div className="flex gap-3 pt-2">

                    <Button>
                      <Save className="mr-2 h-4 w-4" />
                      Save Address
                    </Button>

                    <Button
                      variant="destructive"
                      onClick={() => deleteAddress(addr.id)}
                    >
                      <Trash className="mr-2 h-4 w-4" />
                      Delete
                    </Button>

                  </div>

                </CardContent>

              </Card>

            ))}

          </TabsContent>

          {/* SETTINGS TAB */}

          <TabsContent value="settings">

            <Card>

              <CardContent className="pt-6">

                <Button
                  variant="destructive"
                  className="w-full"
                  onClick={logout}
                >
                  <LogOut className="mr-2 h-4 w-4" />
                  Logout
                </Button>

              </CardContent>

            </Card>

          </TabsContent>

        </Tabs>

      )}

    </div>
  );
};

export default Profile;