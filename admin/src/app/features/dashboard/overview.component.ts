import { CommonModule } from "@angular/common";
import { Component, computed } from "@angular/core";
import { RouterLink } from "@angular/router";
import { ENTITY_CONFIGS } from "../../core/entity-config";

@Component({
  selector: "app-overview",
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <header class="page-header">
      <div>
        <p class="eyebrow">Dashboard</p>
        <h2>Content operations</h2>
      </div>
      <span class="status-pill">{{ sectionCount() }} sections wired</span>
    </header>

    <section class="metric-grid">
      <article>
        <span>Editable sections</span>
        <strong>{{ sectionCount() }}</strong>
      </article>
      <article>
        <span>API root</span>
        <strong>/api/v1/admin</strong>
      </article>
      <article>
        <span>Auth</span>
        <strong>JWT</strong>
      </article>
    </section>

    <section class="entity-grid">
      @for (config of entityConfigs; track config.path) {
        <a class="entity-card" [routerLink]="['/manage', config.path]">
          <span>{{ config.label }}</span>
          <p>{{ config.description }}</p>
        </a>
      }
    </section>
  `,
})
export class OverviewComponent {
  readonly entityConfigs = ENTITY_CONFIGS;
  readonly sectionCount = computed(() => this.entityConfigs.length);
}
