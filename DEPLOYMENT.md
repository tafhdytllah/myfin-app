# Deployment Notes

## Recommended stack

- Source code: GitHub
- Backend app: Render
- Database: Supabase Postgres
- Frontend: Vercel

## Config split

- `application.yaml`: shared defaults and env-based config
- `application-local.yaml`: local development overrides
- `application-prod.yaml`: production overrides
- `.env`: local-only values, not committed
- `.env.example`: committed template for required variables

Spring Boot does not automatically read `.env` by itself. For local development, load the values through IntelliJ run configuration or your terminal session.

In IntelliJ IDEA:

1. Open Run/Debug Configurations.
2. Select your Spring Boot run configuration.
3. Set `SPRING_PROFILES_ACTIVE=local`.
4. Add the rest of the variables from `.env` into Environment Variables.

## Do you need Docker?

- `docker-compose.yml` is only for local development.
- Render can deploy this app without Docker, but this repository already includes a `Dockerfile` so the runtime is predictable.

## Backend environment variables

Set these in Render:

- `SPRING_PROFILES_ACTIVE=prod`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `JWT_ISSUER`
- `JWT_EXPIRATION`
- `JWT_REFRESH_EXPIRATION`
- `SECURITY_COOKIE_SECURE=true`
- `SECURITY_COOKIE_SAME_SITE=None`
- `APP_CORS_ALLOWED_ORIGINS=https://your-frontend-domain.vercel.app`

## Supabase

Use the connection details from your Supabase project:

- Host, port, database name
- Database user
- Database password

Construct the JDBC URL like:

`jdbc:postgresql://HOST:5432/postgres?sslmode=require`

For a persistent backend like Spring Boot:

- start with the Supabase direct connection string
- if your deployment environment cannot use the direct connection cleanly, switch to the Supabase session pooler

Do not start with the transaction pooler for normal JPA traffic.

## Render deploy steps

1. Push this repository to GitHub.
2. In Render, create a new Web Service from the GitHub repo.
3. Render will detect `render.yaml` and `Dockerfile`.
4. Fill in the secret environment variables.
5. Deploy and verify `/health`.

## Vercel frontend notes

When the frontend calls the backend:

- use `credentials: 'include'`
- set the backend base URL from an environment variable
- keep `APP_CORS_ALLOWED_ORIGINS` in Render equal to your exact Vercel origin

Without these settings, refresh-token cookies will fail across domains.
