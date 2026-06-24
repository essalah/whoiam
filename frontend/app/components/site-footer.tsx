import type { Profile } from "~/lib/types";
import { SocialIcon } from "./icons";

export function SiteFooter({ profile }: { profile: Profile }) {
  return <footer className="site-footer"><div className="footer-shell">
    <div><strong>{profile.name}</strong><p>Building dependable mobile products from Tunis.</p></div>
    <div className="social-row">{profile.socialLinks.map((social) => <a key={social.platform} href={social.url} target="_blank" rel="noreferrer" aria-label={social.platform}><SocialIcon platform={social.platform} /></a>)}</div>
    <small>© {new Date().getFullYear()} {profile.name}. Built with React Router.</small>
  </div></footer>;
}
