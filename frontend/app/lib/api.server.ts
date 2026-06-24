import { fallbackCertifications, fallbackEducation, fallbackExperiences, fallbackLanguages, fallbackProfile, fallbackProjects, fallbackSkills } from "./fallback";
import type { Certification, Education, Experience, Language, Profile, Project, Skill } from "./types";

const baseUrl = (process.env.API_BASE_URL || "http://localhost:8080/api/v1/portfolio").replace(/\/$/, "");

async function request<T>(path: string, fallback: T): Promise<T> {
  try {
    const response = await fetch(`${baseUrl}/${path}`, { headers: { Accept: "application/json" }, signal: AbortSignal.timeout(3500) });
    if (!response.ok) throw new Error(`API responded with ${response.status}`);
    return (await response.json()) as T;
  } catch {
    return fallback;
  }
}

export const portfolioApi = {
  profile: () => request<Profile>("profile", fallbackProfile),
  experiences: () => request<Experience[]>("experiences", fallbackExperiences),
  projects: () => request<Project[]>("projects", fallbackProjects),
  featuredProjects: () => request<Project[]>("projects/featured", fallbackProjects.filter((project) => project.featured)),
  skills: () => request<Skill[]>("skills", fallbackSkills),
  education: () => request<Education[]>("education", fallbackEducation),
  certifications: () => request<Certification[]>("certifications", fallbackCertifications),
  languages: () => request<Language[]>("languages", fallbackLanguages),
  project: async (slug: string) => {
    const local = fallbackProjects.find((project) => project.slug === slug);
    const project = await request<Project | null>(`projects/${encodeURIComponent(slug)}`, local ?? null);
    if (!project) throw new Response("Project not found", { status: 404, statusText: "Not Found" });
    return project;
  },
};
