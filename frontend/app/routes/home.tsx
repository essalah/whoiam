import { ArrowRight, CheckCircle2, MapPin, Smartphone } from "lucide-react";
import { Link, type MetaFunction, useLoaderData } from "react-router";
import { ProjectCard } from "~/components/project-card";
import { portfolioApi } from "~/lib/api.server";

export const meta: MetaFunction = () => [
  { title: "Elhachmi Salah | Senior Android Developer" },
  { name: "description", content: "Senior Android developer in Tunis building secure, reliable mobile products with Kotlin, Jetpack Compose, Flutter, and clean architecture." },
  { property: "og:title", content: "Elhachmi Salah | Senior Android Developer" },
  { property: "og:type", content: "website" },
];

export async function loader() {
  const [profile, projects, skills, experiences] = await Promise.all([portfolioApi.profile(), portfolioApi.featuredProjects(), portfolioApi.skills(), portfolioApi.experiences()]);
  return { profile, projects, skills, experiences };
}

export default function Home() {
  const { profile, projects, skills, experiences } = useLoaderData<typeof loader>();
  const primarySkills = skills.filter((skill) => ["Kotlin", "Java", "Jetpack Compose", "Android Native"].some((name) => skill.name.includes(name))).slice(0, 5);
  return <>
    <section className="home-hero"><img className="hero-image" src="/assets/android-workstation.png" alt="Android phone on a precise developer workstation" /><div className="hero-shade" /><div className="content-shell hero-content"><span className="availability"><i /> Available for thoughtful mobile work</span><h1>{profile.name}</h1><p className="hero-role">Senior Android Developer</p><p className="hero-lede">I engineer mobile products that feel simple on the surface and stay dependable underneath.</p><div className="hero-actions"><Link className="button primary" to="/projects">Explore my work <ArrowRight /></Link><Link className="button secondary" to="/contact">Start a conversation</Link></div><div className="hero-meta"><span><MapPin /> {profile.location}</span><span><Smartphone /> 11+ years shipping Android</span></div></div></section>
    <section className="signal-strip" aria-label="Core expertise"><div className="content-shell signal-grid"><p>Native Android</p><p>Kotlin & Compose</p><p>Fintech systems</p><p>Technical leadership</p></div></section>
    <section className="section"><div className="content-shell"><div className="section-heading"><div><span className="eyebrow">Selected work</span><h2>Products built for real-world pressure.</h2></div><Link className="text-link" to="/projects">All projects <ArrowRight /></Link></div><div className="project-grid">{projects.map((project, index) => <ProjectCard key={project.slug} project={project} index={index} />)}</div></div></section>
    <section className="section surface-band"><div className="content-shell expertise-grid"><div><span className="eyebrow">Engineering approach</span><h2>Senior judgment, from first commit to production.</h2><p>{profile.summary}</p><Link className="button secondary" to="/experience">Follow the journey <ArrowRight /></Link></div><div className="capability-list">{primarySkills.map((skill) => <div key={skill.name}><CheckCircle2 /><span><strong>{skill.name}</strong><small>{skill.proficiency} · production experience</small></span></div>)}</div></div></section>
    <section className="section"><div className="content-shell stat-grid"><div><strong>11+</strong><span>years in mobile</span></div><div><strong>{experiences.length}</strong><span>product domains</span></div><div><strong>99.5%</strong><span>crash-free milestone</span></div><div><strong>50K+</strong><span>payment app users</span></div></div></section>
  </>;
}
