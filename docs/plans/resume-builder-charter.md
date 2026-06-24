# Resume Builder Platform — Project Charter and Implementation Plan

**Status:** Approved for phased implementation
**Prepared:** 2026-06-24  
**Working product name:** Resume Builder  
**Current system:** Single-owner portfolio CMS  
**Target system:** Multi-user resume creation and sharing platform

**Implementation status:** See [resume-builder-progress.md](./resume-builder-progress.md).

## 1. Executive summary

The project will evolve from a single-person portfolio into a self-service resume builder inspired by FlowCV. A visitor can register, create and edit a professional resume, preview it using a standard ATS-conscious template, publish it at a stable public link, and export it as a text-based PDF.

Version 0 gives every account one free resume and one default template. The persistence model will support multiple resumes immediately, while an entitlement policy will limit free accounts to one. A later paid release will unlock additional resumes and additional ATS-conscious templates without restructuring the core domain.

The existing Spring Boot, PostgreSQL, React Router, Angular, Flyway, Cloudinary, Docker, and CI foundations will be reused. The key architectural change is to introduce user ownership and a `resume` aggregate so no user can access or mutate another user's information.

## 2. Product vision

### Vision statement

For job seekers who need a polished resume without wrestling with document formatting, Resume Builder is a guided web platform that turns structured career information into an ATS-conscious resume, downloadable PDF, and shareable public page. Unlike a static portfolio CMS, it gives each user ownership, privacy controls, and a foundation for role-specific paid resume variants.

### Product principles

1. **Content first:** users edit structured information; the platform owns layout consistency.
2. **Useful for free:** the first resume remains fully editable, publishable, and downloadable.
3. **Private by default:** a draft is not public until its owner explicitly publishes it.
4. **ATS-conscious output:** semantic sections, selectable text, conventional headings, predictable reading order, and no essential information embedded in images.
5. **Future-ready, not overbuilt:** model multiple resumes and templates now; defer checkout, AI, imports, and advanced customization.
6. **Ownership everywhere:** every private query and mutation is scoped by the authenticated user.

## 3. Business case

The existing application already represents resume-like information, but only for one administrator and with global tables. Converting it into a product creates a repeatable user journey and a clear monetization boundary:

- Acquisition: public landing pages and shared resumes.
- Activation: account created and first resume started.
- Core value: resume completed, previewed, downloaded, or shared.
- Retention: users update the resume as their career changes.
- Revenue in the next iteration: additional resume variants, with room for premium templates or AI assistance later.

FlowCV validates the general model of one free resume, paid multiple versions, ATS-friendly templates, unlimited PDF updates, and unique sharing links. This project should borrow the product pattern, not copy FlowCV's interface, branding, templates, or implementation.

## 4. Objectives and success measures

### Version 0 objectives

- Let a new user register, verify ownership of an email later if required, sign in, and sign out.
- Let an authenticated user create at most one free resume.
- Let the owner edit the resume's core content through a guided builder with autosave.
- Render the same canonical data in a live preview, public page, and PDF.
- Provide one default, responsive, print-ready, ATS-conscious template.
- Let the owner publish/unpublish the resume and share a non-sequential public URL.
- Preserve strict tenant isolation and provide account/resume deletion.
- Deploy and observe the complete flow in production.

### Initial product KPIs

| Measure | Definition | Initial target |
|---|---|---:|
| Registration completion | Successful accounts / registration starts | 70%+ |
| First-resume activation | Users saving personal details and one substantive section within 24h | 60%+ |
| Completion | Activated resumes with personal details plus two substantive sections | 50%+ |
| Value event | Activated users who publish or download within 24h | 40%+ |
| Builder reliability | Successful save requests | 99.5%+ |
| Public reliability | Successful published resume page requests | 99.9%+ |
| Isolation defects | Confirmed cross-user data exposure | 0 |

Targets should be revised after the first 100–250 genuine sign-ups; they are hypotheses, not promises.

## 5. Scope

### Version 0 — committed scope

#### Account

- Email and password registration.
- Login, logout, access-token renewal, and forgot/reset password.
- Password hashing using an adaptive encoder.
- Basic account/profile management and account deletion.
- Acceptance of Terms and Privacy Policy with stored version/timestamp.

