import { bootstrapApplication } from "@angular/platform-browser";
import { provideExperimentalZonelessChangeDetection } from "@angular/core";
import { provideRouter } from "@angular/router";
import { provideAnimations } from "@angular/platform-browser/animations";
import { provideHttpClient, withInterceptors } from "@angular/common/http";
import { AppComponent } from "./app/app.component";
import { appRoutes } from "./app/app.routes";
import { authInterceptor } from "./app/core/auth.interceptor";

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(appRoutes),
    provideAnimations(),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideExperimentalZonelessChangeDetection(),
  ],
}).catch((err) => console.error(err));
