# Frontend Handoff

Back to [README](./README.md)

## Local development quick start

1. Start PostgreSQL locally.
2. Run the backend with `SPRING_PROFILES_ACTIVE=local`.
3. The backend will auto-seed a demo account if `APP_SEED_ENABLED=true`.
4. Login with:
   - username: `demo`
   - password: `Demo123!`

## Auth model

- Access token is returned in the JSON response body.
- Refresh token is stored in an `HttpOnly` cookie named `refreshToken`.
- Protected endpoints require `Authorization: Bearer <accessToken>`.
- Refresh flow:
  1. `POST /api/v1/auth/login`
  2. store `accessToken` in memory on the frontend
  3. call protected APIs with bearer token
  4. when access token expires, call `POST /api/v1/auth/refresh` with `credentials: 'include'`
  5. replace the in-memory access token

For frontend, do not store the access token in `localStorage` unless you deliberately accept that tradeoff. In-memory state plus refresh cookie is the cleaner option for this backend.

## Standard API response

Success:

```json
{
  "data": {},
  "message": "optional",
  "meta": {
    "page": 0,
    "size": 10,
    "totalPages": 1,
    "totalElements": 3,
    "hasNext": false,
    "hasPrevious": false
  }
}
```

Error:

```json
{
  "errors": {
    "code": "BAD_REQUEST",
    "message": "Validation failed",
    "details": {
      "username": "Username is required"
    }
  }
}
```

## Standard request notes

- JSON request body uses `Content-Type: application/json`
- protected endpoints require `Authorization: Bearer <accessToken>`
- refresh and logout should be sent with credentials included
- paginated endpoints use query params like `page`, `size`, and optional filters

Example protected request:

```http
GET /api/v1/accounts?page=0&size=10
Authorization: Bearer <accessToken>
```

## Recommended Frontend pages

- `/login`
- `/register`
- `/dashboard`
- `/accounts`
- `/categories`
- `/transactions`
- `/reports`
- `/analytics`
- `/profile`

## API map

### Health

- `GET /health`

### Auth

- `POST /api/v1/auth/register`
- request:

```json
{
  "username": "demo",
  "email": "demo@example.com",
  "password": "Demo123!"
}
```

- `POST /api/v1/auth/login`
- request:

```json
{
  "username": "demo",
  "password": "Demo123!"
}
```

- response `data`:

```json
{
  "accessToken": "jwt",
  "expiresIn": 3600000
}
```

- `POST /api/v1/auth/refresh`
- `POST /api/v1/auth/logout`

### User

- `GET /api/v1/users/me`

- response `data`:

```json
{
  "id": "usr_xxx",
  "username": "demo",
  "email": "demo@myfin.local",
  "role": "USER",
  "active": true
}
```

### Accounts

- `GET /api/v1/accounts?keyword=&page=0&size=10`
- `POST /api/v1/accounts`
- `GET /api/v1/accounts/{id}`
- `PUT /api/v1/accounts/{id}`
- `DELETE /api/v1/accounts/{id}`

Request body:

```json
{
  "name": "Main Wallet",
  "balance": 0
}
```

Response item:

```json
{
  "id": "acc_xxx",
  "name": "Main Wallet",
  "balance": 1500000.00
}
```

### Categories

- `GET /api/v1/categories?type=INCOME|EXPENSE&keyword=&page=0&size=10`
- `POST /api/v1/categories`
- `GET /api/v1/categories/{id}`
- `PUT /api/v1/categories/{id}`
- `DELETE /api/v1/categories/{id}`

Request body:

```json
{
  "name": "Food",
  "type": "EXPENSE"
}
```

Response item:

```json
{
  "id": "cat_xxx",
  "name": "Food",
  "type": "EXPENSE"
}
```

### Transactions

- `GET /api/v1/transactions`
  - query:
    - `accountId`
    - `type`
    - `categoryId`
    - `startDate`
    - `endDate`
    - `keyword`
    - `page`
    - `size`
