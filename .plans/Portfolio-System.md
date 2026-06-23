# Portfolio System — End-to-End Execution Plan

# 1. Technical Stack Overview

    Note:
    1- Security Model:
    A clear separation is required between:

    - **Public API** → consumed by the React application
    - **Protected API** → consumed by the Angular admin dashboard

    2- Authentication and authorization should be implemented using:

    - **JWT**
    - or **OAuth2**

    3- Asset Management
    A professional portfolio requires media handling for:

    - Project screenshots
    - Profile images
    - Portfolio assets

    Recommended storage solutions:

    - **Cloudinary**
    - or **AWS S3**

     Database Choice
    The selected database is:
    - **PostgreSQL**

    This is ideal for structured and relational resume/portfolio data.

## Deployment & DevOps

To achieve true production readiness, the system requires:

- Dockerization
- CI/CD pipelines
- Environment-based configuration
- Automated deployment workflows

## Public Frontend

- React Router v7 (Framework Mode)
- Tailwind CSS
- Framer Motion

## Admin Dashboard

- Angular (latest version)
- Signals-based state management
- Angular Material + Tailwind CSS

## Backend

- Spring Boot 3.x
- Java 21+
- Spring Security
- JPA / Hibernate

## Database

- PostgreSQL

## Storage

- Cloudinary API (for project images). and make it open to integrate any other alternative solution like S3 etc..

# 2. Phase 1 — Content Extraction & Data Modeling

## Objective

Convert the raw resume into a structured and normalized schema.

## Task 1.1 — Resume Extraction

Analyze the provided resume and extract the data into a `resume_data.json` file with the following structure:

```json
{
  "basics": {},
  "languages": [],
  "hight_skils": [],
  "work_experience": [],
  "education": [],
  "skills": [],
  "projects": [],
  "certifications": []
}
```

## Task 1.2 — PostgreSQL Schema Design

### Profile

Fields:

- Name
- Bio
- Social links
- Contact information

### Experience

Fields:

- Company
- Role
- Duration
- Achievements (array)

### Project

Fields:

- Title
- Description
- Tech stack (array)
- Image URL
- Live link
- GitHub link

### Skill

Fields:

- Name
- Category
  - Frontend
  - Backend
  - DevOps
  - etc.
- Proficiency level

# 3. Phase 2 — Spring Boot Backend (The Core)

## Objective

Build a RESTful API with clear separation between public and private access layers.

## Task 2.1 — Security Implementation

Implement **Spring Security** with JWT authentication.

### Public Endpoints

```http
GET /api/v1/portfolio/**
```

Access:

- `PermitAll`

Used by:

- React public frontend

### Protected Endpoints

```http
/api/v1/admin/**
```

Access:

- `HasRole('ADMIN')`

Used by:

- Angular admin dashboard

## Task 2.2 — API Endpoints

Develop controllers and services for:

- Profile
- Experience
- Projects
- Skills
- Certifications

Also implement:

- Image upload service
- Cloudinary/S3 integration

## Task 2.3 — API Documentation

Setup:

- SpringDoc OpenAPI
- Swagger UI

Purpose:

- Frontend/backend contract validation
- API testing
- Development consistency

# 4. Phase 3 — Angular Admin Dashboard (The Controller)

## Objective

Create a secure internal dashboard for managing portfolio content without modifying code.

## Task 3.1 — Core Setup

Requirements:

- Angular Standalone Components
- Signals-based state management

## Task 3.2 — Features

### Login Page

Responsibilities:

- Authenticate against Spring Boot backend
- Store JWT securely
  - HttpOnly cookies
  - or LocalStorage

### Dynamic Resume Editor

Capabilities:

- Add experience
- Edit experience
- Delete experience
- Manage projects dynamically

### Image Preview

Features:

- Upload project screenshots
- Preview images before saving

## Task 3.3 — HTTP Interceptors

Create an `AuthInterceptor` that:

- Automatically attaches JWT tokens
- Handles authentication errors globally

# 5. Phase 4 — React Router v7 Public App (The Resume)

## Objective

Build a premium, high-performance public portfolio inspired by modern elite portfolio websites such as **juncie.com**.

## Task 4.1 — React Router v7 Framework Mode

### Data Fetching

Use:

- React Router v7 Loaders

Benefits:

- Server-side data loading
- SEO optimization
- Zero CLS (Cumulative Layout Shift)

### SEO & Social Sharing

Implement:

- Dynamic meta tags
- OpenGraph support
- Linkedin post
- Twitter cards
- Facebook post

## Task 4.2 — UI/UX Design

### Visual Style

Requirements:

- Support Dark mode
- Minimal typography
- Subtle grain textures
- Clean spacing

### Animations

Use:

- Framer Motion

Effects:

- Reveal-on-scroll
- Smooth transitions
- Page animations

### Responsiveness

Ensure:

- Mobile-first design
- Resume-friendly layout
- Tablet and desktop optimization

## Task 4.3 — Advanced Layout Patterns

Implement modern portfolio patterns such as:

- Bento Box layouts
- Smooth Timeline navigation
- Animated section transitions

# 6. Phase 5 — Production Readiness & Deployment

## Objective

Prepare the system for secure, scalable production deployment.

## Task 5.1 — Validation

Implement DTO validation using:

```java
@Valid
```

Purpose:

- Prevent malformed data
- Enforce API contracts

## Task 5.2 — Dockerization

### Backend

Create:

```dockerfile
Dockerfile.backend
```

Features:

- Multi-stage JAR build

### Frontend

Create:

```dockerfile
Dockerfile.frontend
```

Features:

- Nginx-based serving
- Optimized static assets

### Infrastructure

Create:

```yaml
docker-compose.yml
```

Responsibilities:

- Run backend
- Run frontend apps
- Run PostgreSQL
- Manage networking

## Task 5.3 — Environment Variables

Use a `.env` file for:

```env
DB_URL=
JWT_SECRET=
CLOUDINARY_URL=
```

## Task 5.4 — Deployment Automation

Setup:

- GitHub Actions
- or deployment scripts

Target platforms:

- Railway
- Render
- AWS
- VPS has debian linux system