#### Resume dashboard and entitlement

- Empty state and “Create my resume” action.
- One free resume maximum per active free account.
- Resume title for the owner's dashboard; it is separate from the candidate's professional title.
- Create, edit, preview, publish/unpublish, download PDF, and delete.
- Data model supports `User 1—N Resume`; Version 0 policy returns a clear limit error when a second creation is attempted.

#### Builder content

- Personal details: full name, headline, email, phone, location, website, photo (optional).
- Professional summary.
- Work experience with achievement/bullet items.
- Education.
- Skills grouped or listed without graphical proficiency bars.
- Projects.
- Certifications.
- Languages.
- Social/custom links.
- Add, edit, remove, and reorder repeatable entries.
- Draft autosave with visible `Saving`, `Saved`, and `Failed` states.
- Validation that guides the user but permits incomplete drafts.

#### Template, preview, and PDF

- One template with immutable key `ats-classic-v1`.
- One-column primary reading order, conventional headings, legible typography, and restrained color.
- A4 and Letter support is desirable; choose A4 as the default if only one ships in Version 0.
- Browser preview based on the same renderer used for the public view.
- Server-controlled, text-based PDF generation with embedded fonts and clickable links.
- No watermark for the first free resume.

#### Public sharing

- Draft resumes are private by default.
- Explicit publish and unpublish controls.
- Public route such as `/r/{publicSlug}` using a random, non-sequential identifier.
- Public route contains only the resume content selected for publication, never account metadata.
- Owner can rotate/revoke the public link.
- Published page has sensible Open Graph metadata and print/download behavior.
- Unpublished, revoked, or deleted resumes return `404`, avoiding existence disclosure.

#### Platform and operations

- PostgreSQL migrations, OpenAPI contract, automated tests, Docker support, CI, structured logs, metrics, error monitoring, backups, rate limiting, and health checks.
- Cloudinary remains optional media storage for profile photos; resume content remains in PostgreSQL.

### Version 0 non-goals

- Multiple active resumes for a free user.
- Payments, subscriptions, invoices, refunds, or tax handling.
- Multiple templates or a user-created template designer.
- AI writing or ATS scoring.
- Resume import from PDF/DOCX/LinkedIn.
- Cover letters, job tracking, collaborative editing, comments, custom domains, analytics for resume viewers, or native mobile applications.
- Pixel-level design customization.

### Next iteration

- Paid entitlement to create additional resumes.
- Resume duplication for tailoring to a role.
- Multiple versioned ATS-conscious templates and a template picker.
- Checkout, webhook processing, billing portal, invoices, and entitlement reconciliation.
- Lock—not delete—paid resumes if entitlement expires; define which resume remains free.
- Optional custom section labels, locales, RTL, A4/Letter selection, and basic design tokens.

## 6. Stakeholders and users

| Role | Interest / responsibility |
|---|---|
| Product owner | Scope, positioning, pricing hypotheses, acceptance decisions |
| Engineering | Architecture, implementation, security, operations, quality |
| Design/content | Guided builder UX, default template, terminology, accessibility |
| Support/operations | Account problems, abuse, recovery, production incidents |
| Job seeker | Owns and maintains one or more resumes |
| Public viewer | Opens a shared resume without an account |
| Platform administrator | Handles support and abuse; does not edit user resumes by default |

Primary Version 0 persona: a job seeker who wants one credible resume quickly and is comfortable entering career information into guided forms. Secondary persona: a recruiter, friend, or client opening a shared link on mobile or desktop.

## 7. Core journeys and acceptance outcomes

### Journey A — first resume

1. Visitor registers and signs in.
2. Dashboard explains that the first resume is free.
3. User creates a resume; `ats-classic-v1` is assigned automatically.
4. Builder opens with personal details and section navigation.
5. Changes autosave and appear in live preview.

**Outcome:** refreshing or signing in on another device restores the latest saved state. A second create request receives a stable `RESUME_LIMIT_REACHED` response and does not create data.

### Journey B — publish and share

