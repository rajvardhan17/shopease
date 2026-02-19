# 🛍️ ShopEase  
### Full Stack E-Commerce Web Application

![Java](https://img.shields.io/badge/Backend-Java%20Servlet-orange)
![React](https://img.shields.io/badge/Frontend-React%20%2B%20Vite-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-green)
![Bootstrap](https://img.shields.io/badge/UI-Bootstrap-purple)
![Maven](https://img.shields.io/badge/Build-Maven-red)
![License](https://img.shields.io/badge/License-Educational-lightgrey)

---

## 📌 Overview

**ShopEase** is a scalable full-stack e-commerce web application designed to deliver a seamless online shopping experience with secure authentication, real-time inventory management, coupon handling, verified reviews, and a complete admin dashboard.

The system follows MVC architecture and integrates a modern React frontend with a Java Servlet-based backend and MySQL relational database.

---

## 🚀 Tech Stack

### 🎨 Frontend
- React.js (Vite)
- Bootstrap
- Axios
- HTML5 / CSS3

### ⚙️ Backend
- Java Servlets
- JSP (Java Server Pages)
- Maven
- Apache Tomcat

### 🗄️ Database
- MySQL (Normalized Schema – 23 Tables)

### 🛠 Tools
- Postman
- Git & GitHub
- Apache Tomcat Server

---

## ✨ Features

### 👤 User Features
- Secure Registration & Login
- Password Encryption
- Category-Based Browsing
- Product Search & Filtering
- Add to Cart / Remove from Cart
- Wishlist System
- Apply Coupons
- Buy Now Option
- Order History
- Real-Time Order Tracking
- Verified Purchase Reviews
- Invoice Generation

### 🛠 Admin Features
- Secure Admin Login
- Product & Category Management
- Inventory Monitoring
- Order Management
- Coupon Management
- Revenue Analytics
- Customer Management
- Audit Logs

---

## 🏗 System Architecture

```
React (Vite + Bootstrap)
        ↓
Axios API Requests
        ↓
Java Servlet Backend (JSP + Maven)
        ↓
MySQL Database
        ↓
Apache Tomcat Server
```

---

## 📂 Project Structure

```
ShopEase/
│
├── frontend/
│   ├── src/
│   ├── components/
│   ├── pages/
│   └── vite.config.js
│
├── backend/
│   ├── controllers/
│   ├── services/
│   ├── dao/
│   ├── models/
│   └── pom.xml
│
├── database/
│   └── shopease.sql
│
└── README.md
```

---

## 🗄 Database Tables (Core)

- users  
- user_addresses  
- admin_users  
- roles  
- categories  
- products  
- product_variants  
- variant_inventory  
- carts  
- cart_items  
- coupons  
- orders  
- order_items  
- payments  
- shipments  
- product_reviews  
- wishlists  
- audit_logs  
- webhooks  

Database design ensures:
- Normalization
- Data integrity
- Reduced redundancy
- Scalability

---

## ⚙️ Installation Guide

### 1️⃣ Clone Repository

```bash
git clone https://github.com/your-username/shopease.git
cd shopease
```

---

### 2️⃣ Database Setup

```sql
CREATE DATABASE shopease;
```

Import the `shopease.sql` file into MySQL.

---

### 3️⃣ Backend Setup

- Open backend folder in IntelliJ / Eclipse
- Configure DB credentials
- Build project:

```bash
mvn clean install
```

- Deploy WAR file to Apache Tomcat
- Start Tomcat Server

Backend runs at:
```
http://localhost:8080
```

---

### 4️⃣ Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at:
```
http://localhost:5173
```

---

## 🔐 Security Implementation

- Password Hashing
- Session-Based Authentication
- Role-Based Access Control
- Verified Review System
- Secure Checkout Handling
- Admin Activity Logging

---

## 📊 Key Highlights

✔ Full Stack Implementation  
✔ MVC Architecture  
✔ 23 Normalized Database Tables  
✔ Real-Time Inventory Handling  
✔ Modular Code Structure  
✔ Enterprise-Level Module Separation  

---

## 🔮 Future Enhancements

- Razorpay / Stripe Payment Integration  
- Email Notifications  
- Product Recommendations  
- Docker Deployment  
- CI/CD Pipeline  
- Progressive Web App Version  

---

## 👨‍💻 Author

**Rajvardhan Singh**  
Full Stack Developer  

GitHub: https://github.com/rajvardhan17 
LinkedIn: https://www.linkedin.com/in/rajvardhan17

---

## 📄 License

This project is developed for academic and demonstration purposes.
