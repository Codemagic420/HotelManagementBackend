# Cloud Deployment Guide

This guide explains how to deploy the Hotel Management Backend to cloud infrastructure using recommended managed database services: **Azure MySQL**, **MongoDB Atlas**, and **Neo4j AuraDB**.

## Architecture Overview

The application is designed to work with three managed cloud database services:
- **MySQL**: Azure MySQL Server (Recommended)
- **MongoDB**: MongoDB Atlas (Cloud-hosted MongoDB)
- **Neo4j**: Neo4j AuraDB (Cloud-hosted Graph Database)

## Prerequisites

### Azure Account Setup (for MySQL Server)
1. Create Azure account at https://azure.microsoft.com
2. Create resource group for hotel backend
3. Create Azure MySQL Single Server or Flexible Server
4. Configure firewall rules to allow application traffic (port 3306)
5. Create database and database user

### MongoDB Atlas Setup
1. Sign up at https://www.mongodb.com/cloud/atlas
2. Create a cluster (M10 or larger for production)
3. Create database user with appropriate permissions
4. Whitelist application IP address or enable VPC peering
5. Get connection string (mongodb+srv://)

### Neo4j AuraDB Setup
1. Sign up at https://neo4j.com/cloud/aura/ (formerly Neo4j Aura)
2. Create a Neo4j database instance
3. Note the connection URL and credentials
4. Create database user
5. Get neo4j+s connection string

## Configuration

### 1. Environment Variables

Set the following environment variables for cloud deployment:

#### MySQL (AWS RDS)
```bash
export MYSQL_HOST=hotel-db.c12345.eu-west-1.rds.amazonaws.com
export MYSQL_PORT=3306
export MYSQL_DATABASE=hotel_db
export MYSQL_USER=admin
export MYSQL_PASSWORD=your_secure_password_here
```

#### MongoDB Atlas
```bash
export MONGODB_ATLAS_URI=mongodb+srv://admin:password@hotel-cluster.abc123.mongodb.net/hotel_db?retryWrites=true&w=majority
```

#### Neo4j Aura
```bash
export NEO4J_AURA_URI=neo4j+s://abc12345.databases.neo4j.io
export NEO4J_USER=neo4j
export NEO4J_PASSWORD=your_neo4j_password_here
```

### 2. Application Configuration

The application uses Spring profiles to switch between environments:

**Development (Local Docker):**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Cloud Production:**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=cloud"
```

The `application-cloud.properties` file contains all cloud-specific configurations:
- Location: `src/main/resources/application-cloud.properties`
- Uses environment variables for sensitive data (passwords, endpoints)
- Disables Ollama AI service (can be replaced with cloud LLM service)
- Optimized connection pooling for cloud databases

### 3. Database Schema Migration

For cloud deployment:

1. **AWS RDS MySQL**: Run SQL scripts in order
   ```bash
   mysql -h <RDS-ENDPOINT> -u admin -p hotel_db < sql/01_database_create.sql
   mysql -h <RDS-ENDPOINT> -u admin -p hotel_db < sql/02_test_data.sql
   mysql -h <RDS-ENDPOINT> -u admin -p hotel_db < sql/03_logic.sql
   mysql -h <RDS-ENDPOINT> -u admin -p hotel_db < sql/04_users_privileges.sql
   mysql -h <RDS-ENDPOINT> -u admin -p hotel_db < sql/05_audit.sql
   mysql -h <RDS-ENDPOINT> -u admin -p hotel_db < sql/06_ai_enrichment.sql
   ```

2. **MongoDB Atlas**: Use migration application
   ```bash
   curl -X POST http://localhost:8080/api/migrate
   ```
   This will migrate data from MySQL to MongoDB Atlas via the application connection.

3. **Neo4j Aura**: Use migration application
   ```bash
   curl -X POST http://localhost:8080/api/migrate
   ```
   This will migrate data from MySQL to Neo4j Aura via the application connection.

## Deployment Options

### Option 1: Azure App Service (Recommended)

Deploy the Spring Boot application to Azure App Service with Azure MySQL Server, MongoDB Atlas, and Neo4j AuraDB.

#### Prerequisites
```bash
# Install Azure CLI
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash

# Login to Azure
az login
```

#### Step-by-Step Deployment

```bash
# 1. Create resource group
az group create --name hotel-rg --location northeurope

# 2. Create Azure MySQL Server
az mysql server create \
  --resource-group hotel-rg \
  --name hotel-mysql-server \
  --location northeurope \
  --admin-user dbadmin \
  --admin-password <secure_password> \
  --sku-name B_Gen5_1 \
  --storage-size 51200

# 3. Configure MySQL firewall to allow Azure services
az mysql server firewall-rule create \
  --resource-group hotel-rg \
  --server-name hotel-mysql-server \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0

# 4. Create database
az mysql db create \
  --resource-group hotel-rg \
  --server-name hotel-mysql-server \
  --name hotel_db

# 5. Create App Service Plan
az appservice plan create \
  --name hotel-plan \
  --resource-group hotel-rg \
  --sku B2 \
  --is-linux

# 6. Create Web App
az webapp create \
  --resource-group hotel-rg \
  --plan hotel-plan \
  --name hotel-backend \
  --runtime "JAVA|21-java21"

# 7. Configure environment variables for cloud databases
az webapp config appsettings set \
  --resource-group hotel-rg \
  --name hotel-backend \
  --settings \
    SPRING_PROFILES_ACTIVE=cloud \
    MYSQL_HOST=hotel-mysql-server.mysql.database.azure.com \
    MYSQL_PORT=3306 \
    MYSQL_DATABASE=hotel_db \
    MYSQL_USER=dbadmin@hotel-mysql-server \
    MYSQL_PASSWORD=<secure_password> \
    MONGODB_ATLAS_URI=mongodb+srv://<user>:<password>@<cluster>.mongodb.net/hotel_db \
    NEO4J_AURA_URI=neo4j+s://<instance-id>.databases.neo4j.io \
    NEO4J_USER=neo4j \
    NEO4J_PASSWORD=<password>

# 8. Build application
mvn clean package

# 9. Deploy WAR/JAR to App Service
az webapp deployment source config-zip \
  --resource-group hotel-rg \
  --name hotel-backend \
  --src target/hotelbackend-0.0.1-SNAPSHOT.jar

# 10. Check deployment status
az webapp deployment slot list --resource-group hotel-rg --name hotel-backend

# 11. View logs
az webapp log tail --resource-group hotel-rg --name hotel-backend
```

#### Post-Deployment Verification
```bash
# Get application URL
az webapp show --resource-group hotel-rg --name hotel-backend --query defaultHostName -o tsv

# Test API endpoints
curl https://<app-name>.azurewebsites.net/api/guests
curl https://<app-name>.azurewebsites.net/swagger-ui.html
```

## Security Considerations

### 1. Database Credentials
- **Never** commit credentials to version control
- Use environment variables or cloud-native secret management:
  - AWS Secrets Manager
  - Azure Key Vault
  - Google Secret Manager

### 2. Database User Privileges
Cloud deployment uses the same user privilege model as local:
- `admin`: Full administrative access (for migrations)
- `app_user`: Application runtime user (SELECT, INSERT, UPDATE, DELETE)
- `readonly_user`: Read-only access (SELECT)
- `staff_limited`: Restricted access (SELECT on specific tables)

### 3. Network Security
- **AWS RDS**: Configure security group to allow only application traffic
- **MongoDB Atlas**: Whitelist application IP address
- **Neo4j Aura**: Use VPC peering or IP whitelist

### 4. SSL/TLS Connections
All cloud databases use encrypted connections:
- MySQL: `useSSL=true` (configured in `application-cloud.properties`)
- MongoDB: `retryWrites=true&w=majority` (configured in connection string)
- Neo4j: `neo4j+s://` protocol (secure)

### 5. AI Service
- Ollama is **disabled** in production (`ai.ollama.url=DISABLED`)
- For production AI enrichment, use:
  - AWS SageMaker
  - Azure Cognitive Services
  - Google Vertex AI
  - External LLM API (Claude, OpenAI, etc.)

## Monitoring & Logging

### CloudWatch (AWS)
```bash
# View logs
aws logs tail /aws/elasticbeanstalk/hotel-management-backend --follow
```

### Application Logging
Set logging level in `application-cloud.properties`:
```properties
logging.level.com.kea.hotel.hotelbackend=INFO
logging.level.org.springframework.web=INFO
```

## Cost Estimation

### Azure MySQL Server
- **Basic Tier (B1s)**: ~$29/month
- **Standard Tier (D2s)**: ~$89/month  
- **Storage**: $0.10/GB (51GB included in price)
- **Backups**: Automatic daily retention (7-35 days)
- **Data Transfer**: $0.12/GB (first 100GB free per month)

### MongoDB Atlas
- **M10 Cluster**: $57/month (minimum, recommended)
- **M20 Cluster**: $171/month (for production)
- **Automatic Backups**: Included
- **Data Transfer**: ~$0.30/GB (free within same region)

### Neo4j AuraDB
- **Free Tier**: Limited resources, suitable for development
- **Professional**: $50-150/month (500-3000 cypher/sec)
- **Enterprise**: Custom pricing
- **Backups**: Automatic daily
- **High Availability**: Available in Professional+

### **Estimated Monthly Cost (Development)**
- Azure MySQL: $29
- MongoDB Atlas M10: $57
- Neo4j AuraDB Professional: $50
- **Total: ~$136/month**

## Rollback & Disaster Recovery

### Backup Strategy
1. **RDS MySQL**: AWS automated backups (retention: 7 days)
2. **MongoDB Atlas**: Automatic backups (retention: 7 days)
3. **Neo4j Aura**: Automatic backups (retention: 7 days)

### Manual Backups
```bash
# MySQL
mysqldump -h <RDS-ENDPOINT> -u admin -p hotel_db > backup_$(date +%Y%m%d).sql

# MongoDB
mongodump --uri "mongodb+srv://..." --out backup_$(date +%Y%m%d)

# Neo4j
neo4j-admin dump --to-path backup_$(date +%Y%m%d)
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| **Connection timeout to Azure MySQL** | Check firewall rules in Azure portal, verify MySQL server status, test with `mysql` CLI from App Service console |
| **"Access denied for user" Azure MySQL** | Ensure username format is `user@server-name`, check password, verify firewall allows App Service IP |
| **MongoDB Atlas connection fails** | Verify IP address is whitelisted in Atlas console, check credentials in connection string, test connection string locally |
| **Neo4j AuraDB auth error** | Verify neo4j+s:// protocol used, check credentials match instance, verify firewall allows application IP |
| **High latency from app to database** | Ensure App Service and databases in same region (North Europe recommended), check network security groups, review database tier |
| **Out of memory errors** | Increase App Service plan size (B2 → B3), increase Azure MySQL tier, increase MongoDB cluster size |
| **"Hostname verification failed" SSL error** | Add `allowPublicKeyRetrieval=false&useSSL=true` to Azure MySQL connection string |
| **MongoDB connection string timeout** | Verify connection string uses correct format `mongodb+srv://`, not `mongodb://`, check Atlas IP whitelist |

## Performance Optimization

### Connection Pooling
Current settings in `application-cloud.properties`:
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
```

Adjust based on application load:
- **High traffic**: Increase `maximum-pool-size` to 20-30
- **Read-heavy**: Ensure MongoDB/Neo4j read replicas enabled

### Database Indexing
Ensure indices are created on frequently queried columns:
- Guest: `email` (unique), `phone`
- Reservation: `status`, `checkInDate`, `checkOutDate`
- Room: `roomNumber`, `roomStatus`

### Query Optimization
Use application-level pagination and filtering:
```bash
# Paginated API requests
curl http://api.example.com/api/guests?page=0&size=50
curl http://api.example.com/api/reservations?status=CONFIRMED
```

## Support & Documentation

- **AWS RDS**: https://docs.aws.amazon.com/rds/
- **MongoDB Atlas**: https://docs.atlas.mongodb.com/
- **Neo4j Aura**: https://neo4j.com/cloud/aura/documentation/
- **Spring Boot Deployment**: https://spring.io/guides/gs/deploying-spring-boot-app-to-azure/