1. Owner previews the resume and chooses Publish.
2. Platform validates the minimum publishable fields and creates/activates a random slug.
3. Owner copies the public URL.
4. A signed-out viewer opens the link.
5. Owner can unpublish or rotate the link immediately.

**Outcome:** only published content is returned publicly. Unpublish/revoke takes effect immediately and the old URL returns `404`.

### Journey C — PDF

1. Owner selects Download PDF.
2. Backend renders the current resume version using the selected template.
3. User receives a selectable-text PDF with links and predictable page breaks.

**Outcome:** the PDF matches the web template closely, contains no platform watermark, and key text can be copied/searched.

### Journey D — isolation

1. User A creates a resume.
2. User B attempts to read or mutate it using its internal UUID.

**Outcome:** the API returns `404` or an equivalent non-disclosing response; no fields from User A are exposed.

## 8. Recommended architecture

### Application shape

```text
Browser
  ├── Public marketing + shared resume pages (React Router SSR)
  └── Authenticated dashboard + builder (React Router)
             │
             ▼
       Spring Boot REST API
  ┌──────────┼─────────────┬──────────────┐
  │ identity │ resume core │ publication  │ rendering/PDF
  └──────────┴─────────────┴──────────────┘
             │                 │
        PostgreSQL        Cloudinary (photos)
```

Use a modular monolith for Version 0. Separate packages by domain (`identity`, `resume`, `template`, `publication`, `rendering`, `entitlement`) while retaining one deployable backend. Microservices would add operational cost without solving a current scaling problem.

### Frontend recommendation

Use the existing React Router application for marketing, authentication, dashboard, builder, preview, and public pages. SSR supports public-link metadata and fast first render, while client-side interactions support the builder.

Retain Angular only as an internal platform-administration console if it has a clear future use. Do not turn the current Angular admin into the customer resume builder: that would create duplicated design systems, authentication behavior, and API clients. During Version 0, reduce it to platform operations or pause it.

### Rendering recommendation

Create a framework-neutral `ResumeDocument` view model. The web preview, public view, and print/PDF renderer consume this same normalized model and template version. Avoid storing generated HTML as the source of truth.

Preferred PDF sequence:

1. Normalize and validate resume data on the backend.
2. Render a dedicated print URL/component for `ats-classic-v1`.
3. Generate PDF using a pinned headless Chromium runtime (Playwright) in a worker or controlled backend-adjacent service.
4. Stream the PDF initially; add object-storage caching only when usage proves it useful.

Do not use screenshots or canvas for PDF generation; ATS systems need real text.

## 9. Domain and data model

### Aggregate model

```text
UserAccount 1 ─── * Resume * ─── 1 ResumeTemplateVersion
                       │
                       ├── 1 ResumeProfile
                       ├── * Experience ─── * Achievement
                       ├── * Education
                       ├── * Skill
                       ├── * Project
                       ├── * Certification
                       ├── * Language
                       └── * ResumeLink

UserAccount 1 ─── 1 EntitlementSnapshot (or resolved plan policy)
Resume      1 ─── 0..1 ResumePublication
```

### Core tables

| Table | Important fields / constraints |
|---|---|
| `user_account` | UUID, normalized unique email, password hash, status, roles, terms version/time, created/updated time |
| `refresh_token` | hashed token, user ID, expiry, revoked time, device metadata; rotate on use |
| `password_reset_token` | hashed one-time token, user ID, expiry, used time |
| `resume` | UUID, owner ID, dashboard title, template key/version, locale, page size, status, version number, timestamps, soft-delete time |
| `resume_profile` | resume ID, candidate identity/contact/summary/photo fields |
| Content tables | UUID, `resume_id`, fields, `sort_order`, timestamps; `achievement` belongs to experience |
| `resume_publication` | resume ID, unique random slug/token hash, state, published/revoked times |
| `template_version` | immutable key/version, display name, active flag, ATS metadata, created time |
| `plan_entitlement` | plan key, max active resumes, allowed templates; policy seed includes `FREE = 1` |
| `audit_event` | actor, action, target type/ID, timestamp, safe metadata; never store resume content or credentials |

### Important modeling decisions

