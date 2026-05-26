-- ============================================
-- 01_database_create.sql
-- Hotel Management Database - Schema Creation
-- Creates database, tables with keys, indexes, constraints
-- FIXED: All data types are now consistent (INT <-> INT, BIGINT <-> BIGINT)
-- ============================================

-- Drop existing database if exists
DROP DATABASE IF EXISTS hotel_db;

-- Create database
CREATE DATABASE hotel_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_db;

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- TABLE: user_account
-- Stores system users with roles and authentication
-- ============================================
CREATE TABLE user_account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CLEANER', 'STAFF') NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: cleaner
-- Stores information about hotel cleaning staff
-- ============================================
CREATE TABLE cleaner (
    cleaner_id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(15),
    active TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (cleaner_id),
    INDEX idx_cleaner_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: extra_service
-- Stores optional services guests can purchase
-- ============================================
CREATE TABLE extra_service (
    extra_service_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit_price DECIMAL(8,2) NOT NULL,
    price_unit VARCHAR(20) NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (extra_service_id),
    INDEX idx_extra_service_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: inventory_item
-- Stores inventory items for billing and tracking
-- ============================================
CREATE TABLE inventory_item (
    inventory_item_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit_price DECIMAL(8,2) NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (inventory_item_id),
    INDEX idx_inventory_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: guest
-- Stores guest information and contact details
-- Uses BIGINT for consistency with user_account
-- ============================================
CREATE TABLE guest (
    guest_id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(15),
    credit_card_last4 VARCHAR(4),
    ai_profile_summary LONGTEXT,
    ai_fields_updated_at TIMESTAMP NULL,
    PRIMARY KEY (guest_id),
    INDEX idx_guest_email (email),
    INDEX idx_guest_name (last_name, first_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: room_type
-- Stores room type definitions (Single, Double, Suite, etc.)
-- ============================================
CREATE TABLE room_type (
    room_type_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    max_occupancy INT NOT NULL,
    PRIMARY KEY (room_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: season_rate
-- Stores pricing for each room type by season
-- Rates vary based on season (Spring, Summer, Autumn, Winter)
-- ============================================
CREATE TABLE season_rate (
    rate_id BIGINT NOT NULL AUTO_INCREMENT,
    room_type_id BIGINT NOT NULL,
    season VARCHAR(20) NOT NULL,
    price_per_night DECIMAL(8,2) NOT NULL,
    valid_from DATE NOT NULL,
    valid_to DATE NOT NULL,
    PRIMARY KEY (rate_id),
    FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id) ON DELETE CASCADE,
    INDEX idx_season_rate_room_type (room_type_id),
    INDEX idx_season_rate_dates (valid_from, valid_to),
    UNIQUE KEY uk_season_rate (room_type_id, season, valid_from, valid_to)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: room
-- Stores individual room information
-- Uses INT for room_id to keep it simple
-- ============================================
CREATE TABLE room (
    room_id BIGINT NOT NULL AUTO_INCREMENT,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type_id BIGINT NOT NULL,
    room_status VARCHAR(20) NOT NULL,
    clean_status VARCHAR(20) NOT NULL,
    occupied BIT(1) NOT NULL DEFAULT 0,
    type VARCHAR(255) NOT NULL,
    ai_assessment_summary LONGTEXT,
    ai_fields_updated_at TIMESTAMP NULL,
    PRIMARY KEY (room_id),
    FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id) ON DELETE CASCADE,
    INDEX idx_room_status (room_status),
    INDEX idx_room_clean_status (clean_status),
    INDEX idx_room_number (room_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: reservation
-- Stores guest reservations
-- Links guest (BIGINT), room (INT), and season rates (INT)
-- IMPORTANT: Keep foreign key types consistent!
-- ============================================
CREATE TABLE reservation (
    reservation_id BIGINT NOT NULL AUTO_INCREMENT,
    reference_no VARCHAR(20) NOT NULL UNIQUE,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    nights INT NOT NULL,
    num_guests INT NOT NULL,
    room_type_id BIGINT NOT NULL,
    assigned_room_id BIGINT,
    booked_rate_id BIGINT NOT NULL,
    booked_nightly_price DECIMAL(8,2) NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'CONFIRMED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    guest_id BIGINT,
    room_id BIGINT,
    ai_notes_summary LONGTEXT,
    ai_fields_updated_at TIMESTAMP NULL,
    PRIMARY KEY (reservation_id),
    FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id) ON DELETE RESTRICT,
    FOREIGN KEY (assigned_room_id) REFERENCES room(room_id) ON DELETE SET NULL,
    FOREIGN KEY (booked_rate_id) REFERENCES season_rate(rate_id) ON DELETE RESTRICT,
    FOREIGN KEY (guest_id) REFERENCES guest(guest_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE SET NULL,
    INDEX idx_reservation_dates (check_in_date, check_out_date),
    INDEX idx_reservation_status (status),
    INDEX idx_reservation_guest (guest_id),
    INDEX idx_reservation_room (room_id),
    INDEX idx_reservation_reference (reference_no),
    INDEX idx_reservation_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: reservation_guest
-- Junction table for multiple guests per reservation
-- Foreign key types must match: INT for reservation_id, BIGINT for guest_id
-- ============================================
CREATE TABLE reservation_guest (
    reservation_id BIGINT NOT NULL,
    guest_id BIGINT NOT NULL,
    is_primary TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (reservation_id, guest_id),
    FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id) ON DELETE CASCADE,
    FOREIGN KEY (guest_id) REFERENCES guest(guest_id) ON DELETE CASCADE,
    INDEX idx_res_guest_guest (guest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: bill
-- Stores bill information for reservations
-- ============================================
CREATE TABLE bill (
    bill_id BIGINT NOT NULL AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL UNIQUE,
    opened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP NULL,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    is_paid TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (bill_id),
    FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id) ON DELETE CASCADE,
    INDEX idx_bill_reservation (reservation_id),
    INDEX idx_bill_opened (opened_at),
    INDEX idx_bill_status (closed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: bill_item
-- Stores line items for bills
-- Includes room charges and extra services
-- ============================================
CREATE TABLE bill_item (
    bill_item_id BIGINT NOT NULL AUTO_INCREMENT,
    bill_id BIGINT NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(8,2) NOT NULL,
    line_total DECIMAL(10,2) NOT NULL,
    amount DECIMAL(10,2),
    posted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bill_item_id),
    FOREIGN KEY (bill_id) REFERENCES bill(bill_id) ON DELETE CASCADE,
    INDEX idx_bill_item_bill (bill_id),
    INDEX idx_bill_item_type (item_type),
    INDEX idx_bill_item_posted (posted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: room_cleaning_task
-- Stores cleaning tasks for rooms
-- ============================================
CREATE TABLE room_cleaning_task (
    task_id BIGINT NOT NULL AUTO_INCREMENT,
    room_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    task_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    note VARCHAR(200),
    PRIMARY KEY (task_id),
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE CASCADE,
    INDEX idx_cleaning_task_room (room_id),
    INDEX idx_cleaning_task_status (task_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLE: room_cleaning_assignment
-- Junction table for assigning cleaners to tasks
-- ============================================
CREATE TABLE room_cleaning_assignment (
    task_id BIGINT NOT NULL,
    cleaner_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (task_id, cleaner_id),
    FOREIGN KEY (task_id) REFERENCES room_cleaning_task(task_id) ON DELETE CASCADE,
    FOREIGN KEY (cleaner_id) REFERENCES cleaner(cleaner_id) ON DELETE CASCADE,
    INDEX idx_assignment_cleaner (cleaner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- End of Database Schema Creation
-- ============================================
