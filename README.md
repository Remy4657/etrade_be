# E-Commerce Web Application

A full-stack eCommerce platform that allows users to browse products,
add items to cart, place orders, and manage accounts.
Admin can manage products, categories, and orders.
## Tech Stack

### Backend
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT Authentication

### Deployment
- Docker
- Nginx
- VPS (Hostinger)
- ## Features

### User
- Register / Login
- Browse products
- Search & filter products
- Add to cart
- Checkout
- View order history

### 1. Clone project
git clone ...

### 2. Setup database

Create database:

CREATE DATABASE ecommerce;

Update application.yml:

spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=yourpassword

### 3. Run backend
mvn spring-boot:run