- Use UUIDs for new externally addressable entities. Public slugs are separate random identifiers.
- Put `resume_id` on every resume-owned row. Never infer ownership through a global singleton.
- Do not add `UNIQUE(owner_id)` to `resume`; the one-resume rule belongs to an entitlement check.
- Enforce the limit transactionally to prevent concurrent requests creating two free resumes. A per-user advisory lock or locked entitlement/account row plus a counted query is sufficient in PostgreSQL.
- Use optimistic locking (`@Version`) on `resume` to detect conflicting edits.
- Store explicit order as integer/fractional ranks and update reorder operations transactionally.
- Template versions are immutable. A resume points to a specific version so a later template change cannot silently reflow an existing PDF.
- Prefer normalized relational content for known sections. A limited JSONB `settings` column is acceptable for template-specific presentation settings, not for identity, ownership, or all resume content.
- Soft delete resumes for a short recovery window, then hard-delete content/media through a scheduled retention job. Account deletion cascades through the same process.

## 10. API outline

Use `/api/v1` and stable machine-readable error codes. Never accept `ownerId` from the client; derive it from the authenticated principal.

### Identity

```text
POST   /api/v1/auth/register
POST   /api/v1/auth/login
POST   /api/v1/auth/refresh
POST   /api/v1/auth/logout
POST   /api/v1/auth/forgot-password
POST   /api/v1/auth/reset-password
GET    /api/v1/me
PATCH  /api/v1/me
DELETE /api/v1/me
```

### Owner resume APIs

```text
GET    /api/v1/resumes
POST   /api/v1/resumes
GET    /api/v1/resumes/{resumeId}
PATCH  /api/v1/resumes/{resumeId}
DELETE /api/v1/resumes/{resumeId}
PUT    /api/v1/resumes/{resumeId}/content
PATCH  /api/v1/resumes/{resumeId}/sections/{section}/{itemId}
POST   /api/v1/resumes/{resumeId}/sections/{section}
DELETE /api/v1/resumes/{resumeId}/sections/{section}/{itemId}
PUT    /api/v1/resumes/{resumeId}/sections/{section}/order
POST   /api/v1/resumes/{resumeId}/publish
POST   /api/v1/resumes/{resumeId}/unpublish
POST   /api/v1/resumes/{resumeId}/rotate-public-link
GET    /api/v1/resumes/{resumeId}/pdf
```

Start with a coarse-grained `PUT /content` if it materially shortens delivery, but include a client revision/ETag to prevent silent overwrites. Fine-grained endpoints can follow where repeatable sections benefit from them.

### Public APIs

```text
GET    /api/v1/public/resumes/{publicSlug}
GET    /r/{publicSlug}
```

The public DTO must be purpose-built. Never serialize the owner-domain entity or reuse a DTO containing internal IDs, account email, status, or audit fields.

### Representative errors

| HTTP | Code | Meaning |
|---:|---|---|
| 400 | `VALIDATION_FAILED` | Invalid field values |
| 401 | `AUTHENTICATION_REQUIRED` | Missing/expired credentials |
| 404 | `RESUME_NOT_FOUND` | Missing or not owned; deliberately non-disclosing |
| 409 | `EMAIL_ALREADY_REGISTERED` | Registration conflict |
| 409 | `EDIT_CONFLICT` | Stale revision/optimistic lock |
| 422 | `RESUME_NOT_PUBLISHABLE` | Minimum public fields missing |
| 403 | `RESUME_LIMIT_REACHED` | Plan does not permit another active resume |
| 429 | `RATE_LIMITED` | Abuse/rate threshold reached |

## 11. Authentication, authorization, privacy, and abuse controls

- Replace `AdminUser` as the customer identity with `UserAccount`; keep admin roles separate.
- Use short-lived access tokens and rotated, revocable refresh tokens. Prefer Secure, HttpOnly, SameSite cookies for the browser-facing application; if bearer tokens remain, do not persist them in `localStorage`.
- Restrict CORS to known origins and restore CSRF protection when cookies authenticate mutations.
- Verify ownership in repository queries such as `findByIdAndOwnerId`, not only controller annotations.
- Normalize email consistently and never log passwords, tokens, full resume payloads, or sensitive contact data.
- Rate-limit registration, login, password reset, public resume reads, link rotation, and PDF generation.
- Add password-reset token expiry/single use and generic forgot-password responses to prevent account enumeration.
- Validate image MIME type, signature, dimensions, and size; delete replaced or orphaned Cloudinary assets.
- Provide Privacy Policy, Terms, data export later, and deletion now. Document retention and subprocessors before launch.
- Add `noindex` by default for public resumes in Version 0 unless the owner explicitly opts into search indexing later.
- Back up PostgreSQL and run a tested restore exercise before production launch.

