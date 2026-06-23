import { CommonModule } from "@angular/common";
import { Component, computed, inject } from "@angular/core";
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from "@angular/router";
import { AuthService } from "../../core/auth.service";
import { ENTITY_CONFIGS } from "../../core/entity-config";

@Component({
  selector: "app-dashboard-shell",
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  template: `
    <main class="dashboard-shell">
      <aside class="sidebar">
        <div>
          <p class="eyebrow">Admin</p>
          <h1>Portfolio</h1>
        </div>

        <nav aria-label="Admin sections">
          <a routerLink="/overview" routerLinkActive="active">Overview</a>
          @for (config of entityConfigs; track config.path) {
            <a
              [routerLink]="['/manage', config.path]"
              routerLinkActive="active"
              >{{ config.label }}</a
            >
          }
        </nav>

        <div class="sidebar-footer">
          <span>{{ accountLabel() }}</span>
          <button type="button" class="ghost-action" (click)="signOut()">Sign out</button>
        </div>
      </aside>

      <section class="workspace">
        <router-outlet></router-outlet>
      </section>
    </main>
  `,
})
export class DashboardShellComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly entityConfigs = ENTITY_CONFIGS;
  readonly accountLabel = computed(() => this.auth.username() ?? "Authenticated");

  signOut(): void {
    this.auth.signOut();
    void this.router.navigateByUrl("/login");
  }
}
