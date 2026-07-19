# Etrade Backend - E-commerce Platform API

A robust, secure, and scalable RESTful API backend for a full-featured e-commerce platform built with Spring Boot. This backend handles user authentication, product management, shopping cart functionality, order processing, payment integration, and administrative controls.

## Features

### Authentication & Security

- **JWT-based Authentication** with access tokens (24h expiry) and refresh tokens (7d expiry)
- **Google OAuth2 Integration** for seamless social login
- **Role-based Access Control** (USER and ADMIN roles)
- **Stateless Security** using Spring Security with JWT filters
- **CORS Configuration** for frontend integration
- **Global Exception Handling** for consistent error responses
- **Password Encoding** with BCrypt for secure credential storage

### Core E-commerce Functionality

- **Product Management**: Browse, search, filter products by category, newest arrivals, best sellers
- **Shopping Cart**: Add/remove items, update quantities, view cart totals
- **Order Processing**: Checkout flow with order confirmation and tracking
- **User Management**: Profile management, order history, address book
- **Category System**: Hierarchical product categorization
- **Wishlist**: Save products for future purchase

### Administrative Features

- **Product CRUD Operations**: Add, update, delete products with validation
- **Category Management**: Create and manage product categories
- **Order Administration**: View and manage all customer orders
- **User Administration**: Manage user accounts and roles

### API Design

- **RESTful Architecture** with consistent endpoint naming
- **Comprehensive Swagger/OpenAPI Documentation**
- **Standardized Response Format** with success/error codes
- **Cookie-based Token Storage** for enhanced security
- **HTTPS-ready Configuration** with secure headers

## Technology Stack

### Backend Framework

- **Spring Boot 3.x** - Modern Java-based microservices framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Object-relational mapping with Hibernate
- **Spring Web MVC** - RESTful web services

### Database & Persistence

- **MySQL 8.x** - Relational database for structured data
- **JPA/Hibernate** - ORM layer with automatic schema updates
- **Auditing** - Automatic creation/update timestamps with @EnableJpaAuditing

### Security & Authentication

- **JSON Web Tokens (JWT)** - Stateless authentication mechanism
- **BCrypt Password Hashing** - Secure password storage
- **Google OAuth2** - Third-party authentication provider
- **Custom JWT Filter** - Token validation and user context setup

### Development & DevOps

- **Maven** - Dependency management and build automation
- **Docker** - Containerization for consistent deployment
- **Swagger/OpenAPI 3.0** - Interactive API documentation
- **Lombok** - Reduced boilerplate code
- **Git** - Version control

### Deployment Infrastructure

- **Dockerfile** - Container image definition
- **docker-compose.yml** - Multi-container orchestration
- **Nginx Ready** - Reverse proxy configuration
- **VPS Deployment** - Tested on Hostinger and similar providers
- **CI/CD Pipeline** with automated deployment

## System Architecture

### Layered Architecture

```
Presentation Layer (REST Controllers)
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (Data Access)
        ↓
Database (MySQL)
```

### Security Flow

1. **Login Request** → AuthController
2. **Credential Validation** → AuthService
3. **Token Generation** → JwtUtils (access + refresh tokens)
4. **Cookie Setting** → HttpOnly, Secure flags
5. **Subsequent Requests** → JwtFilter validates tokens
6. **User Context** → SecurityContextHolder setup
7. **Authorized Access** → Protected endpoints

### Key Security Implementation Details

- **Dual Filter Chains**: Separate chains for Google OAuth and standard JWT auth
- **Stateless Session Management**: No server-side session storage
- **Token Extraction**: From HTTP-only cookies (XSS protection)
- **Blacklist Checking**: Hook for token revocation (commented for extension)
- **CORS Policy**: Configurable origins with credential support

## API Endpoints

### Authentication (`/api/v1/auth/*`)

- `POST /register` - User registration
- `POST /login` - Username/password login
- `POST /google` - Google OAuth2 login
- `POST /logout` - User logout (cookie clearing)
- `POST /refresh` - Token refresh endpoint
- `GET /me` - Current user profile

### Products (`/api/v1/products/*`)

- `GET /` - List all products (with pagination/filtering)
- `GET /{id}` - Get product by ID
- `GET /newest` - Latest arrivals
- `GET /best-seller` - Top selling products
- `GET /{categoryName}` - Products by category

### Cart (`/api/v1/cart/*`)

