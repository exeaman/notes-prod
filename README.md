# Notes — Full-Stack Production Deployment

A production-oriented full-stack Notes application built to explore the complete software delivery lifecycle, from application development and database migrations to containerization, CI/CD, and deployment.

The application is intentionally small. The goal is to understand how a modern application moves from source code to a running production system without introducing unnecessary application complexity.

## Architecture

```text
                           ┌──────────────────────┐
                           │       Browser        │
                           └──────────┬───────────┘
                                      │
                                      │ :3000
                                      ▼
                           ┌──────────────────────┐
                           │    React + Nginx     │
                           │      Frontend        │
                           └──────────┬───────────┘
                                      │
                                   /api/*
                                      │
                                      ▼
                           ┌──────────────────────┐
                           │     Spring Boot      │
                           │       Backend        │
                           └──────────┬───────────┘
                                      │
                                      ▼
                           ┌──────────────────────┐
                           │     PostgreSQL       │
                           │      Database        │
                           └──────────────────────┘
```

## Technology Stack

| Layer | Technology |
|---|---|
| **Frontend** | React, Vite |
| **Styling** | Tailwind CSS |
| **HTTP Client** | Axios |
| **Backend** | Java 25, Spring Boot 4 |
| **Persistence** | Spring Data JPA, Hibernate |
| **Database** | PostgreSQL 17 |
| **Database Migrations** | Flyway |
| **API Documentation** | OpenAPI / Swagger |
| **Testing** | JUnit 5, Spring Boot Test |
| **Web Server** | Nginx |
| **Containerization** | Docker |
| **Orchestration** | Docker Compose |
| **Build** | Gradle |

## Features

* Create, read, update and delete notes
* Search notes by title
* Search notes by content
* Filter notes by author
* Filter notes by date range
* Responsive React interface
* Light and dark themes
* REST API documentation through OpenAPI
* Database schema management through Flyway
* Containerized frontend, backend and database
* Docker Compose based local deployment

## Repository Structure

```text
.
├── backend/
│   ├── src/
│   ├── build.gradle
│   ├── Dockerfile
│   └── README.md
│
├── frontend/
│   ├── src/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── README.md
│
├── docker-compose.yml
└── README.md
```

## Prerequisites

* Java 25
* Node.js
* npm
* Docker
* Docker Compose

*The backend includes the Gradle Wrapper, so a system-wide Gradle installation is not required.*

## Running the Application

The recommended way to run the complete application is through Docker Compose.
From the repository root:

```bash
docker compose up
```

The application is available at:

| Service | Address |
|---|---|
| **Frontend** | `http://localhost:3000` |
| **Backend** | `http://localhost:8080` |
| **Swagger UI** | `http://localhost:8080/swagger-ui/index.html` |
| **PostgreSQL** | `localhost:5432` |

Stop the application:
```bash
docker compose down
```

Rebuild the application images:
```bash
docker compose build
docker compose up
```

## Database Persistence

PostgreSQL uses a named Docker volume: `notes-postgres-data`.
Stopping or recreating containers does not remove the database data. 

To intentionally remove the database and its data:
```bash
docker compose down -v
```
*This should only be used when a clean database is required.*

## Database Migrations

Database schema changes are managed through Flyway. Migration files are located at: `backend/src/main/resources/db/migration/`

Migration naming follows Flyway's versioned migration convention:
* `V1__create_notes_table.sql`
* `V2__add_categories.sql`

The schema lifecycle is:
```text
Application startup
        │
        ▼
Flyway migrations
        │
        ▼
Hibernate schema validation
        │
        ▼
Application startup
```

**Already-applied migrations should not be modified.** Schema changes should be introduced through new migrations.

## Testing

Run the backend test suite:
```bash
cd backend
./gradlew test
```

Run the complete backend build:
```bash
./gradlew clean build
```

Build the frontend:
```bash
cd frontend
npm run build
```

## API

The Notes API is exposed under: `/api/notes`
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
* **OpenAPI specification:** `http://localhost:8080/v3/api-docs`

## Configuration

The backend uses Spring profiles for environment-specific configuration:
* `application.yaml`
* `application-dev.yaml`
* `application-test.yaml`
* `application-prod.yaml`

Database configuration is supplied through environment variables in containerized environments. Expected database variables:
* `DATABASE_URL`
* `DATABASE_USERNAME`
* `DATABASE_PASSWORD`

**Secrets and credentials must not be committed to source control.**

## Deployment Roadmap

**Phase 1 — Application**
* [ ] REST backend
* [ ] React frontend
* [ ] PostgreSQL persistence
* [ ] Automated tests
* [ ] Flyway migrations

**Phase 2 — Containerization**
* [ ] Backend Docker image
* [ ] Frontend Docker image
* [ ] Nginx reverse proxy
* [ ] PostgreSQL container
* [ ] Docker Compose
* [ ] Container networking
* [ ] Persistent database volume

**Phase 3 — CI/CD**
* [ ] Continuous integration
* [ ] Automated test execution
* [ ] Production image builds
* [ ] Image publishing
* [ ] Automated deployment

**Phase 4 — Deployment**
* [ ] Cloud deployment
* [ ] Production configuration
* [ ] Health checks
* [ ] Deployment verification
* [ ] Failure and recovery testing

## Development Philosophy

The project is designed to understand the complete software delivery path:

```text
Code → Test → Build → Package → Containerize → Publish → Deploy → Run → Monitor → Change → Deploy again
```

The objective is practical familiarity with the complete lifecycle rather than deep specialization in individual DevOps technologies.