## 12. ATS-conscious template definition

“ATS-friendly” is not a binary certification and must not be marketed as a guaranteed pass. Version 0 should describe the template as ATS-conscious and validate these properties:

- Semantic, conventional section headings.
- One-column logical reading order.
- Selectable/searchable text; no text baked into images.
- Embedded, common fonts and sufficient contrast.
- No tables used as the primary layout mechanism.
- No essential contact details only in headers/footers.
- Simple bullets, consistent dates, and plain URLs with link annotations.
- Predictable page breaks; an entry should not split awkwardly when avoidable.
- Empty sections omitted and long content handled without overlap or clipping.
- A parsing smoke test extracts the expected name, headings, dates, and bullet text from generated PDFs.

## 13. Migration from the current project

### What can be reused

- Spring Boot application, PostgreSQL, Flyway, validation, structured API errors, OpenAPI, storage abstraction, Docker, CI, and testing patterns.
- Existing profile, experience, achievement, project, skill, education, certification, language, and social-link concepts.
- React Router SSR foundation and its server-side API layer.
- Existing portfolio data as a seeded demo/migrated first account.

### What must change

- Global tables become resume-owned; every content repository and service is tenant-scoped.
- Admin-only login becomes customer registration/session lifecycle plus a separate admin role.
- Public portfolio endpoints become slug-scoped public resume endpoints.
- The portfolio website becomes marketing + authenticated product + public resume renderer.
- Angular CRUD is no longer the user-facing editing experience.
- Global `project.slug` uniqueness must be scoped to a resume or removed from the resume domain.
- Existing enum restrictions should be reconsidered where resume users need free-form values.

### Safe migration sequence

1. Snapshot/backup the database.
2. Add new `user_account`, `resume`, template, publication, token, and entitlement structures without deleting old tables.
3. Create a migration account and one resume for the existing owner.
4. Copy existing rows into new resume-owned tables, preserving order and relationships.
5. Run count, relationship, and rendered-output checks.
6. Switch new APIs and UI to the new schema behind a feature flag or release boundary.
7. Keep legacy tables read-only for one release/backup window.
8. Remove legacy endpoints/tables only after production verification and a rollback window.

Do not mutate the existing V1/V2 Flyway files after they may have run. Add new, forward-only migrations.

## 14. Implementation roadmap

Estimates assume one experienced full-stack engineer, timely product decisions, and reuse of the current foundation. They are planning ranges, not delivery commitments.

### Phase 0 — decisions and UX foundation (3–5 days)

**Deliverables**

- Confirm product name/domain, free-plan policy, publish defaults, deletion retention, supported locale, and PDF page size.
- Builder wireframes for desktop/mobile and the default resume template specification.
- Architecture decision records for auth/session, data ownership, PDF engine, and Angular admin disposition.
- Version 0 analytics event dictionary and privacy review.

**Gate:** approved scope, journeys, template wireframe, and unresolved decisions have named owners/dates.

### Phase 1 — multi-user identity and tenancy (5–8 days)

**Backend**

- Add user account, refresh/reset tokens, roles, auth flows, and session revocation.
- Add resume root, template version, publication, and entitlement policy.
- Create forward-only Flyway migrations and migrate seeded portfolio data.
- Implement owner-scoped repository/service conventions and transactional one-resume enforcement.

**Frontend**

- Registration, login, forgot/reset password, authenticated shell, and dashboard empty state.

**Tests**

- Auth lifecycle, concurrent one-resume creation, cross-user read/write denial, migration verification.

**Gate:** two users can independently sign in and neither can access the other's empty/draft resume.

### Phase 2 — resume content API and builder (8–12 days)

**Backend**

