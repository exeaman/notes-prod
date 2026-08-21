# Notes Backend

Spring Boot REST API for the Notes application.

The backend provides CRUD operations, search functionality, persistence, validation, database migrations and API documentation.

## Technology Stack

* Java 25
* Spring Boot 4
* Spring Web MVC
* Spring Data JPA
* Hibernate
* PostgreSQL 17
* Flyway
* Spring Boot Actuator
* OpenAPI / Swagger
* JUnit 5
* Gradle

## Project Structure

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/notes/backend/
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       ├── application.yaml
│   │       ├── application-dev.yaml
│   │       ├── application-test.yaml
│   │       └── application-prod.yaml
│   │
│   └── test/
│
├── build.gradle
├── Dockerfile
└── README.md
```

## API

**Base path:** `/api/notes`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/notes` | Create a note |
| GET | `/api/notes` | Get all notes |
| GET | `/api/notes/{id}` | Get a note |
| PUT | `/api/notes/{id}` | Update a note |
| DELETE | `/api/notes/{id}` | Delete a note |
| GET | `/api/notes/title/{title}` | Find by exact title |
| GET | `/api/notes/search/title` | Search by title |
| GET | `/api/notes/search/content` | Search by content |
| GET | `/api/notes/author/{author}` | Find by author |
| GET | `/api/notes/search/date` | Search by date range |

## API Documentation

When the backend is running:
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
* **OpenAPI specification:** `http://localhost:8080/v3/api-docs`

## Configuration

The backend uses Spring profiles: `application.yaml`, `application-dev.yaml`, `application-test.yaml`, `application-prod.yaml`.

Select a profile with:
```bash
SPRING_PROFILES_ACTIVE=dev
```

Database configuration is supplied through environment variables in containerized environments:
* `DATABASE_URL`
* `DATABASE_USERNAME`
* `DATABASE_PASSWORD`

## Database

PostgreSQL is used as the persistence layer. 
* **Default database:** `notes_db`

Flyway manages database schema changes. Migration files are located in `src/main/resources/db/migration/` (Example: `V1__create_notes_table.sql`).

Hibernate validates the existing schema rather than creating or modifying it in production-oriented environments.

## Local Development

Start the backend:
```bash
./gradlew bootRun
```

Start with a specific profile:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Run tests:
```bash
./gradlew test
```

Run the complete build:
```bash
./gradlew clean build
```

## Docker

Build the backend:
```bash
./gradlew clean build
docker build -t notes-backend:v1 .
```
The container exposes port: `8080`.

*For the complete application, Docker Compose is preferred over running the backend container manually.*

## Testing

Run the complete test suite:
```bash
./gradlew test
```
Test reports are generated under: `build/reports/tests/test/`

## Database Migration Workflow

When changing the database schema:
1. Create a new Flyway migration (e.g., `V2__add_categories.sql`).
2. **Do not modify an already-applied migration.**
3. Start the application.
4. Verify the migration succeeds.
5. Run the test suite.
6. Verify Hibernate schema validation passes.
7. Commit the migration with the corresponding application changes.

## Production Considerations

The backend incorporates the following production-oriented practices:
* Environment-specific configuration
* Externalized database configuration
* Flyway database migrations
* Hibernate schema validation
* Automated testing
* REST API architecture
* Actuator health endpoints
* OpenAPI documentation
* Containerized runtime

The project intentionally keeps the business domain small while using the deployment infrastructure to explore production software delivery practices.