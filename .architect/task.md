# Portfolio System Progress Tracker

_Last updated: 2026-06-23_

## Summary

All planned application layers are implemented and locally verified: Spring Boot backend, Signals-based Angular admin, React Router v7 SSR frontend, PostgreSQL migrations, and the four-service Docker stack. Backend tests, clean npm installs, production builds, seeded login, public/admin HTTP boundaries, SSR output, and a live CRUD cycle pass. Only credential- or provider-dependent verification remains.

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
- Backend controller coverage now includes shared authenticated OpenAPI responses and nine focused MockMvc integration tests.
- Angular admin editors now support enums, dates, achievements, project skill relationships, image upload previews, and structured API errors.
- React Router v7 public frontend now implements all six loader-based routes, SSR configuration, SEO metadata, responsive dark/light UI, API fallbacks, and a local hero asset.
- Frontend Docker configuration, Compose service, environment variables, CI steps, and deployment documentation are implemented.

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
- Angular admin has moved beyond the first pass into a contract-aware CRUD implementation.
- Public frontend implementation is present in `frontend/` with route loaders and SSR enabled.
- Compose now describes PostgreSQL, backend, admin, and public frontend services.

## Next steps

1. Add valid Cloudinary credentials and verify a real image upload from the Angular editor.
2. Run the GitHub Actions workflow on the remote repository.
3. Choose a deployment provider and execute the deployment checklist.

## Verification completed

- `mvn compile` in `backend/` passes.
- `npm run build` in `admin/` passes.
- `docker compose -f docker/compose.yml config` validates the Compose file.
- `rg "BehaviorSubject" admin/src backend/src` returns no usage, keeping the Angular admin aligned with the Signals-only rule.
- Swagger UI and OpenAPI JSON were previously verified at `/swagger-ui.html` and `/api-docs`.
- All public frontend route modules export loaders; no route-level `useEffect` data fetching is present.
- Frontend JSON/config structure and local visual asset paths validate.
- `mvn test` passes: 9 tests, 0 failures, 0 errors.
- Clean `npm ci`, TypeScript typecheck, and React Router SSR production build pass.
- Clean Angular `npm ci` and production build pass with Angular-compatible Zone.js.
- Four-service Docker build/start passes; PostgreSQL is healthy and Flyway applies both migrations.
- Public API returns `200`, protected admin API returns `401`, admin UI returns `200`, and public SSR returns `200`.
- Seeded `admin` / `admin123` login returns `200`; live skill create/update/delete returns `200` / `200` / `204`.
- Historical `backend/target` and `admin/.angular` artifacts were removed from Git tracking and are ignored.
- Angular was upgraded to 20 LTS and React Router to 7.18.0; production dependency audits report 0 vulnerabilities.

## Pending work

- Verify a successful real Cloudinary upload after credentials are configured.
- Execute hosted CI and production deployment after a provider is selected.

## Remaining roadmap

- Phase 2: Complete and locally verified.
- Phase 3: Complete; CRUD is verified and Cloudinary success-path testing awaits credentials.
- Phase 4: Complete; install, typecheck, SSR build, and HTTP smoke test pass.
- Phase 5: Local Docker runtime verified; hosted CI and provider deployment remain.

## Multi-Agent Execution Plan

Use this board when splitting work across multiple AI agents. The goal is to keep each agent on a narrow slice with clear inputs, outputs, and merge conditions.

| Agent lane            | Scope                                                                                                      | Depends on                                          | Output                                                        |
| --------------------- | ---------------------------------------------------------------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------------------- |
| Backend hardening     | Request validation, centralized error responses, DTO enum coverage, storage errors, OpenAPI cleanup        | Existing backend services/controllers               | Done: tighter backend API contract ready for frontend testing |
| Angular admin agent   | Login, auth guard, dashboard shell, rich CRUD editors, image upload, API integration                        | Stable admin auth endpoints and CRUD contracts      | Done: implementation and production build complete            |
| Public frontend agent | React Router v7 app, loaders, routes, SEO metadata, public portfolio pages                                 | Swagger-stable backend API and agreed response DTOs | Done: typecheck, SSR build, and HTTP smoke test pass           |
| Platform agent        | Dockerfiles, compose, env templates, CI/CD workflow, deployment notes                                      | Stable backend and frontend app structure           | Done: four-service build/deployment configuration             |
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
