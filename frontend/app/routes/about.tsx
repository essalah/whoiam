import { Award, BookOpen, Languages, MapPin } from "lucide-react";
import { type MetaFunction, useLoaderData } from "react-router";
import { PageHero } from "~/components/page-hero";
import { portfolioApi } from "~/lib/api.server";
import { formatDate } from "~/lib/format";

export const meta: MetaFunction = () => [{ title: "About | Elhachmi Salah" }, { name: "description", content: "About Elhachmi Salah, a senior Android engineer with 11 years of mobile product experience." }];
export async function loader() { const [profile, skills, education, certifications, languages] = await Promise.all([portfolioApi.profile(), portfolioApi.skills(), portfolioApi.education(), portfolioApi.certifications(), portfolioApi.languages()]); return { profile, skills, education, certifications, languages }; }

export default function About() {
  const { profile, skills, education, certifications, languages } = useLoaderData<typeof loader>();
  const groups = Object.entries(skills.reduce<Record<string, typeof skills>>((result, skill) => {
    (result[skill.category] ??= []).push(skill);
    return result;
  }, {}));
  return <><PageHero eyebrow="Behind the build" title="Mobile engineering grounded in product thinking." intro={profile.summary} aside={<div className="location-stamp"><MapPin /><span>Based in<strong>{profile.location}</strong></span></div>} />
    <section className="section"><div className="content-shell split-layout"><div className="prose"><span className="eyebrow">How I work</span><h2>Clarity in the architecture. Care in the details.</h2><p>I have spent my career turning complex product requirements into mobile experiences people can trust. That has meant secure wallets, payments used at scale, and offline tools designed for field teams.</p><p>I care about pragmatic architecture, useful tests, and interfaces that respect the person holding the device. I’m happiest collaborating early, asking precise questions, and helping a team ship confidently.</p></div><div className="principles"><div><strong>01</strong><h3>Own the outcome</h3><p>Technical choices serve product quality, reliability, and delivery.</p></div><div><strong>02</strong><h3>Design for change</h3><p>Clean boundaries keep a product adaptable without needless ceremony.</p></div><div><strong>03</strong><h3>Measure reality</h3><p>Crash data, user behavior, and team feedback close the loop.</p></div></div></div></section>
    <section className="section surface-band"><div className="content-shell"><div className="section-heading"><div><span className="eyebrow">Toolbox</span><h2>Technologies I rely on.</h2></div></div><div className="skill-groups">{groups.map(([category, items]) => <div className="skill-group" key={category}><h3>{category}</h3><ul>{items.map((skill) => <li key={skill.name}><span>{skill.name}</span><small>{skill.proficiency}</small></li>)}</ul></div>)}</div></div></section>
    <section className="section"><div className="content-shell credentials-grid"><div><div className="mini-heading"><BookOpen /><h2>Education</h2></div>{education.map((item) => <article className="credential" key={item.institution}><span>{formatDate(item.startDate)} — {formatDate(item.endDate)}</span><h3>{item.degree} in {item.field}</h3><p>{item.institution}</p><small>{item.description}</small></article>)}</div><div><div className="mini-heading"><Award /><h2>Credentials</h2></div>{certifications.map((item) => <article className="credential" key={item.name}><span>{formatDate(item.issueDate)}</span><h3>{item.name}</h3><p>{item.issuer}</p></article>)}</div><div><div className="mini-heading"><Languages /><h2>Languages</h2></div>{languages.map((item) => <article className="language-row" key={item.name}><strong>{item.name}</strong><span>{item.proficiency}</span></article>)}</div></div></section>
  </>;
}
