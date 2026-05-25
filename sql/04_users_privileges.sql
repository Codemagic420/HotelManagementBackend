-- ============================================
-- 04_users_privileges.sql
-- Hotel Management Database - Users and Privileges
-- Creates 4 database users with specific privilege levels
-- ============================================

USE mysql;

-- ============================================
-- DROP EXISTING USERS (cleanup)
-- ============================================
DROP USER IF EXISTS 'admin'@'localhost';
DROP USER IF EXISTS 'admin'@'%';
DROP USER IF EXISTS 'app_user'@'localhost';
DROP USER IF EXISTS 'app_user'@'%';
DROP USER IF EXISTS 'cleaner_user'@'localhost';
DROP USER IF EXISTS 'cleaner_user'@'%';
DROP USER IF EXISTS 'staff_user'@'localhost';
DROP USER IF EXISTS 'staff_user'@'%';
DROP USER IF EXISTS 'staff'@'localhost';
DROP USER IF EXISTS 'staff'@'%';
DROP USER IF EXISTS 'user'@'localhost';
DROP USER IF EXISTS 'user'@'%';

-- ============================================
-- CREATE ADMIN USER
-- Privileges: ALL - Full database administrative access
-- Used by: Database administrators, developers
-- ============================================
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin123';
CREATE USER 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'localhost' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON hotel_db.* TO 'admin'@'%' WITH GRANT OPTION;

-- ============================================
-- CREATE APPLICATION USER
-- Privileges: SELECT, INSERT, UPDATE, DELETE, EXECUTE
-- Used by: Spring Boot backend application (CRUD operations)
-- Minimum privileges needed for app functionality
-- ============================================
CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'app_password123';
CREATE USER 'app_user'@'%' IDENTIFIED BY 'app_password123';
GRANT SELECT, INSERT, UPDATE, DELETE ON hotel_db.* TO 'app_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON hotel_db.* TO 'app_user'@'%';
GRANT EXECUTE ON hotel_db.* TO 'app_user'@'localhost';
GRANT EXECUTE ON hotel_db.* TO 'app_user'@'%';

-- ============================================
-- CREATE CLEANER USER
-- Privileges: RESTRICTED - SELECT only on cleaner-related tables
-- Used by: Cleaning staff, housekeeping management
-- Can see: rooms, room cleaning tasks, cleaner info
-- Cannot see: guests, reservations, bills, financial data
-- ============================================
CREATE USER 'cleaner_user'@'localhost' IDENTIFIED BY 'cleaner_password123';
CREATE USER 'cleaner_user'@'%' IDENTIFIED BY 'cleaner_password123';
GRANT SELECT ON hotel_db.rooms TO 'cleaner_user'@'localhost';
GRANT SELECT ON hotel_db.rooms TO 'cleaner_user'@'%';
GRANT SELECT ON hotel_db.room_cleaning_tasks TO 'cleaner_user'@'localhost';
GRANT SELECT ON hotel_db.room_cleaning_tasks TO 'cleaner_user'@'%';
GRANT SELECT ON hotel_db.room_cleaning_assignments TO 'cleaner_user'@'localhost';
GRANT SELECT ON hotel_db.room_cleaning_assignments TO 'cleaner_user'@'%';
GRANT SELECT ON hotel_db.cleaners TO 'cleaner_user'@'localhost';
GRANT SELECT ON hotel_db.cleaners TO 'cleaner_user'@'%';
GRANT SELECT ON hotel_db.room_types TO 'cleaner_user'@'localhost';
GRANT SELECT ON hotel_db.room_types TO 'cleaner_user'@'%';

-- ============================================
-- CREATE STAFF USER
-- Privileges: RESTRICTED - SELECT on guest-facing and operational tables
-- Used by: Hotel staff (front desk, reservations, customer service)
-- Can see: guests, reservations, rooms, extra services, bills
-- Cannot see: cleaning tasks, internal operations, staff schedules
-- ============================================
CREATE USER 'staff_user'@'localhost' IDENTIFIED BY 'staff_password123';
CREATE USER 'staff_user'@'%' IDENTIFIED BY 'staff_password123';
GRANT SELECT ON hotel_db.guests TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.guests TO 'staff_user'@'%';
GRANT SELECT ON hotel_db.reservations TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.reservations TO 'staff_user'@'%';
GRANT SELECT ON hotel_db.reservation_guests TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.reservation_guests TO 'staff_user'@'%';
GRANT SELECT ON hotel_db.rooms TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.rooms TO 'staff_user'@'%';
GRANT SELECT ON hotel_db.room_types TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.room_types TO 'staff_user'@'%';
GRANT SELECT ON hotel_db.extra_services TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.extra_services TO 'staff_user'@'%';
GRANT SELECT ON hotel_db.bills TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.bills TO 'staff_user'@'%';
GRANT SELECT ON hotel_db.bill_items TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.bill_items TO 'staff_user'@'%';
GRANT SELECT ON hotel_db.season_rates TO 'staff_user'@'localhost';
GRANT SELECT ON hotel_db.season_rates TO 'staff_user'@'%';

-- ============================================
-- APPLY CHANGES
-- ============================================
FLUSH PRIVILEGES;

-- ============================================
-- VERIFICATION QUERIES (optional - uncomment to verify)
-- ============================================
-- SHOW GRANTS FOR 'admin'@'localhost';
-- SHOW GRANTS FOR 'app_user'@'localhost';
-- SHOW GRANTS FOR 'cleaner_user'@'localhost';
-- SHOW GRANTS FOR 'staff_user'@'localhost';

-- ============================================
-- Summary of User Privileges
-- ============================================
-- ADMIN USER
-- admin@localhost         : ALL PRIVILEGES on hotel_db
-- admin@%                 : ALL PRIVILEGES on hotel_db
--
-- APPLICATION USER (for Spring Boot backend)
-- app_user@localhost      : SELECT, INSERT, UPDATE, DELETE, EXECUTE on hotel_db
-- app_user@%              : SELECT, INSERT, UPDATE, DELETE, EXECUTE on hotel_db
--
-- CLEANER USER (restricted read - cleaning operations only)
-- cleaner_user@localhost  : SELECT on rooms, room_cleaning_tasks, room_cleaning_assignments, cleaners, room_types
-- cleaner_user@%          : SELECT on rooms, room_cleaning_tasks, room_cleaning_assignments, cleaners, room_types
--
-- STAFF USER (restricted read - guest-facing operations)
-- staff_user@localhost    : SELECT on guests, reservations, reservation_guests, rooms, room_types, extra_services, bills, bill_items, season_rates
-- staff_user@%            : SELECT on guests, reservations, reservation_guests, rooms, room_types, extra_services, bills, bill_items, season_rates
-- ============================================
