# Capstone 3 - Easyshop 🛒

Easyshop is a capstone Java Spring Boot web application designed to simulate a real-world e-commerce platform. It provides users with the ability to browse products, manage their shopping cart, and place orders, while administrators can manage inventory and view customer data.

## 🚀 Project Overview

This application demonstrates full-stack Java development using Spring Boot, RESTful APIs, MySQL, and Postman for testing. It is designed to showcase backend proficiency, security best practices, and robust data management.

## 🔧 Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **MySQL**
- **JDBC Template**
- **RESTful API**
- **Postman**
- **Maven**
- **Git & GitHub**

## 👤 User Features

- Register and login securely
- View available product catalog
- Filter products by category, color, or price range
- Add products to cart
- View and update items in cart
- Place orders (in-progress)

## 🛠 Admin Features (Coming Soon)

- Add, update, and delete products
- Manage categories
- View all users and their profiles
- View and process orders

## 🔐 Security

- JWT-based authentication
- Role-based access (`ROLE_USER`, `ROLE_ADMIN`)
- Secure endpoints using `@PreAuthorize`

## 🧪 API Testing

API endpoints can be tested using **Postman**:
- GET `/products`
- POST `/cart/products/{productId}`
- GET `/cart`
- POST `/orders` (in development)

Bearer token is required for authenticated endpoints.

## 🗃 Database Schema

- `users`
- `products`
- `categories`
- `orders`
- `shopping_cart` (in-memory implementation)

## ✅ Current Progress

- ✅ Cart operations functional
- ✅ User authentication/authorization
- ✅ Product and category endpoints
- ⚠️ Order creation endpoint under debugging
- ⚙️ MySQL DAO revisions ongoing

## 📝 Future Improvements

- Add UI via Thymeleaf or React
- Enable order tracking
- Add unit and integration testing
- Convert cart from in-memory to persistent storage
- Implement order confirmation system

## 👨‍💻 Author

**David Dorsey**  
[GitHub Profile](https://github.com/DavidD2001)  
Capstone project for Year Up Software Development program

---

> *This is a work-in-progress project created for educational purposes. Contributions, feedback, and constructive criticism are welcome!*
