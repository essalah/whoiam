import { type MetaFunction, useLoaderData } from "react-router";
import { PageHero } from "~/components/page-hero";
import { ProjectCard } from "~/components/project-card";
import { portfolioApi } from "~/lib/api.server";

export const meta: MetaFunction = () => [{ title: "Projects | Elhachmi Salah" }, { name: "description", content: "Selected Android and Flutter products built by senior mobile engineer Elhachmi Salah." }];
export async function loader() { return { projects: await portfolioApi.projects() }; }
export default function Projects() { const { projects } = useLoaderData<typeof loader>(); return <><PageHero eyebrow="Selected projects" title="Mobile work with substance under the surface." intro="A selection of native Android and cross-platform products where architecture, interaction, and production quality move together." aside={<div className="project-count"><strong>{String(projects.length).padStart(2, "0")}</strong><span>case studies</span></div>} /><section className="section"><div className="content-shell project-grid all-projects">{projects.map((project, index) => <ProjectCard key={project.slug} project={project} index={index} />)}</div></section></>; }
