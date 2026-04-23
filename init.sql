-- Hotel Management Database Initialization Script
-- Drop existing tables in reverse dependency order
DROP TABLE IF EXISTS room_cleaning_assignment;
DROP TABLE IF EXISTS room_cleaning_task;
DROP TABLE IF EXISTS bill_item;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS reservation_guest;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS season_rate;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS room_type;
DROP TABLE IF EXISTS guest;
DROP TABLE IF EXISTS extra_service;
DROP TABLE IF EXISTS inventory_item;
DROP TABLE IF EXISTS cleaner;
DROP TABLE IF EXISTS user_account;

-- Create user_account table
CREATE TABLE user_account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CLEANER', 'STAFF'),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Create cleaner table
CREATE TABLE cleaner (
    cleaner_id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(15),
    active TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (cleaner_id)
) ENGINE=InnoDB;

-- Create extra_service table
CREATE TABLE extra_service (
    extra_service_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit_price DECIMAL(8,2) NOT NULL,
    price_unit VARCHAR(20) NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (extra_service_id)
) ENGINE=InnoDB;

-- Create inventory_item table
CREATE TABLE inventory_item (
    inventory_item_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit_price DECIMAL(8,2) NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (inventory_item_id)
) ENGINE=InnoDB;

-- Create guest table
CREATE TABLE guest (
    guest_id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(15),
    credit_card_last4 VARCHAR(4),
    PRIMARY KEY (guest_id)
) ENGINE=InnoDB;

-- Create room_type table
CREATE TABLE room_type (
    room_type_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    max_occupancy INT NOT NULL,
    PRIMARY KEY (room_type_id)
) ENGINE=InnoDB;

-- Create season_rate table
CREATE TABLE season_rate (
    rate_id INT NOT NULL AUTO_INCREMENT,
    room_type_id INT NOT NULL,
    season VARCHAR(20) NOT NULL,
    price_per_night DECIMAL(8,2) NOT NULL,
    valid_from DATE NOT NULL,
    valid_to DATE NOT NULL,
    PRIMARY KEY (rate_id),
    CONSTRAINT fk_season_rate_room_type FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id)
) ENGINE=InnoDB;

-- Create room table
CREATE TABLE room (
    room_id INT NOT NULL AUTO_INCREMENT,
    room_number VARCHAR(255) UNIQUE,
    room_type_id INT NOT NULL,
    room_status VARCHAR(20) NOT NULL,
    clean_status VARCHAR(20) NOT NULL,
    occupied BIT(1) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (room_id),
    CONSTRAINT fk_room_room_type FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id)
) ENGINE=InnoDB;

-- Create reservation table
CREATE TABLE reservation (
    reservation_id INT NOT NULL AUTO_INCREMENT,
    reference_no VARCHAR(20) NOT NULL UNIQUE,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    nights INT NOT NULL,
    num_guests INT NOT NULL,
    room_type_id INT NOT NULL,
    assigned_room_id INT,
    booked_rate_id INT NOT NULL,
    booked_nightly_price DECIMAL(8,2) NOT NULL,
    status VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    guest_id BIGINT,
    room_id BIGINT,
    PRIMARY KEY (reservation_id),
    CONSTRAINT fk_reservation_room_type FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id),
    CONSTRAINT fk_reservation_assigned_room FOREIGN KEY (assigned_room_id) REFERENCES room(room_id),
    CONSTRAINT fk_reservation_booked_rate FOREIGN KEY (booked_rate_id) REFERENCES season_rate(rate_id)
) ENGINE=InnoDB;

-- Create reservation_guest table
CREATE TABLE reservation_guest (
    reservation_id INT NOT NULL,
    guest_id INT NOT NULL,
    is_primary TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (reservation_id, guest_id),
    CONSTRAINT fk_res_guest_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    CONSTRAINT fk_res_guest_guest FOREIGN KEY (guest_id) REFERENCES guest(guest_id)
) ENGINE=InnoDB;

-- Create bill table
CREATE TABLE bill (
    bill_id INT NOT NULL AUTO_INCREMENT,
    reservation_id INT NOT NULL UNIQUE,
    opened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (bill_id),
    CONSTRAINT fk_bill_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id)
) ENGINE=InnoDB;

