# Transaction Starter Project

This project provides a Spring Boot starter for managing customer transactions. It is designed to support the core flow of creating, retrieving, updating, and listing transaction records for a customer.

## What this project can handle

The application is intended to manage the following transaction operations:

- Create a new transaction
- Fetch a single transaction by ID
- Update the status of an existing transaction
- Retrieve all transactions for a specific customer

### Transaction model

Each transaction includes:

- Transaction ID
- Customer ID
- Amount
- Currency
- Transaction Type
- Status

### Validation expectations

The application should enforce that required transaction data is present and valid before storing or updating records. At a minimum, the following should be validated:

- Transaction ID is provided
- Customer ID is provided
- Amount is positive and valid
- Currency is supplied and supported
- Transaction type is supplied
- Initial status is valid

Additional business validation can be added as needed to protect the integrity of transaction processing.

### API capabilities

The project supports a REST-style API for:

- Creating transactions
- Reading a transaction by ID
- Updating a transaction status
- Listing all transactions for a given customer

## Example flow

1. Create a transaction with customer and amount details.
2. Retrieve it by transaction ID to confirm it was created.
3. Update its status as the workflow progresses.
4. Query all transactions linked to the same customer.

## Project setup

Clone the repository and run the project tests using:

### Linux / macOS

```bash
./mvnw clean test
```

### Windows

```bat
mvnw.cmd clean test
```

This starter includes:

- Java 17
- Spring Boot
- Maven Wrapper
- Spring Web
- Spring Data JPA
- H2 embedded database
- JUnit / Spring Boot Test
- Sample REST endpoint: `GET /api/sample`

## Known limitations

1. There is no built-in authentication or authorisation layer for protecting transaction endpoints.
2. The project does not include multi-tenant, audit-trail, or payment-processing features beyond the basic transaction lifecycle.

## Notes

This project is intended as a starter for transaction management and can be extended with additional validation, business rules, and API documentation as required.

