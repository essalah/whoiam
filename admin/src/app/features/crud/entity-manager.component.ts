import { CommonModule } from "@angular/common";
import { Component, computed, effect, inject, signal } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { finalize } from "rxjs";
import { AdminEntity, EntityField } from "../../core/api.models";
import { ApiService } from "../../core/api.service";
import { findEntityConfig } from "../../core/entity-config";

@Component({
  selector: "app-entity-manager",
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <header class="page-header">
      <div>
        <p class="eyebrow">Manage</p>
        <h2>{{ config().label }}</h2>
        <p>{{ config().description }}</p>
      </div>
      <button class="secondary-action" type="button" (click)="load()">Refresh</button>
    </header>

    @if (message()) {
      <p class="notice">{{ message() }}</p>
    }

    @if (error()) {
      <p class="form-error">{{ error() }}</p>
    }

    <section class="crud-layout">
      <form class="editor-panel" (ngSubmit)="save()">
        <h3>{{ editingId() === null ? "Create item" : "Edit item" }}</h3>

        @for (field of config().fields; track field.key) {
          <label [class.checkbox-field]="field.type === 'checkbox'">
            {{ field.label }}

            @switch (field.type) {
              @case ("textarea") {
                <textarea
                  [name]="field.key"
                  [required]="field.required ?? false"
                  [ngModel]="formValue(field.key)"
                  (ngModelChange)="setFormValue(field, $event)"
                ></textarea>
              }
              @case ("checkbox") {
                <input
                  [name]="field.key"
                  type="checkbox"
                  [ngModel]="formValue(field.key)"
                  (ngModelChange)="setFormValue(field, $event)"
                />
              }
              @default {
                <input
                  [name]="field.key"
                  [type]="field.type"
                  [required]="field.required ?? false"
                  [placeholder]="field.placeholder ?? ''"
                  [ngModel]="formValue(field.key)"
                  (ngModelChange)="setFormValue(field, $event)"
                />
              }
            }
          </label>
        }

        <div class="button-row">
          <button class="primary-action" type="submit" [disabled]="saving()">
            {{ saving() ? "Saving..." : "Save" }}
          </button>
          <button class="ghost-action" type="button" (click)="resetForm()">Clear</button>
        </div>
      </form>

      <section class="list-panel">
        <div class="list-heading">
          <h3>Records</h3>
          <span>{{ items().length }}</span>
        </div>

        @if (loading()) {
          <p class="muted">Loading records...</p>
        } @else if (!items().length) {
          <p class="muted">No records returned yet.</p>
        } @else {
          <div class="record-list">
            @for (item of items(); track recordKey(item, $index)) {
              <article class="record-row">
                <div>
                  <strong>{{ displayTitle(item) }}</strong>
                  <p>{{ displaySubtitle(item) }}</p>
                </div>
                <div class="row-actions">
                  <button type="button" class="ghost-action" (click)="edit(item)">
                    Edit
                  </button>
                  <button
                    type="button"
                    class="danger-action"
                    [disabled]="item.id === undefined"
                    (click)="remove(item)"
                  >
                    Delete
                  </button>
                </div>
              </article>
            }
          </div>
        }
      </section>
    </section>
  `,
})
export class EntityManagerComponent {
  private readonly api = inject(ApiService);
  private readonly route = inject(ActivatedRoute);

  readonly path = signal("");
  readonly config = computed(() => findEntityConfig(this.path()));
  readonly items = signal<AdminEntity[]>([]);
  readonly draft = signal<Record<string, unknown>>({});
  readonly editingId = signal<string | number | null>(null);
  readonly loading = signal(false);
  readonly saving = signal(false);
  readonly error = signal<string | null>(null);
  readonly message = signal<string | null>(null);

  constructor() {
    this.route.paramMap.subscribe((params) => {
      this.path.set(params.get("entity") ?? "profile");
    });

    effect(() => {
      this.path();
      this.resetForm();
      this.load();
    });
  }

  formValue(key: string): unknown {
    return this.draft()[key] ?? "";
  }

  setFormValue(field: EntityField, value: unknown): void {
    const nextValue = field.type === "number" && value !== "" ? Number(value) : value;
    this.draft.update((draft) => ({ ...draft, [field.key]: nextValue }));
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);

    this.api
      .list<AdminEntity>(this.config().path)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (items) => this.items.set(Array.isArray(items) ? items : [items]),
        error: () => this.error.set("Could not load records from the admin API."),
      });
  }

  save(): void {
    this.saving.set(true);
    this.error.set(null);
    this.message.set(null);

    const request =
      this.editingId() === null
        ? this.api.create(this.config().path, this.draft())
        : this.api.update(this.config().path, this.editingId()!, this.draft());

    request.pipe(finalize(() => this.saving.set(false))).subscribe({
      next: () => {
        this.message.set("Saved successfully.");
        this.resetForm();
        this.load();
      },
      error: () => this.error.set("Save failed. Check the fields and try again."),
    });
  }

  edit(item: AdminEntity): void {
    const draft = this.config().fields.reduce<Record<string, unknown>>((values, field) => {
      values[field.key] = item[field.key] ?? "";
      return values;
    }, {});

    this.editingId.set(item.id ?? null);
    this.draft.set(draft);
  }

  remove(item: AdminEntity): void {
    if (item.id === undefined) {
      return;
    }

    this.api.remove(this.config().path, item.id).subscribe({
      next: () => {
        this.message.set("Deleted successfully.");
        this.load();
      },
      error: () => this.error.set("Delete failed."),
    });
  }

  resetForm(): void {
    const emptyDraft = this.config().fields.reduce<Record<string, unknown>>(
      (values, field) => {
        values[field.key] = field.type === "checkbox" ? false : "";
        return values;
      },
      {},
    );

    this.editingId.set(null);
    this.draft.set(emptyDraft);
  }

  recordKey(item: AdminEntity, index: number): string | number {
    return item.id ?? index;
  }

  displayTitle(item: AdminEntity): string {
    return String(
      item.title ??
        item.name ??
        item.role ??
        item.institution ??
        item.company ??
        `Record ${item.id ?? ""}`,
    );
  }

  displaySubtitle(item: AdminEntity): string {
    return String(
      item.description ??
        item.summary ??
        item.category ??
        item.proficiency ??
        item.email ??
        "Ready to edit",
    );
  }
}
