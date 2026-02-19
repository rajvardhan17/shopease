import { useState } from "react";
import { Filter, Grid, List } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import ProductCard3D from "@/components/ProductCard3D";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import sneakerImage from "@/assets/sneaker-white.jpg";
import bootsImage from "@/assets/boots-black.jpg";
import sandalsImage from "@/assets/sandals-brown.jpg";
import slippersImage from "@/assets/slippers-gray.jpg";

const mockShoes = {
  sneakers: [
    { id: 1, name: "Air Max Pro", price: 149.99, image: sneakerImage, brand: "Nike", sizes: ["7", "8", "9", "10"] },
    { id: 2, name: "Classic Runner", price: 89.99, image: sneakerImage, brand: "Adidas", sizes: ["8", "9", "10", "11"] },
    { id: 3, name: "Street Walker", price: 129.99, image: sneakerImage, brand: "Puma", sizes: ["7", "8", "9"] },
    { id: 4, name: "Urban Boost", price: 179.99, image: sneakerImage, brand: "Nike", sizes: ["9", "10", "11", "12"] }
  ],
  sandals: [
    { id: 5, name: "Summer Breeze", price: 49.99, image: sandalsImage, brand: "Birkenstock", sizes: ["7", "8", "9", "10"] },
    { id: 6, name: "Beach Walker", price: 34.99, image: sandalsImage, brand: "Crocs", sizes: ["6", "7", "8", "9"] },
    { id: 7, name: "Sport Sandal", price: 69.99, image: sandalsImage, brand: "Teva", sizes: ["8", "9", "10", "11"] },
    { id: 8, name: "Comfort Plus", price: 89.99, image: sandalsImage, brand: "Birkenstock", sizes: ["7", "8", "9"] }
  ],
  slippers: [
    { id: 9, name: "Cozy Home", price: 29.99, image: slippersImage, brand: "UGG", sizes: ["S", "M", "L", "XL"] },
    { id: 10, name: "Memory Foam", price: 24.99, image: slippersImage, brand: "Generic", sizes: ["S", "M", "L"] },
    { id: 11, name: "Luxury Slipper", price: 79.99, image: slippersImage, brand: "UGG", sizes: ["S", "M", "L", "XL"] },
    { id: 12, name: "Warm & Fuzzy", price: 34.99, image: slippersImage, brand: "Generic", sizes: ["M", "L", "XL"] }
  ],
  boots: [
    { id: 13, name: "Winter Boot", price: 199.99, image: bootsImage, brand: "Timberland", sizes: ["8", "9", "10", "11"] },
    { id: 14, name: "Hiking Pro", price: 159.99, image: bootsImage, brand: "Merrell", sizes: ["7", "8", "9", "10"] },
    { id: 15, name: "Combat Style", price: 129.99, image: bootsImage, brand: "Dr. Martens", sizes: ["8", "9", "10"] },
    { id: 16, name: "Work Boot", price: 179.99, image: bootsImage, brand: "Timberland", sizes: ["9", "10", "11", "12"] }
  ]
};

const allShoes = Object.values(mockShoes).flat();

const Shoes = () => {
  const [activeCategory, setActiveCategory] = useState("all");
  const [sortBy, setSortBy] = useState("name");
  const [viewMode, setViewMode] = useState("grid");

  const getCurrentShoes = () => {
    if (activeCategory === "all") return allShoes;
    return mockShoes[activeCategory as keyof typeof mockShoes] || [];
  };

  const sortedShoes = getCurrentShoes().sort((a, b) => {
    switch (sortBy) {
      case "price-low":
        return a.price - b.price;
      case "price-high":
        return b.price - a.price;
      case "name":
        return a.name.localeCompare(b.name);
      default:
        return 0;
    }
  });

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <main className="container mx-auto px-4 py-8 pt-24">
        <div className="max-w-7xl mx-auto">
          <div className="mb-8">
            <h1 className="text-3xl font-bold mb-4">Shoes Collection</h1>
            <p className="text-muted-foreground">
              Discover our complete range of footwear for every occasion
            </p>
          </div>
          
          <Tabs value={activeCategory} onValueChange={setActiveCategory} className="mb-8">
            <TabsList className="grid w-full grid-cols-5">
              <TabsTrigger value="all">All Shoes</TabsTrigger>
              <TabsTrigger value="sneakers">Sneakers</TabsTrigger>
              <TabsTrigger value="sandals">Sandals</TabsTrigger>
              <TabsTrigger value="slippers">Slippers</TabsTrigger>
              <TabsTrigger value="boots">Boots</TabsTrigger>
            </TabsList>
          </Tabs>
          
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
            <div className="flex items-center gap-4">
              <Badge variant="secondary">
                {sortedShoes.length} {sortedShoes.length === 1 ? 'item' : 'items'}
              </Badge>
            </div>
            
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
          
          {viewMode === "grid" ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {sortedShoes.map((shoe) => (
                <ProductCard3D
                  key={shoe.id}
                  product={{
                    id: shoe.id.toString(),
                    name: shoe.name,
                    price: shoe.price,
                    image: shoe.image,
                    category: "shoe"
                  }}
                />
              ))}
            </div>
          ) : (
            <div className="space-y-4">
              {sortedShoes.map((shoe) => (
                <Card key={shoe.id} className="overflow-hidden">
                  <CardContent className="p-0">
                    <div className="flex items-center">
                      <img
                        src={shoe.image}
                        alt={shoe.name}
                        className="w-32 h-32 object-cover"
                      />
                      <div className="flex-1 p-6">
                        <div className="flex justify-between items-start">
                          <div>
                            <h3 className="text-lg font-semibold mb-1">{shoe.name}</h3>
                            <p className="text-muted-foreground mb-2">{shoe.brand}</p>
                            <p className="font-bold text-xl">${shoe.price}</p>
                          </div>
                          <div className="text-right">
                            <div className="mb-2">
                              <span className="text-sm text-muted-foreground">Available sizes:</span>
                              <div className="flex gap-1 mt-1">
                                {shoe.sizes.map((size) => (
                                  <Badge key={size} variant="outline" className="text-xs">
                                    {size}
                                  </Badge>
                                ))}
                              </div>
                            </div>
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
          
          {sortedShoes.length === 0 && (
            <div className="text-center py-12">
              <h3 className="text-xl font-semibold mb-2">No shoes found</h3>
              <p className="text-muted-foreground">Try selecting a different category</p>
            </div>
          )}
        </div>
      </main>
      
      <Footer />
    </div>
  );
};

export default Shoes;