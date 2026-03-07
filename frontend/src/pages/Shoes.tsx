import { useEffect, useState } from "react";
import { Grid, List } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import ProductCard3D from "@/components/ProductCard3D";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

const BACKEND_URL = "https://shopease-production-acc0.up.railway.app";

// Map all possible category names to simplified keys
const CATEGORY_MAP: Record<string, string> = {
  shoes: "shoes",
  shoe: "shoes",
  sneakers: "sneakers",
  sneaker: "sneakers",
  sandals: "sandals",
  slipper: "slippers",
  slippers: "slippers",
  boots: "boots",
  boot: "boots",
};

const Shoes = () => {
  const [products, setProducts] = useState<any[]>([]);
  const [activeCategory, setActiveCategory] = useState("all"); // all, sneakers, sandals, slippers, boots
  const [sortBy, setSortBy] = useState("name");
  const [viewMode, setViewMode] = useState("grid");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await fetch(`${BACKEND_URL}/api/products`);
        const data = await res.json();
        if (data?.products) setProducts(data.products);
      } catch (error) {
        console.error("Error fetching shoes:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, []);

  // Normalize categories: lowercase and map to simplified keys
  const normalizeCategory = (cat: string | undefined) => {
    if (!cat) return "";
    const key = cat.toLowerCase().replace(/\s/g, "").replace(/-/g, "");
    return CATEGORY_MAP[key] || "";
  };

  // Filter shoes based on active tab
  const filteredShoes = products.filter((p) => {
    const cat = normalizeCategory(p.category);
    if (activeCategory === "all") return ["shoes", "sneakers", "sandals", "slippers", "boots"].includes(cat);
    return cat === activeCategory;
  });

  // Sort filtered shoes
  const sortedShoes = [...filteredShoes].sort((a, b) => {
    switch (sortBy) {
      case "price-low":
        return (a.price || 0) - (b.price || 0);
      case "price-high":
        return (b.price || 0) - (a.price || 0);
      case "name":
        return (a.name || "").localeCompare(b.name || "");
      default:
        return 0;
    }
  });

  return (
    <div className="min-h-screen bg-background">
      <Header />

      <main className="container mx-auto px-4 py-8 pt-24">
        <div className="max-w-7xl mx-auto">
          {/* Page Header */}
          <div className="mb-8">
            <h1 className="text-3xl font-bold mb-4">Shoes Collection</h1>
            <p className="text-muted-foreground">Discover our complete range of footwear for every occasion</p>
          </div>

          {/* Category Tabs */}
          <Tabs value={activeCategory} onValueChange={setActiveCategory} className="mb-8">
            <TabsList className="grid w-full grid-cols-5">
              <TabsTrigger value="all">All Shoes</TabsTrigger>
              <TabsTrigger value="sneakers">Sneakers</TabsTrigger>
              <TabsTrigger value="sandals">Sandals</TabsTrigger>
              <TabsTrigger value="slippers">Slippers</TabsTrigger>
              <TabsTrigger value="boots">Boots</TabsTrigger>
            </TabsList>
          </Tabs>

          {/* Filters & View */}
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
            <Badge variant="secondary">
              {sortedShoes.length} {sortedShoes.length === 1 ? "item" : "items"}
            </Badge>

            <div className="flex items-center gap-4">
              <Select value={sortBy} onValueChange={setSortBy}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue placeholder="Sort by" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="name">Name A-Z</SelectItem>
                  <SelectItem value="price-low">Price: Low to High</SelectItem>
                  <SelectItem value="price-high">Price: High to Low</SelectItem>
                </SelectContent>
              </Select>

              <div className="flex border rounded-md">
                <Button
                  variant={viewMode === "grid" ? "default" : "ghost"}
                  size="sm"
                  onClick={() => setViewMode("grid")}
                  className="rounded-r-none"
                >
                  <Grid className="w-4 h-4" />
                </Button>
                <Button
                  variant={viewMode === "list" ? "default" : "ghost"}
                  size="sm"
                  onClick={() => setViewMode("list")}
                  className="rounded-l-none"
                >
                  <List className="w-4 h-4" />
                </Button>
              </div>
            </div>
          </div>

          {/* Loading / Empty / Grid / List */}
          {loading ? (
            <div className="text-center py-12">Loading shoes...</div>
          ) : sortedShoes.length === 0 ? (
            <div className="text-center py-12">
              <h3 className="text-xl font-semibold mb-2">No shoes found</h3>
              <p className="text-muted-foreground">Try selecting a different category</p>
            </div>
          ) : viewMode === "grid" ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {sortedShoes.map((shoe) => (
                <ProductCard3D
                  key={shoe.id?.toString() || Math.random().toString()}
                  product={{
                    id: shoe.id?.toString() || "0",
                    title: shoe.name || "Unnamed Shoe",
                    price: shoe.price || 0,
                    image: shoe.image || "/default-shoe.jpg",
                    category: "shoe",
                  }}
                />
              ))}
            </div>
          ) : (
            <div className="space-y-4">
              {sortedShoes.map((shoe) => (
                <Card key={shoe.id?.toString() || Math.random().toString()} className="overflow-hidden">
                  <CardContent className="p-0">
                    <div className="flex items-center">
                      <img
                        src={shoe.image || "/default-shoe.jpg"}
                        alt={shoe.name || "Shoe"}
                        className="w-32 h-32 object-cover"
                      />
                      <div className="flex-1 p-6">
                        <div className="flex justify-between items-start">
                          <div>
                            <h3 className="text-lg font-semibold mb-1">{shoe.name || "Unnamed Shoe"}</h3>
                            {shoe.brand && <p className="text-muted-foreground mb-2">{shoe.brand}</p>}
                            <p className="font-bold text-xl">${shoe.price || 0}</p>
                          </div>
                          <div className="text-right">
                            {shoe.sizes?.length > 0 && (
                              <div className="mb-2">
                                <span className="text-sm text-muted-foreground">Available sizes:</span>
                                <div className="flex gap-1 mt-1">
                                  {shoe.sizes.map((size: string) => (
                                    <Badge key={size} variant="outline" className="text-xs">
                                      {size}
                                    </Badge>
                                  ))}
                                </div>
                              </div>
                            )}
                            <Button>Add to Bag</Button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default Shoes;