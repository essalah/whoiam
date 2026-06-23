# Portfolio System Progress Tracker

_Last updated: 2026-06-22_

## Summary

The portfolio system has completed the Phase 1 resume extraction baseline and a substantial Phase 2/3/5 implementation pass. The backend API, security, validation, storage endpoint, Swagger/OpenAPI exposure, first Angular admin UI, Docker/Compose setup, CI workflow, and deployment notes are now in place. The next work should focus on testing real admin CRUD flows against a running backend, improving complex admin editors, finishing image upload UX, expanding endpoint documentation, and then starting the public React frontend.

## Completed

- Phase 1 resume extraction: `data/resume_data.json` exists and contains structured profile, experience, education, skills, languages, and project data.
- Phase 2 domain model is implemented in `backend/src/main/java/com/elhachmi/portfolio/entity`.
- DTOs are implemented in `backend/src/main/java/com/elhachmi/portfolio/dto/request` and `.../dto/response`.
- Repositories are implemented in `backend/src/main/java/com/elhachmi/portfolio/repository`.
- Mappers are present in `backend/src/main/java/com/elhachmi/portfolio/mapper`.
- Flyway migrations exist: `backend/src/main/resources/db/migration/V1__initial_schema.sql` and `V2__seed_data.sql`.
- Backend dependencies are configured in `backend/pom.xml` for Spring Boot, JPA, security, validation, Flyway, JWT, Cloudinary, and SpringDoc.
- Application configuration for datasource, JWT, Cloudinary, and Swagger is defined in `backend/src/main/resources/application.yml`.
- Cloudinary storage upload service and admin file upload endpoints are implemented.
- Admin social link CRUD and profile social management are implemented.
- Angular admin dashboard first pass has been created in `admin/` and the standalone app build is validated.
- Swagger UI and OpenAPI docs have been verified at `/swagger-ui.html` and `/api-docs` using a local dev profile.
- Backend request contracts now use stronger enum typing, URL limits, and structured validation errors with field-level violations.
- Angular admin dashboard now includes login, JWT auth state, interceptor, guard, dashboard shell, overview, and reusable CRUD management routes.
- Docker, Compose, environment template, CI workflow, and deployment notes have been added for backend/admin/PostgreSQL production readiness.

## Current status

- Backend structure now includes models, repositories, DTOs, mappers, services, controllers, and JWT security wiring.
- Implemented backend components:
  - `service` package with implementations for Profile, Auth, Experience, Project, Skill, Education, Certification, and Language
  - `controller` package with public portfolio endpoints and admin CRUD endpoints for core entities
  - JWT security configuration, authentication filter, and login endpoint
  - validation handling via `RestExceptionHandler`
  - structured API error responses with stable error codes and validation violation details
  - slug generation utility and entity mapping for project tech stacks
- Verified runtime API documentation:
  - `/swagger-ui.html` redirects correctly to `/swagger-ui/index.html`
  - `/api-docs` returns OpenAPI JSON successfully
- Angular admin dashboard now includes:
  - `/login` JWT login flow backed by `/api/v1/admin/auth/login`
  - signal-backed auth state persisted in `localStorage`
  - functional auth interceptor and route guard
  - protected dashboard shell, overview stats, and reusable CRUD manager
- Platform scaffolding now includes:
  - `backend/Dockerfile`
  - `admin/Dockerfile` and Nginx config
  - `docker/compose.yml`
  - `.env.example`
  - `.github/workflows/ci.yml`
  - `docs/plans/deployment.md`
- Remaining components:
  - Deepen Angular CRUD forms for complex nested fields such as achievements and project tech stack IDs
  - Add image upload controls into the admin entity editor UI
  - Continue SpringDoc/OpenAPI metadata coverage for all endpoints
  - Public React frontend planning and implementation
- Angular admin dashboard has moved beyond scaffold into an integrated first pass.

## Next steps

1. Test the Angular admin CRUD flows against a running backend and refine complex fields.
2. Add image upload controls and previews to the admin project/profile editors.
3. Continue SpringDoc/OpenAPI metadata coverage and ensure endpoint responses are documented.
4. Start the public React Router v7 frontend now that backend/admin contracts are stable enough for first integration.

## Verification completed

- `mvn compile` in `backend/` passes.
- `npm run build` in `admin/` passes.
- `docker compose -f docker/compose.yml config` validates the Compose file.
- `rg "BehaviorSubject" admin/src backend/src` returns no usage, keeping the Angular admin aligned with the Signals-only rule.
- Swagger UI and OpenAPI JSON were previously verified at `/swagger-ui.html` and `/api-docs`.

## Pending work

- Run the Angular admin against the backend and verify create/update/delete flows entity by entity.
- Replace generic JSON/text entry for nested admin fields with purpose-built form controls.
- Add image upload controls, previews, and upload error handling in admin project/profile editors.
- Add more SpringDoc endpoint metadata and examples where the API contract is still sparse.
- Add backend/API integration tests for auth, validation errors, protected routes, and storage failure paths.
- Start the public React Router v7 frontend after the admin/backend contract has been exercised.

## Remaining roadmap

- Phase 2: Backend API is functionally implemented; remaining work is deeper documentation, runtime integration testing, and focused regression coverage.
- Phase 3: Angular admin first pass is implemented; remaining work is real CRUD verification, richer forms, image upload UI, and polish.
- Phase 4: React public frontend remains pending.
- Phase 5: Dockerization, env template, CI, and deployment notes are present; remaining work is full end-to-end deployment validation.

## Multi-Agent Execution Plan

Use this board when splitting work across multiple AI agents. The goal is to keep each agent on a narrow slice with clear inputs, outputs, and merge conditions.

| Agent lane            | Scope                                                                                                      | Depends on                                          | Output                                                        |
| --------------------- | ---------------------------------------------------------------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------------------- |
| Backend hardening     | Request validation, centralized error responses, DTO enum coverage, storage errors, OpenAPI cleanup        | Existing backend services/controllers               | Done: tighter backend API contract ready for frontend testing |
| Angular admin agent   | Login, auth guard, dashboard shell, CRUD pages, image upload, API integration                              | Stable admin auth endpoints and CRUD contracts      | Mostly done: first UI pass is wired; image upload UI remains  |
| Public frontend agent | React Router v7 app, loaders, routes, SEO metadata, public portfolio pages                                 | Swagger-stable backend API and agreed response DTOs | Public portfolio site consuming backend data                  |
| Platform agent        | Dockerfiles, compose, env templates, CI/CD workflow, deployment notes                                      | Stable backend and frontend app structure           | Done: repeatable backend/admin/PostgreSQL build setup         |
| Review agent          | Cross-cutting QA, integration checks, regression review after merges                                       | Completed slices from other agents                  | Feedback on correctness, gaps, and merge risk                 |

### Suggested handoff order

1. Backend hardening is complete enough for frontend integration; keep tests/docs moving as follow-up work.
2. Continue Angular admin verification and begin public frontend work in parallel.
3. Extend platform work once the public frontend exists and the deployment target is chosen.
4. Review agent runs after each slice to catch integration drift early.

### What to hand each agent

- A single target area, not the whole roadmap.
- The relevant files and API boundaries only.
- The acceptance check they must satisfy before handoff.
- The next agent that depends on their output.

### Merge rule

- Frontend work can now proceed from the current backend contract, but each new API shape change should update the admin/frontend clients and docs together.
- Do not expand deployment automation from speculative assumptions; use the concrete backend, admin, and future public frontend outputs produced by the other agents.
