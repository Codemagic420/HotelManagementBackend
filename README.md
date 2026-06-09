# Hotel Management Backend

Spring Boot 4 REST API with three parallel databases: MySQL, MongoDB and Neo4j. All 14 entities are available via separate endpoint sets for each database.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 4.0.6, Java 21 |
| Relational DB | MySQL 8.0 |
| Document DB | MongoDB 7 |
| Graph DB | Neo4j 5 |
| Auth | JWT + BCrypt |
| Docs | Swagger / OpenAPI |
| Container | Docker Compose |

---

## Getting Started — Docker (recommended)

Requires only Docker Desktop. Starts MySQL, MongoDB, Neo4j and the Spring Boot app together.

```bash
git clone https://github.com/Codemagic420/HotelManagementBackend.git
cd HotelManagementBackend
docker compose up --build
```

The app starts on **http://localhost:8080** once all health checks pass (approx. 60 seconds).

> **Fix compile errors before build** — add these if missing:
>
> `RoomRepository.java`: `Optional<Room> findByRoomNumber(String roomNumber);`
>
> `ReservationService.java`: `checkIn(Long id)` and `checkOut(Long id)` methods that update `status` to `CHECKED_IN` / `CHECKED_OUT`

---

## Getting Started — Local (without Docker)

### Prerequisites

- Java 21
- MySQL 8 running on port 3307
- MongoDB running on port 27017
- Neo4j running on port 7687

### Database setup (MySQL)

SQL scripts are in the `sql/` folder. Run them in order:

```bash
mysql -u root -p < sql/01_database_create.sql
mysql -u root -p < sql/02_test_data.sql
mysql -u root -p < sql/03_logic.sql
mysql -u root -p < sql/04_users_privileges.sql
mysql -u root -p < sql/05_audit.sql
```

### Configuration

`src/main/resources/application.properties` uses environment variables with defaults:

```properties
# MySQL (default: localhost:3307)
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3307}/${MYSQL_DATABASE:hotel_db}
spring.datasource.username=${MYSQL_ROOT_USERNAME:root}
spring.datasource.password=${MYSQL_ROOT_PASSWORD:root}

# MongoDB (default: localhost:27017, auth: admin/admin123)
spring.mongodb.uri=mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin

# Neo4j (default: localhost:7687)
spring.neo4j.uri=bolt://localhost:7687
spring.neo4j.authentication.username=neo4j
spring.neo4j.authentication.password=yourpassword
```

### Start the application

```bash
./mvnw.cmd spring-boot:run
```

---

## Swagger UI

All endpoints are documented and testable at:

```
http://localhost:8080/swagger-ui.html
```

---

## Default Users

Created from `sql/02_test_data.sql` during database initialization. Passwords are BCrypt-hashed (strength 10).

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| staff | staff123 | STAFF |
| cleaner1–5 | cleaner123 | CLEANER |

---

## API Endpoints

All endpoints require a JWT token except login and the guest AI endpoint. Get a token via login:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Use the token in subsequent requests: `Authorization: Bearer <token>`

---

### Auth

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/auth/login` | No | Login, returns JWT |
| POST | `/api/auth/logout` | No | Logout |

---

### MySQL endpoints — `/api/mysql/`

#### Rooms
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/mysql/rooms` | List (paginated, filter: `roomStatus`) |
| GET | `/api/mysql/rooms/{id}` | Get room |
| POST | `/api/mysql/rooms` | Create room |
| PUT | `/api/mysql/rooms/{id}` | Update room |
| DELETE | `/api/mysql/rooms/{id}` | Delete room |

#### Guests
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/mysql/guests` | List (paginated, filter: `lastName`) |
| GET | `/api/mysql/guests/{id}` | Get guest |
| POST | `/api/mysql/guests` | Create guest |
| PUT | `/api/mysql/guests/{id}` | Update guest |
| DELETE | `/api/mysql/guests/{id}` | Delete guest |

#### Reservations
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/mysql/reservations` | List (paginated, filter: `status`) |
| GET | `/api/mysql/reservations/{id}` | Get reservation |
| POST | `/api/mysql/reservations` | Create reservation |
| PUT | `/api/mysql/reservations/{id}` | Update reservation |
| PUT | `/api/mysql/reservations/{id}/check-in` | Check in |
| PUT | `/api/mysql/reservations/{id}/check-out` | Check out |
| DELETE | `/api/mysql/reservations/{id}` | Delete reservation |

