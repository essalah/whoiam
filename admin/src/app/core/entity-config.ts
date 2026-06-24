import { EntityConfig } from "./api.models";

export const ENTITY_CONFIGS: EntityConfig[] = [
  {
    path: "profile",
    label: "Profile",
    description: "Core identity, contact details, and headline content.",
    create: false,
    delete: false,
    fields: [
      { key: "name", label: "Name", type: "text", required: true },
      { key: "title", label: "Title", type: "text", required: true },
      { key: "summary", label: "Summary", type: "textarea", placeholder: "Short professional introduction" },
      { key: "email", label: "Email", type: "email" },
      { key: "phone", label: "Phone", type: "text" },
      { key: "location", label: "Location", type: "text" },
      { key: "website", label: "Website", type: "url" },
      { key: "avatarUrl", label: "Avatar", type: "image" },
    ],
  },
  {
    path: "experiences",
    label: "Experiences",
    description: "Roles, companies, dates, and notable work history.",
    fields: [
      { key: "role", label: "Role", type: "text", required: true },
      { key: "company", label: "Company", type: "text", required: true },
      { key: "location", label: "Location", type: "text" },
      { key: "startDate", label: "Start date", type: "date" },
      { key: "endDate", label: "End date", type: "date" },
      { key: "description", label: "Description", type: "textarea" },
      { key: "sortOrder", label: "Sort order", type: "number" },
      { key: "achievements", label: "Achievements", type: "string-list", help: "Add each achievement as a separate item." },
    ],
  },
  {
    path: "projects",
    label: "Projects",
    description: "Portfolio projects, links, images, and featured state.",
    fields: [
      { key: "title", label: "Title", type: "text", required: true },
      { key: "description", label: "Description", type: "textarea" },
      { key: "imageUrl", label: "Project image", type: "image" },
      { key: "liveUrl", label: "Live URL", type: "url" },
      { key: "githubUrl", label: "GitHub URL", type: "url" },
      { key: "featured", label: "Featured", type: "checkbox" },
      { key: "sortOrder", label: "Sort order", type: "number" },
      { key: "techStackIds", label: "Skills", type: "multi-select", optionsPath: "skills", help: "Choose the skills used by this project." },
    ],
  },
  {
    path: "skills",
    label: "Skills",
    description: "Skill names, categories, proficiency, and display order.",
    fields: [
      { key: "name", label: "Name", type: "text", required: true },
      { key: "category", label: "Category", type: "select", required: true, options: ["LANGUAGES", "MOBILE", "FRONTEND", "BACKEND", "ARCHITECTURE", "LIBRARIES", "TESTING", "DATABASE", "DEVOPS"] },
      { key: "proficiency", label: "Proficiency", type: "select", required: true, options: ["EXPERT", "ADVANCED", "INTERMEDIATE", "BEGINNER"] },
      { key: "sortOrder", label: "Sort order", type: "number" },
    ],
  },
  {
    path: "education",
    label: "Education",
    description: "Degrees, institutions, fields, and dates.",
    fields: [
      { key: "institution", label: "Institution", type: "text", required: true },
      { key: "degree", label: "Degree", type: "text", required: true },
      { key: "field", label: "Field", type: "text" },
      { key: "startDate", label: "Start date", type: "date" },
      { key: "endDate", label: "End date", type: "date" },
      { key: "description", label: "Description", type: "textarea" },
      { key: "sortOrder", label: "Sort order", type: "number" },
    ],
  },
  {
    path: "certifications",
    label: "Certifications",
    description: "Certificate names, issuers, dates, and credential links.",
    fields: [
      { key: "name", label: "Name", type: "text", required: true },
      { key: "issuer", label: "Issuer", type: "text" },
      { key: "issueDate", label: "Issue date", type: "date" },
      { key: "url", label: "URL", type: "url" },
      { key: "sortOrder", label: "Sort order", type: "number" },
    ],
  },
  {
    path: "languages",
    label: "Languages",
    description: "Spoken languages and proficiency levels.",
    fields: [
      { key: "name", label: "Name", type: "text", required: true },
      { key: "proficiency", label: "Proficiency", type: "select", required: true, options: ["NATIVE", "FLUENT", "PROFESSIONAL", "INTERMEDIATE", "BEGINNER"] },
    ],
  },
  {
    path: "social-links",
    label: "Social links",
    description: "Public profile links attached to the portfolio profile.",
    nestedUnderProfile: true,
    fields: [
      { key: "platform", label: "Platform", type: "select", required: true, options: ["LINKEDIN", "GITHUB", "TWITTER", "WEBSITE", "OTHER"] },
      { key: "url", label: "URL", type: "url", required: true },
    ],
  },
];

export function findEntityConfig(path: string | null): EntityConfig {
  return ENTITY_CONFIGS.find((config) => config.path === path) ?? ENTITY_CONFIGS[0];
}
