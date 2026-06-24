import { CommonModule } from "@angular/common";
import { HttpErrorResponse } from "@angular/common/http";
import { Component, computed, effect, inject, signal } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { finalize } from "rxjs";
import { AdminEntity, EntityField } from "../../core/api.models";
import { ApiService } from "../../core/api.service";
import { findEntityConfig } from "../../core/entity-config";

interface SelectOption {
  id: string | number;
  label: string;
}

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
      <button class="secondary-action" type="button" [disabled]="loading()" (click)="load()">
        {{ loading() ? "Refreshing..." : "Refresh" }}
      </button>
    </header>

    @if (message()) {
      <p class="notice" role="status">{{ message() }}</p>
    }
    @if (error()) {
      <div class="form-error" role="alert">
        <strong>Request failed</strong>
        <span>{{ error() }}</span>
      </div>
    }

    <section class="crud-layout" [attr.aria-busy]="loading() || saving()">
      <form class="editor-panel" #editorForm="ngForm" (ngSubmit)="save()">
        <div class="editor-heading">
          <h3>{{ editingId() === null ? (config().create === false ? "Select a record to edit" : "Create item") : "Edit item" }}</h3>
          @if (editingId() !== null) { <span class="muted">ID {{ editingId() }}</span> }
        </div>

        @for (field of config().fields; track field.key) {
          @switch (field.type) {
            @case ("textarea") {
              <label>
                {{ field.label }}
                <textarea [name]="field.key" [required]="field.required ?? false"
                  [placeholder]="field.placeholder ?? ''" [ngModel]="formValue(field.key)"
                  (ngModelChange)="setFormValue(field, $event)"></textarea>
              </label>
            }
            @case ("checkbox") {
              <label class="checkbox-field">
                <input [name]="field.key" type="checkbox" [ngModel]="formValue(field.key)"
                  (ngModelChange)="setFormValue(field, $event)" />
                <span>{{ field.label }}</span>
              </label>
            }
            @case ("select") {
              <label>
                {{ field.label }}
                <select [name]="field.key" [required]="field.required ?? false"
                  [ngModel]="formValue(field.key)" (ngModelChange)="setFormValue(field, $event)">
                  <option value="">Select {{ field.label.toLowerCase() }}</option>
                  @for (option of field.options ?? []; track option) {
                    <option [value]="option">{{ enumLabel(option) }}</option>
                  }
                </select>
              </label>
            }
            @case ("string-list") {
              <fieldset class="array-field">
                <legend>{{ field.label }}</legend>
                @for (value of stringList(field.key); track $index) {
                  <div class="array-row">
                    <input [name]="field.key + $index" [value]="value"
                      (input)="setListItem(field.key, $index, $any($event.target).value)"
                      [placeholder]="'Achievement ' + ($index + 1)" />
                    <button class="icon-action" type="button" title="Remove achievement"
                      aria-label="Remove achievement" (click)="removeListItem(field.key, $index)">×</button>
                  </div>
                }
                <button class="ghost-action compact-action" type="button" (click)="addListItem(field.key)">+ Add achievement</button>
                @if (field.help) { <small>{{ field.help }}</small> }
              </fieldset>
            }
            @case ("multi-select") {
              <label>
                {{ field.label }}
                <select multiple [name]="field.key" [ngModel]="formValue(field.key)"
                  (ngModelChange)="setFormValue(field, $event)" [disabled]="optionsLoading()">
                  @for (option of fieldOptions(field); track option.id) {
                    <option [ngValue]="option.id">{{ option.label }}</option>
                  }
                </select>
                <small>{{ optionsLoading() ? "Loading available skills..." : field.help }}</small>
              </label>
            }
            @case ("image") {
              <fieldset class="image-field">
                <legend>{{ field.label }}</legend>
                @if (imagePreview(field.key)) {
                  <img [src]="imagePreview(field.key)" [alt]="field.label + ' preview'" />
                }
                <input [name]="field.key" type="url" placeholder="https://..."
                  [ngModel]="formValue(field.key)" (ngModelChange)="setFormValue(field, $event)" />
                <label class="upload-action" [class.disabled]="uploadingField() !== null">
                  <span>{{ uploadingField() === field.key ? "Uploading..." : "Choose image" }}</span>
                  <input type="file" accept="image/*" [disabled]="uploadingField() !== null"
                    (change)="upload(field, $event)" />
                </label>
                @if (uploadError(field.key)) { <small class="field-error">{{ uploadError(field.key) }}</small> }
              </fieldset>
            }
            @default {
              <label>
                {{ field.label }}
                <input [name]="field.key" [type]="field.type" [required]="field.required ?? false"
                  [placeholder]="field.placeholder ?? ''" [ngModel]="formValue(field.key)"
                  (ngModelChange)="setFormValue(field, $event)" />
              </label>
            }
          }
        }

        <div class="button-row">
          <button class="primary-action" type="submit"
            [disabled]="saving() || uploadingField() !== null || editorForm.invalid">
            {{ saving() ? "Saving..." : editingId() === null ? "Create" : "Save changes" }}
          </button>
          <button class="ghost-action" type="button" [disabled]="saving()" (click)="resetForm()">
            {{ editingId() === null ? "Clear" : "Cancel" }}
          </button>
        </div>
        @if (editorForm.invalid && editorForm.submitted) {
          <small class="field-error">Complete all required fields.</small>
        }
      </form>

      <section class="list-panel">
        <div class="list-heading">
          <h3>Records</h3>
          <span>{{ items().length }}</span>
        </div>

        @if (loading()) {
          <div class="state-panel" role="status"><span class="spinner"></span><strong>Loading records</strong><p>Fetching the latest content from the API.</p></div>
        } @else if (error() && !items().length) {
          <div class="state-panel"><strong>Records unavailable</strong><p>The API did not return this section.</p><button class="ghost-action" type="button" (click)="load()">Try again</button></div>
        } @else if (!items().length) {
          <div class="state-panel"><strong>No records yet</strong><p>{{ config().create === false ? "The API has not returned an editable record." : "Create the first " + config().label.toLowerCase() + " entry using the form." }}</p></div>
        } @else {
          <div class="record-list">
            @for (item of items(); track recordKey(item, $index)) {
              <article class="record-row" [class.active-record]="editingId() === item.id">
                <div class="record-copy">
                  @if (recordImage(item)) { <img [src]="recordImage(item)" alt="" /> }
                  <div><strong>{{ displayTitle(item) }}</strong><p>{{ displaySubtitle(item) }}</p></div>
                </div>
                <div class="row-actions">
                  <button type="button" class="ghost-action" [disabled]="!canEdit()" (click)="edit(item)">Edit</button>
                  @if (config().delete !== false) {
                    <button type="button" class="danger-action" [disabled]="deletingId() === item.id || item.id === undefined" (click)="remove(item)">
                      {{ deletingId() === item.id ? "Deleting..." : "Delete" }}
                    </button>
                  }
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
  readonly deletingId = signal<string | number | null>(null);
  readonly uploadingField = signal<string | null>(null);
  readonly uploadErrors = signal<Record<string, string>>({});
  readonly localPreviews = signal<Record<string, string>>({});
  readonly options = signal<Record<string, SelectOption[]>>({});
  readonly optionsLoading = signal(false);
  readonly error = signal<string | null>(null);
  readonly message = signal<string | null>(null);
  private readonly endpoint = signal("");
  private loadGeneration = 0;

  constructor() {
    this.route.paramMap.subscribe((params) => this.path.set(params.get("entity") ?? "profile"));
    effect(() => {
      this.path();
      this.resetForm();
      this.load();
      this.loadFieldOptions();
    });
  }

  formValue(key: string): unknown { return this.draft()[key] ?? ""; }
  stringList(key: string): string[] { return (this.draft()[key] as string[] | undefined) ?? []; }
  imagePreview(key: string): string { return this.localPreviews()[key] ?? String(this.draft()[key] ?? ""); }
  uploadError(key: string): string | null { return this.uploadErrors()[key] ?? null; }
  fieldOptions(field: EntityField): SelectOption[] { return this.options()[field.key] ?? []; }

  setFormValue(field: EntityField, value: unknown): void {
    const nextValue = field.type === "number" && value !== "" ? Number(value) : value;
    this.draft.update((draft) => ({ ...draft, [field.key]: nextValue }));
  }

  addListItem(key: string): void {
    this.draft.update((draft) => ({ ...draft, [key]: [...this.stringList(key), ""] }));
  }

  setListItem(key: string, index: number, value: string): void {
    const values = [...this.stringList(key)];
    values[index] = value;
    this.draft.update((draft) => ({ ...draft, [key]: values }));
  }

  removeListItem(key: string, index: number): void {
    this.draft.update((draft) => ({ ...draft, [key]: this.stringList(key).filter((_, itemIndex) => itemIndex !== index) }));
  }

  load(): void {
    const generation = ++this.loadGeneration;
    this.loading.set(true);
    this.error.set(null);
    if (this.config().nestedUnderProfile) {
      this.api.get<AdminEntity>("profile").subscribe({
        next: (profile) => {
          if (generation !== this.loadGeneration) return;
          if (profile.id === undefined) { this.finishLoadError("The profile has no ID, so social links cannot be loaded."); return; }
          this.endpoint.set(`profiles/${profile.id}/social-links`);
          this.loadList(generation);
        },
        error: (error) => { if (generation === this.loadGeneration) this.finishLoadError(this.apiError(error, "Could not resolve the profile for social links.")); },
      });
      return;
    }
    this.endpoint.set(this.config().path);
    if (this.config().path === "profile") {
      this.api.get<AdminEntity>(this.endpoint()).pipe(finalize(() => { if (generation === this.loadGeneration) this.loading.set(false); })).subscribe({
        next: (item) => { if (generation === this.loadGeneration) this.items.set(item ? [item] : []); },
        error: (error) => { if (generation === this.loadGeneration) this.error.set(this.apiError(error, "Could not load the profile.")); },
      });
      return;
    }
    this.loadList(generation);
  }

  private loadList(generation: number): void {
    this.api.list<AdminEntity>(this.endpoint()).pipe(finalize(() => { if (generation === this.loadGeneration) this.loading.set(false); })).subscribe({
      next: (items) => { if (generation === this.loadGeneration) this.items.set(Array.isArray(items) ? items : []); },
      error: (error) => { if (generation === this.loadGeneration) this.error.set(this.apiError(error, "Could not load records from the admin API.")); },
    });
  }

  private finishLoadError(message: string): void { this.loading.set(false); this.error.set(message); }

  private loadFieldOptions(): void {
    const optionField = this.config().fields.find((field) => field.optionsPath);
    if (!optionField?.optionsPath) { this.options.set({}); return; }
    this.optionsLoading.set(true);
    this.api.list<AdminEntity>(optionField.optionsPath).pipe(finalize(() => this.optionsLoading.set(false))).subscribe({
      next: (items) => this.options.set({ [optionField.key]: items.filter((item) => item.id !== undefined).map((item) => ({ id: item.id!, label: String(item.name ?? item.title ?? item.id) })) }),
      error: (error) => this.error.set(this.apiError(error, "Could not load field options.")),
    });
  }

  save(): void {
    if (this.config().create === false && this.editingId() === null) {
      this.error.set("Select the existing profile before saving changes.");
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    this.message.set(null);
    const payload = this.cleanPayload();
    const request = this.editingId() === null
      ? this.api.create(this.endpoint(), payload)
      : this.api.update(this.endpoint(), this.editingId()!, payload);
    request.pipe(finalize(() => this.saving.set(false))).subscribe({
      next: () => { this.message.set("Changes saved successfully."); this.resetForm(); this.load(); },
      error: (error) => this.error.set(this.apiError(error, "Save failed. Check the fields and try again.")),
    });
  }

  private cleanPayload(): Record<string, unknown> {
    return Object.fromEntries(Object.entries(this.draft()).map(([key, value]) => [key,
      Array.isArray(value) && key === "achievements" ? value.map(String).map((item) => item.trim()).filter(Boolean) : value === "" ? null : value,
    ]));
  }

  edit(item: AdminEntity): void {
    if (!this.canEdit()) {
      this.error.set("Wait for relationship options to load before editing this record.");
      return;
    }
    const draft = this.config().fields.reduce<Record<string, unknown>>((values, field) => {
      if (field.type === "multi-select") {
        const stack = Array.isArray(item["techStackIds"]) ? item["techStackIds"] as unknown[] : item["techStack"] as unknown[] | undefined;
        values[field.key] = (stack ?? []).map((entry) => this.fieldOptions(field).find((option) => option.id === entry || option.label === entry)?.id).filter((id) => id !== undefined);
      } else {
        values[field.key] = item[field.key] ?? (field.type === "checkbox" ? false : field.type === "string-list" ? [] : "");
      }
      return values;
    }, {});
    this.editingId.set(item.id ?? null);
    this.draft.set(draft);
    this.error.set(null);
    this.message.set(null);
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  remove(item: AdminEntity): void {
    if (item.id === undefined || !window.confirm(`Delete ${this.displayTitle(item)}?`)) return;
    this.deletingId.set(item.id);
    this.error.set(null);
    this.api.remove(this.endpoint(), item.id).pipe(finalize(() => this.deletingId.set(null))).subscribe({
      next: () => { this.message.set("Record deleted."); if (this.editingId() === item.id) this.resetForm(); this.load(); },
      error: (error) => this.error.set(this.apiError(error, "Delete failed.")),
    });
  }

  upload(field: EntityField, event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;
    if (!file.type.startsWith("image/")) { this.setUploadError(field.key, "Choose a valid image file."); input.value = ""; return; }
    if (file.size > 5 * 1024 * 1024) { this.setUploadError(field.key, "Image must be 5 MB or smaller."); input.value = ""; return; }
    this.setUploadError(field.key, null);
    const reader = new FileReader();
    reader.onload = () => this.localPreviews.update((previews) => ({ ...previews, [field.key]: reader.result as string }));
    reader.readAsDataURL(file);
    this.uploadingField.set(field.key);
    this.api.uploadImage(file).pipe(finalize(() => { this.uploadingField.set(null); input.value = ""; })).subscribe({
      next: ({ url }) => {
        this.draft.update((draft) => ({ ...draft, [field.key]: url }));
        this.localPreviews.update((previews) => { const next = { ...previews }; delete next[field.key]; return next; });
      },
      error: (error) => this.setUploadError(field.key, this.apiError(error, "Image upload failed.")),
    });
  }

  private setUploadError(key: string, value: string | null): void {
    this.uploadErrors.update((errors) => { const next = { ...errors }; if (value) next[key] = value; else delete next[key]; return next; });
  }

  resetForm(): void {
    const emptyDraft = this.config().fields.reduce<Record<string, unknown>>((values, field) => {
      values[field.key] = field.type === "checkbox" ? false : ["string-list", "multi-select"].includes(field.type) ? [] : "";
      return values;
    }, {});
    this.editingId.set(null);
    this.draft.set(emptyDraft);
    this.uploadErrors.set({});
    this.localPreviews.set({});
  }

  recordKey(item: AdminEntity, index: number): string | number { return item.id ?? index; }
  canEdit(): boolean {
    const dynamicField = this.config().fields.find((field) => field.optionsPath);
    return !dynamicField || (!this.optionsLoading() && this.fieldOptions(dynamicField).length > 0);
  }
  recordImage(item: AdminEntity): string { return String(item["avatarUrl"] ?? item["imageUrl"] ?? ""); }
  enumLabel(value: string): string { return value.toLowerCase().replace(/_/g, " ").replace(/^./, (letter: string) => letter.toUpperCase()); }
  displayTitle(item: AdminEntity): string { return String(item.title ?? item.name ?? item.role ?? item.institution ?? item.company ?? `Record ${item.id ?? ""}`); }
  displaySubtitle(item: AdminEntity): string { return String(item.description ?? item.summary ?? item.category ?? item.proficiency ?? item.email ?? item["url"] ?? "Ready to edit"); }

  private apiError(error: unknown, fallback: string): string {
    if (!(error instanceof HttpErrorResponse)) return fallback;
    const body = error.error as unknown;
    if (typeof body === "string" && body.trim()) return body;
    if (body && typeof body === "object") {
      const response = body as Record<string, unknown>;
      const violations = response["violations"];
      if (Array.isArray(violations)) {
        const messages = violations
          .filter((item): item is Record<string, unknown> => Boolean(item) && typeof item === "object")
          .map((item) => `${String(item["field"] ?? "field")}: ${String(item["message"] ?? "invalid value")}`);
        if (messages.length) return messages.join("; ");
      }
      const fieldErrors = response["errors"];
      if (fieldErrors && typeof fieldErrors === "object") return Object.entries(fieldErrors as Record<string, unknown>).map(([field, message]) => `${field}: ${String(message)}`).join("; ");
      for (const key of ["message", "error", "detail"]) if (typeof response[key] === "string" && response[key]) return String(response[key]);
    }
    return error.status ? `${fallback} (HTTP ${error.status})` : fallback;
  }
}
