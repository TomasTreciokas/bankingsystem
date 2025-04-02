# Banking System

A Spring Boot-based banking system that provides RESTful APIs for managing bank accounts and customers. The application supports different customer types (INDIVIDUAL, PRIVATE, PUBLIC) and features a comprehensive data model with auditing capabilities.

## Features

- Account management (create, retrieve, update)
- Customer management with different types:
  - Individual customers (persons)
  - Private company customers
  - Public company customers
- Account-customer relationship management
- Search functionality with pagination
- Database auditing with Hibernate Envers

## Technologies

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (in-memory)
- Liquibase for database migration
- Hibernate Envers for entity auditing
- Lombok for boilerplate code reduction
- SpringDoc OpenAPI for API documentation

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Running the Application

To run the application locally:

```bash
./mvnw spring-boot:run
```

or with tests skipped:

```bash
./mvnw spring-boot:run -DskipTests
```

The application will start on port 8081 by default.

## API Documentation

The API documentation is available via Swagger UI at:

```
http://localhost:8081/swagger-ui/index.html
```

You can explore all available endpoints, models, and test the APIs directly from the Swagger UI.

## H2 Database Console

The H2 in-memory database console is available at:

```
http://localhost:8081/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:mem:bankdb`
- Username: `sa`
- Password: (leave empty)

## API Usage Examples

### Creating an Account

```bash
curl -X POST http://localhost:8081/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"ACC123456", "balance":1000.00}'
```

### Creating a Customer

#### Individual Customer
```bash
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerType": "INDIVIDUAL",
    "name": "John",
    "lastName": "Doe",
    "phoneNumber": "1234567890",
    "email": "john@example.com",
    "nationalId": "ID12345",
    "dateOfBirth": "1990-01-01",
    "accountIds": [1]
  }'
```

#### Private Customer
```bash
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerType": "PRIVATE",
    "name": "Private Ltd",
    "phoneNumber": "9876543210",
    "email": "contact@private.com",
    "privateReferenceCode": "PRI123",
    "accountIds": [1]
  }'
```

#### Public Customer
```bash
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerType": "PUBLIC",
    "name": "Public Corp",
    "phoneNumber": "5556667777",
    "email": "info@public.com",
    "registrationNumber": "REG456",
    "accountIds": [1]
  }'
```

### Search Customers

```bash
curl -X POST http://localhost:8081/api/customers/search \
  -H "Content-Type: application/json" \
  -d '{"customerType": "INDIVIDUAL"}'
```

Pagination:
```bash
curl -X POST "http://localhost:8081/api/customers/search?page=0&size=10" \
  -H "Content-Type: application/json" \
  -d '{}'
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.