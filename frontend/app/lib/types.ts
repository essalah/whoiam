export interface SocialLink { id?: number; platform: string; url: string }
export interface Profile { id?: number; name: string; title: string; summary: string; email?: string | null; phone?: string | null; location?: string | null; website?: string | null; avatarUrl?: string | null; socialLinks: SocialLink[] }
export interface Experience { id?: number; company: string; role: string; startDate: string; endDate: string | null; location: string; description: string; sortOrder?: number; achievements: string[] }
export interface Project { id?: number; title: string; slug: string; description: string; imageUrl?: string; liveUrl?: string; githubUrl?: string; featured: boolean; sortOrder?: number; techStack: string[] }
export interface Skill { id?: number; name: string; category: string; proficiency: string; sortOrder?: number }
export interface Education { id?: number; institution: string; degree: string; field: string; startDate: string; endDate: string; description: string; sortOrder?: number }
export interface Certification { id?: number; name: string; issuer: string; issueDate: string; url?: string; sortOrder?: number }
export interface Language { id?: number; name: string; proficiency: string }
