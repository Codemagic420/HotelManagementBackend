# Hotel Management System - Installation Guide

Complete setup guide for running the Hotel Management Backend with MySQL, MongoDB, and Neo4j.

## Prerequisites

- Docker & Docker Compose installed
- Git
- Java 21+ (LTS)
- Maven 3.9+
- Node.js (for frontend team)

## Quick Start (5 minutes)

### 1. Clone Repository
```bash
git clone https://github.com/yourrepo/HotelManagementBackend.git
cd HotelManagementBackend
```

### 2. Start Databases
```bash
docker-compose up -d
```

Wait for containers to be healthy (~30 seconds):
```bash
docker-compose ps
# All services should show "healthy"
```

### 3. Build & Run Application
```bash
mvn clean install
mvn spring-boot:run
```

Application will start at: **http://localhost:8080**

Swagger UI: **http://localhost:8080/swagger-ui.html**

---

## Detailed Setup

### Step 1: Environment Configuration

Create `.env` file (already exists, verify settings):
```
MYSQL_HOST=mysql
MYSQL_PORT=3306
MYSQL_DATABASE=hotel_db
MYSQL_ROOT_PASSWORD=root

MONGODB_HOST=mongodb
MONGODB_PORT=27017
MONGODB_USER=admin
MONGODB_PASSWORD=admin123

NEO4J_AUTH=neo4j/yourpassword
NEO4J_PORT=7687
```

### Step 2: Start Docker Containers

```bash
# Start all services
docker-compose up -d

# Verify all are running
docker-compose ps

# View logs
docker-compose logs -f
docker-compose logs -f mysql
docker-compose logs -f mongodb
docker-compose logs -f neo4j
```

### Step 3: Initialize Databases

#### MySQL (Relational Database)
Database is auto-initialized via docker-compose volumes.

Verify:
```bash
docker exec hotel_db_container mysql -u root -proot -e \
  "SHOW TABLES FROM hotel_db;"
```

Expected tables: guest, reservation, bill, room, cleaner, etc. (14 tables total)

#### MongoDB (Document Database)
Data loads automatically via MongoDataInitializer on app startup.

Verify:
```bash
docker exec hotel_mongo_container mongosh \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  --eval "db.guests.countDocuments()"
```

Expected output: 150+ documents

#### Neo4j (Graph Database)
Data loads automatically via Neo4jDataInitializer on app startup.

Access Neo4j Browser:
- URL: **http://localhost:7474**
- Username: `neo4j`
- Password: `yourpassword`

Query:
```cypher
MATCH (n) RETURN count(n) as nodeCount
```

### Step 4: Build Spring Boot Application

```bash
# Install dependencies
mvn clean install

# Run application
mvn spring-boot:run

# Or run from IDE (IntelliJ IDEA recommended)
# File > Open > Select project root
# Right-click on HotelManagementBackendApplication.java > Run
```

Application console output:
```
Started HotelManagementBackendApplication in X seconds
Tomcat started on port(s): 8080 (http)
Application started successfully
```

### Step 5: Verify Application

1. **Swagger UI**
   - Open: http://localhost:8080/swagger-ui.html
   - Try: GET /api/guests (should return 150+ guests)

2. **Test API Endpoints**
   ```bash
   # Get all guests
   curl -u admin:admin123 http://localhost:8080/api/guests
   
   # Create new guest
   curl -X POST http://localhost:8080/api/guests \
     -H "Content-Type: application/json" \
     -u admin:admin123 \
     -d '{"firstName":"Test","lastName":"User","email":"test@example.com"}'
   
   # Migrate data to MongoDB and Neo4j
   curl -X POST http://localhost:8080/api/migrate \
     -u admin:admin123
   ```

3. **Check Databases**
   - MySQL: 150+ guests, 120+ reservations, 120+ bills
   - MongoDB: All collections synced
   - Neo4j: All nodes and relationships created

---

## Database Access

### MySQL
```bash
# Connect directly
docker exec -it hotel_db_container mysql \
  -u root -proot hotel_db

# Inside MySQL
mysql> SELECT COUNT(*) FROM guest;
mysql> SELECT * FROM audit_log ORDER BY changed_at DESC LIMIT 5;
```

