-- Seed data from resume_data.json
-- Default admin: admin / admin123 (BCrypt hash)

-- Admin User (password: admin123)
INSERT INTO admin_user (username, password_hash, role, enabled)
VALUES ('admin', '$2a$10$ItRhIh.hkKjGfq7dCWE75uJrOxTxv5nz.6wxQChNwnrh7jyXOt72a', 'ROLE_ADMIN', true);

-- Profile
INSERT INTO profile (name, title, summary, email, phone, location, website, avatar_url)
VALUES (
    'ELhachmi Salah',
    'Senior Android Developer',
    'Highly skilled Android Developer with 11 years of experience. Solid understanding of the full mobile development life cycle with hands-on expertise with a wide variety of Android device resolutions and SDK versions. Dedicated to continuously discovering, evaluating, and implementing new technologies to maximize development efficiency. Experienced in most popular programming languages and Frameworks involved in Android development such as Java, Kotlin, Dart and Flutter. Has good knowledge and experience in Front-End and Back-End development using Spring Boot, ReactJs.',
    'essalah.elhechmi@gmail.com',
    '+216 27 441 054',
    'Tunis, Tunisia',
    'https://elhachmi.dev',
    'https://res.cloudinary.com/portfolio/image/upload/v1/avatar/elhachmi-salah.jpg'
);

-- Social Links
INSERT INTO social_link (platform, url, profile_id) VALUES ('LINKEDIN', 'https://www.linkedin.com/in/essalah-elhechmi', 1);
INSERT INTO social_link (platform, url, profile_id) VALUES ('GITHUB', 'https://github.com/elhachmi-salah', 1);
INSERT INTO social_link (platform, url, profile_id) VALUES ('TWITTER', 'https://twitter.com/elhachmi_salah', 1);

-- Skills (Languages)
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Java', 'LANGUAGES', 'EXPERT', 1);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Kotlin', 'LANGUAGES', 'EXPERT', 2);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Dart', 'LANGUAGES', 'ADVANCED', 3);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('TypeScript', 'LANGUAGES', 'ADVANCED', 4);

-- Skills (Mobile)
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Android Native (Java/Kotlin)', 'MOBILE', 'EXPERT', 5);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Flutter', 'MOBILE', 'ADVANCED', 6);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Jetpack Compose', 'MOBILE', 'ADVANCED', 7);

-- Skills (Frontend / Backend)
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('ReactJs', 'FRONTEND', 'INTERMEDIATE', 8);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Spring Boot', 'BACKEND', 'INTERMEDIATE', 9);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Firebase', 'BACKEND', 'ADVANCED', 10);

-- Skills (Architecture / Libraries)
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('MVM / MVVM', 'ARCHITECTURE', 'EXPERT', 11);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Dagger / Hilt', 'ARCHITECTURE', 'EXPERT', 12);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Retrofit', 'LIBRARIES', 'EXPERT', 13);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Room', 'LIBRARIES', 'EXPERT', 14);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('RxJava', 'LIBRARIES', 'ADVANCED', 15);

-- Skills (Testing)
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('JUnit', 'TESTING', 'ADVANCED', 16);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Espresso', 'TESTING', 'ADVANCED', 17);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Mockito', 'TESTING', 'ADVANCED', 18);

-- Skills (Database)
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('SQLite', 'DATABASE', 'EXPERT', 19);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('PostgreSQL', 'DATABASE', 'INTERMEDIATE', 20);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('MySQL', 'DATABASE', 'INTERMEDIATE', 21);

-- Skills (DevOps)
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('JIRA', 'DEVOPS', 'ADVANCED', 22);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('GitLab', 'DEVOPS', 'ADVANCED', 23);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('GitLab CI/CD', 'DEVOPS', 'ADVANCED', 24);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Jenkins', 'DEVOPS', 'INTERMEDIATE', 25);
INSERT INTO skill (name, category, proficiency, sort_order) VALUES ('Docker', 'DEVOPS', 'INTERMEDIATE', 26);

-- Experience 1: Digitus
INSERT INTO experience (company, role, start_date, end_date, location, description, sort_order)
VALUES ('Digitus', 'Senior Android Developer', '2021-03-01', NULL, 'Tunis, Tunisia',
        'Development of digital assets wallet as Android app, that allows users to instant transfer, store, build and grow their portfolio of assets in a single application.', 1);

INSERT INTO achievement (description, experience_id) VALUES ('Built digital assets wallet Android application from scratch using Kotlin and Jetpack Compose', 1);
INSERT INTO achievement (description, experience_id) VALUES ('Implemented instant transfer and portfolio management features with real-time updates', 1);
INSERT INTO achievement (description, experience_id) VALUES ('Integrated secure authentication and biometric login for asset protection', 1);
INSERT INTO achievement (description, experience_id) VALUES ('Achieved 99.5% crash-free rate with comprehensive error handling', 1);

-- Experience 2: Tayara
INSERT INTO experience (company, role, start_date, end_date, location, description, sort_order)
VALUES ('Tayara', 'Android Developer', '2018-06-01', '2021-02-28', 'Tunis, Tunisia',
        'Development of a mobile payment Android app (tPay by Tayara), that enables users to send and receive money, pay bills, etc.', 2);

