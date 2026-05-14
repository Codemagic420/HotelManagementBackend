# SQL Scripts - Hotel Management Database

This directory contains all SQL scripts needed to set up the hotel management database system.

## 📋 Files Overview

### 1. **01_database_create.sql** (Required - Run First)
**Purpose**: Database and schema creation

**Creates**:
- Database: `hotel_db`
- 13 tables with proper structure
- Primary keys, foreign keys, and indexes
- Constraints and referential integrity checks

**Tables Created**:
```
user_account, cleaner, extra_service, inventory_item, guest,
room_type, season_rate, room, reservation, reservation_guest,
bill, bill_item, room_cleaning_task, room_cleaning_assignment
```

**Execution**:
```bash
mysql -u root -p < 01_database_create.sql
```

---

### 2. **02_test_data.sql** (Optional - For Testing)
**Purpose**: Load test/seed data

**Inserts**:
- 10 cleaners
- 12 extra services
- 15 inventory items
- 65 guests
- 6 room types
- 12 season rates
- 30 rooms
- 55 reservations
- 55 bills and bill items
- 30 cleaning tasks and assignments

**Execution**:
```bash
mysql -u root -p hotel_db < 02_test_data.sql
```

---

### 3. **03_logic.sql** (Required - Business Logic)
**Purpose**: Implement business logic with stored procedures, functions, views, and triggers

**Creates**:

#### Stored Functions:
- `fn_GetRoomRate(room_type_id, season)` - Get price for room type + season

#### Stored Procedures:
- `sp_CalculateFinalBill(reservation_id)` - Calculate total bill amount

#### Triggers:
- `tr_AfterCheckout` - Set room to dirty when guest checks out
- `tr_RoomStatusUpdate` - Update room status on reservation changes

#### Views:
- `vw_HousekeepingList` - Rooms needing cleaning
- `vw_ReservationDetails` - Complete reservation information
- `vw_BillDetails` - Bill information with line items

**Execution**:
```bash
mysql -u root -p hotel_db < 03_logic.sql
```

---

### 4. **04_users_privileges.sql** (Required - Security)
**Purpose**: Create database users with role-based privileges

**Users Created**:

| User | Host | Password | Privileges |
|------|------|----------|-----------|
| admin | localhost, % | admin123 | ALL PRIVILEGES |
| staff | localhost, % | staff123 | SELECT, INSERT, UPDATE, EXECUTE |
| user | localhost, % | user123 | SELECT only |

**Execution**:
```bash
mysql -u root -p < 04_users_privileges.sql
```

**Important**: Change passwords in production!

---

### 5. **05_audit.sql** (Required - Auditing)
**Purpose**: Implement comprehensive audit logging

**Creates**:

#### Audit Table:
- `audit_log` - Stores all changes (INSERT, UPDATE, DELETE)

#### Audit Triggers:
- Reservation table (insert, update, delete)
- Bill table (insert, update, delete)
- Bill_item table (insert, delete)
- Guest table (insert, update)
- Room table (update)

#### Audit Views:
- `vw_recent_audit_logs` - Recent changes
- `vw_audit_by_table` - Changes grouped by table
- `vw_audit_by_user` - Changes grouped by user

#### Audit Procedures:
- `sp_cleanup_old_audit_logs()` - Delete old audit entries
- `sp_get_audit_history()` - Get history for specific record

**Execution**:
```bash
mysql -u root -p < 05_audit.sql
```

---

## 🚀 Quick Setup

### Option 1: Run All Scripts (Recommended)
```bash
#!/bin/bash
# Navigate to sql directory
cd sql/

# Run in order
mysql -u root -p < 01_database_create.sql
mysql -u root -p hotel_db < 02_test_data.sql
mysql -u root -p hotel_db < 03_logic.sql
mysql -u root -p < 04_users_privileges.sql
mysql -u root -p hotel_db < 05_audit.sql

echo "✅ Database setup complete!"
```

### Option 2: With Docker
```bash
# Start MySQL container
docker-compose up -d mysql

# Wait for MySQL to be ready
sleep 10

# Run scripts
docker exec hotel_mysql mysql -u root -proot < sql/01_database_create.sql
docker exec hotel_mysql mysql -u root -proot hotel_db < sql/02_test_data.sql
docker exec hotel_mysql mysql -u root -proot hotel_db < sql/03_logic.sql
docker exec hotel_mysql mysql -u root -proot < sql/04_users_privileges.sql
docker exec hotel_mysql mysql -u root -proot hotel_db < sql/05_audit.sql
```

### Option 3: MySQL Workbench
1. Open all scripts in MySQL Workbench
2. Execute each file in order
3. Verify in Database Navigator

---

## ✅ Verification

### Check Database Created
```sql
SHOW DATABASES;
USE hotel_db;
SHOW TABLES;
```

### Check Users Created
```sql
SELECT user, host FROM mysql.user WHERE user IN ('admin', 'staff', 'user');
SHOW GRANTS FOR 'admin'@'localhost';
```

### Check Stored Procedures
```sql
SHOW PROCEDURES;
CALL sp_CalculateFinalBill(1);
```

### Check Triggers
```sql
SHOW TRIGGERS;
```

### Check Audit Logs
```sql
SELECT * FROM audit_log ORDER BY changed_at DESC LIMIT 10;
```

### Check Views
```sql
SELECT * FROM vw_HousekeepingList;
SELECT * FROM vw_ReservationDetails LIMIT 1;
```

---

## 🔐 Security Notes

### Default Passwords (Change in Production!)
- admin123
- staff123
- user123

### Recommended for Production
1. Change all default passwords
2. Use environment variables for credentials
3. Limit user hosts to specific IPs
4. Enable MySQL binary logging for backups
5. Implement regular backups
6. Review audit logs regularly

---

## 📊 Database Statistics

| Metric | Count |
|--------|-------|
| Tables | 14 |
| Stored Procedures | 3 |
| Stored Functions | 1 |
| Views | 6 |
| Triggers | 10 |
| Indexes | 25+ |
| Audit Entries (after test data) | 100+ |

---

## 🐛 Troubleshooting

### Error: "Access denied for user 'root'"
- Verify MySQL is running
- Check password for root user
- Use: `mysql -u root -p` and enter password

### Error: "Database hotel_db already exists"
- Comment out `DROP DATABASE IF EXISTS` in 01_database_create.sql
- Or drop manually: `DROP DATABASE hotel_db;`

### Error: "Trigger already exists"
- Drop trigger: `DROP TRIGGER tr_audit_reservation_insert;`
- Or comment out trigger creation in 05_audit.sql

### Error: "User already exists"
- Drop users first: `DROP USER 'admin'@'localhost';`
- Or use `CREATE USER IF NOT EXISTS`

---

## 📝 Notes

- All scripts use `IF NOT EXISTS` clauses where possible
- UTF8mb4 charset for proper character support
- Foreign key constraints enabled
- Audit triggers capture all changes automatically
- Stored procedures include error handling

---

**Created by**: Asger, Magnus, Sophus, Joel  
**Last Updated**: 2026-05-11  
**Version**: 1.0
