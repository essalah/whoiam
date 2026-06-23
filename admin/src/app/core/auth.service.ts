import { Injectable, computed, signal } from "@angular/core";
import { AuthResponse } from "./api.models";

const TOKEN_KEY = "portfolio_admin_token";
const USER_KEY = "portfolio_admin_user";

@Injectable({ providedIn: "root" })
export class AuthService {
  private readonly tokenState = signal<string | null>(this.readStorage(TOKEN_KEY));
  private readonly userState = signal<string | null>(this.readStorage(USER_KEY));

  readonly token = this.tokenState.asReadonly();
  readonly username = this.userState.asReadonly();
  readonly isAuthenticated = computed(() => Boolean(this.tokenState()));

  signIn(response: AuthResponse): void {
    const username = response.username ?? "admin";
    this.tokenState.set(response.token);
    this.userState.set(username);
    localStorage.setItem(TOKEN_KEY, response.token);
    localStorage.setItem(USER_KEY, username);
  }

  signOut(): void {
    this.tokenState.set(null);
    this.userState.set(null);
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }

  private readStorage(key: string): string | null {
    try {
      return localStorage.getItem(key);
    } catch {
      return null;
    }
  }
}
