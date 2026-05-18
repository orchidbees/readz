# 📚 Readz

The backend for a book tracker application, which allows users to manage their personal reading lists.

## Content
- [Features](#features)
- [Database Design](#database-design)
- [Development](#development)
- [Local Testing](#local-testing)

## Features
(Note: this is a portfolio project so this section exists purely to provide a summary for the benefit of any viewers!)

- **CRUD functionality** for books and authors with separate APIs for users to manage their personal reading lists
- Database migrations managed with **Flyway**
- **Authentication** and stateless JWT-based **authorisation** with **Spring Security**

## Database Design

Doc on its way...

## Development

Pre-requisites:
- JDK 25+
- Maven 3.9.x+

Build:

```shell
mvn clean install
```

Run:
```shell
mvn clean spring-boot:run
```

## Local Testing

Spin up the Postgres database:

```shell
docker compose up
```

Run the application with the local profile enabled:
```shell
mvn clean spring-boot:run -Dspring-boot.run.profiles=local
```

The Flyway migrations will automatically be applied on the Postgres container. To seed in test data:
```shell
cd src/test/resources/
./insert-test-data.sh # You may need to run chmod +x first!
```

The above script will insert some book, author and user data into the database. One admin and one regular user will be created, with both using the plaintext password **test**.

To generate a JWT for e.g. the admin user:
```shell
curl -H "Content-Type:application/json" \
http://localhost:8080/api/auth/token \
-d '{"username": "admin", "password":"test"}' | jq .
```

The token can now be attached to subsequent requests:
```shell
curl -H "Content-Type:application/json" \
-H "Authorization: Bearer ${JWT}" \
http://localhost:8080/${ENDPOINT} | jq .
```

The APIs are documented with Swagger. To see the full OpenAPI specification, navigate to: http://localhost:8080/swagger-ui.html while the application is running.