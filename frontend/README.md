# Notes Frontend

React frontend for the Notes application.

The frontend provides the interface for creating, editing, deleting, searching and filtering notes.

## Technology Stack

* React
* Vite
* Tailwind CSS
* Axios
* Nginx

## Project Structure

```text
frontend/
├── public/
├── src/
│   ├── components/
│   ├── App.jsx
│   └── ...
│
├── Dockerfile
├── nginx.conf
├── package.json
├── package-lock.json
└── README.md
```

## Features

* Note creation
* Note editing
* Note deletion
* Note browsing
* Search by title
* Search by content
* Filtering by author
* Filtering by date range
* Light and dark themes
* Responsive layout
* Editorial visual design

## Local Development

Install dependencies:
```bash
npm install
```

Start the development server:
```bash
npm run dev
```
The Vite development server is normally available at: `http://localhost:5173`

## Environment Configuration

The backend API base URL is configured through: `VITE_API_BASE_URL`

For local development:
```env
VITE_API_BASE_URL=http://localhost:8080/api/notes
```

For the containerized application:
```env
VITE_API_BASE_URL=/api/notes
```

Vite environment variables are embedded into the frontend during the build process. `VITE_*` variables must not contain secrets because they are ultimately exposed to the browser. *(Note: Make sure your `.env` is in `.gitignore` and document required values in `.env.example`)*.

## Production Build

Build the frontend:
```bash
npm run build
```
The production assets are generated under: `dist/`

Preview the production build:
```bash
npm run preview
```

## Docker

The production frontend uses a multi-stage Docker build:

```text
Node
  │
  ├── Install dependencies
  │
  ├── Build React application
  │
  ▼
dist/
  │
  ▼
Nginx
  │
  ▼
Production container
```

Build the image:
```bash
docker build   --build-arg VITE_API_BASE_URL=/api/notes   -t notes-frontend:v1 .
```
The container exposes port: `80`. (The host-facing port is configured through Docker Compose).

## Nginx

Nginx serves the compiled React application and proxies API requests to the backend.

```text
Browser
   │
   │ /api/*
   ▼
Nginx
   │
   ▼
backend:8080
```

This allows the browser to communicate with a single origin (`http://localhost:3000`). The normal containerized deployment therefore does not require browser-side CORS between the frontend and backend.

## Development vs Containerized Deployment

**Development**
```text
Browser
   │
   ▼
Vite :5173
   │
   ▼
Spring Boot :8080
```

**Containerized**
```text
Browser
   │
   ▼
Nginx :3000
   ├── React static assets
   │
   └── /api/*
          │
          ▼
       Backend :8080
```

## Quality Checks

Before committing frontend changes, run:
```bash
npm run build
```
The production build should complete successfully.

For application-level changes, verify:
* Note creation, editing, and deletion
* Title and content search
* Author and date filtering
* Theme switching
* Responsive layout
* Backend connectivity

## Docker Compose

The frontend is normally started through the root-level Compose configuration:
```bash
docker compose up
```
The application is available at: `http://localhost:3000`

When working with the complete application stack, Docker Compose should be preferred over manually running the frontend container.

## Deployment Model

The production frontend follows a static deployment model:

```text
Source → Vite build → Static assets → Nginx image → Container → Deployment
```

A Node.js runtime is not required by the frontend after the production build has been generated.