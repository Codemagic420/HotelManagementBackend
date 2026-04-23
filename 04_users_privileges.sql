-- 04_users_privileges.sql
-- Run this on the hotel_management database after running 01_schema.sql

-- Admin user: full access
CREATE USER IF NOT EXISTS 'hotel_admin'@'localhost' IDENTIFIED BY 'Admin1234!';
GRANT ALL PRIVILEGES ON hotel_management.* TO 'hotel_admin'@'localhost';

-- Staff user: read + write but no DROP/ALTER
CREATE USER IF NOT EXISTS 'hotel_staff'@'localhost' IDENTIFIED BY 'Staff1234!';
GRANT SELECT, INSERT, UPDATE ON hotel_management.* TO 'hotel_staff'@'localhost';

-- Read-only reporting user
CREATE USER IF NOT EXISTS 'hotel_readonly'@'localhost' IDENTIFIED BY 'Read1234!';
GRANT SELECT ON hotel_management.* TO 'hotel_readonly'@'localhost';

FLUSH PRIVILEGES;
