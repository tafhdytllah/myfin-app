# Myfin App

Backend API for a personal finance tracker built with Spring Boot.

## Stack

- Java 17
- Spring Boot 3
- PostgreSQL
- Flyway
- Spring Security + JWT
- Swagger

## Features

- Auth: register, login, refresh, logout
- User profile
- Accounts
- Categories
- Transactions
- Dashboard summary
- Analytics
- Reports
- Export PDF / Excel

## Documentation

- [Frontend handoff](./FRONTEND_HANDOFF.md)
- [Deployment notes](./DEPLOYMENT.md)

## Run locally

### Database

Run PostgreSQL from Docker:

```bash
docker compose up -d
```

### App

Use local profile when running from IntelliJ or terminal.

- active profile: `local`
- config base: `application.yaml`
- local override: `application-local.yaml`
- production override: `application-prod.yaml`

Important:

- `Dockerfile` is not used when you click Run from IntelliJ
- local development can run with the defaults already defined in `application.yaml` and `application-local.yaml`
- use IntelliJ Run Configuration or terminal env vars only if you want to override the defaults

## Demo seed

In local profile, demo data is created automatically.

Demo account:

- username: `demo`
- password: `Demo123!`

Seed includes:

- 3 accounts
- 10 categories
- 25 transactions

## Auth flow

- access token is returned in response body
- refresh token is stored in `HttpOnly` cookie
- protected endpoints use `Authorization: Bearer <token>`

For frontend:

- call refresh and logout with credentials included
- local frontend origin is `http://localhost:3000`

## Deploy plan

- code: GitHub
- backend: Render
- database: Supabase
- frontend: Vercel

## Notes

- `docker-compose.yml` is for local DB only
- `Dockerfile` is for deployment
- detailed API examples are in [FRONTEND_HANDOFF.md](./FRONTEND_HANDOFF.md)
- detailed deploy steps are in [DEPLOYMENT.md](./DEPLOYMENT.md)
