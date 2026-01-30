import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Link, useNavigate } from "react-router-dom";
import { Eye, EyeOff, LogIn } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useToast } from "@/hooks/use-toast";
import { AuthScene3D } from "@/components/3d/AuthScene3D";
import { login, getSession, LoginData } from "@/api";

const loginSchema = z.object({
  email: z.string().email("Please enter a valid email address"),
  password: z.string().min(6, "Password must be at least 6 characters"),
});

type LoginFormData = z.infer<typeof loginSchema>;

{/*const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const { toast } = useToast();
  const navigate = useNavigate();

  const form = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: "", password: "" },
  });

  // 🔹 Check if already logged in (session exists)
  useEffect(() => {
    const checkSession = async () => {
      try {
        const res = await getSession();
        if (res.success && res.user) {
          navigate("/dashboard");
        }
      } catch (err) {
        // session not found - continue
      }
    };
    checkSession();
  }, [navigate]);

  const onSubmit = async (data: LoginFormData) => {
    try {
      const res = await login(data as LoginData);
      if (res.success) {
        toast({
          title: "Login Successful",
          description: `Welcome back, ${res.user?.fullName || "User"}`,
        });
        navigate("/home");
      } else {
        toast({
          title: "Login Failed",
          description: res.message,
          variant: "destructive",
        });
      }
    } catch (err) {
      toast({
        title: "Error",
        description: "Something went wrong while logging in",
        variant: "destructive",
      });
    }
  };*/

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-bg p-4 relative overflow-hidden">
      <div className="absolute top-20 left-20 w-72 h-72 bg-primary/20 rounded-full blur-3xl animate-float" />
      <div className="absolute bottom-20 right-20 w-96 h-96 bg-secondary/20 rounded-full blur-3xl animate-float" />
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-80 h-80 bg-accent/20 rounded-full blur-3xl animate-glow" />

      <div className="container mx-auto grid md:grid-cols-2 gap-8 items-center max-w-6xl">
        <div className="hidden md:block animate-fade-in">
          <AuthScene3D />
        </div>

        <Card className="w-full animate-slide-up shadow-primary backdrop-blur-sm bg-background/95 border-primary/20">
          <CardHeader className="text-center space-y-2">
            <div className="mx-auto mb-4 w-16 h-16 bg-gradient-hero rounded-2xl flex items-center justify-center shadow-primary animate-float">
              <LogIn className="w-8 h-8 text-white" />
            </div>
            <CardTitle className="text-3xl font-bold bg-gradient-hero bg-clip-text text-transparent">
              Welcome Back
            </CardTitle>
            <CardDescription className="text-base">
              Sign in to your account to continue
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Form {...form}>
              <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                  control={form.control}
                  name="email"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Email</FormLabel>
                      <FormControl>
                        <Input type="email" placeholder="Enter your email" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="password"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Password</FormLabel>
                      <FormControl>
                        <div className="relative">
                          <Input type={showPassword ? "text" : "password"} placeholder="Enter your password" {...field} />
                          <Button
                            type="button"
                            variant="ghost"
                            size="sm"
                            className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
                            onClick={() => setShowPassword(!showPassword)}
                          >
                            {showPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                          </Button>
                        </div>
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <Button type="submit" className="w-full bg-gradient-hero hover:opacity-90 transition-all shadow-primary hover:shadow-lg">
                  Sign In
                </Button>
              </form>
            </Form>

            <div className="mt-6 text-center">
              <p className="text-sm text-muted-foreground">
                Don’t have an account?{" "}
                <Link to="/register" className="text-primary hover:text-primary/80 font-semibold transition-colors">
                  Sign up
                </Link>
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default Login;
