-- ============================================
-- 04_users_privileges.sql
-- Hotel Management Database - Users and Privileges
-- Creates database users with specific privilege levels
-- ============================================

USE mysql;

-- ============================================
-- DROP EXISTING USERS (cleanup)
-- ============================================
DROP USER IF EXISTS 'admin'@'localhost';
DROP USER IF EXISTS 'admin'@'%';
DROP USER IF EXISTS 'staff'@'localhost';
DROP USER IF EXISTS 'staff'@'%';
DROP USER IF EXISTS 'user'@'localhost';
DROP USER IF EXISTS 'user'@'%';

-- ============================================
-- CREATE ADMIN USER
-- Privileges: ALL - Full access to hotel_db
-- Used by: Database administrators
-- ============================================
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin123';
CREATE USER 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'localhost' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'%' WITH GRANT OPTION;

-- ============================================
-- CREATE STAFF USER
-- Privileges: SELECT, INSERT, UPDATE - Read and modify data
-- Used by: Hotel staff (front desk, managers)
-- ============================================
CREATE USER 'staff'@'localhost' IDENTIFIED BY 'staff123';
CREATE USER 'staff'@'%' IDENTIFIED BY 'staff123';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'staff'@'localhost';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'staff'@'%';

-- ============================================
-- CREATE READ-ONLY USER
-- Privileges: SELECT ONLY - Read data only
-- Used by: Reporting, analytics, auditors
-- ============================================
CREATE USER 'user'@'localhost' IDENTIFIED BY 'user123';
CREATE USER 'user'@'%' IDENTIFIED BY 'user123';
GRANT SELECT ON hotel_db.* TO 'user'@'localhost';
GRANT SELECT ON hotel_db.* TO 'user'@'%';

-- ============================================
-- SPECIAL PRIVILEGES FOR PROCEDURES
-- Grant EXECUTE on stored procedures to staff and admin
-- ============================================
GRANT EXECUTE ON hotel_db.* TO 'staff'@'localhost';
GRANT EXECUTE ON hotel_db.* TO 'staff'@'%';

-- ============================================
-- APPLY CHANGES
-- ============================================
FLUSH PRIVILEGES;

-- ============================================
-- VERIFICATION QUERIES (optional - uncomment to verify)
-- ============================================
-- SHOW GRANTS FOR 'admin'@'localhost';
-- SHOW GRANTS FOR 'staff'@'localhost';
-- SHOW GRANTS FOR 'user'@'localhost';

-- ============================================
-- Summary of User Privileges
-- ============================================
-- admin@localhost    : ALL PRIVILEGES on hotel_db
-- admin@%            : ALL PRIVILEGES on hotel_db
-- staff@localhost    : SELECT, INSERT, UPDATE, EXECUTE on hotel_db
-- staff@%            : SELECT, INSERT, UPDATE, EXECUTE on hotel_db
-- user@localhost     : SELECT only on hotel_db
-- user@%             : SELECT only on hotel_db
-- ============================================
