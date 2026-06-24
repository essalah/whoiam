import { ArrowUpRight, Code2, Github } from "lucide-react";
import { Link } from "react-router";
import type { Project } from "~/lib/types";

export function ProjectCard({ project, index = 0 }: { project: Project; index?: number }) {
  return <article className={`project-card project-tone-${index % 3}`}>
    <Link className="project-visual" to={`/projects/${project.slug}`} aria-label={`View ${project.title}`}>
      <div className="device-mock" aria-hidden="true"><div className="device-speaker" /><div className="mock-screen"><Code2 /><span>{project.title.slice(0, 1)}</span><i /></div></div>
    </Link>
    <div className="project-body"><div className="project-heading"><div><span className="eyebrow">{project.featured ? "Featured build" : "Selected build"}</span><h2>{project.title}</h2></div><Link className="icon-button" to={`/projects/${project.slug}`} aria-label={`Open ${project.title}`}><ArrowUpRight /></Link></div>
    <p>{project.description}</p><ul className="tag-list" aria-label="Technology stack">{project.techStack.slice(0, 4).map((tech) => <li key={tech}>{tech}</li>)}</ul>
    {project.githubUrl && <a className="text-link" href={project.githubUrl} target="_blank" rel="noreferrer"><Github size={17} /> Source code</a>}</div>
  </article>;
}
