# Pulse

Pulse is a multi-tenant IT asset and alert platform built with Spring Boot. It keeps an inventory of a customer's assets (servers, repositories, cloud accounts, subcriptions), lets you search them with dynamic filters, and is being extended to sync assets from third-party systems such as GitHub and to raise and triage alerts.

It is a learning project modelled on the kind of software MSPs and IT teams use (Kaseya, Datto, NinjaOne). The goal is to practise production-style backend engineering one feature at a time.

## Status

| Feature | State |
|---|---|
| Asset inventory: create, fetch by id, dynamic search | Done |
| GitHub integration: REST client, pagination, retries, idempotent import | In progress |
| Alert management API | Planned |
| Kafka event pipeline, Redis caching, AI alert enrichment | Planned |
| Docker image and AWS deployment | Planned |

## Tech stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.x (Web, Data JPA, Validation, Actuator, Security, DevTools) |
| Database | PostgreSQL 16 or newer |
| Migrations | Flyway |
| Search | JPA Criteria API with Spring Data Specifications |
| HTTP client | Spring RestClient |
| Build | Maven (wrapper included) |
| Planned | Resilience4j, Kafka, Redis, Testcontainers, Docker, AWS |

## Prerequisites

- JDK 17 or newer (`java -version`)
- PostgreSQL 16 or newer, installed locally (Docker is not required)
- Git
- An IDE: Spring Tool Suite (STS) or IntelliJ IDEA
- curl or Postman for trying the API

You do not need to install Maven. Use the wrapper (`mvnw.cmd` on Windows, `./mvnw` on macOS and Linux).

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/pulse.git
cd pulse
```

### 2. Create the database

Connect as the PostgreSQL superuser and create the application user and database:

```bash
psql -U postgres
```

```sql
CREATE USER pulse WITH ENCRYPTED PASSWORD 'pulse';
CREATE DATABASE pulse OWNER pulse;
```

Making `pulse` the database owner avoids a PostgreSQL 15+ default where ordinary users cannot create objects in the `public` schema.

Check it works:

```bash
psql -h localhost -U pulse -d pulse
```

### 3. Check the configuration

`src/main/resources/application.properties` ships with local development defaults:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pulse
spring.datasource.username=pulse
spring.datasource.password=pulse

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

management.endpoints.web.exposure.include=health,info
```

These values are for local development only. Never commit real credentials or API tokens. To override anything on your machine, create `src/main/resources/application-local.properties` (it is gitignored) and start the app with the `local` profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

In STS or IntelliJ, set the active profile to `local` in the run configuration.

GitHub integration settings (only needed once that feature lands):

```properties
pulse.github.base-url=https://api.github.com
pulse.github.token=
```

Leave the token empty for unauthenticated calls (60 requests per hour). If you add one, set it as the environment variable `PULSE_GITHUB_TOKEN` instead of writing it into a file.

### 4. Run the application

```bash
./mvnw spring-boot:run          # macOS / Linux
mvnw.cmd spring-boot:run        # Windows
```

On first start Flyway applies every migration in `src/main/resources/db/migration` and creates the tables. Look for a line like `Successfully applied N migrations`.

### 5. Verify

```bash
curl -u admin:admin http://localhost:8080/actuator/health
```

Expected response: `{"status":"UP"}`.

## Using the API

All endpoints need HTTP Basic auth (local dev credentials are set in `application.properties`) and an `X-Tenant-Id` header. Every query is scoped to that tenant.

### Create an asset

macOS / Linux:

```bash
curl -X POST http://localhost:8080/api/v1/asset \
  -u admin:admin \
  -H "X-Tenant-Id: acme" \
  -H "Content-Type: application/json" \
  -d '{"externalId":"srv-01","type":"SERVER","name":"prod-db-1","status":"ACTIVE","tagsCsv":"prod,db"}'
```

