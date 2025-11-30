# Order Management System

This project it is created using **Sprinboot** , **Swagger** for API documentation and **Postgres** designed to help to
manage orders from the users.
---
## Project Structure

```
order-management/
├── src/
│   ├── main/
│   │   ├── java/com/digitalNAO/ordermanagement/orderapp
│   │   │   ├── OrderappApplicacion.java
│   │   │   ├── config/
│   │   │   │   └── SwaggerConfig.java
│   │   │   ├── entity/
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Order.java
│   │   │   │   └── OrderItem.java
│   │   │   ├── repository/
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── OrderItemRepository.java
│   │   │   ├── service/
│   │   │   │   ├── CustomerService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   └── OrderService.java
│   │   │   ├── dto/
│   │   │   │   ├── OrderCreateDTO.java
│   │   │   │   ├── OrderResponseDTO.java
│   │   │   │   └── OrderItemDTO.java
│   │   │   └── controller/
│   │   │       ├── CustomerController.java
│   │   │       ├── ProductController.java
│   │   │       └── OrderController.java
│   └── test/
├── pom.xml
└──  README.md
```
## Features
- API documentation in Swagger
- Postgres with development and production database
- Test

## 1. Instalation
### Prerequisites
- **Java SDK 17+**
- **Postgres 42.7.7**
- **Maven 3.9+**
- **Git**
---
### 2. Clone the Repository

```bash
git clone https://github.com/LuisGLP/OrderManagement-challenge5.git
cd orderapp
```
### 3. Create Local Databases in PostgreSQL
Run the following SQL script in pgAdmin or your terminal (e.g., psql):
```sql
-- Development Database
CREATE DATABASE online_store_dev
WITH
OWNER = postgres
ENCODING = 'UTF8'
LC_COLLATE = 'en_US.utf8'
LC_CTYPE = 'en_US.utf8'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;

-- Production Database
CREATE DATABASE online_store_prod
WITH
OWNER = postgres
ENCODING = 'UTF8'
LC_COLLATE = 'en_US.utf8'
LC_CTYPE = 'en_US.utf8'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;
```
## 5. Then, connect to online_store_dev and run the following to create tables and insert sample data:

The full SQL script for tables, indexes, and sample data is included in the root of this repository).

## 6.Configure the Database Connection
In the file ```src/main/resources/application-dev.yml```
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/online_store_dev
    username: postgres
    password: yourpassword
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true

server:
  port: 8081
