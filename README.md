
---

  #  Book Store Service

Book Store is a comprehensive service for managing book purchases. It supports various operations including book catalog management, shopping cart functionality, and order processing.

## Features

- **Assortment Management**: Add, update, or remove books, and organize them into categories for efficient cataloging.
- **Shopping Cart Operations**: Manage the contents of shopping carts, facilitating easy checkout.
- **Order Management**: Administer and monitor the entire order fulfillment process.

## Technology Stack

- **Java**: 17
- **Build Tool**: Maven
- **Framework**: Spring Boot 3.2.0
  - Spring Boot Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **Database**: MySQL 8.0.33, H2 (for testing)
- **ORM**: Hibernate
- **Versioning**: Liquibase
- **Testing**: JUnit, Mockito, Spring Boot Starter Test, Docker Test Containers
- **Containerization**: Docker
- **Utilities**: Lombok, MapStruct, Swagger, JWT (for security)

## API Endpoints

### **AuthController**

Handles user registration and login, supporting both Basic and JWT authentication.

- `POST /api/auth/registration`: User registration
- `POST /api/auth/login`: User login

### **BookController**

Manages CRUD operations for books.

- `GET /api/books`: Retrieve all books
- `GET /api/books/{id}`: Retrieve a book by its ID
- `POST /api/books`: Create a new book (Admin only)
- `PUT /api/books/{id}`: Update a book (Admin only)
- `DELETE /api/books/{id}`: Delete a book (Admin only)

### **CategoryController**

Handles category management and retrieves books by category.

- `GET /api/categories`: Retrieve all categories
- `GET /api/categories/{id}`: Retrieve a category by its ID
- `GET /api/categories/{id}/books`: Retrieve books by category ID
- `POST /api/categories`: Create a new category (Admin only)
- `PUT /api/categories/{id}`: Update category information (Admin only)
- `DELETE /api/categories/{id}`: Delete a category (Admin only)

### **OrderController**

Manages CRUD operations for orders.

- `GET /api/orders`: Retrieve order history
- `GET /api/orders/{order-id}/items`: Retrieve items from a specific order
- `GET /api/orders/{order-id}/items/{item-id}`: Retrieve a specific item from an order
- `POST /api/orders`: Place a new order
- `PATCH /api/orders/{id}`: Update an order status (Admin only)

### **ShoppingCartController**

Handles CRUD operations for shopping cart items.

- `GET /api/cart`: Retrieve all items in the shopping cart
- `POST /api/cart`: Add an item to the shopping cart
- `PUT /api/cart/cart-items/{cartItemId}`: Update the quantity of a specific item in the cart
- `DELETE /api/cart/cart-items/{cartItemId}`: Remove an item from the cart

## How to Run the Project

### Prerequisites

- **Java**: Ensure you have Java 17 installed.
- **Docker**: Docker is required to run the project in a containerized environment.

### Steps to Run

1. Clone the repository.
2. Ensure Docker is running.
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the Docker containers:
   ```bash
   docker-compose up
   ```
5. Access the application via the provided endpoints.

---
