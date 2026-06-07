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
DROP USER IF EXISTS 'hotel_app'@'localhost';
DROP USER IF EXISTS 'hotel_app'@'%';
DROP USER IF EXISTS 'hotel_reader'@'localhost';
DROP USER IF EXISTS 'hotel_reader'@'%';

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
-- CREATE APP USER (hotel_app)
-- Minimum privileges for the Spring Boot application.
-- SELECT + INSERT + UPDATE + EXECUTE only — no DELETE,
-- no DDL. If the app needs to delete, it must do so
-- through the admin or staff user explicitly.
-- Used by: application.properties datasource
-- ============================================
CREATE USER 'hotel_app'@'localhost' IDENTIFIED BY 'apppassword123';
CREATE USER 'hotel_app'@'%' IDENTIFIED BY 'apppassword123';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'hotel_app'@'localhost';
GRANT SELECT, INSERT, UPDATE ON hotel_db.* TO 'hotel_app'@'%';
GRANT EXECUTE ON hotel_db.* TO 'hotel_app'@'localhost';
GRANT EXECUTE ON hotel_db.* TO 'hotel_app'@'%';

-- ============================================
-- CREATE RESTRICTED READER (hotel_reader)
-- SELECT-only on all tables EXCEPT sensitive columns
-- in the guest table (email, phone, credit_card_last4).
-- user_account table is excluded entirely (password hashes).
-- Used by: reporting tools, auditors, analytics
-- ============================================
CREATE USER 'hotel_reader'@'localhost' IDENTIFIED BY 'reader123';
CREATE USER 'hotel_reader'@'%' IDENTIFIED BY 'reader123';

-- Full SELECT on non-sensitive tables
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

-- user_account: NO grant — password hashes must never be readable by readers

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
