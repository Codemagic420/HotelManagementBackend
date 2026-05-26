# Hotel Management Backend - Final Project

A comprehensive hotel management system built with polyglot persistence using MySQL, MongoDB, Neo4j, and Spring Boot with AI integration.

## Quick Start

### Docker Setup (Recommended)
```bash
docker-compose -f docker/docker-compose.yml up -d
```

Visit:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Neo4j Browser**: http://localhost:7474

See `docker/README.md` for complete Docker documentation.

### Manual Setup
1. Create MySQL database: `hotel_db`
2. Run SQL scripts from `sql/` directory in order
3. Configure `.env` file with database credentials
4. Build with Maven: `mvn clean package`
5. Run application: `java -jar target/hotelbackend-*.jar`

## Project Structure

```
HotelManagementBackend/
├── docker/                    # Docker configuration
│   ├── Dockerfile            # Multi-stage build for Spring Boot
│   ├── docker-compose.yml    # Complete stack orchestration
│   └── README.md             # Docker setup guide
├── sql/                       # Database initialization scripts
│   ├── 01_database_create.sql
│   ├── 02_test_data.sql
│   ├── 03_logic.sql
│   ├── 04_users_privileges.sql
│   ├── 05_audit.sql
│   └── 06_ai_enrichment.sql
├── src/                       # Spring Boot application
│   ├── main/java
│   ├── main/resources
│   └── test/java
├── dumps/                     # Database exports and samples
├── docs/                      # Project documentation
├── scripts/                   # Utility scripts
├── pom.xml                    # Maven configuration
└── README.md                  # This file
```

## Key Features

### Databases
- **MySQL**: Transactional core with ACID guarantees
- **MongoDB**: Document storage for fast dashboard reads
- **Neo4j**: Graph analysis for loyalty and relationships
- All three synchronized from single source of truth

### REST API
- 20+ endpoints covering all hotel operations
- JWT authentication with role-based access
- Pagination and sorting support
- Full Swagger documentation

### Data
- 150+ guests with AI enrichment
- 45 rooms across multiple types
- 120 reservations with full lifecycle tracking
- 120 bills with itemized charges
- Complete audit trail for compliance

### AI Integration
- Staff assistant answering questions in Danish
- RAG (Retrieval Augmented Generation) for accuracy
- Automatic response persistence to database
- Powered by Ollama + Mistral 7B

## Documentation

- **[Final Report](docs/Hotel_Management_System_Final_Report_CLEAN.md)** - Complete system documentation
- **[Docker Setup](docker/README.md)** - Docker deployment guide
- **[Database Schema](dumps/)** - Database exports and samples

## Technology Stack

- **Backend**: Java 21, Spring Boot 3.x
- **Databases**: MySQL 8.0, MongoDB 7, Neo4j 5
- **Build**: Maven 3.9
- **Containerization**: Docker & Docker Compose
- **AI**: Ollama, Mistral 7B, LangChain
- **API Documentation**: Swagger UI

## Running Tests

```bash
# All tests
mvn test

# Specific test
mvn test -Dtest=BookingFlowE2EIntegrationTest

# In Docker
docker-compose -f docker/docker-compose.yml exec app mvn test
```

## API Endpoints

Core endpoints (see Swagger for complete list):

### Guest Management
- `GET /api/guests` - List all guests (paginated)
- `POST /api/guests` - Create guest
- `PUT /api/guests/{id}` - Update guest
- `PUT /api/guests/{id}/ai-profile` - Add AI profile

### Reservations
- `GET /api/reservations` - List reservations (paginated)
- `POST /api/reservations` - Create reservation
- `PUT /api/reservations/{id}/ai-notes` - Add AI notes

### Rooms
- `GET /api/rooms` - List rooms (paginated)
- `PUT /api/rooms/{id}/ai-assessment` - Add AI assessment

### Billing
- `GET /api/bills` - List bills
- `POST /api/bills` - Generate bill

### Data Migration
- `POST /api/migrate` - Sync MySQL data to MongoDB/Neo4j

### Authentication
- `POST /api/login` - Get JWT token

## Default Credentials

| User | Password | Role |
|------|----------|------|
| admin | admin123 | ADMIN |
| staff | staff123 | STAFF |
| cleaner | cleaner123 | CLEANER |

## Configuration

### Environment Variables
See `.env.example` for complete configuration.

Key variables:
```bash
MYSQL_PORT=3307
MYSQL_DATABASE=hotel_db
MONGO_ROOT_USER=admin
MONGO_ROOT_PASSWORD=secure_mongo_password_dev
NEO4J_PASSWORD=yourpassword
```

## Database Initialization

On Docker startup, MySQL automatically runs:
1. Schema creation
2. Test data population
3. Business logic (stored procedures, triggers)
4. User roles and permissions
5. Audit logging setup
6. AI enrichment columns

## Contributing

This is a final project for KEA Copenhagen School of Design and Technology.

### Group Members
- Asger Valdemar Bergøe
- Magnus Sørensen
- Sophus Ingi Sophusson
- Joel Darko-Martinez

## Troubleshooting

### Docker won't start
```bash
# Full reset
docker-compose -f docker/docker-compose.yml down -v
docker-compose -f docker/docker-compose.yml up -d
```

### Can't connect to databases
- Check `.env` credentials match Docker environment
- Verify all services are healthy: `docker-compose -f docker/docker-compose.yml ps`
- Check logs: `docker-compose -f docker/docker-compose.yml logs mysql`

### Tests failing
- Ensure test database is initialized
- Check database users have correct permissions
- See TEST_SUITE_ANALYSIS.md for known issues

## References

- MySQL 8.0: https://dev.mysql.com/doc/
- MongoDB: https://docs.mongodb.com/manual/
- Neo4j: https://neo4j.com/docs/
- Spring Boot: https://spring.io/projects/spring-boot
- Ollama: https://github.com/ollama/ollama

---

**Document Version**: 1.0  
**Last Updated**: May 26, 2026  
**Status**: Complete and Ready for Submission
