import { Menu, Moon, Sun, X } from "lucide-react";
import { useState } from "react";
import { NavLink } from "react-router";

const links = [
  ["/", "Home"], ["/about", "About"], ["/experience", "Experience"], ["/projects", "Projects"], ["/contact", "Contact"],
] as const;

export function SiteHeader() {
  const [open, setOpen] = useState(false);
  const toggleTheme = () => {
    const next = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
    document.documentElement.dataset.theme = next;
    localStorage.setItem("theme", next);
  };

  return <header className="site-header">
    <div className="nav-shell">
      <NavLink to="/" className="brand" aria-label="Elhachmi Salah, home"><span className="brand-mark">ES</span><span className="brand-copy">Elhachmi Salah<small>Android engineer</small></span></NavLink>
      <nav id="primary-menu" className={open ? "main-nav is-open" : "main-nav"} aria-label="Primary navigation">
        {links.map(([to, label]) => <NavLink key={to} to={to} end={to === "/"} onClick={() => setOpen(false)} className={({ isActive }) => isActive ? "active" : undefined}>{label}</NavLink>)}
      </nav>
      <div className="nav-actions">
        <button className="icon-button theme-button" onClick={toggleTheme} aria-label="Toggle color theme" title="Toggle color theme"><Sun className="sun-icon" /><Moon className="moon-icon" /></button>
        <button className="icon-button menu-button" onClick={() => setOpen(!open)} aria-expanded={open} aria-controls="primary-menu" aria-label={open ? "Close menu" : "Open menu"}>{open ? <X /> : <Menu />}</button>
      </div>
    </div>
  </header>;
}
