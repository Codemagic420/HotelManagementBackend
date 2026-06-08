-- Hotel Management System - Users and Privileges Setup
-- This script creates database users with specific privilege levels
-- NOTE: The canonical version used by Docker is sql/04_users_privileges.sql

-- ============================================
-- CREATE USERS
-- ============================================

-- Create ADMIN user with all privileges
CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin123';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin123';

-- Create STAFF user with SELECT, INSERT, UPDATE, EXECUTE
CREATE USER IF NOT EXISTS 'staff'@'localhost' IDENTIFIED BY 'staff123';
CREATE USER IF NOT EXISTS 'staff'@'%' IDENTIFIED BY 'staff123';

-- Create USER (read-only)
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'user123';
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'user123';

-- Create APP user (minimum privileges for Spring Boot application)
CREATE USER IF NOT EXISTS 'hotel_app'@'localhost' IDENTIFIED BY 'apppassword123';
CREATE USER IF NOT EXISTS 'hotel_app'@'%' IDENTIFIED BY 'apppassword123';

-- Create RESTRICTED READER (column-level restrictions on sensitive data)
CREATE USER IF NOT EXISTS 'hotel_reader'@'localhost' IDENTIFIED BY 'reader123';
CREATE USER IF NOT EXISTS 'hotel_reader'@'%' IDENTIFIED BY 'reader123';

-- ============================================
-- GRANT PRIVILEGES TO ADMIN (ALL PRIVILEGES)
-- ============================================
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'localhost' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'%' WITH GRANT OPTION;

-- ============================================
-- GRANT PRIVILEGES TO STAFF (SELECT, INSERT, UPDATE, EXECUTE)
-- ============================================
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'staff'@'localhost';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'staff'@'%';
GRANT EXECUTE ON hotel_db.* TO 'staff'@'localhost';
GRANT EXECUTE ON hotel_db.* TO 'staff'@'%';

-- ============================================
-- GRANT PRIVILEGES TO USER (SELECT ONLY)
-- ============================================
GRANT SELECT ON hotel_db.* TO 'user'@'localhost';
GRANT SELECT ON hotel_db.* TO 'user'@'%';

-- ============================================
-- GRANT PRIVILEGES TO APP USER (minimum needed)
-- ============================================
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'hotel_app'@'localhost';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'hotel_app'@'%';
GRANT EXECUTE ON hotel_db.* TO 'hotel_app'@'localhost';
GRANT EXECUTE ON hotel_db.* TO 'hotel_app'@'%';

-- ============================================
-- GRANT PRIVILEGES TO RESTRICTED READER
-- SELECT on all tables except user_account
-- guest table: column-level — hides email, phone, credit_card_last4
-- ============================================
GRANT SELECT ON hotel_db.bill                      TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.bill                      TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.bill_item                 TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.bill_item                 TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.cleaner                   TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.cleaner                   TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.extra_service             TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.extra_service             TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.inventory_item            TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.inventory_item            TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.reservation               TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.reservation               TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.reservation_guest         TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.reservation_guest         TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.room                      TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.room                      TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.room_cleaning_assignment  TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.room_cleaning_assignment  TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.room_cleaning_task        TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.room_cleaning_task        TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.room_type                 TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.room_type                 TO 'hotel_reader'@'%';
GRANT SELECT ON hotel_db.season_rate               TO 'hotel_reader'@'localhost';
GRANT SELECT ON hotel_db.season_rate               TO 'hotel_reader'@'%';

-- guest: column-level grant — hides email, phone, credit_card_last4
GRANT SELECT (guest_id, first_name, last_name)     ON hotel_db.guest TO 'hotel_reader'@'localhost';
GRANT SELECT (guest_id, first_name, last_name)     ON hotel_db.guest TO 'hotel_reader'@'%';

-- user_account: NO grant — password hashes must never be readable

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
-- SHOW GRANTS FOR 'hotel_app'@'localhost';
-- SHOW GRANTS FOR 'hotel_reader'@'localhost';

-- ============================================
-- Summary of User Privileges
-- ============================================
-- admin@%         : ALL PRIVILEGES on hotel_db (WITH GRANT OPTION)
-- staff@%         : SELECT, INSERT, UPDATE, EXECUTE on hotel_db
-- user@%          : SELECT only on hotel_db (all tables)
-- hotel_app@%     : SELECT, INSERT, UPDATE, EXECUTE — no DELETE, no DDL
-- hotel_reader@%  : SELECT on all tables; guest restricted to
--                   (guest_id, first_name, last_name) only —
--                   email/phone/credit_card_last4 hidden;
--                   user_account excluded entirely
-- ============================================
