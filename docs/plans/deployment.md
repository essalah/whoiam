# Deployment Notes

This project is prepared for a four-service production shape:

- `postgres`: PostgreSQL 16 with a named volume for persistent data.
- `backend`: Spring Boot API built with Java 21 and Maven.
- `admin`: Angular admin dashboard served by Nginx.
- `frontend`: React Router v7 SSR public portfolio served by Node.

## Local Container Run

1. Copy `.env.example` to `.env`.
2. Replace `POSTGRES_PASSWORD` and `JWT_SECRET` with strong values.
3. Add Cloudinary credentials if image uploads are needed.
4. Start the stack:

```bash
docker compose --env-file .env -f docker/compose.yml up --build
```

The backend is published on `http://localhost:8080` by default. The admin UI is published on `http://localhost:8081`, and the public portfolio is published on `http://localhost:3000`.

## Production Checklist

- Set `SPRING_PROFILES_ACTIVE=prod` so the backend uses PostgreSQL instead of the development H2 profile.
- Use a generated `JWT_SECRET` with at least 32 random bytes.
- Keep database and Cloudinary secrets in the deployment platform secret store, not in source control.
- Put the admin container behind TLS and restrict access if the dashboard should not be public.
- Set `API_BASE_URL` to the backend public portfolio endpoint for standalone frontend deployments.
- Run backend, admin, frontend, and Compose checks in CI before deployment.
