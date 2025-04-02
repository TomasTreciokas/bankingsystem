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

## Testing with Postman

A Postman collection is available for testing the Banking System API. This collection includes requests for all endpoints and tests to validate the responses.

### Creating the Collection

You can create the Postman collection by:

1. Opening Postman
2. Creating a new collection named "Banking System API"
3. Setting up the following request folders:
   - Accounts
   - Customers
   - Search
   - Verification
4. For each endpoint, create corresponding requests using the examples provided below in the "API Usage Examples" section

### Setting Up Environment Variables

Create a new environment in Postman called "Banking System Local" with these variables:

- `baseUrl`: `http://localhost:8081`
- `accountId`: (empty initial value, will be set by tests)
- `individualCustomerId`: (empty initial value, will be set by tests)
- `privateCustomerId`: (empty initial value, will be set by tests)
- `publicCustomerId`: (empty initial value, will be set by tests)

### Running the Tests

1. Make sure the Banking System application is running on port 8081
2. In Postman, open the "Banking System API" collection
3. Follow this testing sequence:
   - Create Account (in Accounts folder)
   - Create Individual Customer (in Customers folder)
   - Create Private Customer (in Customers folder)
   - Create Public Customer (in Customers folder)
   - Verify Account Owners (in Verification folder)
   - Run the search tests (in Search folder)

The collection should include tests that automatically validate responses and store important values like IDs in environment variables, making it easy to run the requests in sequence.

### Key Test Scripts to Add

For the Create Account request, add this test script:
```javascript
pm.test("Status code is 201", function() {
    pm.response.to.have.status(201);
});

var jsonData = pm.response.json();
pm.test("Account created successfully", function() {
    pm.expect(jsonData.id).to.exist;
    pm.environment.set("accountId", jsonData.id);
});
```

For Customer creation requests, add similar scripts to store the customer IDs.

### Using the Collection Runner

For automated testing of all endpoints:

1. Click on the "Collection Runner" button (▶) in Postman
2. Select the "Banking System API" collection
3. Check "Keep variable values" to persist values between requests
4. Click "Run" to execute all requests in sequence

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