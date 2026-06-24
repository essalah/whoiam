import { useNavigation } from "react-router";

export function NavigationProgress() {
  const navigation = useNavigation();
  if (navigation.state === "idle") return null;
  return <div className="navigation-progress" role="status" aria-live="polite">
    <span />
    <span className="sr-only">Loading page</span>
  </div>;
}