- `POST /api/v1/transactions`
- `GET /api/v1/transactions/{id}`
- `GET /api/v1/transactions/summary?accountId={id}`
- `DELETE /api/v1/transactions/{id}`

Request body:

```json
{
  "accountId": "acc_xxx",
  "categoryId": "cat_xxx",
  "amount": 185000,
  "type": "EXPENSE",
  "description": "Weekend groceries"
}
```

Response item:

```json
{
  "id": "trx_xxx",
  "accountId": "acc_xxx",
  "categoryId": "cat_xxx",
  "amount": 185000,
  "type": "EXPENSE",
  "description": "Weekend groceries",
  "createdAt": "2026-04-20T11:30:00"
}
```

Summary response:

```json
{
  "totalIncome": 2000000,
  "totalExpense": 1250000,
  "balance": 750000
}
```

### Dashboard

- `GET /api/v1/dashboard`
- `GET /api/v1/dashboard/{accountId}`

Response:

```json
{
  "totalIncome": 22000000,
  "totalExpense": 9500000,
  "balance": 12500000
}
```

### Analytics

- `GET /api/v1/analytics/spending-by-category`
- `GET /api/v1/analytics/income-expense-summary`
- `GET /api/v1/analytics/monthly-trend?accountId=&year=2026`
- `GET /api/v1/analytics/biggest-transaction?accountId=&startDate=&endDate=&limit=5`

Spending by category item:

```json
{
  "categoryId": "cat_xxx",
  "categoryName": "Food",
  "type": "EXPENSE",
  "total": 845000
}
```

Monthly trend item:

```json
{
  "month": "2026-04",
  "totalIncome": 10200000,
  "totalExpense": 4380000,
  "balance": 5820000
}
```

Biggest transaction item:

```json
{
  "transactionId": "trx_xxx",
  "categoryName": "Salary",
  "type": "INCOME",
  "amount": 8750000,
  "createdAt": "2026-04-01T09:00:00",
  "description": "Monthly salary"
}
```

### Reports

- `GET /api/v1/reports/daily?accountId=&date=2026-04-20`
- `GET /api/v1/reports/weekly?accountId=&date=2026-04-20`
- `GET /api/v1/reports/monthly?accountId=&year=2026`

Daily or weekly item:

```json
{
  "period": "WEEKLY",
  "totalIncome": 1000000,
  "totalExpense": 350000,
  "balance": 650000
}
```

Monthly report item:

```json
{
  "month": "2026-04",
  "totalIncome": 10200000,
  "totalExpense": 4380000,
  "balance": 5820000
}
```

### Export

- `POST /api/v1/exports`

- request:

```json
{
  "exportType": "PDF"
}
```

- `GET /api/v1/exports/{id}`
- `GET /api/v1/exports/{id}/download`

Status item:

```json
{
  "id": "exp_xxx",
  "status": "DONE",
  "fileName": "transactions_20260419.pdf",
  "downloadUrl": "/api/v1/exports/exp_xxx/download",
  "createdAt": "2026-04-19T08:30:00"
}
```

## Seed data overview

The local seed creates:

- 1 demo user
- 3 accounts
- 10 categories
- 25 transactions across the last 4 months

This is enough to build:

- account list and cards
- transaction table with pagination
- category filters
- dashboard summary cards
- spending pie/bar chart
- monthly trend chart
- biggest transaction widget

## Frontend implementation notes

- Always call refresh and logout with `credentials: 'include'`.
- For local frontend, backend CORS is prepared for `http://localhost:3000`.
- Keep the access token in memory, for example in Zustand or React context.
- Add an API wrapper that retries once after `401` by calling `/auth/refresh`.

## Backend gaps to be aware of

- There is no transaction update endpoint yet.
- `TransactionResponse` only returns `accountId` and `categoryId`, not account/category names.
- Account and category responses do not include UI metadata such as icon or color.
- Login response returns only token data, not the user profile, so the frontend should call `/api/v1/users/me` after login.

These gaps do not block frontend development, but they affect how much client-side joining you need.
