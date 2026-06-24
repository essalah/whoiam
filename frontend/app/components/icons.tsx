import { Github, Linkedin, Twitter } from "lucide-react";

export function SocialIcon({ platform }: { platform: string }) {
  const props = { size: 18, "aria-hidden": true } as const;
  if (platform.toLowerCase() === "github") return <Github {...props} />;
  if (platform.toLowerCase() === "linkedin") return <Linkedin {...props} />;
  return <Twitter {...props} />;
}