- Resume aggregate DTOs, validation, CRUD, ordering, optimistic revision, photo ownership/cleanup.

**Frontend**

- Guided sections, repeatable-entry editors, reorder controls, autosave queue/debounce, recovery from save failure, responsive preview shell.

**Tests**

- Contract/integration tests per section, optimistic conflict handling, accessibility keyboard flows, browser tests for persistence.

**Gate:** a user can complete the supported content, refresh, and recover exactly what was saved.

### Phase 3 — default template, public links, and PDF (7–10 days)

**Deliverables**

- `ResumeDocument` normalization layer and `ats-classic-v1` renderer.
- Publish validation, public DTO/route, revoke/rotate behavior, Open Graph metadata, and `noindex` behavior.
- Deterministic text-based PDF generation and download.
- Golden/sample resumes covering sparse, average, dense, long-word, URL, and multi-page cases.

**Tests**

- Visual regression at print breakpoints, PDF text extraction, link checks, public cache/revocation behavior, signed-out access.

**Gate:** the same saved data renders consistently in preview/public/PDF; revoked links fail immediately.

### Phase 4 — hardening and launch (5–8 days)

**Deliverables**

- Rate limits, security headers, CSRF/CORS policy, audit events, structured logging, error monitoring, metrics, and alerts.
- Account/resume deletion workflow and orphan media cleanup.
- Production migrations, backups/restore test, deployment runbook, support runbook, Terms/Privacy pages.
- End-to-end browser suite, performance/accessibility checks, and threat-model review.

**Gate:** launch checklist passes, rollback is rehearsed, no critical/high security findings remain, and operational ownership is clear.

### Version 0 total

Approximately **28–43 engineering days** for one engineer, excluding brand/legal review and feedback delays. A sensible calendar target is **7–10 weeks** with testing, design iteration, and production stabilization included.

### Next iteration — paid multiple resumes and templates (10–18 days)

- Integrate a payment provider with hosted checkout and billing portal.
- Persist billing customer/subscription identifiers; process signed, idempotent webhooks.
- Resolve entitlements server-side and unlock create/duplicate operations.
- Define downgrade/failed-payment behavior; preserve but lock excess resumes.
- Add template catalog/picker and additional immutable template versions.
- Test webhook replay/order, entitlement races, refund/cancellation, and template regressions.

## 15. Prioritized backlog

### Must have for Version 0

- `AUTH-01` register/login/logout/refresh/reset.
- `TENANT-01` owner-scoped data access and isolation tests.
- `RESUME-01` one-free-resume entitlement and transactional enforcement.
- `RESUME-02` CRUD for all committed content sections and ordering.
- `BUILDER-01` autosave with conflict/failure UX.
- `TEMPLATE-01` versioned default ATS-conscious template.
- `PUBLIC-01` publish/unpublish/rotate and safe public DTO.
- `PDF-01` text-based PDF and extraction checks.
- `PRIVACY-01` deletion and media cleanup.
- `OPS-01` monitoring, rate limiting, backups, CI/CD, runbooks.

### Should have if schedule permits

- A4/Letter toggle.
- Email verification before publishing rather than before drafting.
- Resume completion indicator.
- Duplicate-friendly internal APIs kept disabled until paid entitlement ships.
- Draft recovery/local fallback for unsent edits.

### Later

- Payments and multiple resumes.
- More ATS-conscious templates.
- Import, AI assistance, custom labels/locales, RTL, cover letters, and job tracking.

## 16. Test strategy and Definition of Done

### Automated layers

- Unit tests: entitlement, normalization, publication validation, slug generation, mapper behavior.
- Repository tests with PostgreSQL/Testcontainers: ownership filters, constraints, concurrent create, ordering.
- API integration tests: auth/session, all content sections, error contracts, public redaction, deletion.
- Contract/type generation where practical so frontend DTOs track OpenAPI.
- Browser E2E: register → create → edit → refresh → publish → signed-out view → PDF → unpublish.
- Visual tests: template examples at A4/Letter and mobile public view.
- Security tests: IDOR/cross-tenant attempts, token reuse/revocation, rate limit, enumeration, unsafe upload.
- PDF tests: text extraction, URLs, page count bounds, font embedding, no clipping on fixtures.

