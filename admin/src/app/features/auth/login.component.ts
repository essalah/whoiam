import { CommonModule } from "@angular/common";
import { Component, computed, inject, signal } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { finalize } from "rxjs";
import { ApiService } from "../../core/api.service";
import { AuthService } from "../../core/auth.service";

@Component({
  selector: "app-login",
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <main class="login-page">
      <form class="login-panel" (ngSubmit)="submit()">
        <p class="eyebrow">Portfolio Admin</p>
        <h1>Sign in</h1>
        <p class="muted">Manage the content served by the portfolio API.</p>

        <label>
          Username
          <input
            name="username"
            autocomplete="username"
            required
            [ngModel]="username()"
            (ngModelChange)="username.set($event)"
          />
        </label>

        <label>
          Password
          <input
            name="password"
            type="password"
            autocomplete="current-password"
            required
            [ngModel]="password()"
            (ngModelChange)="password.set($event)"
          />
        </label>

        @if (error()) {
          <p class="form-error">{{ error() }}</p>
        }

        <button class="primary-action" type="submit" [disabled]="isDisabled()">
          {{ loading() ? "Signing in..." : "Sign in" }}
        </button>
      </form>
    </main>
  `,
})
export class LoginComponent {
  private readonly api = inject(ApiService);
  private readonly auth = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly username = signal("");
  readonly password = signal("");
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly isDisabled = computed(
    () => this.loading() || !this.username().trim() || !this.password(),
  );

  submit(): void {
    if (this.isDisabled()) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.api
      .login(this.username().trim(), this.password())
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (response) => {
          this.auth.signIn(response);
          const returnUrl = this.route.snapshot.queryParamMap.get("returnUrl") ?? "/";
          void this.router.navigateByUrl(returnUrl);
        },
        error: () => {
          this.error.set("The username or password was not accepted.");
        },
      });
  }
}
