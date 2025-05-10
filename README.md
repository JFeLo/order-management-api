# Order API

This project is a backend API for an order management platform built using Spring Boot. It allows displaying products, creating and managing orders, and includes a mock payment gateway. The API is designed to handle the management of product categories, orders, and user authentication, with a strong focus on business logic, security, and extensibility.

## Features
- **Product Management**: Endpoints to retrieve, create, update, and delete products.
- **Category Management**: Endpoints to retrieve, create, update, and delete product categories.
- **Order Management**: Create, update, finish, and cancel orders with real-time price recalculation and payment status management.
- **Security**: Basic authentication with Spring Security.
- **Unit Testing**: Unit tests to validate core functionalities and business logic.
- **CI/CD Integration**: Continuous Integration and Continuous Deployment using GitHub Actions and Docker.

## Technologies Used
- **Spring Boot**: For building the backend application.
- **Spring Security**: To handle security and authentication.
- **H2 Database**: Used as an in-memory database for development and testing.
- **Docker**: For containerization of the application.
- **GitHub Actions**: For continuous integration and deployment.
- **JUnit**: For writing and running unit tests.

## Project Structure

The project is organized into several key packages to ensure clear separation of concerns and maintainability:

### 1. controller
Contains the REST API controllers that handle HTTP requests and responses. The controllers are responsible for receiving input from clients, calling appropriate services, and returning results.

- **CategoryController**: Manages endpoints for category-related operations, such as retrieving and creating categories.
- **ProductController**: Handles operations for products, such as retrieving and updating product information.
- **OrderController**: Provides endpoints to manage orders, including creating, updating, and finishing orders.

### 2. service
Contains the business logic of the application. Services interact with repositories to perform CRUD operations on the database and implement the core functionality of the application.

- **CategoryService**: Contains business logic related to managing product categories.
- **ProductService**: Responsible for the business logic surrounding product management (e.g., stock checking).
- **OrderService**: Handles the business rules for creating, updating, and processing orders, including payment and stock validation.

### 3. repository
Contains the repository interfaces responsible for interacting with the database. They define methods for retrieving, saving, updating, and deleting entities.

- **CategoryRepository**: Repository interface for managing Category entities.
- **ProductRepository**: Repository interface for managing Product entities.
- **OrderRepository**: Repository interface for managing Order entities.

### 4. model
Contains the entity classes that map to database tables. These models represent the core data structure of the application.

- **Category**: Represents a product category.
- **Product**: Represents a product.
- **Order**: Represents a customer order, including buyer details and payment information.

### 5. config
Contains configuration classes for setting up various application features, such as security or custom beans.

- **GlobalExceptionHandler**: Manages exceptions and error handling across the API.

### 6. exception
Handles custom exceptions and error responses. This package contains custom exception classes and exception handlers for managing errors across the API.

- **ResourceNotFoundException**: Custom exception for resource not found errors.

### 7. test
Contains unit and integration tests to ensure the functionality and correctness of the application.

- **CategoryServiceTest**: Unit tests for the CategoryService.
- **ProductServiceTest**: Unit tests for the ProductService.
- **OrderServiceTest**: Unit tests for the OrderService.

## Setup

### Prerequisites
- Java 21 or later.
- Maven 3.x or later.
- Docker (optional, for containerization).

### Local Setup
Clone the repository:

```bash
git clone https://github.com/yourusername/order-api.git
cd order-api
