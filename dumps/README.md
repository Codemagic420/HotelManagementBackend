# Database Dumps

This folder contains dump files and export scripts for MongoDB and Neo4j.

## Generate Dumps (run after POST /api/migrate)

### MongoDB dump
```bash
mongodump --uri="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin" --out=dumps/mongodb
```

### Neo4j dump (stop Neo4j first)
```bash
neo4j-admin database dump neo4j --to-path=dumps/neo4j
```

### MySQL backup
```bash
mysqldump -u root -p hotel_db > dumps/mysql/hotel_db_backup.sql
```

## Restore

### MongoDB
```bash
mongorestore --uri="mongodb://admin:admin123@localhost:27017/?authSource=admin" dumps/mongodb/
```

### Neo4j (stop Neo4j first)
```bash
neo4j-admin database load neo4j --from-path=dumps/neo4j --overwrite-destination=true
```

### MySQL
```bash
mysql -u root -p hotel_db < dumps/mysql/hotel_db_backup.sql
```

## Notes

- Run `POST /api/migrate` before generating MongoDB and Neo4j dumps
- Dumps are not committed to git (see .gitignore) — generate locally or from deployed instance
- The `scripts/` folder contains equivalent seed scripts:
  - `scripts/load-mongodb-data.js` — MongoDB seed data
  - `scripts/load-neo4j-data.cypher` — Neo4j seed data