### MongoDB
```bash
# Connect via Mongo Shell
docker exec -it hotel_mongo_container mongosh \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin"

# Inside Mongo Shell
db.guests.find().limit(5)
db.reservations.countDocuments()
```

### Neo4j
```bash
# Via Browser UI
http://localhost:7474

# Or via cypher-shell
docker exec -it hotel_neo4j_container cypher-shell \
  -u neo4j -p yourpassword
```

---

## Default Credentials

### Application Users
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| staff | staff123 | STAFF |
| cleaner1 | cleaner123 | CLEANER |

### Database Users
| User | Password | Privileges |
|------|----------|-----------|
| root | root | ALL (MySQL) |
| admin | admin123 | MongoDB Full Access |
| neo4j | yourpassword | Neo4j Full Access |

---

## Common Issues

### 1. "Connection refused" on localhost:3306
```bash
# Check if MySQL container is running
docker ps | grep mysql

# Restart if needed
docker-compose restart mysql
```

### 2. MongoDB Authentication Error
```
"Command find requires authentication"
```
Solution: Check `.env` has correct credentials and SPRING_DATA_MONGODB_URI includes `?authSource=admin`

### 3. Neo4j Cannot Connect
```bash
# Check Neo4j is healthy
docker-compose ps neo4j

# Check logs
docker-compose logs neo4j
```

### 4. Spring Boot Won't Start
```bash
# Check Java version
java -version  # Must be 17+

# Check port 8080 is free
netstat -an | grep 8080

# Try different port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### 5. Out of Memory
```bash
# Increase Docker memory
# Settings > Resources > Memory (set to 4GB or more)

# Restart containers
docker-compose down
docker-compose up -d
```

---

## Development Workflow

### For Backend Developers
1. Pull latest `mongonneo4j` branch
2. Run `docker-compose up -d`
3. Run `mvn spring-boot:run`
4. Access http://localhost:8080/swagger-ui.html
5. Test endpoints with provided credentials

### For Frontend Developers
1. Ensure backend is running on port 8080
2. Base API URL: `http://localhost:8080/api`
3. Include Basic Auth header: `Authorization: Basic <base64(username:password)>`
4. All GET endpoints support pagination: `?page=0&size=10`

### For Database Developers
```bash
# Access MySQL Workbench
# Host: localhost:3306
# User: root
# Password: root
# Database: hotel_db

# Or use command line
mysql -h localhost -u root -proot hotel_db
```

---

## Data Backup & Restore

### Backup All Databases
```bash
# Create backup directory
mkdir backups
date_str=$(date +%Y-%m-%d_%H-%M-%S)

# MySQL backup
docker exec hotel_db_container mysqldump -u root -proot hotel_db > backups/mysql_${date_str}.sql

# MongoDB backup
docker exec hotel_mongo_container mongodump \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  --out=backups/mongodb_${date_str}
```

### Restore from Backup
```bash
# MySQL
docker exec -i hotel_db_container mysql -u root -proot hotel_db < backups/mysql_dump.sql

# MongoDB
docker exec hotel_mongo_container mongorestore \
  --uri="mongodb://admin:admin123@localhost:27017" \
  backups/mongodb_dump_dir
```

See `DUMPS_README.md` for detailed dump/restore procedures.

---

## Stopping Services

```bash
# Stop all containers (keep data)
docker-compose stop

# Stop and remove containers (keep volumes)
docker-compose down

# Stop and remove everything including volumes (DELETES DATA!)
docker-compose down -v
```

---

## Further Documentation

- **Database Schema**: See `sql/01_database_create.sql`
- **Stored Procedures**: See `sql/03_logic.sql`
- **Audit System**: See `sql/05_audit.sql`
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Dump/Restore**: See `DUMPS_README.md`
- **Security**: See `SECURITY.md` (if exists)

---

## Support

For issues or questions:
1. Check Docker container logs: `docker-compose logs`
2. Verify `.env` configuration
3. Ensure all ports (3306, 27017, 7474, 7687, 8080) are available
4. Check this guide for "Common Issues" section

Last updated: 2026-05-12