Windows (cmd.exe has no line continuation and no single-quoted strings, so use one line and escape the inner quotes):

```cmd
curl -X POST http://localhost:8080/api/v1/asset -u admin:admin -H "X-Tenant-Id: acme" -H "Content-Type: application/json" -d "{\"externalId\":\"srv-01\",\"type\":\"SERVER\",\"name\":\"prod-db-1\",\"status\":\"ACTIVE\",\"tagsCsv\":\"prod,db\"}"
```

### Fetch one asset

```bash
curl -u admin:admin -H "X-Tenant-Id: acme" http://localhost:8080/api/v1/asset/1
```

### Search assets

```bash
curl -u admin:admin -H "X-Tenant-Id: acme" "http://localhost:8080/api/v1/asset/search?status=ACTIVE&type=SERVER&tag=prod&q=db&page=0&size=20"
```

| Parameter | Meaning |
|---|---|
| `type` | `SERVER`, `WORKSTATION`, `REPOSITORY`, `CLOUD_ACCOUNT` |
| `status` | `ACTIVE`, `INACTIVE`, `DECOMMISSIONED` |
| `tag` | Matches assets whose tags contain this value |
| `q` | Case-insensitive match on the asset name |
| `page`, `size`, `sort` | Standard Spring pagination. Default: 20 per page, newest first |

Every filter is optional and filters combine with AND.

## Project structure

```
src/main/java/com/pulse
├── asset/                 entity, repository, service, controller, dto
├── integration/github/    GitHub client, properties, DTOs
├── config/                security and bean configuration
└── common/exception/      shared exceptions and the global error handler

src/main/resources
├── application.properties
└── db/migration/          Flyway migrations (V1, V2, ...)
```

## Conventions

- **No Lombok.** Constructors, getters and builders are written by hand so the patterns stay visible.
- **Constructor injection only.**
- **DTOs are records.** Entities are never returned from controllers.
- **Every query is tenant-scoped.** Never fetch by id alone.
- **Flyway owns the schema.** Hibernate runs with `ddl-auto=validate`. Never edit a migration that has been applied or merged. Add a new one instead. Use timestamp versions such as `V202609211430__create_alert_table.sql` so two people never both claim the same number.
- **Git workflow.** One feature branch per task (for example `feature/alerts-api`), small commits, and a pull request reviewed by someone else before merging to `main`.

## Running the tests

```bash
./mvnw test          # macOS / Linux
mvnw.cmd test        # Windows
```

## Troubleshooting

| Symptom | Likely cause and fix |
|---|---|
| App fails on startup with a connection error | PostgreSQL is not running, or the `pulse` user or database does not exist. Redo step 2. |
| `Migration checksum mismatch` | A migration that was already applied has been edited. Revert the edit and put the change in a new migration. |
| `relation "asset" already exists` | A table was created by hand in your local database. Drop and recreate the `pulse` database. |
| `Schema-validation: missing table` | Flyway did not run. Check the migration file name (`V1__description.sql`, two underscores) and its folder. |
| `401` on a `POST` while `GET` works | CSRF protection is rejecting the request. Make sure `SecurityConfig` disables CSRF for this API. |
| `401` on everything | Wrong credentials, or curly quotes pasted into the command. Retype it by hand. |
| Windows curl: `URL rejected: Bad hostname` | You used `\` line continuation. cmd.exe needs the whole command on one line. |

## Roadmap

1. Asset inventory with dynamic search (done)
2. GitHub integration with retries and idempotent sync
3. Alert management API
4. Multithreaded sync orchestration with Redis locks
5. Kafka event pipeline with dead-letter handling
6. AI alert enrichment
7. Docker, AWS deployment, CI/CD

## Contributing

1. Create a branch: `git checkout -b feature/<short-name>`
2. Make small commits with clear messages (`feat: ...`, `fix: ...`, `chore: ...`)
3. Push the branch and open a pull request against `main`
4. Wait for a review before merging