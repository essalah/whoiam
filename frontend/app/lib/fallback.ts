import type { Certification, Education, Experience, Language, Profile, Project, Skill } from "./types";

export const fallbackProfile: Profile = {
  name: "Elhachmi Salah",
  title: "Senior Android Developer",
  summary: "Senior Android developer with 11 years of experience building reliable mobile products across fintech, digital assets, payments, and public health. I work across the full delivery lifecycle, from architecture and polished UI to secure integrations and production quality.",
  email: "essalah.elhechmi@gmail.com",
  phone: "+216 27 441 054",
  location: "Tunis, Tunisia",
  website: "https://elhachmi.dev",
  avatarUrl: "https://res.cloudinary.com/portfolio/image/upload/v1/avatar/elhachmi-salah.jpg",
  socialLinks: [
    { platform: "LinkedIn", url: "https://www.linkedin.com/in/essalah-elhechmi" },
    { platform: "GitHub", url: "https://github.com/elhachmi-salah" },
    { platform: "Twitter", url: "https://twitter.com/elhachmi_salah" },
  ],
};

export const fallbackExperiences: Experience[] = [
  { company: "Digitus", role: "Senior Android Developer", startDate: "2021-03", endDate: null, location: "Tunis, Tunisia", description: "Building a digital-assets wallet for instant transfers, secure storage, and portfolio growth.", achievements: ["Built the Android product from scratch with Kotlin and Jetpack Compose", "Delivered instant transfer and real-time portfolio management", "Integrated secure authentication and biometric login", "Sustained a 99.5% crash-free rate through robust error handling"] },
  { company: "Tayara", role: "Android Developer", startDate: "2018-06", endDate: "2021-02", location: "Tunis, Tunisia", description: "Developed tPay, a mobile payment app for transfers and bill payment.", achievements: ["Shipped a payment app serving more than 50K users", "Integrated send and receive flows with banking APIs", "Built bill payment support across multiple providers", "Migrated the codebase from Java to Kotlin, improving code quality by 40%"] },
  { company: "ConceptLab", role: "Android Developer", startDate: "2015-01", endDate: "2018-05", location: "Sousse, Tunisia", description: "Created a field application for WHO agents to register and map hospital information.", achievements: ["Deployed the application across three countries", "Designed an offline-first hospital registration workflow", "Integrated GPS tracking and custom map overlays", "Built a REST data layer with Retrofit and RxJava"] },
];

export const fallbackProjects: Project[] = [
  { title: "CryptoTrack", slug: "cryptotrack", description: "Real-time cryptocurrency portfolio tracking with price alerts, interactive charts, and portfolio analytics, built with clean architecture and Material Design 3.", imageUrl: "https://res.cloudinary.com/portfolio/image/upload/v1/projects/cryptotrack.png", liveUrl: "https://play.google.com/store/apps/details?id=com.example.cryptotrack", githubUrl: "https://github.com/elhachmi-salah/cryptotrack", featured: true, techStack: ["Kotlin", "Jetpack Compose", "Hilt", "Room", "Retrofit", "Coroutines"] },
  { title: "TaskFlow", slug: "taskflow", description: "A focused task manager with drag-and-drop Kanban boards, team collaboration, resilient offline sync, and expressive motion.", imageUrl: "https://res.cloudinary.com/portfolio/image/upload/v1/projects/taskflow.png", githubUrl: "https://github.com/elhachmi-salah/taskflow", featured: true, techStack: ["Flutter", "Dart", "Firebase", "Bloc"] },
  { title: "WeatherNow", slug: "weathernow", description: "A location-aware weather application with animated conditions and clear seven-day forecasting powered by OpenWeatherMap.", imageUrl: "https://res.cloudinary.com/portfolio/image/upload/v1/projects/weathernow.png", githubUrl: "https://github.com/elhachmi-salah/weathernow", featured: false, techStack: ["Kotlin", "MVVM", "Retrofit", "Coroutines", "Lottie"] },
];

export const fallbackSkills: Skill[] = [
  ...["Java", "Kotlin", "Dart", "TypeScript"].map((name) => ({ name, category: "Languages", proficiency: name === "Kotlin" || name === "Java" ? "Expert" : "Advanced" })),
  ...["Android Native", "Jetpack Compose", "Flutter"].map((name) => ({ name, category: "Mobile", proficiency: name === "Android Native" ? "Expert" : "Advanced" })),
  ...["MVVM", "Dagger / Hilt", "Retrofit", "Room", "RxJava"].map((name) => ({ name, category: "Architecture", proficiency: "Expert" })),
  ...["JUnit", "Espresso", "Mockito"].map((name) => ({ name, category: "Testing", proficiency: "Advanced" })),
  ...["Spring Boot", "Firebase", "PostgreSQL", "Docker", "GitLab CI/CD"].map((name) => ({ name, category: "Platform", proficiency: "Intermediate" })),
];
export const fallbackEducation: Education[] = [
  { institution: "University of Sousse", degree: "Bachelor", field: "Computer Networks", startDate: "2011-09", endDate: "2014-06", description: "Network architecture and distributed systems." },
  { institution: "Higher Institute of Computer Science, Tunis", degree: "BS", field: "Computer Science", startDate: "2008-09", endDate: "2011-06", description: "Software engineering and algorithms." },
];
export const fallbackCertifications: Certification[] = [
  { name: "Associate Android Developer", issuer: "Google", issueDate: "2020-08", url: "https://www.credential.net/example" },
  { name: "Kotlin for Android Developers", issuer: "Udacity", issueDate: "2019-03", url: "https://www.udacity.com/certificate/example" },
];
export const fallbackLanguages: Language[] = [
  { name: "Arabic", proficiency: "Native" }, { name: "French", proficiency: "Professional" }, { name: "English", proficiency: "Professional" },
];
