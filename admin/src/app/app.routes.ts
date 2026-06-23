import { Routes } from "@angular/router";
import { authGuard } from "./core/auth.guard";
import { EntityManagerComponent } from "./features/crud/entity-manager.component";
import { DashboardShellComponent } from "./features/dashboard/dashboard-shell.component";
import { OverviewComponent } from "./features/dashboard/overview.component";
import { LoginComponent } from "./features/auth/login.component";

export const appRoutes: Routes = [
  { path: "login", component: LoginComponent },
  {
    path: "",
    component: DashboardShellComponent,
    canActivate: [authGuard],
    children: [
      { path: "", pathMatch: "full", redirectTo: "overview" },
      { path: "overview", component: OverviewComponent },
      { path: "manage/:entity", component: EntityManagerComponent },
    ],
  },
  { path: "**", redirectTo: "" },
];
