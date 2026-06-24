export function formatDate(value?: string | null) {
  if (!value) return "Present";
  const normalized = /^\d{4}-\d{2}$/.test(value) ? `${value}-01` : value;
  return new Intl.DateTimeFormat("en", { month: "short", year: "numeric" }).format(new Date(`${normalized}T00:00:00`));
}

export function yearsSince(value: string) {
  return Math.max(1, new Date().getFullYear() - Number(value.slice(0, 4)));
}
