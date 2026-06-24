export interface AuthResponse {
  token: string;
  username?: string;
  role?: string;
}

export interface AdminEntity {
  id?: number | string;
  name?: string;
  title?: string;
  role?: string;
  category?: string;
  proficiency?: string;
  sortOrder?: number;
  sort_order?: number;
  featured?: boolean;
  [key: string]: unknown;
}

export interface EntityConfig {
  path: string;
  label: string;
  description: string;
  fields: EntityField[];
  create?: boolean;
  delete?: boolean;
  nestedUnderProfile?: boolean;
}

export interface EntityField {
  key: string;
  label: string;
  type:
    | "text"
    | "textarea"
    | "number"
    | "date"
    | "url"
    | "email"
    | "checkbox"
    | "select"
    | "string-list"
    | "multi-select"
    | "image";
  required?: boolean;
  placeholder?: string;
  options?: readonly string[];
  optionsPath?: string;
  help?: string;
}
