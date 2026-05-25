# Installation & Setup Guide

This guide explains how to set up and run the Hotel Management Backend locally using Docker Compose.

## Prerequisites

### Required Software
- **Java 21 JDK** - Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/#java21) or use [OpenJDK](https://openjdk.org/)
- **Maven 3.8+** - [Download here](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose** - [Install Docker Desktop](https://www.docker.com/products/docker-desktop)
- **Git** - [Download here](https://git-scm.com/)
- **Ollama** (for AI enrichment) - [Download here](https://ollama.ai)

### Verify Installation
```bash
java -version
mvn -version
docker --version
docker-compose --version
git --version
```

## Local Development Setup

### 1. Clone Repository
```bash
git clone https://github.com/your-org/hotel-management-backend.git
cd hotel-management-backend
```

### 2. Start Docker Databases
```bash
# Start all databases (MySQL, MongoDB, Neo4j)
docker-compose up -d

# Verify containers are running
docker-compose ps

# View logs (optional)
docker-compose logs -f
```

The databases are initialized automatically with scripts from `sql/` directory:
- `01_database_create.sql` - Creates schema
- `02_test_data.sql` - Loads 300+ test records
- `03_logic.sql` - Creates stored procedures, functions, triggers, events
- `04_users_privileges.sql` - Sets up 4 database users with different privilege levels
- `05_audit.sql` - Creates audit tables and triggers
- `06_ai_enrichment.sql` - Adds AI enrichment columns

### 3. Start Ollama (for AI enrichment)
```bash
# In a separate terminal
ollama serve

# In another terminal, download the model
ollama pull mistral:7b
```

### 4. Build the Application
```bash
# Clean and build
mvn clean package

# Or compile only (without running tests)
mvn clean compile
```

### 5. Run the Application
```bash
# Development mode
mvn spring-boot:run

# Or run the built JAR
java -jar target/hotelbackend-0.0.1-SNAPSHOT.jar
```

The application will be available at: **http://localhost:8080**

## Accessing the Application

### Swagger UI (API Documentation)
```
http://localhost:8080/swagger-ui.html
```

### Test the API
```bash
# Get all guests
curl http://localhost:8080/api/guests

# Create a guest
curl -X POST http://localhost:8080/api/guests \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "+4540123456"
  }'
```

### Database Access

#### MySQL (Port 3306)
```bash
# Connect with docker
docker exec -it hotel_db_container mysql -uroot -proot

# Commands
USE hotel_db;
SHOW TABLES;
SELECT * FROM guest;
```

#### MongoDB (Port 27017)
```bash
# Connect with docker
docker exec -it hotel_mongo_container mongosh mongodb://admin:admin123@localhost:27017/hotel_db

# Commands in MongoDB shell
use hotel_db
db.guests.find()
```

#### Neo4j (Port 7687)
```
http://localhost:7474
```
- Username: `neo4j`
- Password: `yourpassword`

## Configuration

### Default Credentials

#### Database Users (MySQL)
| User | Password | Privileges |
|------|----------|------------|
| `admin` | `admin123` | ALL PRIVILEGES |
| `app_user` | `app_password123` | SELECT, INSERT, UPDATE, DELETE |
| `cleaner_user` | `cleaner_password123` | SELECT (limited tables) |
| `staff_user` | `staff_password123` | SELECT (limited tables) |

#### Application Credentials
- Username: `staff`
- Password: `staff123`

#### MongoDB
- Username: `admin`
- Password: `admin123`

#### Neo4j
- Username: `neo4j`
- Password: `yourpassword`

### Environment Variables

Create `.env` file for custom settings:

```bash
# MySQL
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=hotel_db
MYSQL_ROOT_USERNAME=root
MYSQL_ROOT_PASSWORD=root

# MongoDB
MONGO_ROOT_USER=admin
MONGO_ROOT_PASSWORD=admin123
MONGODB_PORT=27017
MONGODB_DATABASE=hotel_db

# Neo4j
NEO4J_AUTH=neo4j/yourpassword
NEO4J_PORT=7687
```

## Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### Run Specific Test Class
```bash
mvn test -Dtest=GuestServiceTest
```

## Data Migration

### Migrate Data from MySQL to MongoDB and Neo4j
```bash
# Call the migration endpoint
curl -X POST http://localhost:8080/api/migrate

# Expected response
# "Migration completed successfully."
```

This will:
1. Read all data from MySQL
2. Transform and load into MongoDB
3. Transform and load into Neo4j

## AI Enrichment

### Enrich a Guest with AI
```bash
# Generate AI profile for guest with ID 1
curl -X POST http://localhost:8080/api/guests/1/enrich-ai

# Verify AI data was saved
curl http://localhost:8080/api/guests/1 | jq '.aiProfileSummary'
```

### Available Enrichment Endpoints
- `POST /api/guests/{id}/enrich-ai` - Enrich guest profile
- `POST /api/reservations/{id}/enrich-ai` - Enrich reservation notes
- `POST /api/rooms/{id}/enrich-ai` - Enrich room assessment

## Troubleshooting

### Docker Issues

**Containers not starting:**
```bash
# Check logs
docker-compose logs mysql
docker-compose logs mongodb
docker-compose logs neo4j

# Rebuild containers
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

**Port already in use:**
```bash
# Change ports in docker-compose.yml or use environment variables
MYSQL_PORT=3307 docker-compose up -d
```

### Java/Maven Issues

**Maven build fails:**
```bash
# Clean Maven cache
rm -rf ~/.m2/repository
mvn clean install
```

**Java version mismatch:**
```bash
# Check installed Java version
java -version

# Ensure Java 21 is used
export JAVA_HOME=/path/to/java21
```

### Database Connection Issues

**Cannot connect to MySQL:**
```bash
# Test connection
mysql -h localhost -u root -proot -e "SELECT 1;"

# Check if port is accessible
telnet localhost 3306
```

**MongoDB connection error:**
```bash
# Test with mongosh
mongosh mongodb://admin:admin123@localhost:27017/hotel_db

# Verify authentication
docker exec hotel_mongo_container mongosh --eval "db.adminCommand('ping')"
```

### Application Issues

**Port 8080 already in use:**
```bash
# Run on different port
java -Dserver.port=8081 -jar target/hotelbackend-0.0.1-SNAPSHOT.jar
```

**Ollama not connecting:**
- Ensure Ollama is running: `ollama serve`
- Download model: `ollama pull mistral:7b`
- Check connection: `curl http://localhost:11434/api/tags`

## Stopping the Application

### Stop Docker Containers
```bash
# Stop all containers
docker-compose down

# Stop and remove volumes (WARNING: deletes data)
docker-compose down -v

# Stop without removing
docker-compose stop
```

## Development Workflow

### 1. Make Code Changes
Edit Java files in `src/main/java/`

### 2. Compile
```bash
mvn compile
```

### 3. Run Tests
```bash
mvn test
```

### 4. Restart Application
```bash
# Stop with Ctrl+C, then
mvn spring-boot:run
```

## Project Structure

```
hotel-management-backend/
├── src/
│   ├── main/
│   │   ├── java/com/kea/hotel/hotelbackend/
│   │   │   ├── controller/          # REST endpoints
│   │   │   ├── service/             # Business logic
│   │   │   ├── model/               # JPA entities
│   │   │   ├── repository/          # Data access (MySQL)
│   │   │   ├── mongodb/             # MongoDB documents & repositories
│   │   │   ├── neo4j/               # Neo4j nodes & repositories
│   │   │   ├── migration/           # Data migration
│   │   │   └── security/            # Authentication & security
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/kea/hotel/hotelbackend/  # Unit & integration tests
├── sql/                           # Database scripts
│   ├── 01_database_create.sql
│   ├── 02_test_data.sql
│   ├── 03_logic.sql
│   ├── 04_users_privileges.sql
│   ├── 05_audit.sql
│   └── 06_ai_enrichment.sql
├── docs/                          # Documentation
│   ├── INSTALLATION.md            # This file
│   ├── CLOUD_DEPLOYMENT.md        # Cloud setup guide
│   └── exam_requirements.md       # Project requirements
├── docker-compose.yml             # Docker configuration
├── pom.xml                        # Maven configuration
└── .env.cloud.example             # Cloud environment template
```

## Next Steps

1. **Review the API** - Visit http://localhost:8080/swagger-ui.html
2. **Test the databases** - Connect to each database and explore the schema
3. **Test migrations** - Call the migration endpoint and verify data in MongoDB/Neo4j
4. **Read the documentation** - See `docs/` folder for detailed guides
5. **Deploy to cloud** - Follow `docs/CLOUD_DEPLOYMENT.md` for Azure deployment

## Support

For issues or questions:
- Check `docs/` folder for detailed documentation
- Review SQL scripts in `sql/` folder
- Check test files in `src/test/` for usage examples
- Review application logs: `docker-compose logs -f`
