# Database Dumps & Data Loading

This document explains how to export and load database dumps for Hotel Management System.

## MongoDB Dumps

### Export MongoDB Data
```bash
# Export to archive format (compressed)
docker exec hotel_mongo_container mongodump \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  --archive=mongodb_dump.archive

# Export to directory format
docker exec hotel_mongo_container mongodump \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  --out=/backup/mongodb_dump
```

### Load MongoDB Data
```bash
# From archive
docker exec hotel_mongo_container mongorestore \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  --archive=mongodb_dump.archive

# From directory
docker exec hotel_mongo_container mongorestore \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  /backup/mongodb_dump

# Using JavaScript file
docker exec hotel_mongo_container mongosh \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  scripts/load-mongodb-data.js
```

## Neo4j Dumps

### Export Neo4j Data
```bash
# Stop Neo4j first
docker stop hotel_neo4j_container

# Create dump
docker exec hotel_neo4j_container neo4j-admin database dump neo4j \
  --to-path=/backups

# Start Neo4j again
docker start hotel_neo4j_container
```

### Load Neo4j Data
```bash
# Using Cypher script
docker exec hotel_neo4j_container cypher-shell \
  -u neo4j \
  -p yourpassword \
  -f scripts/load-neo4j-data.cypher

# Or via browser UI
# 1. Navigate to http://localhost:7474
# 2. Copy-paste queries from load-neo4j-data.cypher
# 3. Execute each section
```

## MySQL Dumps

### Export MySQL Data
```bash
# Full database dump
docker exec hotel_db_container mysqldump \
  -u root \
  -proot \
  hotel_db > mysql_dump.sql

# Specific tables only
docker exec hotel_db_container mysqldump \
  -u root \
  -proot \
  hotel_db guest reservation bill > tables_dump.sql
```

### Load MySQL Data
```bash
# Full database
docker exec -i hotel_db_container mysql \
  -u root \
  -proot \
  hotel_db < mysql_dump.sql

# Using SQL scripts
docker exec -i hotel_db_container mysql \
  -u root \
  -proot \
  hotel_db < sql/01_database_create.sql

docker exec -i hotel_db_container mysql \
  -u root \
  -proot \
  hotel_db < sql/02_test_data.sql
```

## Data Statistics

After loading test data, verify:

```bash
# MySQL
docker exec hotel_db_container mysql -u root -proot -e \
  "SELECT 'guest' as table_name, COUNT(*) as count FROM hotel_db.guest UNION ALL \
   SELECT 'reservation', COUNT(*) FROM hotel_db.reservation UNION ALL \
   SELECT 'bill', COUNT(*) FROM hotel_db.bill UNION ALL \
   SELECT 'room_cleaning_task', COUNT(*) FROM hotel_db.room_cleaning_task;"

# MongoDB
docker exec hotel_mongo_container mongosh \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  --eval "db.guests.countDocuments()"

# Neo4j
docker exec hotel_neo4j_container cypher-shell \
  -u neo4j \
  -p yourpassword \
  "MATCH (n) RETURN labels(n)[0] as label, count(*) as count GROUP BY label"
```

## Backup Strategy

### Daily Backups
```bash
# Create backup directory
mkdir -p backups/$(date +%Y-%m-%d)

# Backup all databases
docker exec hotel_mongo_container mongodump \
  --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" \
  --out=backups/$(date +%Y-%m-%d)/mongodb

docker exec hotel_db_container mysqldump \
  -u root -proot hotel_db > backups/$(date +%Y-%m-%d)/mysql.sql
```

### Restore from Backup
```bash
# Restore MongoDB
mongorestore --uri="mongodb://admin:admin123@localhost:27017" \
  backups/2026-05-12/mongodb

# Restore MySQL
mysql -u root -proot < backups/2026-05-12/mysql.sql
```

## Important Notes

1. **Authentication**: All commands use default credentials. Change in production!
2. **Backup Size**: MongoDB dumps can be large with full data
3. **Compression**: Use `--archive` format for better compression
4. **Verification**: Always verify data after dump/restore cycle
5. **Encryption**: Encrypt backups in transit and at rest in production