```
## 7. Build and Run the Project
Run the following command from the root directory:
```bash
mvn spring-boot:run
```
Or package it and run:
```bash
mvn clean package
java -jar target/orderapp-0.0.1-SNAPSHOT.jar
```
## 8. Access the Application
Swagger UI: http://localhost:8081/swagger-ui/index.html

API Root: http://localhost:8081/api

## CRUD Endpoints

### Complete API Endpoints Reference

####  Customers API

| Method | Endpoint | Parameters | Request Body | Response | Description |
|--------|----------|------------|--------------|-------|-------------|
| POST | `/api/customers` | - | Customer JSON | Customer (201) | Create a new customer |
| GET | `/api/customers` | - | - | Customer[] (200) | Get all customers |
| GET | `/api/customers/{id}` | `id` (path) | - | Customer (200) | Get customer by ID |
| PUT | `/api/customers/{id}` | `id` (path) | Customer JSON | Customer (200) | Update customer |
| DELETE | `/api/customers/{id}` | `id` (path) | - | (204) | Delete customer |

###  Products API

| Method | Endpoint | Parameters | Request Body | Response | Description |
|--------|----------|------------|--------------|--------|-------------|
| POST | `/api/products` | - | Product JSON | Product (201) | Create a new product |
| GET | `/api/products` | `activeOnly` (query, optional) | - | Product[] (200) | Get all or active products |
| GET | `/api/products/{id}` | `id` (path) | - | Product (200) | Get product by ID |
| GET | `/api/products/search` | `name` (query) | - | Product[] (200) | Search products by name |
| PUT | `/api/products/{id}` | `id` (path) | Product JSON | Product (200) | Update product |
| DELETE | `/api/products/{id}` | `id` (path) | - | (204) | Delete product |

### Orders API (Main Resource)

| Method | Endpoint | Parameters | Request Body | Response | Description |
|--------|----------|------------|--------------|--------|-------------|
| POST | `/api/orders` | - | OrderCreateDTO | OrderResponseDTO (201) | Create a new order |
| GET | `/api/orders` | - | - | OrderResponseDTO[] (200) | Get all orders |
| GET | `/api/orders/{id}` | `id` (path) | - | OrderResponseDTO (200) | Get order by ID |
| GET | `/api/orders/customer/{customerId}` | `customerId` (path) | - | OrderResponseDTO[] (200) | Get orders by customer |
| PATCH | `/api/orders/{id}/status` | `id` (path)<br>`status` (query) | - | OrderResponseDTO (200) | Update order status |
| DELETE | `/api/orders/{id}` | `id` (path) | - |  (204) | Delete order |

### Data Models

#### Customer
```typescript
{
  "id": number,
  "name": string,
  "phone": number,
  "email": string
}
```

#### Product
```typescript
{
  "id": number,
  "name": string,
  "description": string,
  "price": number,
  "isActive": boolean
}
```

#### OrderCreateDTO
```typescript
{
  "customerId": number,
  "items": [
    {
      "productId": number,
      "quantity": number
    }
  ]
}
```

#### OrderResponseDTO
```typescript
{
  "id": number,
  "customerId": number,
  "customerName": string,
  "customerEmail": string,
  "items": [
    {
      "id": number,
      "productId": number,
      "productName": string,
      "quantity": number,
      "unitPrice": number,
      "subtotal": number
    }
  ],
  "totalAmount": number,
  "status": string,
  "createdAt": string,
  "updatedAt": string
}
```
---
## User Stories
| **ID**   | **User Story**                                                                                                                                              | **Priority** | **Acceptance Criteria**                                                                                                                          |
| :------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------- | :----------- | :----------------------------------------------------------------------------------------------------------------------------------------------- |
| **US01** | As an **administrator**, I want to **configure different environments (dev, test, prod)** so that the system behaves correctly depending on the deployment. | High         | The system must support **Spring profiles** and load configurations dynamically for each environment.                                            |
| **US02** | As a **developer**, I want to **document all API endpoints using Swagger** so that other teams can understand and test the services easily.                 | High         | Swagger UI must automatically generate documentation for each endpoint and allow visual testing through a browser interface.                     |
| **US03** | As a **user**, I want to **place and track orders** so that I can verify the current status of my purchases.                                                | High         | The API must allow creating, reading, and updating orders in the PostgreSQL database and return real-time status information.                    |
| **US04** | As a **system administrator**, I want to **monitor database connections and handle failures** so that service interruptions are minimized.                  | Medium       | The system must include error-handling logic for failed database nodes and provide meaningful error messages or fallback procedures.             |
| **US05** | As a **project manager**, I want to **validate the integration and documentation of all services** so that the solution is stable and meets business needs. | Medium       | The application must successfully run integration tests using Swagger, Spring Boot, and the configured PostgreSQL database for all environments. |

## Requirements and Deliverables
| **ID**    | **Requirement Description**                                                                                                     | **Type**   | **Associated User Story** |
| :-------- | :------------------------------------------------------------------------------------------------------------------------------ | :--------- | :------------------------ |
| **REQ01** | Configure **Spring profiles** (`application-dev.yml`, `application-prod.yml`, etc.) for environment-based database connections. | Functional | US01, US04                |
| **REQ02** | Integrate **Swagger/OpenAPI** to automatically generate REST API documentation.                                                 | Functional | US02, US05                |
| **REQ03** | Implement **CRUD operations for orders** with PostgreSQL as the database.                                                       | Functional | US03                      |
| **REQ04** | Implement **error handling and logging** for failed database nodes or connection issues.                                        | Functional | US04                      |
| **REQ05** | Run **integration tests** to validate environment configuration, API documentation, and database connectivity.                  | Functional | US05                      |

## Tech Stack
- **Springboot 3.6**
- **Postgres  42.7**
- **Swagger**
- **Postman (API Testing)**
- **JUnit 4.13**
---

## License

MIT License © 2025 Luis Alberto García López