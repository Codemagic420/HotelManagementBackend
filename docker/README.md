# Docker Setup - Hotel Management Backend

This directory contains all Docker-related files for running the Hotel Management Backend stack.

## Quick Start

### 1. Prerequisites
- Docker Desktop installed and running
- Docker Compose (included with Docker Desktop)
- Minimum 4GB RAM available for Docker

### 2. Start all services from project root
```bash
docker-compose -f docker/docker-compose.yml up -d
```

Or simply (if using symlink or copy from root):
```bash
docker-compose up -d
```

This will:
- Build the Spring Boot application
- Start MySQL 8.0 (with database schema, test data, users, and logic)
- Start MongoDB 7
- Start Neo4j 5
- Start the Hotel Management Backend application

### 3. Verify services are running
```bash
docker-compose -f docker/docker-compose.yml ps
```

Expected output:
```
NAME                  STATUS
hotelappcontainer     Up (healthy)
hoteldbcontainer      Up (healthy)
hotelmongocontainer   Up (healthy)
hotelneo4jcontainer   Up (healthy)
```

### 4. Access the application
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **MySQL**: localhost:3307 (user: `appuser`, password: `apppassword123`)
- **MongoDB**: localhost:27017 (user: `admin`, password: `secure_mongo_password_dev`)
- **Neo4j Browser**: http://localhost:7474 (user: `neo4j`, password: your configured password)

## Files

- **docker-compose.yml** - Orchestrates MySQL, MongoDB, Neo4j, and Spring Boot application
- **Dockerfile** - Multistage build for the Spring Boot application (Java 21)
- **README.md** - This file

## Common Commands

### View logs
```bash
# All services
docker-compose -f docker/docker-compose.yml logs -f

# Specific service
docker-compose -f docker/docker-compose.yml logs -f app
docker-compose -f docker/docker-compose.yml logs -f mysql
docker-compose -f docker/docker-compose.yml logs -f mongodb
docker-compose -f docker/docker-compose.yml logs -f neo4j
```

### Stop services
```bash
docker-compose -f docker/docker-compose.yml down
```

### Remove all data (full reset)
```bash
docker-compose -f docker/docker-compose.yml down -v
```

### Rebuild application (after code changes)
```bash
docker-compose -f docker/docker-compose.yml build app
docker-compose -f docker/docker-compose.yml up -d app
```

### Access container shell
```bash
docker-compose -f docker/docker-compose.yml exec app sh
docker-compose -f docker/docker-compose.yml exec mysql bash
docker-compose -f docker/docker-compose.yml exec mongodb bash
docker-compose -f docker/docker-compose.yml exec neo4j bash
```

## Configuration

### Environment Variables

Edit `.env` file in project root to customize:

```bash
# MySQL
MYSQL_PORT=3307
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=hotel_db
MYSQL_USER=appuser
MYSQL_PASSWORD=apppassword123

# MongoDB
MONGO_ROOT_USER=admin
MONGO_ROOT_PASSWORD=secure_mongo_password_dev
MONGODB_DATABASE=hotel_db
MONGODB_PORT=27017

# Neo4j
NEO4J_USER=neo4j
NEO4J_PASSWORD=yourpassword
NEO4J_PORT=7687

# Application
SPRING_PROFILES_ACTIVE=default
LOGGING_LEVEL_COM_KEA_HOTEL=INFO
```

## Database Initialization

On first run, MySQL automatically executes SQL scripts from the `sql/` directory:

1. **01_database_create.sql** - Schema creation
2. **02_test_data.sql** - Sample data (100+ records per table)
3. **03_logic.sql** - Stored functions, procedures, triggers, views, indexes
4. **04_users_privileges.sql** - Database users and permissions
5. **05_audit.sql** - Audit logging setup
6. **06_ai_enrichment.sql** - AI data enrichment columns

## Troubleshooting

### Services won't start
```bash
# Check Docker resources
docker info

# Rebuild everything from scratch
docker-compose -f docker/docker-compose.yml down -v
docker-compose -f docker/docker-compose.yml build --no-cache
docker-compose -f docker/docker-compose.yml up -d
```

### Can't connect to databases from app
- Verify all services are healthy: `docker-compose -f docker/docker-compose.yml ps`
- Check app logs: `docker-compose -f docker/docker-compose.yml logs app`
- Ensure `.env` file has correct credentials

### Port conflicts
If ports 3307, 8080, 27017, or 7687 are in use:
- Stop conflicting services: `lsof -i :PORT_NUMBER`
- Edit `.env` to use different ports

### Database not initialized
- Check MySQL logs: `docker-compose -f docker/docker-compose.yml logs mysql`
- Verify SQL scripts exist in `./sql/` directory
- Remove volume and restart: `docker-compose -f docker/docker-compose.yml down -v && docker-compose -f docker/docker-compose.yml up -d`

## Performance Tips

- Allocate at least 4GB RAM to Docker
- Use named volumes for persistent data
- Monitor resource usage: `docker stats`
- Enable compression for database dumps if backing up

## Production Deployment

For cloud deployment (Azure, AWS, GCP):
1. Update `.env.cloud` with cloud credentials
2. Use `application-cloud.properties` profile
3. Push Docker image to container registry
4. Deploy with cloud-managed databases

See `docs/CLOUD_DEPLOYMENT.md` for detailed instructions.
