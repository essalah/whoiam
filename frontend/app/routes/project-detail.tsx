import { ArrowLeft, ArrowUpRight, Check, Github, Layers3, Smartphone } from "lucide-react";
import { Link, type LoaderFunctionArgs, type MetaFunction, useLoaderData } from "react-router";
import { portfolioApi } from "~/lib/api.server";

export async function loader({ params }: LoaderFunctionArgs) { return { project: await portfolioApi.project(params.slug ?? "") }; }
export const meta: MetaFunction<typeof loader> = ({ data }) => [{ title: `${data?.project.title ?? "Project"} | Elhachmi Salah` }, { name: "description", content: data?.project.description ?? "Android project case study." }];

export default function ProjectDetail() {
  const { project } = useLoaderData<typeof loader>();
  return <><section className="detail-hero"><div className="content-shell"><Link className="back-link" to="/projects"><ArrowLeft /> Back to projects</Link><div className="detail-grid"><div><span className="eyebrow">Mobile case study</span><h1>{project.title}</h1><p>{project.description}</p><div className="hero-actions">{project.liveUrl && <a className="button primary" href={project.liveUrl} target="_blank" rel="noreferrer">View live <ArrowUpRight /></a>}{project.githubUrl && <a className="button secondary" href={project.githubUrl} target="_blank" rel="noreferrer"><Github /> Source</a>}</div></div><div className="detail-device"><div className="device-mock large"><div className="device-speaker" /><div className="mock-screen"><Smartphone /><span>{project.title.slice(0, 1)}</span><i /></div></div></div></div></div></section>
    <section className="section"><div className="content-shell case-grid"><div className="case-main"><span className="eyebrow">The build</span><h2>Built to stay fast, clear, and maintainable.</h2><p>{project.description}</p><p>The implementation favors explicit state, resilient data boundaries, and a focused interface. Each layer is designed to be independently testable while preserving a straightforward path from user intent to on-screen feedback.</p><div className="case-points"><div><Check /><span><strong>Product-minded delivery</strong><small>Interaction details support the core user job.</small></span></div><div><Check /><span><strong>Resilient architecture</strong><small>Clear data and domain boundaries absorb change.</small></span></div><div><Check /><span><strong>Production quality</strong><small>Testing, errors, and edge cases are part of the build.</small></span></div></div></div><aside className="stack-panel"><Layers3 /><h2>Technology</h2><ul>{project.techStack.map((tech) => <li key={tech}>{tech}</li>)}</ul></aside></div></section>
  </>;
}
