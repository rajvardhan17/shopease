import { useState } from "react";
import { CreditCard, Calendar, Lock, Plus, Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Badge } from "@/components/ui/badge";
import { toast } from "sonner";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

const mockPaymentMethods = [
  {
    id: 1,
    type: "visa",
    last4: "4532",
    expiryMonth: "12",
    expiryYear: "2027",
    holderName: "John Doe",
    isDefault: true
  },
  {
    id: 2,
    type: "mastercard",
    last4: "8901",
    expiryMonth: "09",
    expiryYear: "2026",
    holderName: "John Doe",
    isDefault: false
  }
];

const mockTransactions = [
  {
    id: "PAY-2024-001",
    date: "2024-01-15",
    amount: 149.99,
    status: "completed",
    method: "**** 4532",
    orderId: "ORD-2024-001"
  },
  {
    id: "PAY-2024-002",
    date: "2024-01-20",
    amount: 79.99,
    status: "completed",
    method: "**** 8901",
    orderId: "ORD-2024-002"
  },
  {
    id: "PAY-2024-003",
    date: "2024-01-22",
    amount: 199.99,
    status: "pending",
    method: "**** 4532",
    orderId: "ORD-2024-003"
  }
];

const Payments = () => {
  const [paymentMethods, setPaymentMethods] = useState(mockPaymentMethods);
  const [showAddCard, setShowAddCard] = useState(false);
  const [selectedMethod, setSelectedMethod] = useState("1");

  const handleDeleteCard = (id: number) => {
    setPaymentMethods(methods => methods.filter(method => method.id !== id));
    toast.success("Payment method removed");
  };

  const handleAddCard = () => {
    toast.success("New payment method added");
    setShowAddCard(false);
  };

  const getCardIcon = (type: string) => {
    const iconClass = "w-8 h-8";
    switch (type) {
      case "visa":
        return <div className={`${iconClass} bg-blue-600 rounded flex items-center justify-center text-white text-xs font-bold`}>VISA</div>;
      case "mastercard":
        return <div className={`${iconClass} bg-red-600 rounded flex items-center justify-center text-white text-xs font-bold`}>MC</div>;
      default:
        return <CreditCard className={iconClass} />;
    }
  };

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <main className="container mx-auto px-4 py-8 pt-24">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-3xl font-bold mb-8">Payment Methods</h1>
          
          <div className="grid md:grid-cols-2 gap-8">
            {/* Payment Methods */}
            <div>
              <div className="flex justify-between items-center mb-6">
                <h2 className="text-xl font-semibold">Saved Cards</h2>
                <Button onClick={() => setShowAddCard(true)}>
                  <Plus className="w-4 h-4 mr-2" />
                  Add Card
                </Button>
              </div>
              
              <RadioGroup value={selectedMethod} onValueChange={setSelectedMethod} className="space-y-4">
                {paymentMethods.map((method) => (
                  <Card key={method.id} className="relative">
                    <CardContent className="p-4">
                      <div className="flex items-center space-x-3">
                        <RadioGroupItem value={method.id.toString()} id={method.id.toString()} />
                        <div className="flex-1 flex items-center justify-between">
                          <div className="flex items-center space-x-3">
                            {getCardIcon(method.type)}
                            <div>
                              <div className="flex items-center gap-2">
                                <span className="font-medium">**** **** **** {method.last4}</span>
                                {method.isDefault && <Badge variant="secondary">Default</Badge>}
                              </div>
                              <p className="text-sm text-muted-foreground">
                                Expires {method.expiryMonth}/{method.expiryYear}
                              </p>
                              <p className="text-sm text-muted-foreground">{method.holderName}</p>
                            </div>
                          </div>
                          <Button
                            variant="ghost"
                            size="icon"
                            onClick={() => handleDeleteCard(method.id)}
                            className="text-destructive hover:text-destructive"
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </RadioGroup>
              
              {showAddCard && (
                <Card className="mt-6">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                      <CreditCard className="w-5 h-5" />
                      Add New Card
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <div className="space-y-2">
                      <Label htmlFor="cardNumber">Card Number</Label>
                      <Input id="cardNumber" placeholder="1234 5678 9012 3456" />
                    </div>
                    
                    <div className="grid grid-cols-2 gap-4">
                      <div className="space-y-2">
                        <Label htmlFor="expiry">Expiry Date</Label>
                        <Input id="expiry" placeholder="MM/YY" />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="cvv">CVV</Label>
                        <Input id="cvv" placeholder="123" />
                      </div>
                    </div>
                    
                    <div className="space-y-2">
                      <Label htmlFor="holderName">Cardholder Name</Label>
                      <Input id="holderName" placeholder="John Doe" />
                    </div>
                    
                    <div className="flex gap-2">
                      <Button onClick={handleAddCard} className="flex-1">
                        <Lock className="w-4 h-4 mr-2" />
                        Add Card
                      </Button>
                      <Button variant="outline" onClick={() => setShowAddCard(false)}>
                        Cancel
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              )}
            </div>
            
            {/* Transaction History */}
            <div>
              <h2 className="text-xl font-semibold mb-6">Recent Transactions</h2>
              
              <div className="space-y-4">
                {mockTransactions.map((transaction) => (
                  <Card key={transaction.id}>
                    <CardContent className="p-4">
                      <div className="flex justify-between items-start mb-2">
                        <div>
                          <p className="font-medium">{transaction.id}</p>
                          <p className="text-sm text-muted-foreground">{transaction.date}</p>
                        </div>
                        <div className="text-right">
                          <p className="font-semibold">${transaction.amount}</p>
                          <Badge 
                            variant={transaction.status === "completed" ? "default" : "secondary"}
                            className={transaction.status === "completed" ? "bg-green-600" : ""}
                          >
                            {transaction.status}
                          </Badge>
                        </div>
                      </div>
                      
                      <div className="text-sm text-muted-foreground">
                        <p>Paid with {transaction.method}</p>
                        <p>Order: {transaction.orderId}</p>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </div>
          </div>
        </div>
      </main>
      
      <Footer />
    </div>
  );
};

export default Payments;