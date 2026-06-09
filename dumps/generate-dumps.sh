#!/bin/bash
# Run this script after POST /api/migrate to generate all database dumps

MONGO_URI="mongodb://admin:admin123@localhost:27017/hotel_db?authSource=admin"
MYSQL_USER="root"
MYSQL_PASS="root"
MYSQL_DB="hotel_db"

mkdir -p dumps/mongodb dumps/neo4j dumps/mysql

echo "==> Dumping MongoDB..."
mongodump --uri="$MONGO_URI" --out=dumps/mongodb
echo "MongoDB dump complete: dumps/mongodb/"

echo "==> Dumping MySQL..."
mysqldump -u "$MYSQL_USER" -p"$MYSQL_PASS" "$MYSQL_DB" > dumps/mysql/hotel_db_backup.sql
echo "MySQL dump complete: dumps/mysql/hotel_db_backup.sql"

echo "==> Neo4j dump requires stopping Neo4j first."
echo "    Run manually: neo4j-admin database dump neo4j --to-path=dumps/neo4j"

echo "Done."
