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

## Instalation
### Prerequisites
- **Java SDK 17+**
- **Postgres 42.7.7**
---
## CRUD Endpoints

| Method     | Endpoint         | Description     |
|------------|------------------|-----------------|
| **GET**    | `/api/users/`    | Get all users   |
| **GET**    | `/api/users/:id` | Get users       |
| **POST**   | `/api/users`     | Create new User |
| **PUT**    | `/api/users/:id` | Update user     |
| **DELETE** | `/api/users/:id` | Delete user     |         
|            |                  |                 |

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