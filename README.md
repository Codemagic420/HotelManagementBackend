# Hotel Management Backend

A Spring Boot REST API for managing hotel guests, rooms, and reservations — with multi-database support (MySQL, MongoDB, Neo4j).

## Prerequisites

- Java 17
- Maven (or use the included `./mvnw` wrapper)
- MySQL 8
- MongoDB
- Neo4j

## Database Setup

SQL scripts are maintained in a separate repo: https://github.com/Driconaari/HotelSQLGroupWork

Run the scripts in order against your MySQL instance:

```bash
mysql -u root -p < 01_schema.sql
mysql -u root -p < 02_test_data.sql
mysql -u root -p < 03_logic.sql
mysql -u root -p < 04_users_privileges.sql
```

## Configuration

Edit `src/main/resources/application.properties` and fill in your credentials:

```properties
spring.datasource.password=yourpassword          # MySQL root password
spring.neo4j.authentication.password=yourpassword # Neo4j password
```

MongoDB defaults to `localhost:27017` with no authentication.

## Run

```bash
./mvnw.cmd spring-boot:run
```

The server starts on **http://localhost:8080**.

## Swagger UI

Browse and test all endpoints at:

```
http://localhost:8080/swagger-ui.html
```

## Default Users

Seeded automatically on first startup:

| Username | Password  | Role  |
|----------|-----------|-------|
| admin    | admin123  | ADMIN |
| staff    | staff123  | STAFF |

## API Endpoints

### Guests — `/api/guests` (requires ADMIN or STAFF)

| Method | Path              | Description       |
|--------|-------------------|-------------------|
| GET    | /api/guests       | List all guests   |
| GET    | /api/guests/{id}  | Get guest by ID   |
| POST   | /api/guests       | Create guest      |
| PUT    | /api/guests/{id}  | Update guest      |
| DELETE | /api/guests/{id}  | Delete guest      |

### Rooms — `/api/rooms` (public)

| Method | Path             | Description      |
|--------|------------------|------------------|
| GET    | /api/rooms       | List all rooms   |
| GET    | /api/rooms/{id}  | Get room by ID   |
| POST   | /api/rooms       | Create room      |
| PUT    | /api/rooms/{id}  | Update room      |
| DELETE | /api/rooms/{id}  | Delete room      |

### Reservations — `/api/reservations` (public)

| Method | Path                      | Description             |
|--------|---------------------------|-------------------------|
| GET    | /api/reservations         | List all reservations   |
| GET    | /api/reservations/{id}    | Get reservation by ID   |
| POST   | /api/reservations         | Create reservation      |
| PUT    | /api/reservations/{id}    | Update reservation      |
| DELETE | /api/reservations/{id}    | Delete reservation      |

### Migration — `/api/migrate` (requires ADMIN)

| Method | Path          | Description                              |
|--------|---------------|------------------------------------------|
| POST   | /api/migrate  | Migrate MySQL data to MongoDB and Neo4j  |

## Trigger Migration

```bash
curl -X POST http://localhost:8080/api/migrate \
  -u admin:admin123
```
