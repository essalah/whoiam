-- Resume Builder foundation schema.
-- Legacy portfolio tables are intentionally retained during the product transition.

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE user_account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(320) NOT NULL,
    normalized_email VARCHAR(320) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    terms_version VARCHAR(50),
    terms_accepted_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_user_account_status
        CHECK (status IN ('ACTIVE', 'DISABLED', 'DELETED'))
);

CREATE INDEX idx_user_account_status ON user_account(status);

CREATE TABLE refresh_token (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    device_metadata VARCHAR(500),
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_user ON refresh_token(user_id);
CREATE INDEX idx_refresh_token_expires_at ON refresh_token(expires_at);

CREATE TABLE password_reset_token (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_reset_token_user
        FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_token_user ON password_reset_token(user_id);
CREATE INDEX idx_password_reset_token_expires_at ON password_reset_token(expires_at);

CREATE TABLE template_version (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    template_key VARCHAR(100) NOT NULL,
    version INTEGER NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    ats_metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_template_version_key_version UNIQUE (template_key, version),
    CONSTRAINT chk_template_version_positive CHECK (version > 0)
);

CREATE INDEX idx_template_version_active ON template_version(active);

INSERT INTO template_version (template_key, version, display_name, ats_metadata)
VALUES (
    'ats-classic-v1',
    1,
    'ATS Classic',
    '{"atsFriendly": true, "layout": "single-column"}'::jsonb
);

CREATE TABLE plan_entitlement (
    plan_key VARCHAR(50) PRIMARY KEY,
    max_active_resumes INTEGER NOT NULL,
    allowed_template_keys JSONB NOT NULL DEFAULT '[]'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_plan_entitlement_resume_limit CHECK (max_active_resumes >= 0),
    CONSTRAINT chk_plan_entitlement_templates_array
        CHECK (jsonb_typeof(allowed_template_keys) = 'array')
);

INSERT INTO plan_entitlement (plan_key, max_active_resumes, allowed_template_keys)
VALUES ('FREE', 1, '["ats-classic-v1"]'::jsonb);

CREATE TABLE resume (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id UUID NOT NULL,
    dashboard_title VARCHAR(255) NOT NULL,
    template_key VARCHAR(100) NOT NULL DEFAULT 'ats-classic-v1',
    template_version INTEGER NOT NULL DEFAULT 1,
    locale VARCHAR(35) NOT NULL DEFAULT 'en',
    page_size VARCHAR(20) NOT NULL DEFAULT 'A4',
    status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
    lock_version BIGINT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_owner
        FOREIGN KEY (owner_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CONSTRAINT fk_resume_template
        FOREIGN KEY (template_key, template_version)
        REFERENCES template_version(template_key, version),
    CONSTRAINT chk_resume_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
    CONSTRAINT chk_resume_lock_version CHECK (lock_version >= 0)
);

CREATE INDEX idx_resume_owner ON resume(owner_id);
CREATE INDEX idx_resume_owner_active ON resume(owner_id, deleted_at);
CREATE INDEX idx_resume_owner_status ON resume(owner_id, status);
CREATE INDEX idx_resume_owner_updated_at ON resume(owner_id, updated_at DESC);
CREATE INDEX idx_resume_status ON resume(status);

CREATE TABLE resume_profile (
    resume_id UUID PRIMARY KEY,
    full_name VARCHAR(255),
    headline VARCHAR(255),
    email VARCHAR(320),
    phone VARCHAR(50),
    location VARCHAR(255),
    website_url VARCHAR(1000),
    photo_url VARCHAR(1000),
    summary TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_profile_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE
);

CREATE TABLE resume_experience (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NOT NULL,
    employer VARCHAR(255),
    position VARCHAR(255),
    location VARCHAR(255),
    start_date DATE,
    end_date DATE,
    current_position BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_resume_experience_id_resume UNIQUE (id, resume_id),
    CONSTRAINT fk_resume_experience_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_experience_sort_order CHECK (sort_order >= 0)
);

CREATE INDEX idx_resume_experience_order
    ON resume_experience(resume_id, sort_order);

CREATE TABLE resume_achievement (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NOT NULL,
    experience_id UUID NOT NULL,
    description TEXT NOT NULL,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_achievement_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT fk_resume_achievement_experience
        FOREIGN KEY (experience_id, resume_id)
        REFERENCES resume_experience(id, resume_id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_achievement_sort_order CHECK (sort_order >= 0)
);

CREATE INDEX idx_resume_achievement_order
    ON resume_achievement(resume_id, experience_id, sort_order);

CREATE TABLE resume_education (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NOT NULL,
    institution VARCHAR(255),
    degree VARCHAR(255),
    field_of_study VARCHAR(255),
    location VARCHAR(255),
    start_date DATE,
    end_date DATE,
    current_study BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_education_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_education_sort_order CHECK (sort_order >= 0)
);

CREATE INDEX idx_resume_education_order
    ON resume_education(resume_id, sort_order);

CREATE TABLE resume_skill (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    proficiency VARCHAR(100),
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_skill_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_skill_sort_order CHECK (sort_order >= 0)
);

CREATE INDEX idx_resume_skill_order ON resume_skill(resume_id, sort_order);

CREATE TABLE resume_project (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    start_date DATE,
    end_date DATE,
    description TEXT,
    project_url VARCHAR(1000),
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_project_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_project_sort_order CHECK (sort_order >= 0)
);

CREATE INDEX idx_resume_project_order ON resume_project(resume_id, sort_order);

CREATE TABLE resume_certification (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    issuer VARCHAR(255),
    issue_date DATE,
    expiration_date DATE,
    credential_id VARCHAR(255),
    credential_url VARCHAR(1000),
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_certification_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_certification_sort_order CHECK (sort_order >= 0)
);

CREATE INDEX idx_resume_certification_order
    ON resume_certification(resume_id, sort_order);

CREATE TABLE resume_language (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    proficiency VARCHAR(100),
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_language_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_language_sort_order CHECK (sort_order >= 0)
);

CREATE INDEX idx_resume_language_order
    ON resume_language(resume_id, sort_order);

CREATE TABLE resume_link (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NOT NULL,
    label VARCHAR(100),
    url VARCHAR(1000) NOT NULL,
    link_type VARCHAR(50),
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_link_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_link_sort_order CHECK (sort_order >= 0)
);

CREATE INDEX idx_resume_link_order ON resume_link(resume_id, sort_order);

CREATE TABLE resume_publication (
    resume_id UUID PRIMARY KEY,
    public_slug VARCHAR(255) NOT NULL UNIQUE,
    state VARCHAR(32) NOT NULL DEFAULT 'UNPUBLISHED',
    published_at TIMESTAMPTZ,
    revoked_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_publication_resume
        FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE,
    CONSTRAINT chk_resume_publication_state
        CHECK (state IN ('UNPUBLISHED', 'PUBLISHED', 'REVOKED'))
);

CREATE INDEX idx_resume_publication_state ON resume_publication(state);

CREATE TABLE audit_event (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_id UUID,
    action VARCHAR(100) NOT NULL,
    target_type VARCHAR(100) NOT NULL,
    target_id UUID NOT NULL,
    safe_metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_event_actor
        FOREIGN KEY (actor_id) REFERENCES user_account(id) ON DELETE SET NULL,
    CONSTRAINT chk_audit_event_metadata_object
        CHECK (jsonb_typeof(safe_metadata) = 'object')
);

CREATE INDEX idx_audit_event_actor_created_at
    ON audit_event(actor_id, created_at DESC);
CREATE INDEX idx_audit_event_target_created_at
    ON audit_event(target_type, target_id, created_at DESC);