-- Create bill_item table
CREATE TABLE bill_item (
    bill_item_id INT NOT NULL AUTO_INCREMENT,
    bill_id INT NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(8,2) NOT NULL,
    line_total DECIMAL(10,2) NOT NULL,
    posted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bill_item_id),
    CONSTRAINT fk_bill_item_bill FOREIGN KEY (bill_id) REFERENCES bill(bill_id)
) ENGINE=InnoDB;

-- Create room_cleaning_task table
CREATE TABLE room_cleaning_task (
    task_id INT NOT NULL AUTO_INCREMENT,
    room_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    task_status VARCHAR(20) NOT NULL,
    note VARCHAR(200),
    PRIMARY KEY (task_id),
    CONSTRAINT fk_clean_task_room FOREIGN KEY (room_id) REFERENCES room(room_id)
) ENGINE=InnoDB;

-- Create room_cleaning_assignment table
CREATE TABLE room_cleaning_assignment (
    task_id INT NOT NULL,
    cleaner_id INT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (task_id, cleaner_id),
    CONSTRAINT fk_assignment_task FOREIGN KEY (task_id) REFERENCES room_cleaning_task(task_id),
    CONSTRAINT fk_assignment_cleaner FOREIGN KEY (cleaner_id) REFERENCES cleaner(cleaner_id)
) ENGINE=InnoDB;

-- ============================================
-- INSERT DEFAULT DATA
-- ============================================

-- Insert default users
INSERT INTO user_account (username, password_hash, role)
VALUES
    ('admin', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2', 'ADMIN'),
    ('staff', '$2a$10$aWtT8jxXkJuYqWqWN3R3aeNGXWqXrIrMr.4WqW.Y2ZeZfvMJGJDEm', 'STAFF');

-- Insert sample cleaners
INSERT INTO cleaner (first_name, last_name, phone, active)
VALUES
    ('Maria', 'Garcia', '555-0101', 1),
    ('John', 'Smith', '555-0102', 1),
    ('Lisa', 'Chen', '555-0103', 1);

-- Insert sample extra services
INSERT INTO extra_service (name, unit_price, price_unit, active)
VALUES
    ('Room Service', 25.00, 'per meal', 1),
    ('Spa Treatment', 100.00, 'per session', 1),
    ('Airport Transfer', 50.00, 'per trip', 1);

-- Insert sample inventory items
INSERT INTO inventory_item (name, unit_price, active)
VALUES
    ('Bed Sheets', 15.00, 1),
    ('Towels', 8.00, 1),
    ('Shampoo', 3.50, 1);

-- Insert sample guests
INSERT INTO guest (first_name, last_name, email, phone, credit_card_last4)
VALUES
    ('John', 'Doe', 'john.doe@example.com', '555-1001', '1234'),
    ('Jane', 'Smith', 'jane.smith@example.com', '555-1002', '5678'),
    ('Bob', 'Johnson', 'bob.johnson@example.com', '555-1003', '9012');

-- Insert sample room types
INSERT INTO room_type (name, max_occupancy)
VALUES
    ('Single', 1),
    ('Double', 2),
    ('Suite', 4);

-- Insert sample season rates
INSERT INTO season_rate (room_type_id, season, price_per_night, valid_from, valid_to)
VALUES
    (1, 'Low', 80.00, '2024-01-01', '2024-05-31'),
    (1, 'High', 120.00, '2024-06-01', '2024-12-31'),
    (2, 'Low', 120.00, '2024-01-01', '2024-05-31'),
    (2, 'High', 180.00, '2024-06-01', '2024-12-31'),
    (3, 'Low', 200.00, '2024-01-01', '2024-05-31'),
    (3, 'High', 300.00, '2024-06-01', '2024-12-31');

-- Insert sample rooms
INSERT INTO room (room_number, room_type_id, room_status, clean_status, occupied, type)
VALUES
    ('101', 1, 'AVAILABLE', 'CLEAN', 0, 'Single'),
    ('102', 1, 'AVAILABLE', 'CLEAN', 0, 'Single'),
    ('201', 2, 'AVAILABLE', 'CLEAN', 0, 'Double'),
    ('202', 2, 'OCCUPIED', 'CLEAN', 1, 'Double'),
    ('301', 3, 'AVAILABLE', 'CLEAN', 0, 'Suite');
