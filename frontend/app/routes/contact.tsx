import { ArrowUpRight, Copy, Mail, MapPin, Phone } from "lucide-react";
import { type MetaFunction, useLoaderData } from "react-router";
import { SocialIcon } from "~/components/icons";
import { PageHero } from "~/components/page-hero";
import { portfolioApi } from "~/lib/api.server";

export const meta: MetaFunction = () => [{ title: "Contact | Elhachmi Salah" }, { name: "description", content: "Contact Elhachmi Salah about senior Android engineering and mobile product work." }];
export async function loader() { return { profile: await portfolioApi.profile() }; }

export default function Contact() {
  const { profile } = useLoaderData<typeof loader>();
  return <><PageHero eyebrow="Start a conversation" title="Have a mobile product worth getting right?" intro="I’m open to senior Android opportunities, focused consulting, and conversations with teams that care about the quality behind the interface." aside={<div className="availability large"><i /> Open to the right opportunity</div>} />
    <section className="section"><div className="content-shell contact-grid"><div className="contact-primary"><span className="eyebrow">Best place to start</span><h2>{profile.email ? "Email me directly." : "Connect with me."}</h2>{profile.email && <><a className="email-link" href={`mailto:${profile.email}`}>{profile.email}<ArrowUpRight /></a><p>Share a little about the product, team, or problem. I’ll reply with a thoughtful next step.</p><a className="button primary" href={`mailto:${profile.email}?subject=Let's build something thoughtful`}>Write an email <Mail /></a></>}</div><div className="contact-details">{profile.phone && <a href={`tel:${profile.phone.replace(/\s/g, "")}`}><Phone /><span><small>Phone</small><strong>{profile.phone}</strong></span><ArrowUpRight /></a>}{profile.location && <div><MapPin /><span><small>Location</small><strong>{profile.location}</strong></span></div>}{profile.socialLinks.map((social) => <a key={social.platform} href={social.url} target="_blank" rel="noreferrer"><SocialIcon platform={social.platform} /><span><small>Connect</small><strong>{social.platform}</strong></span><ArrowUpRight /></a>)}</div></div></section>
    <section className="contact-band"><div className="content-shell"><Copy /><p>Good software starts with a precise conversation.</p></div></section>
  </>;
}
