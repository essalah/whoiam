import type { ReactNode } from "react";

export function PageHero({ eyebrow, title, intro, aside }: { eyebrow: string; title: string; intro: string; aside?: ReactNode }) {
  return <section className="page-hero"><div className="content-shell page-hero-grid"><div><span className="eyebrow">{eyebrow}</span><h1>{title}</h1><p>{intro}</p></div>{aside && <div className="hero-aside">{aside}</div>}</div></section>;
}
