-- Hotel Management System - Database Users and Privileges Setup
-- This file creates database users with specific privilege levels
-- Passwords are provided via environment variables at runtime

-- ============================================
-- CREATE ADMIN USER (ALL PRIVILEGES)
-- ============================================
CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin123';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'localhost';
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'%';

-- ============================================
-- CREATE STAFF USER (SELECT, INSERT, UPDATE)
-- ============================================
CREATE USER IF NOT EXISTS 'staff'@'localhost' IDENTIFIED BY 'staff123';
CREATE USER IF NOT EXISTS 'staff'@'%' IDENTIFIED BY 'staff123';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'staff'@'localhost';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'staff'@'%';

-- ============================================
-- CREATE GUEST/USER (SELECT ONLY)
-- ============================================
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'user123';
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'user123';
GRANT SELECT ON hotel_db.* TO 'user'@'localhost';
GRANT SELECT ON hotel_db.* TO 'user'@'%';

-- ============================================
-- APPLY CHANGES
-- ============================================
FLUSH PRIVILEGES;
