import { useState, useEffect } from "react";
import { ArrowLeft, Save, LogOut, Plus, Trash } from "lucide-react";
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
  const [editingAddress, setEditingAddress] = useState<any>(null);

  const [loading, setLoading] = useState(true);
  const [isEditingProfile, setIsEditingProfile] = useState(false);

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

  // =====================
  // PROFILE UPDATE
  // =====================

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
          title: "Profile Updated"
        });

        setIsEditingProfile(false);
      }

    } catch (err) {
      console.error(err);
    }
  };

  // =====================
  // ADDRESS FUNCTIONS
  // =====================

  const addAddress = () => {

    const newAddress = {
      id: Date.now(),
      fullName: "",
      phone: "",
      street: "",
      city: "",
      state: "",
      postalCode: "",
      country: ""
    };

    setAddresses([...addresses, newAddress]);
    setEditingAddress(newAddress.id);
  };

  const updateAddress = (id: number, field: string, value: string) => {

    const updated = addresses.map((addr) =>
      addr.id === id ? { ...addr, [field]: value } : addr
    );

    setAddresses(updated);
  };

  const deleteAddress = (id: number) => {

    setAddresses(addresses.filter((addr) => addr.id !== id));

    toast({
      title: "Address removed"
    });
  };

  // =====================
  // LOGOUT
  // =====================

  const logout = async () => {

    await fetch(`${BACKEND_URL}/api/logout`, {
      method: "POST",
      credentials: "include"
    });

    navigate("/login");
  };

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  return (

    <div className="container mx-auto px-4 py-10">

      {/* HEADER */}

      <div className="flex items-center gap-4 mb-8">

        <Button variant="ghost" onClick={() => navigate("/home")}>
          <ArrowLeft />
        </Button>

        <h1 className="text-2xl font-bold">My Profile</h1>

      </div>

      <Tabs defaultValue="profile">

        <TabsList className="grid grid-cols-3 w-full mb-6">
          <TabsTrigger value="profile">Profile</TabsTrigger>
          <TabsTrigger value="addresses">Addresses</TabsTrigger>
          <TabsTrigger value="settings">Settings</TabsTrigger>
        </TabsList>

        {/* PROFILE TAB */}

        <TabsContent value="profile">

          <Card>

            <CardHeader className="flex justify-between items-center">

              <CardTitle>Personal Information</CardTitle>

              {!isEditingProfile ? (
                <Button onClick={() => setIsEditingProfile(true)}>Edit</Button>
              ) : (
                <Button onClick={saveProfile}>
                  <Save className="mr-2 h-4 w-4" />
                  Save
                </Button>
              )}

            </CardHeader>

            <CardContent className="space-y-4">

              <Input
                value={profile.fullName || ""}
                disabled={!isEditingProfile}
                placeholder="Full Name"
                onChange={(e) =>
                  setProfile({ ...profile, fullName: e.target.value })
                }
              />

              <Input
                value={profile.email || ""}
                disabled={!isEditingProfile}
                placeholder="Email"
                onChange={(e) =>
                  setProfile({ ...profile, email: e.target.value })
                }
              />

              <Input
                value={profile.phone || ""}
                disabled={!isEditingProfile}
                placeholder="Phone"
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
                  placeholder="Street Address"
                  value={addr.street}
                  onChange={(e) =>
                    updateAddress(addr.id, "street", e.target.value)
                  }
                />

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

                {/* ACTION BUTTONS */}

                <div className="flex gap-3">

                  <Button
                    onClick={() =>
                      toast({
                        title: "Address Saved",
                        description: "Your address has been stored locally."
                      })
                    }
                  >
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

          {addresses.length === 0 && (
            <p className="text-center text-gray-500">
              No address added yet
            </p>
          )}

        </TabsContent>


        {/* SETTINGS TAB */}

        <TabsContent value="settings">

          <Card>

            <CardContent className="pt-6">

              <Button
                variant="outline"
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

    </div>

  );
};

export default Profile;