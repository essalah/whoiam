# Portfolio System — Implementation Reference

This folder contains the implementation plan and task tracking for the portfolio system. It reflects the current state after the backend hardening, Angular admin first pass, and deployment scaffolding work.

## Structure
- `implementation_plan.md` — Full technical plan
- `task.md` — Progress tracker
- `resume_data_schema.md` — Data model reference

## Phase Sequence
1. ✅ Phase 1 — Resume Extraction & Data Modeling
2. ✅ Phase 2 — Spring Boot Backend core implementation
3. 🔄 Phase 3 — Angular Admin Dashboard first pass complete; CRUD verification and richer editors pending
4. ⬜ Phase 4 — React Router v7 Public Frontend pending
5. 🔄 Phase 5 — Production & Deployment scaffold present; full deployment validation pending

## Latest Completed Work
- Backend request validation, enum request contracts, structured API errors, and storage error handling.
- Angular admin login, JWT auth state, interceptor, guard, protected shell, overview, and reusable CRUD manager.
- Dockerfiles, Compose file, environment template, CI workflow, and deployment notes.

## Next Focus
- Test admin CRUD flows against the backend.
- Add admin image upload UI and richer controls for nested fields.
- Expand OpenAPI metadata and backend integration tests.
- Start the public React Router v7 frontend.
