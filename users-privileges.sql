-- Hotel Management System - Users and Privileges Setup
-- This script creates database users with specific privilege levels

-- ============================================
-- CREATE USERS
-- ============================================

-- Create ADMIN user with all privileges
CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin_password_123';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin_password_123';

-- Create STAFF user with SELECT, INSERT, UPDATE
CREATE USER IF NOT EXISTS 'staff'@'localhost' IDENTIFIED BY 'staff_password_123';
CREATE USER IF NOT EXISTS 'staff'@'%' IDENTIFIED BY 'staff_password_123';

-- Create USER (guest/read-only user) with SELECT only
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'user_password_123';
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'user_password_123';

-- ============================================
-- GRANT PRIVILEGES TO ADMIN (ALL PRIVILEGES)
-- ============================================
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'localhost';
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'%';

-- ============================================
-- GRANT PRIVILEGES TO STAFF (SELECT, INSERT, UPDATE)
-- ============================================
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'staff'@'localhost';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'staff'@'%';

-- ============================================
-- GRANT PRIVILEGES TO USER (SELECT ONLY)
-- ============================================
GRANT SELECT ON hotel_db.* TO 'user'@'localhost';
GRANT SELECT ON hotel_db.* TO 'user'@'%';

-- ============================================
-- APPLY PRIVILEGE CHANGES
-- ============================================
FLUSH PRIVILEGES;

-- ============================================
-- VERIFICATION QUERIES (optional - uncomment to verify)
-- ============================================
-- SHOW GRANTS FOR 'admin'@'localhost';
-- SHOW GRANTS FOR 'staff'@'localhost';
-- SHOW GRANTS FOR 'user'@'localhost';