#### Bills
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/mysql/bills` | List (paginated) |
| GET | `/api/mysql/bills/{id}` | Get bill |
| GET | `/api/mysql/bills/reservation/{reservationId}` | Bills for a reservation |
| POST | `/api/mysql/bills` | Create bill |
| POST | `/api/mysql/bills/{billId}/items` | Add line item to bill |
| PUT | `/api/mysql/bills/{id}/close` | Close bill |
| DELETE | `/api/mysql/bills/{id}` | Delete bill |

#### Other MySQL endpoints
- `/api/mysql/room-types` — CRUD for room types
- `/api/mysql/season-rates` — CRUD for season rates
- `/api/mysql/cleaners` — CRUD for cleaning staff
- `/api/mysql/extra-services` — CRUD for extra services
- `/api/mysql/inventory-items` — CRUD for inventory
- `/api/mysql/room-cleaning-tasks` — CRUD for cleaning tasks
- `/api/mysql/room-cleaning-assignments` — CRUD for cleaning assignments
- `/api/mysql/reservation-guests` — CRUD for reservation guests

---

### MongoDB endpoints — `/api/mongodb/`

Same structure as MySQL. All 14 entities are available:

```
/api/mongodb/rooms
/api/mongodb/guests
/api/mongodb/reservations
/api/mongodb/bills
/api/mongodb/bill-items
/api/mongodb/room-types
/api/mongodb/season-rates
/api/mongodb/cleaners
/api/mongodb/extra-services
/api/mongodb/inventory-items
/api/mongodb/room-cleaning-tasks
/api/mongodb/room-cleaning-assignments
/api/mongodb/reservation-guests
```

All support GET (list + single), POST, PUT, DELETE.

---

### Neo4j endpoints — `/api/neo4j/`

Same structure. All 13 node types are available:

```
/api/neo4j/rooms
/api/neo4j/guests
/api/neo4j/reservations
/api/neo4j/bills
/api/neo4j/bill-items
/api/neo4j/room-types
/api/neo4j/season-rates
/api/neo4j/cleaners
/api/neo4j/extra-services
/api/neo4j/inventory-items
/api/neo4j/room-cleaning-tasks
/api/neo4j/room-cleaning-assignments
/api/neo4j/reservation-guests
```

---

### AI Chatbot — `/api/ai/`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/ai/guest/ask` | No | Guest chatbot (public) |
| POST | `/api/ai/staff/ask` | Yes | Staff chatbot with sources |
| GET | `/api/ai/interactions` | Yes | Retrieve saved interaction history (filter: `botType`) |

Request body:
```json
{ "question": "What time is check-in?" }
```

Responses are persisted in MongoDB (`ai_interactions` collection). Requires an AI service running at `http://localhost:8000` (set `AI_SERVICE_URL` as an environment variable). Fails gracefully if the AI service is unavailable.

---

### Migration

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/migrate` | No | Run once: MySQL → MongoDB + Neo4j |

```bash
curl -X POST http://localhost:8080/api/migrate
```

The migration is idempotent — it will not run again if MongoDB already contains data. Populates all 14 collections in MongoDB and all 13 node types with relationships in Neo4j.

---

## Database Users (MySQL)

Defined in `sql/04_users_privileges.sql`:

| User | Privileges | Purpose |
|------|------------|---------|
| `admin` | ALL + GRANT OPTION | DBA |
| `hotel_app` | SELECT, INSERT, UPDATE, EXECUTE | Spring Boot application |
| `staff` | SELECT, INSERT, UPDATE, EXECUTE | Front desk |
| `user` | SELECT only | Reporting |
| `hotel_reader` | SELECT on selected columns only | Audit — cannot see email, phone or credit card number on guests |

---

## Running Tests

```bash
./mvnw.cmd test
```

41 integration tests cover MySQL, MongoDB, Neo4j and AI endpoints. No running databases required — uses H2 in-memory and mocks.

```bash
# API integration tests only
./mvnw.cmd test "-Dtest=RoomAPITest,AuthApiIntegrationTest,MongoDbApiIntegrationTest,Neo4jApiIntegrationTest,AiApiIntegrationTest"
```

---

## Project Structure

```
src/main/java/com/kea/hotel/hotelbackend/
├── controller/          # MySQL REST controllers
├── service/             # MySQL services
├── model/               # JPA entities
├── repository/          # JPA repositories
├── mongodb/
│   ├── controller/      # MongoDB REST controllers
│   ├── service/         # MongoDB services
│   ├── document/        # MongoDB documents
│   └── repository/      # MongoDB repositories
├── neo4j/
│   ├── controller/      # Neo4j REST controllers
│   ├── service/         # Neo4j services
│   ├── node/            # Neo4j node entities
│   └── repository/      # Neo4j repositories
├── security/            # JWT, SecurityConfig, DataInitializer
└── service/
    ├── AiService.java        # AI chatbot integration
    └── DataMigrator.java     # MySQL → MongoDB + Neo4j migration

sql/
├── 01_database_create.sql  # Schema, tables, FK, indexes
├── 02_test_data.sql        # 100+ records per entity
├── 03_logic.sql            # Stored procedures, triggers, views, events
├── 04_users_privileges.sql # DB users and privileges
└── 05_audit.sql            # Audit log table and triggers
```