- `POST /add` - Add item to cart
- `GET /get-current` - View user's cart
- `PUT /update/{id}` - Update item quantity
- `DELETE /remove-item/{id}` - Remove cart item

### Orders (`/api/v1/orders/*`)

- `GET /` - User's order history
- `GET /{id}` - Order details
- `POST /checkout` - Process cart checkout

### Admin (`/api/v1/admin/*`)

- Product CRUD operations
- Category management
- Order supervision

## Setup & Installation

### Prerequisites

- Java JDK 17+
- MySQL 8.0+
- Maven 3.8+
- Docker (optional for containerized deployment)

### Local Development Setup

1. **Clone the Repository**

```bash
git clone <repository-url>
cd etrade_be
```

2. **Configure Database**

```sql
CREATE DATABASE e_trade;
-- Update credentials in src/main/resources/application.properties
```

3. **Build and Run**

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

4. **Access API Documentation**

- Swagger UI: http://localhost:8080/swagger-ui.html
- API Specs: http://localhost:8080/v3/api-docs

### Docker Deployment

1. **Build Docker Image**

```bash
docker build -t etrade-backend .
```

2. **Run with Docker Compose**

```bash
docker-compose up -d
```

3. **Access Application**

- API: http://localhost:8080
- Documentation: http://localhost:8080/swagger-ui.html

## 🔧 Configuration

### Application Properties (`src/main/resources/application.properties`)

```properties
# Database Connection
spring.datasource.url=jdbc:mysql://localhost:3306/e_trade
spring.datasource.username=root
spring.datasource.password=root

# JPA/Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration
jwt.access-token-expiration=86400   # 24 hours in seconds
jwt.refresh-token-expiration=1209600 # 7 days in seconds

# Security
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://accounts.google.com
```

### Security Configuration Highlights

- Custom `SecurityConfig` with dual filter chains
- `JwtFilter` extracts tokens from HTTP-only cookies
- `JwtUtils` handles token generation, validation, and parsing
- `SecurityUtils` provides helper for extracting current user ID

## Key Technical Challenges Solved

### 1. **Stateless Authentication with Cookie Storage**

- Implemented JWT tokens stored in HTTP-only cookies to prevent XSS attacks
- Created custom filter to extract and validate tokens from cookies
- Balanced security with usability for SPA frontend integration

### 2. **Dual Authentication Chains**

- Separate security chains for Google OAuth2 (Spring-managed) and standard JWT
- Prevented filter conflicts while maintaining consistent user experience
- Configured `@Order` annotations for proper chain precedence

### 3. **Token Management & Renewal**

- Implemented access token (short-lived) and refresh token (long-lived) pattern
- Created automatic token refresh mechanism
- Added scheduled cleanup for expired tokens

### 4. **CORS Configuration for Production**

- Configured dynamic origin patterns for development and production
- Supported credentials for authenticated requests
- Whitelisted specific domains (localhost, staging, production)

### 5. **Global Error Handling**

- Implemented `@ControllerAdvice` for consistent API error responses
- Standardized error format across all endpoints
- Proper HTTP status codes for different error scenarios

## Potential Enhancements

### Near-term Improvements

- **Unit & Integration Testing** with JUnit 5 and Mockito
- **API Rate Limiting** to prevent abuse
- **Input Validation Enhancements** using Hibernate Validator
- **Pagination** for large dataset endpoints
- **Search Optimization** with full-text search capabilities

### Mid-term Features

- **Payment Gateway Integration** (Stripe/PayPal)
- **Email Notification Service** (order confirmations, shipping updates)
- **Admin Dashboard Analytics** (sales reports, user metrics)
- **Inventory Management** with low-stock alerts
- **Product Reviews & Rating System**

### Long-term Scalability

- **Microservices Architecture** decomposition
- **Redis Caching** for frequent queries
- **Message Queues** (RabbitMQ/Kafka) for async processing
- **ELK Stack** for centralized logging and monitoring

## Target Audience

This backend is designed for:

- **Frontend Developers** seeking a reliable API for e-commerce applications
- **DevOps Engineers** looking for containerized, deployable backend services
- **Technical Leads** evaluating Spring Boot expertise and architectural decisions
- **Recruiters** assessing full-stack development capabilities

---

**Built with**: Java, Spring Boot, MySQL, JWT Security, Docker

_Note: This README focuses on the backend API component. A complete e-commerce solution would include a complementary frontend application consuming these endpoints._
