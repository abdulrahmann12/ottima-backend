# OTTIMA — Finishing Project Tracking System

> **Backend API Contract · v1.0 · For Frontend Developers**

A production-grade, enterprise-level backend for tracking residential and commercial finishing projects. Built with a strict **Package-by-Feature** architecture, it orchestrates three distinct user roles (Admin, Engineer, Client) through a secure, event-driven, real-time platform.

---

## Table of Contents

1. [Project Overview & Architecture](#1-project-overview--architecture)
2. [Technology Stack](#2-technology-stack)
3. [Package-by-Feature Directory Tree](#3-package-by-feature-directory-tree)
4. [Environment Configuration (.env)](#4-environment-configuration-env)
5. [Security & Authentication Contract](#5-security--authentication-contract)
6. [Core Business Logic Rules for Frontend](#6-core-business-logic-rules-for-frontend)
7. [Unified API Response Envelope](#7-unified-api-response-envelope)
8. [API Endpoints Reference](#8-api-endpoints-reference)
   - [8.1 Authentication](#81-authentication)
   - [8.2 User Management (Admin)](#82-user-management-admin)
   - [8.3 Standard Item Catalog](#83-standard-item-catalog)
   - [8.4 Projects — Admin](#84-projects--admin)
   - [8.5 Projects — Engineer](#85-projects--engineer)
   - [8.6 Projects — Client](#86-projects--client)
   - [8.7 Daily Updates — Engineer (Submit)](#87-daily-updates--engineer-submit)
   - [8.8 Daily Updates — Admin (Approval Center)](#88-daily-updates--admin-approval-center)
   - [8.9 Daily Updates — Client (Timeline View)](#89-daily-updates--client-timeline-view)
   - [8.10 Comments — Client (Feedback)](#810-comments--client-feedback)
   - [8.11 Comments — Admin (Reply)](#811-comments--admin-reply)
   - [8.12 Internal Tickets](#812-internal-tickets)
   - [8.13 Financial Records — Admin](#813-financial-records--admin)
   - [8.14 Financial Records — Client](#814-financial-records--client)
   - [8.15 Notifications](#815-notifications)
   - [8.16 Activity Logs (System Audit)](#816-activity-logs-system-audit)
9. [Async Events & Real-time (RabbitMQ + WebSocket)](#9-async-events--real-time-rabbitmq--websocket)
10. [Pagination Convention](#10-pagination-convention)
11. [Error Handling Contract](#11-error-handling-contract)
12. [Getting Started / Running Locally](#12-getting-started--running-locally)
13. [Interactive API Docs (Swagger)](#13-interactive-api-docs-swagger)

---

## 1. Project Overview & Architecture

**OTTIMA Finishing Tracker** is an enterprise SPA backend that enables:

- **Admins (Managers)** to orchestrate projects, approve content, manage finances, and audit all system activity.
- **Engineers** to report live on-site progress through daily updates and private internal tickets — without any financial visibility.
- **Clients** to transparently monitor their projects: live progress percentages, financial summaries, engineer updates, and official invoices.

### Architectural Principles

| Principle | Implementation |
|---|---|
| **Package-by-Feature** | All code for a domain (entity, DTO, service, controller, repository) lives in one package, not split by layer |
| **Role-Isolated Controllers** | Each role has its own dedicated controller (`AdminProjectController`, `EngineerProjectController`, `ClientProjectController`) |
| **Event-Driven Async** | Emails (password reset, code regeneration) and activity logs are dispatched via RabbitMQ, keeping the HTTP request lifecycle lean |
| **Real-time Notifications** | In-app notifications are pushed to users via WebSocket (STOMP over SockJS) |
| **Soft Deletes** | Users are never physically deleted; a `deletesAt` timestamp is set and a `@SQLRestriction` filter excludes them from all queries automatically |
| **ID Obfuscation** | User IDs are `Long` values encoded via **Hashids** for public exposure, while project/item/update IDs use `UUID` |
| **In-Memory Caching** | Caffeine cache is used for hot read paths |
| **Rate Limiting** | Bucket4j guards sensitive endpoints (login, password reset) |

---

## 2. Technology Stack

| Layer | Technology | Version |
|---|---|---|
| Framework | Spring Boot | 4.1.1 |
| Language | Java | 21 |
| Database | MySQL | (latest) |
| ORM | Spring Data JPA / Hibernate | — |
| Security | Spring Security + JJWT | 0.12.6 |
| Message Broker | RabbitMQ (CloudAMQP) | — |
| Real-time | WebSocket (STOMP / SockJS) | — |
| Email Service | Brevo (formerly Sendinblue) via SMTP | — |
| Media Storage | Cloudinary | — |
| Caching | Caffeine | — |
| Rate Limiting | Bucket4j | 8.14.0 |
| Object Mapping | MapStruct | 1.5.5 |
| API Docs | SpringDoc OpenAPI (Swagger UI) | 2.8.8 |
| Geo-IP | MaxMind GeoIP2 | 4.2.0 |
| ID Obfuscation | Hashids | 1.0.3 |
| PDF Generation | OpenHTMLtoPDF + Thymeleaf | — |
| QR Code | ZXing | 3.5.3 |

---

## 3. Package-by-Feature Directory Tree

```
src/main/java/com/ottima/finishing_tracking/
│
├── auth/                        # Authentication & token lifecycle
│   ├── controller/AuthController.java
│   ├── dto/request/             # LoginRequestDTO, ResetPasswordRequestDTO, ...
│   ├── dto/response/AuthResponse.java
│   └── service/AuthService.java
│
├── user/                        # Shared User entity (all roles)
│   ├── entity/User.java
│   ├── dto/request/             # CreateUserRequest, UpdateProfileRequest, ...
│   ├── dto/response/            # UserResponse, UserSummaryResponse
│   └── repository/UserRepository.java
│
├── admin/                       # Admin-specific management & dashboard
│   ├── controller/AdminController.java
│   ├── dto/                     # AdminDashboardSummary, ...
│   └── service/AdminService.java
│
├── engineer/                    # Engineer CRUD (admin-managed)
│   ├── controller/EngineerController.java
│   └── service/EngineerService.java
│
├── client/                      # Client CRUD (admin-managed)
│   ├── controller/ClientController.java
│   └── service/ClientService.java
│
├── role/                        # Role entity & seeding
│
├── project/                     # Project & Project Items — core domain
│   ├── controller/
│   │   ├── AdminProjectController.java   # /api/v1/admin/projects
│   │   ├── EngineerProjectController.java # /api/v1/engineer/projects
│   │   └── ClientProjectController.java  # /api/v1/client/projects
│   ├── dto/
│   │   ├── request/             # CreateProjectRequest, AssignProjectItemsRequest, ...
│   │   └── response/            # ProjectResponse, ProjectItemResponse, ClientProjectResponse, ...
│   ├── entity/                  # Project, ProjectItem
│   ├── enums/                   # ProjectStatus, ProjectItemStatus
│   └── service/                 # ProjectAdminService, ProjectDashboardService, ProjectTrackingService
│
├── standard_item/               # The global catalog of finishing work types
│   ├── controller/StandardItemController.java
│   └── service/StandardItemService.java
│
├── daily_update/                # Engineer daily progress reports
│   ├── controller/
│   │   ├── AdminDailyUpdateController.java
│   │   ├── EngineerDailyUpdateController.java
│   │   └── ClientDailyUpdateController.java
│   ├── dto/request/             # CreateDailyUpdateRequest, EvaluateDailyUpdateRequest
│   ├── entity/                  # DailyUpdate, UpdateImage
│   └── enums/UpdateStatus.java  # PENDING, APPROVED, REJECTED
│
├── comment/                     # Client feedback on daily updates
│   ├── controller/
│   │   ├── AdminCommentController.java
│   │   └── ClientCommentController.java
│   └── service/CommentService.java
│
├── ticket/                      # Internal tickets (Admin <=> Engineer private channel)
│   ├── controller/InternalTicketController.java
│   ├── dto/request/             # CreateTicketRequest, UpdateTicketRequest, ...
│   ├── enums/                   # TicketType, TicketStatus, AttachmentType
│   └── service/TicketService.java
│
├── financial/                   # Financial records (deposits, expenses, invoices)
│   ├── controller/
│   │   ├── AdminFinancialController.java
│   │   └── ClientFinancialController.java
│   ├── dto/request/             # CreateFinancialRecordRequest, UpdateFinancialRecordRequest
│   ├── enums/                   # RecordType, PaymentMethod, DocumentType
│   └── service/FinancialService.java
│
├── notification/                # In-app real-time notification system
│   ├── controller/NotificationController.java
│   ├── event/                   # 5 domain events (DailyUpdateSubmitted, CommentEvent, ...)
│   └── service/NotificationService.java
│
├── logging/                     # System audit trail (AOP + RabbitMQ)
│   ├── controller/ActivityLogController.java
│   ├── annotation/@LogActivity  # AOP annotation applied to critical service methods
│   └── service/ActivityLogService.java
│
├── jwt/                         # JWT issuance, validation & refresh token store
│
├── security/                    # SecurityConfig, JwtAuthFilter, CustomUserDetails
│
├── rate_limit/                  # Bucket4j rate limiting filter/annotation
│
├── config/                      # App-wide configs (RabbitMQ, WebSocket, Cloudinary, Cache, Swagger)
│
├── common/                      # Shared DTOs (BaseResponse), Messages, Validators
│
└── exception/                   # GlobalExceptionHandler + custom exception classes
```

---

## 4. Environment Configuration (.env)

Create a `.env` file in the project root. The application will **fail to start** if required variables are missing.

```dotenv
# --- Database (MySQL) ---
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# --- Bootstrap Admin (seeded on first run) ---
BOOTSTRAP_ADMIN_EMAIL=admin@yourcompany.local
BOOTSTRAP_ADMIN_PASSWORD=ChangeMe!2025

# --- JWT ---
# 64-char hex secret used to sign HS256 access tokens
JWT_SECRET=your_64_char_hex_secret_here

# --- Email — Brevo (Transactional SMTP) ---
BREVO_API_KEY=xkeysib-...
BREVO_SENDER_EMAIL=noreply@yourdomain.com
BREVO_SENDER_NAME=OTTIMA

# --- Cloudinary (Image & File Storage) ---
CLOUDINARY_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# --- RabbitMQ (CloudAMQP or self-hosted) ---
RABBITMQ_HOST=your.rabbitmq.host
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=your_vhost_user
RABBITMQ_PASSWORD=your_vhost_password
RABBITMQ_VHOST=your_vhost

# --- Application ---
APP_BASE_URL=https://yourdomain.com/

# --- Swagger UI (set false in production) ---
SWAGGER_ENABLED=true

# --- Hashids (ID Obfuscation Salt) ---
# Used to encode/decode Long user IDs when exposed via the API
HASHIDS_SALT=your_random_salt_string_here
```

| Variable | Required | Purpose |
|---|---|---|
| `DB_USERNAME` / `DB_PASSWORD` | YES | MySQL credentials |
| `BOOTSTRAP_ADMIN_EMAIL` / `BOOTSTRAP_ADMIN_PASSWORD` | YES | Seeded super-admin account (first run only) |
| `JWT_SECRET` | YES | HMAC-SHA256 signing key for access tokens |
| `BREVO_*` | YES | Transactional email (password reset, verification codes) |
| `CLOUDINARY_*` | YES | Image/PDF upload & hosting |
| `RABBITMQ_*` | YES | Async messaging for emails and audit logging |
| `APP_BASE_URL` | YES | Used in email template links |
| `SWAGGER_ENABLED` | WARNING | Set `false` in production |
| `HASHIDS_SALT` | YES | Must be consistent; changing it invalidates all existing encoded IDs |

---

## 5. Security & Authentication Contract

### 5.1 Authentication Flow

```
Frontend  ---POST /api/v1/auth/login--->  AuthService
          <-- { accessToken, refreshToken } --

On every subsequent request:
  Authorization: Bearer <accessToken>

When accessToken expires (HTTP 401):
  POST /api/v1/auth/refresh-token  { refreshToken: "..." }
  Returns new { accessToken, refreshToken }
```

### 5.2 Token Structure

**Access Token** — Short-lived JWT (HS256), signed with `JWT_SECRET`.

| Claim | Value | Notes |
|---|---|---|
| `sub` | `username` | The user's username string |
| `role` | `ROLE_ADMIN` / `ROLE_ENGINEER` / `ROLE_CLIENT` | Single role per user |
| `iat` | Unix timestamp | Issued at |
| `exp` | Unix timestamp | Expiry (configured server-side) |

**Refresh Token** — Opaque UUID stored in the database. Sent as a `{ refreshToken: "uuid" }` JSON body to `/auth/refresh-token`. Provides a new access token + new refresh token (rotation).

### 5.3 How to Attach Tokens

```http
GET /api/v1/admin/projects
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

> **NEVER** include Bearer tokens in URL query params or log them to the console.

### 5.4 Role-Based Access Control (RBAC)

The system uses Spring Security `@PreAuthorize` with three roles:

| Role | Login Credential | Base URL Prefix | Description |
|---|---|---|---|
| `ROLE_ADMIN` | Username or Email + Password | `/api/v1/admin/*` | Full system control |
| `ROLE_ENGINEER` | Username + Password | `/api/v1/engineer/*` | Field operations, zero financial access |
| `ROLE_CLIENT` | Phone Number or Username + Password | `/api/v1/client/*` | Read-only project & financial transparency |

> **Login field:** All roles use the same `/api/v1/auth/login` endpoint. The `usernameOrEmailOrNumber` field accepts any of the three identifiers and the backend resolves the correct user.

### 5.5 Password Reset Flow

```
1. POST /api/v1/auth/forget-password  { email: "..." }
   Backend generates a verification code and sends it via Brevo email (async via RabbitMQ)

2. User receives code in email, then calls:
   POST /api/v1/auth/reset-password
   { usernameOrEmailOrPhoneNumber, code, newPassword }
   Access is restored.

3. Optional: POST /api/v1/auth/regenerate-code  { email: "..." }
   Re-sends a fresh verification code if the previous one expired.
```

### 5.6 Authenticated Password Change (logged-in users)

```
POST /api/v1/auth/change-password
Authorization: Bearer <accessToken>
Body: { currentPassword, newPassword }
```

---

## 6. Core Business Logic Rules for Frontend

> These are non-negotiable derived values and behavioral rules that **must be understood** before building any UI component.

### 6.1 Progress & Spent Calculations (Server-Side Derived)

These values are **never stored** in the database — they are always computed on-the-fly at query time:

```
overallProgressPercentage = SUM( item.weightPercentage * item.completionPercentage / 100 )
                            for all active items in a project

calculatedSpent (per item) = item.budget * item.completionPercentage / 100

totalCalculatedSpent       = SUM( calculatedSpent ) for all items
```

**Frontend implication:** Do not cache or derive these values yourself. Always fetch them from the API which returns pre-calculated values. Display them as received.

### 6.2 Item Completion Percentage Can Exceed 100%

This is intentional. An engineer or admin may record `completionPercentage = 115` to indicate extra work performed (e.g., snagging, rework). **The UI must render values above 100% without clipping or capping them.**

### 6.3 Soft Deletes — Users Are Never Physically Removed

When a user is deactivated/deleted by an admin, the `deletesAt` timestamp is set. A Hibernate `@SQLRestriction` annotation automatically filters them out of all queries. The frontend should:
- Treat a user disappearing from lists as "deactivated," not an error.
- Archived/suspended user references in old records (projects, updates) may still appear as `clientName` / `engineerName` strings in project responses (historical data is preserved).

### 6.4 Daily Update Approval Workflow

```
Engineer submits update --> Status: PENDING (Draft)
        |
        v
Admin reviews, optionally edits title/notes, approves/rejects individual images
        |
        v
Admin evaluates --> Status: APPROVED or REJECTED
        |
        v
Client can ONLY see APPROVED updates (backend filters automatically)
```

- **Client timeline:** Only `APPROVED` updates are returned from the Client Daily Update endpoint.
- **Rejected images:** Individual images within an update can be rejected while the overall update is approved. Rejected images are excluded from the client's view.

### 6.5 Project Status Enum

| Value | Meaning |
|---|---|
| `ACTIVE` | Work is ongoing; engineers can submit updates |
| `PAUSED` | Work is temporarily halted |
| `DELIVERED` | Project is complete and handed over |

### 6.6 Project Item Status Enum

| Value | Meaning |
|---|---|
| `PENDING` | Not yet started on-site |
| `IN_PROGRESS` | Work is underway |
| `COMPLETED` | Item is finished |

### 6.7 Internal Tickets Are Private

Internal tickets exchanged between Admins and Engineers are **completely hidden from the Client role**. The `/api/v1/internal-tickets/*` endpoints are secured with `hasAnyRole('ADMIN', 'ENGINEER')` — the client JWT will always receive a `403 Forbidden`.

### 6.8 Financial Record Types

| `RecordType` | Meaning |
|---|---|
| `DEPOSIT` | A payment received from the client |
| `EXPENSE` | A cost/expense charged to the project |

Client's "Financial Summary" displays:
- **Total Paid** = Sum of all `DEPOSIT` records
- **Total Spent** = `totalCalculatedSpent` (derived from item progress)
- **Remaining Balance** = Total Paid minus Total Spent

### 6.9 Ticket Types

| `TicketType` | Typical Sender | Description |
|---|---|---|
| `EXPENSE` | Engineer to Admin | Petty cash / labor cost request with amount |
| `MEASUREMENT` | Engineer to Admin | On-site measurements or survey results |
| `INSTRUCTION` | Admin to Engineer | Work orders, blueprints, PDF attachments |
| `SITE_REPORT` | Engineer to Admin | General site status report |

### 6.10 ID Types — A Critical Note

| Entity | ID Type | Frontend Handling |
|---|---|---|
| User (Admin, Engineer, Client) | `Long` (Hashids-encoded in API) | Treat as opaque string |
| Project | `UUID` | Use as-is |
| Project Item | `UUID` | Use as-is |
| Daily Update | `UUID` | Use as-is |
| Comment | `UUID` | Use as-is |
| Internal Ticket | `UUID` | Use as-is |
| Financial Record | `UUID` | Use as-is |
| Notification | `UUID` | Use as-is |

### 6.11 Image Upload — Separate Pre-Upload Step

The system does **not** accept `multipart/form-data` in the JSON API endpoints. The frontend must:

1. Upload images to **Cloudinary directly** (using Cloudinary's frontend SDK or unsigned upload preset) to get the CDN URLs.
2. Include those URLs in the JSON request body (e.g., `imageUrls` in `CreateDailyUpdateRequest`).

---

## 7. Unified API Response Envelope

**Every** successful API response is wrapped in the following envelope:

```json
{
  "message": "Human-readable success message",
  "data": { },
  "timestamp": "2026-09-01T19:52:31Z"
}
```

- `data` may be `null` for operations that return no payload (e.g., DELETE, logout, mark-as-read).
- `data` may be a paginated object (see Section 10) or a plain object/array.

---

## 8. API Endpoints Reference

> **Base URL:** `http://localhost:8080` (local) or your production domain.
> All paths are prefixed with `/api/v1`.

---

### 8.1 Authentication

**Base path:** `/api/v1/auth` — All endpoints are **public** (no token required) unless noted.

---

#### `POST /api/v1/auth/login`

Log in and obtain tokens. Works for all roles.

**Request Body:**
```json
{
  "usernameOrEmailOrNumber": "admin_user",
  "password": "SecurePass123"
}
```

> Clients may use their phone number (e.g., `"01012345678"`) as the identifier.

**Response `data`:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

#### `POST /api/v1/auth/refresh-token`

Exchange a valid refresh token for a new access + refresh token pair.

**Request Body:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response `data`:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "661f9511-f30c-52e5-b827-557766551111"
}
```

---

#### `POST /api/v1/auth/logout` — Authenticated

Invalidates the refresh token in the database.

**Request Body:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response `data`:** `null`

---

#### `POST /api/v1/auth/forget-password`

Initiates the password reset flow. Sends a verification code to the user's email (async via RabbitMQ to Brevo).

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response `data`:** `null`

---

#### `POST /api/v1/auth/regenerate-code`

Regenerates and resends the verification code (if it expired).

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response `data`:** `null`

---

#### `POST /api/v1/auth/reset-password`

Resets the password using the verification code received by email.

**Request Body:**
```json
{
  "usernameOrEmailOrPhoneNumber": "user@example.com",
  "code": "ABC123",
  "newPassword": "NewSecurePass456"
}
```
> `newPassword` minimum length: **8 characters**.

**Response `data`:** `null`

---

#### `POST /api/v1/auth/change-password` — Authenticated (any role)

Changes password for the currently logged-in user.

**Request Body:**
```json
{
  "currentPassword": "OldPass123",
  "newPassword": "NewPass456"
}
```

**Response `data`:** `null`

---

### 8.2 User Management (Admin)

---

#### Admin Accounts — `/api/v1/admins`

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/api/v1/admins` | `ADMIN` | Create a new admin account |
| `GET` | `/api/v1/admins` | `ADMIN` | List all admins (paginated via `?page=0&size=10`) |
| `GET` | `/api/v1/admins/dashboard` | `ADMIN` | Get top-level admin dashboard statistics |

##### `POST /api/v1/admins` — Create Admin

**Request Body (`CreateUserRequest`):**
```json
{
  "username": "john_doe",
  "email": "john@ottima.com",
  "fullNameAr": "جون دو",
  "fullNameEn": "John Doe",
  "password": "SecurePass123",
  "phoneNumber": "01012345678"
}
```
> `phoneNumber`: exactly 11 digits. `username`: 6–50 chars. `password`: min 8 chars.

**Response `data` (`UserResponse`):**
```json
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@ottima.com",
  "fullNameAr": "جون دو",
  "fullNameEn": "John Doe",
  "phoneNumber": "01012345678",
  "roleName": "ADMIN",
  "requestCode": null,
  "active": true,
  "createdAt": "2026-09-01T19:52:31Z",
  "updatedAt": "2026-09-01T19:52:31Z"
}
```

---

#### Engineer Accounts — `/api/v1/engineers`

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/api/v1/engineers` | `ADMIN` | Create a new engineer account |
| `GET` | `/api/v1/engineers` | `ADMIN` | List all engineers (paginated via `?page=0&size=10`) |

> Uses the same `CreateUserRequest` body and `UserResponse` shape as admins.

---

#### Client Accounts — `/api/v1/clients`

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/api/v1/clients` | `ADMIN` | Create a new client account |
| `GET` | `/api/v1/clients` | `ADMIN` | List all clients (paginated via `?page=0&size=10`) |

> Uses the same `CreateUserRequest` body and `UserResponse` shape.

---

### 8.3 Standard Item Catalog

The **Standard Item Catalog** is the global list of finishing work types (e.g., "Wall Painting", "Floor Tiling", "Electrical Wiring"). Admins manage this catalog; items from it are then assigned to specific projects.

**Base path:** `/api/v1/standard-items`

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/api/v1/standard-items` | `ADMIN` | Create a new catalog item |
| `PUT` | `/api/v1/standard-items/{itemId}` | `ADMIN` | Update a catalog item |
| `DELETE` | `/api/v1/standard-items/{itemId}` | `ADMIN` | Delete a catalog item |
| `GET` | `/api/v1/standard-items/{itemId}` | Any auth | Get a single catalog item |
| `GET` | `/api/v1/standard-items` | Any auth | Search and list catalog items |

##### `POST /api/v1/standard-items` — Create Catalog Item

**Request Body (`StandardItemRequest`):**
```json
{
  "nameAr": "دهانات جدران",
  "nameEn": "Wall Painting"
}
```

**Query Params for `GET /api/v1/standard-items`:**

| Param | Type | Default | Description |
|---|---|---|---|
| `search` | `string` | — | Filter by name (Arabic or English) |
| `page` | `int` | `0` | Page number (0-indexed) |
| `size` | `int` | `10` | Items per page |

---

### 8.4 Projects — Admin

**Base path:** `/api/v1/admin/projects` · **Role:** `ADMIN`

#### Project CRUD

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/admin/projects` | Create a new project |
| `PUT` | `/api/v1/admin/projects/{projectId}` | Full update of project metadata |
| `DELETE` | `/api/v1/admin/projects/{projectId}` | Delete a project |
| `PATCH` | `/api/v1/admin/projects/{projectId}/status` | Change project status only |
| `GET` | `/api/v1/admin/projects` | Paginated list of all projects (summary cards) |
| `GET` | `/api/v1/admin/projects/{projectId}` | Full project details including all items |

##### `POST /api/v1/admin/projects` — Create Project

**Request Body (`CreateProjectRequest`):**
```json
{
  "clientId": 5,
  "engineerId": 3,
  "nameAr": "فيلا النيل - الدور الأول",
  "nameEn": "Nile Villa - First Floor",
  "addressAr": "المعادي، القاهرة",
  "addressEn": "Maadi, Cairo",
  "estimatedBudget": 500000.00,
  "startDate": "2026-09-01",
  "targetCompletionDate": "2027-03-01"
}
```
> `clientId` and `engineerId` are numeric `Long` user IDs.
> `targetCompletionDate` must be a **future** date.
> `addressAr` and `addressEn` are optional.

**Response `data` (`ProjectResponse`):**
```json
{
  "projectId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
  "nameAr": "فيلا النيل - الدور الأول",
  "nameEn": "Nile Villa - First Floor",
  "addressAr": "المعادي، القاهرة",
  "addressEn": "Maadi, Cairo",
  "clientName": "Ahmed Hassan",
  "engineerName": "Mohamed Ali",
  "overallStatus": "ACTIVE",
  "estimatedBudget": 500000.00,
  "startDate": "2026-09-01",
  "targetCompletionDate": "2027-03-01",
  "overallProgressPercentage": 0.00,
  "totalCalculatedSpent": 0.00,
  "items": []
}
```

---

##### `PUT /api/v1/admin/projects/{projectId}` — Update Project

**Request Body (`UpdateProjectRequest`):**
```json
{
  "engineerId": 4,
  "nameAr": "فيلا النيل - الدور الأول (محدث)",
  "nameEn": "Nile Villa - First Floor (Updated)",
  "addressAr": "المعادي، القاهرة",
  "addressEn": "Maadi, Cairo",
  "overallStatus": "ACTIVE",
  "estimatedBudget": 520000.00,
  "startDate": "2026-09-01",
  "targetCompletionDate": "2027-04-01"
}
```

---

##### `PATCH /api/v1/admin/projects/{projectId}/status` — Change Status Only

**Query Param (sent in URL):**
```
PATCH /api/v1/admin/projects/{projectId}/status?status=PAUSED
```
> Values: `ACTIVE`, `PAUSED`, `DELIVERED`

---

##### `GET /api/v1/admin/projects` — List All Projects (Summary)

Uses Spring Data pagination. Add query params: `?page=0&size=10&sort=createdAt,desc`

**Response `data`:** Paginated list of `ProjectSummaryResponse`:
```json
{
  "content": [
    {
      "projectId": "a1b2c3d4-...",
      "nameAr": "فيلا النيل",
      "nameEn": "Nile Villa",
      "clientName": "Ahmed Hassan",
      "engineerName": "Mohamed Ali",
      "overallStatus": "ACTIVE",
      "targetCompletionDate": "2027-03-01",
      "overallProgressPercentage": 34.50,
      "totalCalculatedSpent": 172500.00
    }
  ],
  "totalElements": 25,
  "totalPages": 3,
  "number": 0,
  "size": 10
}
```

---

#### Project Items Management

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/admin/projects/{projectId}/items` | Assign items from catalog to a project |
| `PUT` | `/api/v1/admin/projects/{projectId}/items/{itemId}` | Update item config (budget, weight, sequence, notes) |
| `DELETE` | `/api/v1/admin/projects/{projectId}/items/{itemId}` | Remove an item from a project |
| `PUT` | `/api/v1/admin/projects/{projectId}/items/{itemId}/progress` | Admin override of item progress and status |

##### `POST /api/v1/admin/projects/{projectId}/items` — Assign Items

**Request Body (`AssignProjectItemsRequest`):**
```json
{
  "items": [
    {
      "standardItemId": "f1e2d3c4-b5a6-7890-abcd-ef1234567890",
      "budget": 80000.00,
      "weightPercentage": 25.00,
      "sequenceOrder": 1,
      "generalNotes": "Use imported Italian tiles only"
    },
    {
      "standardItemId": "a9b8c7d6-e5f4-3210-fedc-ba9876543210",
      "budget": 60000.00,
      "weightPercentage": 18.00,
      "sequenceOrder": 2,
      "generalNotes": null
    }
  ]
}
```
> `weightPercentage`: range `0.01` – `100.00`. `sequenceOrder` is optional and auto-increments if omitted.

---

##### `PUT /api/v1/admin/projects/{projectId}/items/{itemId}` — Update Item Config

**Request Body (`UpdateProjectItemConfigRequest`):**
```json
{
  "budget": 85000.00,
  "weightPercentage": 27.00,
  "sequenceOrder": 1,
  "generalNotes": "Updated: Use premium Italian marble"
}
```

---

##### `PUT /api/v1/admin/projects/{projectId}/items/{itemId}/progress` — Override Item Progress

**Request Body (`UpdateItemProgressRequest`):**
```json
{
  "completionPercentage": 110.00,
  "status": "COMPLETED"
}
```
> `completionPercentage` has **no upper limit** — values above 100 are valid and expected.
> `status`: `PENDING`, `IN_PROGRESS`, `COMPLETED`

**Response `data` (`ProjectItemResponse`):**
```json
{
  "projectItemId": "b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e",
  "itemNameAr": "بلاط أرضيات",
  "itemNameEn": "Floor Tiling",
  "status": "COMPLETED",
  "budget": 80000.00,
  "weightPercentage": 25.00,
  "completionPercentage": 110.00,
  "sequenceOrder": 1,
  "generalNotes": "Use imported Italian tiles only",
  "calculatedSpent": 88000.00
}
```

---

### 8.5 Projects — Engineer

**Base path:** `/api/v1/engineer/projects` · **Role:** `ENGINEER`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/engineer/projects` | All active projects overview (paginated) |
| `GET` | `/api/v1/engineer/projects/assigned` | Projects where THIS engineer is the lead |
| `GET` | `/api/v1/engineer/projects/{projectId}` | Full project details for a specific project |
| `PUT` | `/api/v1/engineer/projects/{projectId}/items/{itemId}/progress` | Update item progress and status (field report) |

> The `GET /assigned` endpoint is the primary entry point for the engineer's main project list.

> The progress update endpoint uses the same `UpdateItemProgressRequest` body as the Admin version.

**Response for project details (`EngineerProjectResponse`):**
```json
{
  "projectId": "a1b2c3d4-...",
  "nameAr": "فيلا النيل",
  "nameEn": "Nile Villa",
  "overallStatus": "ACTIVE",
  "overallProgressPercentage": 34.50,
  "items": [
    {
      "projectItemId": "b2c3d4e5-...",
      "itemNameAr": "بلاط أرضيات",
      "itemNameEn": "Floor Tiling",
      "status": "IN_PROGRESS",
      "budget": 80000.00,
      "weightPercentage": 25.00,
      "completionPercentage": 55.00,
      "sequenceOrder": 1,
      "generalNotes": "Use imported Italian tiles only",
      "calculatedSpent": 44000.00
    }
  ]
}
```

---

### 8.6 Projects — Client

**Base path:** `/api/v1/client/projects` · **Role:** `CLIENT`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/client/projects` | List of the client's own projects |
| `GET` | `/api/v1/client/projects/{projectId}` | Full project detail (no budget figures — progress and calculatedSpent only) |

> If the client owns **multiple** projects, the list endpoint returns all of them. The frontend should show a **project selection screen** before loading the dashboard.

**Response for client project details (`ClientProjectResponse`):**
```json
{
  "projectId": "a1b2c3d4-...",
  "nameAr": "فيلا النيل",
  "nameEn": "Nile Villa",
  "overallProgressPercentage": 34.50,
  "totalCalculatedSpent": 172500.00,
  "items": [
    {
      "projectItemId": "b2c3d4e5-...",
      "itemNameAr": "بلاط أرضيات",
      "itemNameEn": "Floor Tiling",
      "status": "IN_PROGRESS",
      "weightPercentage": 25.00,
      "completionPercentage": 55.00,
      "calculatedSpent": 44000.00
    }
  ]
}
```

> NOTE: `budget` (the monetary allocation per item) is deliberately excluded from the client-facing response. Clients see `calculatedSpent` only.

---

### 8.7 Daily Updates — Engineer (Submit)

**Base path:** `/api/v1/engineer/projects/{projectId}/daily-updates` · **Role:** `ENGINEER`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/engineer/projects/{projectId}/daily-updates` | Submit a new daily update (initial status: PENDING) |
| `GET` | `/api/v1/engineer/projects/{projectId}/daily-updates` | Get engineer's own submitted updates |

##### `POST /api/v1/engineer/projects/{projectId}/daily-updates` — Submit Update

**Request Body (`CreateDailyUpdateRequest`):**
```json
{
  "projectItemId": "b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e",
  "title": "Completed first layer of wall painting",
  "notes": "All surfaces primed. Second coat scheduled for tomorrow. Weather conditions are ideal.",
  "imageUrls": [
    "https://res.cloudinary.com/djhbgtqbg/image/upload/v1/site/img1.jpg",
    "https://res.cloudinary.com/djhbgtqbg/image/upload/v1/site/img2.jpg"
  ]
}
```
> `title`: max 255 chars. `notes`: max 2000 chars. `imageUrls`: list of pre-uploaded Cloudinary URLs.

##### `GET /api/v1/engineer/projects/{projectId}/daily-updates` — My Updates

**Query Params:**

| Param | Type | Description |
|---|---|---|
| `projectItemId` | `UUID` | Filter by a specific project item |
| `status` | `UpdateStatus` | Filter: `PENDING`, `APPROVED`, `REJECTED` |
| `page`, `size`, `sort` | — | Standard pagination |

---

### 8.8 Daily Updates — Admin (Approval Center)

**Base path:** `/api/v1/admin` · **Role:** `ADMIN`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/admin/projects/{projectId}/daily-updates` | Fetch all updates for a project with filters |
| `PUT` | `/api/v1/admin/daily-updates/{dailyUpdateId}/evaluate` | Approve or reject an update |

##### `GET /api/v1/admin/projects/{projectId}/daily-updates` — All Updates for a Project

**Query Params:**

| Param | Type | Description |
|---|---|---|
| `projectItemId` | `UUID` | Filter by a specific item |
| `engineerId` | `Long` | Filter by a specific engineer |
| `status` | `UpdateStatus` | Filter: `PENDING`, `APPROVED`, `REJECTED` |
| `page`, `size`, `sort` | — | Standard pagination |

> For the **Approval Center**, query with `?status=PENDING` to see the draft queue.

---

##### `PUT /api/v1/admin/daily-updates/{dailyUpdateId}/evaluate` — Evaluate Update

This is the most feature-rich endpoint. The admin can:
1. Edit the title and notes before publishing.
2. Approve or reject individual images by ID.
3. Set the overall update status to `APPROVED` or `REJECTED`.

**Request Body (`EvaluateDailyUpdateRequest`):**
```json
{
  "status": "APPROVED",
  "title": "First layer of wall painting — approved",
  "notes": "Good progress. Materials look professional.",
  "imageEvaluations": [
    {
      "updateImageId": "c7d8e9f0-1a2b-3c4d-5e6f-7a8b9c0d1e2f",
      "approved": true
    },
    {
      "updateImageId": "d8e9f0a1-2b3c-4d5e-6f7a-8b9c0d1e2f3a",
      "approved": false
    }
  ]
}
```
> `imageEvaluations` is optional. If omitted, all images retain their previous status.
> `status` must be `APPROVED` or `REJECTED`.

---

### 8.9 Daily Updates — Client (Timeline View)

**Base path:** `/api/v1/client/items/{projectItemId}/daily-updates` · **Role:** `CLIENT`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/client/items/{projectItemId}/daily-updates` | Paginated list of APPROVED updates for an item |

> Only approved updates are returned. Rejected images within approved updates are excluded. This filtering is enforced server-side.

---

### 8.10 Comments — Client (Feedback)

**Base path:** `/api/v1/client/daily-updates/{dailyUpdateId}/comments` · **Role:** `CLIENT`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/client/daily-updates/{dailyUpdateId}/comments` | Add a comment on a daily update |
| `GET` | `/api/v1/client/daily-updates/{dailyUpdateId}/comments` | Get all comments for an update |
| `PUT` | `/api/v1/client/daily-updates/{dailyUpdateId}/comments/{commentId}` | Edit own comment |
| `DELETE` | `/api/v1/client/daily-updates/{dailyUpdateId}/comments/{commentId}` | Delete own comment |

##### `POST .../comments` — Add Comment

**Request Body (`AddCommentRequest`):**
```json
{
  "content": "ما شاء الله، الشغل كويس. متى الطبقة التانية؟"
}
```

##### `PUT .../comments/{commentId}` — Edit Comment

**Request Body (`EditCommentRequest`):**
```json
{
  "content": "تم التعديل: العمل رائع جداً"
}
```

---

### 8.11 Comments — Admin (Reply)

**Base path:** `/api/v1/admin` · **Role:** `ADMIN`

| Method | Path | Description |
|---|---|---|
| `PUT` | `/api/v1/admin/comments/{commentId}/reply` | Post an official admin reply to a client comment |
| `GET` | `/api/v1/admin/daily-updates/{dailyUpdateId}/comments` | Get all comments for an update |
| `DELETE` | `/api/v1/admin/comments/{commentId}` | Delete any comment (admin override) |

##### `PUT /api/v1/admin/comments/{commentId}/reply` — Reply to Comment

**Request Body (`ReplyCommentRequest`):**
```json
{
  "reply": "أهلاً أستاذ أحمد، الطبقة الثانية مجدولة الأربعاء القادم إن شاء الله."
}
```

---

### 8.12 Internal Tickets

**Base path:** `/api/v1/internal-tickets` · **Role:** `ADMIN` and `ENGINEER` (CLIENT receives 403)

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/api/v1/internal-tickets/projects/{projectId}` | `ADMIN` or `ENGINEER` | Create a new internal ticket |
| `PUT` | `/api/v1/internal-tickets/{ticketId}` | Sender only | Update ticket content |
| `PATCH` | `/api/v1/internal-tickets/{ticketId}/status` | Receiver or `ADMIN` | Update ticket status |
| `DELETE` | `/api/v1/internal-tickets/{ticketId}` | Sender or `ADMIN` | Delete a ticket |
| `GET` | `/api/v1/internal-tickets/{ticketId}` | Involved party | Get a single ticket |
| `GET` | `/api/v1/internal-tickets/projects/{projectId}` | `ADMIN` | All tickets for a project |
| `GET` | `/api/v1/internal-tickets/my-inbox` | Any | Tickets received by current user |
| `GET` | `/api/v1/internal-tickets/my-sent` | Any | Tickets sent by current user |
| `GET` | `/api/v1/internal-tickets/users/{userId}` | `ADMIN` | All tickets for a specific user |

##### `POST /api/v1/internal-tickets/projects/{projectId}` — Create Ticket

**Request Body (`CreateTicketRequest`):**
```json
{
  "receiverId": 3,
  "ticketType": "INSTRUCTION",
  "title": "Measurements for bathroom B-3",
  "description": "Please refer to the attached blueprint for the exact tile cutting dimensions.",
  "amount": null,
  "attachments": [
    {
      "fileUrl": "https://res.cloudinary.com/djhbgtqbg/raw/upload/v1/blueprints/bathroom_b3.pdf",
      "attachmentType": "PDF"
    }
  ]
}
```

> For `EXPENSE` tickets, include `"amount": 5000.00`.
> `attachmentType`: `IMAGE` or `PDF`
> `ticketType`: `EXPENSE`, `MEASUREMENT`, `INSTRUCTION`, `SITE_REPORT`

##### `PATCH /api/v1/internal-tickets/{ticketId}/status` — Update Ticket Status

**Request Body (`UpdateTicketStatusRequest`):**
```json
{
  "status": "APPROVED"
}
```
> `status` values: `PENDING`, `VIEWED`, `APPROVED`, `REJECTED`

---

### 8.13 Financial Records — Admin

**Base path:** `/api/v1/admin` · **Role:** `ADMIN`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/admin/projects/{projectId}/financial-records` | Record a new deposit or expense |
| `PUT` | `/api/v1/admin/financial-records/{financialRecordId}` | Update an existing record |
| `DELETE` | `/api/v1/admin/financial-records/{financialRecordId}` | Delete a record |
| `GET` | `/api/v1/admin/projects/{projectId}/financial-records` | All records for a project |
| `GET` | `/api/v1/admin/financial-records/{financialRecordId}` | Get a single record by ID |
| `GET` | `/api/v1/admin/financial-records/type/{recordType}` | All records across all projects by type |
| `GET` | `/api/v1/admin/projects/{projectId}/financial-records/type/{recordType}` | Records for one project by type |

> `{recordType}` path variable values: `DEPOSIT`, `EXPENSE`

##### `POST /api/v1/admin/projects/{projectId}/financial-records` — Create Record

**Request Body (`CreateFinancialRecordRequest`):**
```json
{
  "projectItemId": null,
  "recordType": "DEPOSIT",
  "amount": 150000.00,
  "paymentMethod": "BANK_TRANSFER",
  "transactionDate": "2026-09-01",
  "documentUrl": "https://res.cloudinary.com/.../receipt.pdf",
  "documentType": "PDF",
  "notes": "First installment from client"
}
```

| Field | Required | Values |
|---|---|---|
| `projectItemId` | Optional | UUID of a project item, or `null` for project-level records |
| `recordType` | YES | `DEPOSIT`, `EXPENSE` |
| `amount` | YES | Positive decimal |
| `paymentMethod` | YES | `CASH`, `BANK_TRANSFER`, `INSTAPAY`, `CHEQUE` |
| `transactionDate` | YES | `YYYY-MM-DD` |
| `documentUrl` | Optional | Cloudinary URL of receipt/invoice image or PDF |
| `documentType` | Optional | `IMAGE`, `PDF` |
| `notes` | Optional | Free text, max 500 chars |

---

### 8.14 Financial Records — Client

**Base path:** `/api/v1/client/projects/{projectId}` · **Role:** `CLIENT`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/client/projects/{projectId}/financial-summary` | High-level financial dashboard |
| `GET` | `/api/v1/client/projects/{projectId}/invoices` | Paginated gallery of invoice documents |
| `GET` | `/api/v1/client/projects/{projectId}/financial-records` | Full paginated list of all records |
| `GET` | `/api/v1/client/projects/{projectId}/financial-records/{financialRecordId}` | Get a single financial record |
| `GET` | `/api/v1/client/projects/{projectId}/financial-records/type/{recordType}` | Filter records by type |

##### `GET .../financial-summary` — Financial Dashboard

**Response `data` (example):**
```json
{
  "totalPaid": 300000.00,
  "totalCalculatedSpent": 172500.00,
  "remainingBalance": 127500.00
}
```

##### `GET .../invoices` — Invoice Gallery

Returns paginated records that have a `documentUrl` attached. Intended for the client's invoice gallery view.

---

### 8.15 Notifications

**Base path:** `/api/v1/notifications` · **Role:** Any authenticated user

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/notifications` | Paginated list of MY notifications |
| `GET` | `/api/v1/notifications/unread-count` | Count of unread notifications (for badge icon) |
| `PATCH` | `/api/v1/notifications/{notificationId}/read` | Mark a single notification as read |
| `PATCH` | `/api/v1/notifications/read-all` | Mark all my notifications as read |

##### `GET /api/v1/notifications/unread-count` — Badge Count

**Response `data`:**
```json
{ "unreadCount": 7 }
```

##### `GET /api/v1/notifications` — My Notifications (paginated)

**Each item in `content`:**
```json
{
  "notificationId": "e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b",
  "title": "Daily Update Approved",
  "body": "Your update 'First layer of wall painting' has been approved by the admin.",
  "isRead": false,
  "createdAt": "2026-09-01T20:15:00Z",
  "relatedEntityType": "DAILY_UPDATE",
  "relatedEntityId": "a1b2c3d4-..."
}
```

---

### 8.16 Activity Logs (System Audit)

**Base path:** `/api/v1/admin/activity-logs` · **Role:** `ADMIN`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/admin/activity-logs` | Paginated log of all system activity |
| `GET` | `/api/v1/admin/activity-logs/users/{userId}` | Logs for a specific user |

**Each log entry (example):**
```json
{
  "logId": "...",
  "actorName": "john_doe (ADMIN)",
  "action": "UPDATE_ITEM_BUDGET",
  "entityType": "PROJECT_ITEM",
  "entityId": "b2c3d4e5-...",
  "oldValue": "80000.00",
  "newValue": "85000.00",
  "timestamp": "2026-09-01T20:30:00Z",
  "ipAddress": "192.168.1.100"
}
```

> Activity logs are generated automatically by an **AOP `@LogActivity` aspect** that intercepts annotated service methods and dispatches log events asynchronously via RabbitMQ.

---

## 9. Async Events & Real-time (RabbitMQ + WebSocket)

### 9.1 RabbitMQ — Async Background Jobs

The following operations are processed **asynchronously** via RabbitMQ. The HTTP response returns immediately while the side effect is processed in the background:

| Trigger | Queue | Consumer Action |
|---|---|---|
| Forget-password request | `auth.password.reset.queue` | Brevo sends OTP email |
| Verification code regenerated | `auth.code.regenerated.queue` | Brevo resends OTP email |
| Critical admin action | `logging.activity.log.queue` | Activity log written to DB |

All queues have corresponding **Dead Letter Queues (DLQ)** for failed message reprocessing:
- Auth DLX Exchange: `auth.dlx.exchange`
- Logging DLX Exchange: `logging.dlx.exchange`

### 9.2 WebSocket — Real-Time In-App Notifications

The backend pushes in-app notifications to connected users via **STOMP over SockJS**.

**Connection Endpoint:**
```
ws://localhost:8080/ws
```
> Use `SockJS` client library — plain WebSocket is not supported.

**STOMP Setup (JavaScript):**
```javascript
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const client = new Client({
  webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
  connectHeaders: {
    Authorization: `Bearer ${accessToken}`
  },
  onConnect: () => {
    // Subscribe to the user-specific notification queue
    client.subscribe('/queue/notifications', (message) => {
      const notification = JSON.parse(message.body);
      // Update notification badge and show toast
    });
  }
});

client.activate();
```

**Notification Payload Shape (pushed via WebSocket — same shape as REST):**
```json
{
  "notificationId": "e5f6a7b8-...",
  "title": "New Comment on Your Update",
  "body": "A client commented on 'First layer of wall painting'",
  "isRead": false,
  "createdAt": "2026-09-01T20:15:00Z"
}
```

**Events that trigger a WebSocket push:**

| System Event | Who Gets Notified |
|---|---|
| Engineer submits a daily update | Admin |
| Admin approves or rejects a daily update | Engineer |
| Client adds a comment | Admin |
| Admin replies to a comment | Client |
| New internal ticket created | Receiver (Admin or Engineer) |
| Ticket status changes | Sender of the ticket |

### 9.3 Recommended Frontend Strategy

1. **On Login:** Establish WebSocket connection with the Bearer token in `connectHeaders`.
2. **On Connect:** Subscribe to `/queue/notifications`.
3. **On Disconnect / Token Refresh:** Reconnect with the new token.
4. **Badge:** Poll `GET /api/v1/notifications/unread-count` on page load, then update reactively via WebSocket messages.

---

## 10. Pagination Convention

All list endpoints that accept pagination use **Spring Data's Pageable** format.

**Request Query Params:**

| Param | Type | Default | Description |
|---|---|---|---|
| `page` | `int` | `0` | Zero-indexed page number |
| `size` | `int` | `10` | Items per page |
| `sort` | `string` | Entity default | e.g. `createdAt,desc` or `nameEn,asc` |

> Example: `GET /api/v1/admin/projects?page=1&size=20&sort=targetCompletionDate,asc`

**Response Envelope (paginated):**

```json
{
  "message": "...",
  "data": {
    "content": [ ],
    "totalElements": 47,
    "totalPages": 5,
    "number": 0,
    "size": 10,
    "first": true,
    "last": false
  },
  "timestamp": "..."
}
```

> Note: For simpler list endpoints (e.g., `GET /admins`, `GET /engineers`), pagination is passed as `?page=0&size=10` query params instead of Spring's `Pageable` — these return the same paginated shape.

---

## 11. Error Handling Contract

All errors are handled by `GlobalExceptionHandler` and return a consistent error shape:

```json
{
  "message": "Human-readable error message",
  "data": null,
  "timestamp": "2026-09-01T19:52:31Z"
}
```

**Common HTTP Status Codes:**

| Status | Scenario |
|---|---|
| `200 OK` | Successful GET, PUT, PATCH, DELETE, POST with data |
| `201 Created` | Successful POST that creates a new resource |
| `400 Bad Request` | Validation failure (e.g., blank required field, invalid enum value) |
| `401 Unauthorized` | Missing, expired, or invalid JWT token |
| `403 Forbidden` | Token is valid but the role does not have permission |
| `404 Not Found` | Requested resource does not exist |
| `409 Conflict` | Duplicate resource (e.g., username or email already exists) |
| `422 Unprocessable Entity` | Business rule violation |
| `429 Too Many Requests` | Rate limit exceeded on login or password reset endpoints |
| `500 Internal Server Error` | Unexpected server error |

**Validation Error (400) — Example:**
```json
{
  "message": "Username must be between 6 and 50 characters",
  "data": null,
  "timestamp": "2026-09-01T19:52:31Z"
}
```

---

## 12. Getting Started / Running Locally

### Prerequisites

- Java 21 JDK
- Maven 3.9+
- MySQL 8.0+ (running locally or via Docker)
- RabbitMQ (CloudAMQP free tier or local Docker)
- Cloudinary account (free tier)
- Brevo account (free tier)

### Steps

**1. Clone the repository:**
```bash
git clone <repository-url>
cd finishing-tracking
```

**2. Set up the database:**
```sql
CREATE DATABASE finishing_tracking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**3. Create the `.env` file:**
Copy the template from Section 4 and fill in your values.

**4. Run the application:**
```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The application will:
- Auto-create all database tables via Hibernate DDL
- Seed the default roles (`ADMIN`, `ENGINEER`, `CLIENT`) via `DataInitializer`
- Create the bootstrap admin account from `BOOTSTRAP_ADMIN_EMAIL` / `BOOTSTRAP_ADMIN_PASSWORD`
- Connect to RabbitMQ and declare all exchanges, queues, and DLQs

**5. Verify the app is running:**
```
GET http://localhost:8080/actuator/health
```

Expected response:
```json
{ "status": "UP" }
```

---

## 13. Interactive API Docs (Swagger)

When `SWAGGER_ENABLED=true`, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

The OpenAPI JSON spec is available at:
```
http://localhost:8080/v3/api-docs
```

> **Authorize in Swagger:** Click the `Authorize` button and enter `Bearer <your_access_token>` to test protected endpoints directly in the browser. Copy the `accessToken` from the login response.

---

*OTTIMA Finishing Tracker — Backend API Contract*
*Built with Spring Boot 4 · Java 21 · MySQL · RabbitMQ · WebSocket*
