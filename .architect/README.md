# Portfolio System — Implementation Reference

This folder contains the implementation plan and task tracking for the portfolio system. All application layers are implemented and locally verified through tests, clean builds, Docker startup, HTTP smoke tests, authentication, and CRUD.

## Structure
- `implementation_plan.md` — Full technical plan
- `task.md` — Progress tracker
- `resume_data_schema.md` — Data model reference

## Phase Sequence
1. ✅ Phase 1 — Resume Extraction & Data Modeling
2. ✅ Phase 2 — Spring Boot Backend core implementation
3. ✅ Phase 3 — Angular Admin Dashboard implementation and production build
4. ✅ Phase 4 — React Router v7 Public Frontend implementation, typecheck, SSR build, and smoke test
5. 🔄 Phase 5 — Local Docker runtime verified; hosted CI and provider deployment pending

## Latest Completed Work
- Backend request validation, enum request contracts, structured API errors, and storage error handling.
- Angular admin login, JWT auth state, interceptor, guard, protected shell, overview, and reusable CRUD manager.
- Rich admin editors for enums, achievements, skill relationships, image upload previews, and structured API errors.
- React Router v7 SSR frontend with six loader routes, SEO metadata, responsive theme, and local visual assets.
- Dockerfiles, four-service Compose file, environment template, CI workflow, and deployment notes.
- Angular 20 LTS and React Router 7.18 security upgrades with clean production dependency audits.

## Next Focus
- Configure Cloudinary and verify the successful upload path.
- Run hosted CI and deploy to the selected provider.
