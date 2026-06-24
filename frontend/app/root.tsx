import { Links, Meta, Outlet, Scripts, ScrollRestoration, useRouteLoaderData, isRouteErrorResponse, useRouteError, type LinksFunction } from "react-router";
import type { ReactNode } from "react";
import { SiteHeader } from "./components/site-header";
import { SiteFooter } from "./components/site-footer";
import { NavigationProgress } from "./components/navigation-progress";
import { fallbackProfile } from "./lib/fallback";
import type { Profile } from "./lib/types";
import { portfolioApi } from "./lib/api.server";
import "./styles.css";

export const links: LinksFunction = () => [
  { rel: "preconnect", href: "https://fonts.googleapis.com" },
  { rel: "preconnect", href: "https://fonts.gstatic.com", crossOrigin: "anonymous" },
  { rel: "stylesheet", href: "https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Manrope:wght@600;700;800&display=swap" },
];

export async function loader() { return { profile: await portfolioApi.profile() }; }

const themeScript = `(function(){try{var t=localStorage.getItem('theme');document.documentElement.dataset.theme=t||(matchMedia('(prefers-color-scheme:light)').matches?'light':'dark')}catch(e){}})()`;

export function Layout({ children }: { children: ReactNode }) {
  const rootData = useRouteLoaderData("root") as { profile?: Profile } | undefined;
  const profile = rootData?.profile ?? fallbackProfile;
  return <html lang="en" suppressHydrationWarning><head><meta charSet="utf-8" /><meta name="viewport" content="width=device-width, initial-scale=1" /><meta name="theme-color" content="#101210" /><script dangerouslySetInnerHTML={{ __html: themeScript }} /><Meta /><Links /></head><body><a className="skip-link" href="#main-content">Skip to content</a><NavigationProgress /><SiteHeader /><main id="main-content">{children}</main><SiteFooter profile={profile} /><ScrollRestoration /><Scripts /></body></html>;
}

export default function App() { return <Outlet />; }

export function HydrateFallback() { return <div className="route-loading" role="status"><span /><p>Assembling the interface…</p></div>; }

export function ErrorBoundary() {
  const error = useRouteError();
  const status = isRouteErrorResponse(error) ? error.status : 500;
  return <section className="error-page"><span className="error-code">{status}</span><h1>{status === 404 ? "This route took a detour." : "Something did not compile."}</h1><p>{status === 404 ? "The page you requested is not in this build." : "The page hit an unexpected problem. Please try again."}</p><a className="button primary" href="/">Back to home</a></section>;
}
