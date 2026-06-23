-- Portfolio System Initial Schema
-- V1: All tables with constraints, indexes, and foreign keys

CREATE TABLE profile (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    summary TEXT,
    email VARCHAR(255),
    phone VARCHAR(50),
    location VARCHAR(255),
    website VARCHAR(500),
    avatar_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE social_link (
    id BIGSERIAL PRIMARY KEY,
    platform VARCHAR(50) NOT NULL,
    url VARCHAR(500) NOT NULL,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_social_link_profile FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE
);

CREATE INDEX idx_social_link_profile ON social_link(profile_id);

CREATE TABLE experience (
    id BIGSERIAL PRIMARY KEY,
    company VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    start_date DATE,
    end_date DATE,
    location VARCHAR(255),
    description TEXT,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_experience_sort ON experience(sort_order);

CREATE TABLE achievement (
    id BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    experience_id BIGINT NOT NULL,
    CONSTRAINT fk_achievement_experience FOREIGN KEY (experience_id) REFERENCES experience(id) ON DELETE CASCADE
);

CREATE INDEX idx_achievement_experience ON achievement(experience_id);

CREATE TABLE skill (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    proficiency VARCHAR(50) NOT NULL,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_skill_category ON skill(category);

CREATE TABLE project (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),
    live_url VARCHAR(500),
    github_url VARCHAR(500),
    featured BOOLEAN DEFAULT FALSE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_project_slug ON project(slug);
CREATE INDEX idx_project_featured ON project(featured);

CREATE TABLE project_skill (
    project_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    PRIMARY KEY (project_id, skill_id),
    CONSTRAINT fk_ps_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT fk_ps_skill FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE
);

CREATE TABLE education (
    id BIGSERIAL PRIMARY KEY,
    institution VARCHAR(255) NOT NULL,
    degree VARCHAR(255) NOT NULL,
    field VARCHAR(255),
    start_date DATE,
    end_date DATE,
    description TEXT,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE certification (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    issuer VARCHAR(255),
    issue_date DATE,
    url VARCHAR(500),
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE language (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    proficiency VARCHAR(50) NOT NULL
);

CREATE TABLE admin_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_ADMIN',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_admin_user_username ON admin_user(username);