INSERT INTO achievement (description, experience_id) VALUES ('Developed tPay mobile payment Android application serving 50K+ users', 2);
INSERT INTO achievement (description, experience_id) VALUES ('Implemented money transfer (send/receive) functionality with bank API integration', 2);
INSERT INTO achievement (description, experience_id) VALUES ('Built bill payment features supporting multiple service providers', 2);
INSERT INTO achievement (description, experience_id) VALUES ('Migrated codebase from Java to Kotlin, improving code quality by 40%', 2);

-- Experience 3: ConceptLab
INSERT INTO experience (company, role, start_date, end_date, location, description, sort_order)
VALUES ('ConceptLab', 'Android Developer', '2015-01-01', '2018-05-31', 'Sousse, Tunisia',
        'Developed an Android application offered to WHO (World Health Organization) agents providing the possibility to register information about hospitals such as GPS location, Name, Address, Specialty, etc.', 3);

INSERT INTO achievement (description, experience_id) VALUES ('Built Android app for WHO (World Health Organization) agents deployed across 3 countries', 3);
INSERT INTO achievement (description, experience_id) VALUES ('Implemented hospital information registration system with offline-first architecture', 3);
INSERT INTO achievement (description, experience_id) VALUES ('Integrated GPS location tracking for hospital mapping with custom map overlays', 3);
INSERT INTO achievement (description, experience_id) VALUES ('Designed and implemented RESTful API consumption layer using Retrofit and RxJava', 3);

-- Education
INSERT INTO education (institution, degree, field, start_date, end_date, description, sort_order)
VALUES ('University of Sousse', 'Bachelor', 'Computer Networks', '2011-09-01', '2014-06-30',
        'Bachelor degree in Computer Networks with focus on network architecture and distributed systems', 1);

INSERT INTO education (institution, degree, field, start_date, end_date, description, sort_order)
VALUES ('Higher Institute of Computer Science, Tunis', 'BS', 'Computer Science', '2008-09-01', '2011-06-30',
        'BS in Computer Science with emphasis on software engineering and algorithms', 2);

-- Projects
INSERT INTO project (title, slug, description, image_url, live_url, github_url, featured, sort_order)
VALUES ('CryptoTrack', 'cryptotrack',
        'Real-time cryptocurrency portfolio tracker with price alerts, interactive charts, and portfolio analytics. Built with clean architecture and Material Design 3.',
        'https://res.cloudinary.com/portfolio/image/upload/v1/projects/cryptotrack.png',
        'https://play.google.com/store/apps/details?id=com.example.cryptotrack',
        'https://github.com/elhachmi-salah/cryptotrack', true, 1);

INSERT INTO project (title, slug, description, image_url, live_url, github_url, featured, sort_order)
VALUES ('TaskFlow', 'taskflow',
        'Minimalist task management app with drag-and-drop Kanban boards, team collaboration, and offline sync. Features smooth animations and dark mode support.',
        'https://res.cloudinary.com/portfolio/image/upload/v1/projects/taskflow.png',
        '', 'https://github.com/elhachmi-salah/taskflow', true, 2);

INSERT INTO project (title, slug, description, image_url, live_url, github_url, featured, sort_order)
VALUES ('WeatherNow', 'weathernow',
        'Beautiful weather application with location-based forecasts, animated weather conditions, and 7-day predictions using OpenWeatherMap API.',
        'https://res.cloudinary.com/portfolio/image/upload/v1/projects/weathernow.png',
        '', 'https://github.com/elhachmi-salah/weathernow', false, 3);

-- Project-Skill associations (tech stacks)
-- CryptoTrack: Kotlin, Jetpack Compose, Dagger/Hilt, Room, Retrofit
INSERT INTO project_skill (project_id, skill_id) VALUES (1, 2);   -- Kotlin
INSERT INTO project_skill (project_id, skill_id) VALUES (1, 7);   -- Jetpack Compose
INSERT INTO project_skill (project_id, skill_id) VALUES (1, 12);  -- Dagger/Hilt
INSERT INTO project_skill (project_id, skill_id) VALUES (1, 14);  -- Room
INSERT INTO project_skill (project_id, skill_id) VALUES (1, 13);  -- Retrofit

-- TaskFlow: Dart, Flutter, Firebase
INSERT INTO project_skill (project_id, skill_id) VALUES (2, 3);   -- Dart
INSERT INTO project_skill (project_id, skill_id) VALUES (2, 6);   -- Flutter
INSERT INTO project_skill (project_id, skill_id) VALUES (2, 10);  -- Firebase

-- WeatherNow: Kotlin, Retrofit, RxJava
INSERT INTO project_skill (project_id, skill_id) VALUES (3, 2);   -- Kotlin
INSERT INTO project_skill (project_id, skill_id) VALUES (3, 13);  -- Retrofit
INSERT INTO project_skill (project_id, skill_id) VALUES (3, 15);  -- RxJava

-- Certifications
INSERT INTO certification (name, issuer, issue_date, url, sort_order)
VALUES ('Associate Android Developer', 'Google', '2020-08-01', 'https://www.credential.net/example', 1);

INSERT INTO certification (name, issuer, issue_date, url, sort_order)
VALUES ('Kotlin for Android Developers', 'Udacity', '2019-03-01', 'https://www.udacity.com/certificate/example', 2);

-- Languages
INSERT INTO language (name, proficiency) VALUES ('Arabic', 'NATIVE');
INSERT INTO language (name, proficiency) VALUES ('French', 'PROFESSIONAL');
INSERT INTO language (name, proficiency) VALUES ('English', 'PROFESSIONAL');
