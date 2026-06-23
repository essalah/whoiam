# Deployment Notes

This project is prepared for a three-service production shape:

- `postgres`: PostgreSQL 16 with a named volume for persistent data.
- `backend`: Spring Boot API built with Java 21 and Maven.
- `admin`: Angular admin dashboard served by Nginx.

## Local Container Run

1. Copy `.env.example` to `.env`.
2. Replace `POSTGRES_PASSWORD` and `JWT_SECRET` with strong values.
3. Add Cloudinary credentials if image uploads are needed.
4. Start the stack:

```bash
docker compose --env-file .env -f docker/compose.yml up --build
```

The backend is published on `http://localhost:8080` by default. The admin UI is published on `http://localhost:8081`.

## Production Checklist

- Set `SPRING_PROFILES_ACTIVE=prod` so the backend uses PostgreSQL instead of the development H2 profile.
- Use a generated `JWT_SECRET` with at least 32 random bytes.
- Keep database and Cloudinary secrets in the deployment platform secret store, not in source control.
- Put the admin container behind TLS and restrict access if the dashboard should not be public.
- Add the public React frontend service to `docker/compose.yml` once the `frontend/` app exists.