### Definition of Done for each story

- Acceptance criteria pass and error states are designed.
- Ownership/authorization is tested where data is private.
- API/OpenAPI and frontend types are updated together.
- Database changes are forward-only, indexed, and migration-tested from the current schema.
- Accessibility includes keyboard navigation, labels, focus behavior, and contrast.
- Logs/metrics contain useful operational context without sensitive content.
- Tests pass in CI; deployment and rollback impact is documented.

## 17. Risks and mitigations

| Risk | Impact | Mitigation |
|---|---|---|
| Cross-user data leakage during global-to-tenant migration | Critical | Owner-scoped query convention, isolation integration suite, security review |
| One-resume race condition | Revenue/policy defect | Transactional lock + server-side entitlement check |
| PDF differs from preview or is unreadable by parsers | Core value failure | Shared normalized renderer, pinned Chromium, golden fixtures, extraction tests |
| Autosave overwrites newer work | Data loss | Revision/ETag, serialized client save queue, explicit conflict UX |
| Public link reveals drafts or remains cached after revoke | Privacy incident | Private default, explicit publication record, immediate authorization check, conservative caching |
| Feature creep toward all FlowCV features | Schedule failure | Charter non-goals, phase gates, change control |
| Angular + React product duplication | Delivery/maintenance cost | React for customer product; Angular limited to platform operations |
| “ATS-friendly” overclaim | Trust/legal risk | Use ATS-conscious language, publish concrete criteria, test output |
| Payment design coupled into core domain too early | Rework | Entitlement interface now, payment adapter later |
| Cloudinary asset orphaning/abuse | Cost/privacy | Scoped upload, validation, quotas, replacement/deletion jobs |

## 18. Product and technical decisions required

These do not block initial domain work if the recommended defaults are accepted:

| Decision | Recommended Version 0 default |
|---|---|
| Product/brand name | Temporary neutral name until domain check |
| First resume | Free forever, unlimited edits/downloads, no watermark |
| Publish visibility | Private by default; explicit publish |
| Public indexing | `noindex` by default |
| Public URL | Random slug, owner-rotatable |
| Default format | A4 first; design model permits Letter |
| Language | English UI/content labels first; Unicode-safe data model |
| Email verification | Required before publishing, not before drafting |
| Auth transport | Short access token + rotated refresh token in Secure HttpOnly cookies |
| Customer frontend | React Router; Angular reserved for platform admin |
| PDF | Pinned headless Chromium against a dedicated print renderer |
| Deleted resume retention | Soft delete 30 days, then purge; disclose policy |
| Additional resume pricing | Decide during the paid iteration after usage data |

## 19. Launch checklist

- Version 0 scope and copy approved.
- Production domain, email sender, Terms, and Privacy Policy configured.
- Database migration verified from a production-like snapshot.
- Backup and restore drill passed.
- Secrets rotated and no default admin credentials remain.
- Rate limits and security headers verified.
- Cross-tenant/IDOR test suite green.
- Public/unpublish/rotate behavior tested behind CDN/proxy configuration.
- PDF samples inspected and parser smoke tests green.
- Accessibility and responsive builder flows pass.
- Monitoring dashboards, alerts, and support/incident runbooks are live.
- Account/resume deletion tested end to end, including Cloudinary cleanup.
- Rollback plan rehearsed.

## 20. Immediate execution order

1. Approve the decisions in section 18 and freeze Version 0 scope.
2. Produce the default template and builder wireframes before deep frontend implementation.
3. Add identity, resume ownership, template version, publication, and entitlement migrations.
4. Implement tenant-safe auth/repository foundations and isolation tests.
5. Build the resume content API and React builder.
6. Build the shared renderer, public route, and PDF pipeline.
7. Harden, migrate the current portfolio data, and launch to a small beta cohort.
8. Measure activation and value events before finalizing paid multi-resume pricing.

## References

- FlowCV product pattern reviewed on 2026-06-24: one resume free forever, paid multiple versions, professional templates, PDF download, and unique sharing links: <https://flowcv.com/resume-builder>
- FlowCV's positioning describes payment for multiple versions/advanced features: <https://flowcv.com/about/>
