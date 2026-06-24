import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { AdminEntity, AuthResponse } from "./api.models";

const API_BASE_URL = "/api/v1";

@Injectable({ providedIn: "root" })
export class ApiService {
  private readonly http = inject(HttpClient);

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${API_BASE_URL}/admin/auth/login`, {
      username,
      password,
    });
  }

  list<T extends AdminEntity>(path: string): Observable<T[]> {
    return this.http.get<T[]>(this.adminUrl(path));
  }

  create<T extends AdminEntity>(path: string, payload: Partial<T>): Observable<T> {
    return this.http.post<T>(this.adminUrl(path), payload);
  }

  update<T extends AdminEntity>(
    path: string,
    id: string | number,
    payload: Partial<T>,
  ): Observable<T> {
    return this.http.put<T>(`${this.adminUrl(path)}/${id}`, payload);
  }

  remove(path: string, id: string | number): Observable<void> {
    return this.http.delete<void>(`${this.adminUrl(path)}/${id}`);
  }

  uploadImage(file: File): Observable<{ url: string }> {
    const body = new FormData();
    body.append("file", file);
    return this.http
      .post(`${API_BASE_URL}/admin/storage/upload`, body, { responseType: "text" })
      .pipe(map((url) => ({ url })));
  }

  get<T extends AdminEntity>(path: string): Observable<T> {
    return this.http.get<T>(this.adminUrl(path));
  }

  private adminUrl(path: string): string {
    return `${API_BASE_URL}/admin/${path}`;
  }
}
