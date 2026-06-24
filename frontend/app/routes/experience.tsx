import { BriefcaseBusiness, MapPin } from "lucide-react";
import { type MetaFunction, useLoaderData } from "react-router";
import { PageHero } from "~/components/page-hero";
import { portfolioApi } from "~/lib/api.server";
import { formatDate } from "~/lib/format";

export const meta: MetaFunction = () => [{ title: "Experience | Elhachmi Salah" }, { name: "description", content: "A decade of Android engineering across digital assets, payments, and public health products." }];
export async function loader() { return { experiences: await portfolioApi.experiences() }; }
export default function ExperiencePage() { const { experiences } = useLoaderData<typeof loader>(); return <><PageHero eyebrow="Career path" title="Eleven years of shipping mobile products." intro="From field software for global health teams to secure financial products, each role has deepened the same craft: making complex systems dependable and humane." aside={<div className="experience-total"><strong>11+</strong><span>years of Android craft</span></div>} /><section className="section"><div className="content-shell timeline">{experiences.map((experience, index) => <article className="timeline-item" key={experience.company}><div className="timeline-marker"><span>{String(index + 1).padStart(2, "0")}</span></div><div className="timeline-meta"><span>{formatDate(experience.startDate)} — {formatDate(experience.endDate)}</span><small><MapPin />{experience.location}</small></div><div className="timeline-content"><span className="company"><BriefcaseBusiness />{experience.company}</span><h2>{experience.role}</h2><p>{experience.description}</p><ul>{experience.achievements.map((achievement) => <li key={achievement}>{achievement}</li>)}</ul></div></article>)}</div></section></>; }
