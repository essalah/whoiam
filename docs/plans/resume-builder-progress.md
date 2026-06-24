# Resume Builder Platform — Progress Tracker

**Charter:** [resume-builder-charter.md](./resume-builder-charter.md)  
**Started:** 2026-06-24  
**Current phase:** Phase 1 — Multi-user identity and tenancy  
**Overall status:** In progress

## Status legend

- ⬜ Not started
- 🔄 In progress
- ✅ Complete and verified
- ⚠️ Blocked or requires a decision

## Delivery board

| Phase | Workstream | Status | Verification / notes |
|---|---|---:|---|
| 0 | Project charter and scope | ✅ | Charter created and reviewed as the implementation baseline |
| 0 | Product decisions and UX wireframes | ⬜ | Recommended defaults are recorded in charter section 18; explicit approval still pending |
| 1 | Forward-only resume-builder database migration | ✅ | V3 adds the foundation without changing legacy V1/V2; PostgreSQL runtime application remains to be verified when Docker is available |
| 1 | Customer identity persistence foundation | ✅ | UUID customer accounts and hashed refresh/reset-token persistence implemented |
| 1 | Resume/template/publication persistence foundation | ✅ | Resume-owned entities, versioned templates, publication state, and owner-scoped repositories implemented |
| 1 | Customer registration/login and JWT wiring | ✅ | Customer/admin JWT principal types are separated; registration and login are public; resume APIs require authentication |
| 1 | Refresh, logout, forgot/reset password APIs | ⬜ | Persistence exists; lifecycle endpoints are the next identity slice |
| 1 | Transactional one-free-resume entitlement service | ✅ | FREE plan row lock serializes count-and-create in V0; API returns `RESUME_LIMIT_REACHED` |
| 1 | Tenant-isolation and concurrency tests | 🔄 | Owner-scoped service tests pass; PostgreSQL concurrent-create integration test remains |
| 1 | Existing portfolio data migration | ⬜ | Implement after new persistence model is verified |
| 1 | Registration/login/dashboard frontend | ⬜ | Starts after auth API contract is stable |
| 2 | Resume content API and builder | ⬜ | Not started |
| 3 | Default template, public links, and PDF | ⬜ | Not started |
| 4 | Hardening and launch | ⬜ | Not started |
| Next | Paid multiple resumes and additional templates | ⬜ | Deferred by Version 0 scope |

## Active agent lanes

| Lane | Owner | Scope | Shared-file rule |
|---|---|---|---|
| Database/review | `cavecrew_migration_builder` | V3 migration and cross-lane review | ✅ Migration delivered; final review completed/in progress |
| Identity | `cavecrew_identity_builder` | Customer account persistence, registration/login, focused tests | ✅ Delivered and verified |
| Resume domain | `cavecrew_resume_builder` | Resume persistence, root API, entitlement, focused tests | ✅ Delivered and verified |
| Integration lead | Root agent | Contracts, security, services, tests, reviews, documentation | Integrates after agent lanes complete |

## Phase 1 gate

Phase 1 is complete only when:

- Two customer accounts can register and authenticate independently.
- Each free account can create one resume.
- Concurrent create requests cannot bypass the one-resume entitlement.
- A user cannot read, update, publish, or delete another user's resume.
- Draft resumes remain private.
- Migrations apply cleanly from the existing V1/V2 database.
- Backend tests pass and the API contract is documented.
- Existing portfolio data has a verified migration path.

## Progress log

### 2026-06-24

- ✅ Reframed the project from a single-owner portfolio into a multi-user resume builder.
- ✅ Added the full project charter, architecture, domain model, phased plan, risks, and launch criteria.
- ✅ Agreed to continue implementation with specialized parallel agents and one integration lead.
- 🔄 Started Phase 1 with separate database, identity, and resume-domain lanes.
- 🔄 Established this tracker as the canonical implementation status board.
- ✅ Added forward-only V3 schema with user accounts, resume ownership, normalized sections, template versions, publication, entitlements, tokens, and audit events.
- ✅ Added customer registration/login with normalized email, BCrypt hashing, server-controlled Terms version, generic login failures, and UTF-8 password byte validation.
- ✅ Added explicit ADMIN/CUSTOMER JWT principal typing, disabled-account rejection, 15-minute access tokens, exact public auth routes, and mandatory production JWT secret configuration.
- ✅ Added resume list/create/detail endpoints with owner-scoped lookups and transactional one-free-resume enforcement.
- ✅ Hardened all resume-content repositories with owner-scoped query methods.
- ✅ Fixed cross-resume achievement validation and verified the full JPA application context starts against the H2 development profile.
- ✅ Added 15 focused identity/resume service tests plus a customer/admin route-boundary test; the complete backend suite now passes 28 tests.
- ✅ Restricted customer resume APIs to `ROLE_USER`; administrator tokens cannot access customer resumes.
- ⚠️ Authentication rate limiting, refresh-token rotation, password reset, and real PostgreSQL migration/concurrency verification remain before the Phase 1 gate can close.

## Decisions and blockers

No implementation blocker currently prevents the tenancy foundation. The following product defaults are being used provisionally until changed:

- First resume is free and has no watermark.
- Resume drafts are private by default.
- Public resumes use random, owner-rotatable links and default to `noindex`.
- The customer product will use React Router; Angular remains an internal administration surface.
- The initial template key is `ats-classic-v1`, with A4 as the default page size.
- The data model supports multiple resumes; plan entitlement limits Version 0 users to one.

## Verification history

| Date | Check | Result |
|---|---|---|
| 2026-06-24 | Charter file created and readable | ✅ |
| 2026-06-24 | Parallel work divided into non-overlapping file ownership | ✅ |
| 2026-06-24 | Backend unit and MVC suite — 28 tests | ✅ 28 passed, 0 failures/errors |
| 2026-06-24 | Full Spring/JPA development-profile startup | ✅ Started successfully with 26 repositories |
| 2026-06-24 | `git diff --check` for tracked changes | ✅ |
| 2026-06-24 | V3 applied to a real PostgreSQL instance | ⚠️ Pending; Docker daemon unavailable |
