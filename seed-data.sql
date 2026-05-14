-- Hotel Management System - Test Data Seed
-- Run this script in MySQL Workbench to populate test data

USE hotel_db;

-- ============================================
-- DISABLE FOREIGN KEY CHECKS (temporarily)
-- ============================================
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- CLEAR EXISTING DATA (optional - uncomment if needed)
-- ============================================
-- DELETE FROM room_cleaning_assignment;
-- DELETE FROM room_cleaning_task;
-- DELETE FROM bill_item;
-- DELETE FROM bill;
-- DELETE FROM reservation_guest;
-- DELETE FROM reservation;
-- DELETE FROM season_rate;
-- DELETE FROM room;
-- DELETE FROM room_type;
-- DELETE FROM guest;
-- DELETE FROM extra_service;
-- DELETE FROM inventory_item;
-- DELETE FROM cleaner;
-- DELETE FROM user_account;

-- ============================================
-- INSERT USERS
-- ============================================
INSERT INTO user_account (username, password_hash, role) VALUES
('cleaner1', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2', 'CLEANER'),
('cleaner2', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2', 'CLEANER'),
('cleaner3', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2', 'CLEANER'),
('cleaner4', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2', 'CLEANER'),
('cleaner5', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2', 'CLEANER')
ON DUPLICATE KEY UPDATE username=VALUES(username);

-- ============================================
-- INSERT CLEANERS
-- ============================================
INSERT INTO cleaner (first_name, last_name, phone, active) VALUES
('Maria', 'Garcia', '555-0101', 1),
('John', 'Smith', '555-0102', 1),
('Lisa', 'Chen', '555-0103', 1),
('Ahmed', 'Hassan', '555-0104', 1),
('Elena', 'Rodriguez', '555-0105', 1),
('Michael', 'Johnson', '555-0106', 1),
('Sofia', 'Petrov', '555-0107', 1),
('Carlos', 'Martinez', '555-0108', 1),
('Anna', 'Kowalski', '555-0109', 1),
('David', 'Kim', '555-0110', 1);

-- ============================================
-- INSERT EXTRA SERVICES
-- ============================================
INSERT INTO extra_service (name, unit_price, price_unit, active) VALUES
('Room Service Breakfast', 25.00, 'per meal', 1),
('Room Service Lunch', 35.00, 'per meal', 1),
('Room Service Dinner', 45.00, 'per meal', 1),
('Spa Treatment - Massage', 100.00, 'per session', 1),
('Spa Treatment - Facial', 80.00, 'per session', 1),
('Airport Transfer', 50.00, 'per trip', 1),
('City Tour', 120.00, 'per person', 1),
('Wine Tasting', 75.00, 'per person', 1),
('Gym Access', 15.00, 'per day', 1),
('Late Checkout', 30.00, 'per hour', 1),
('Early Breakfast', 20.00, 'per person', 1),
('Mini Bar Restocking', 10.00, 'per item', 1);

-- ============================================
-- INSERT INVENTORY ITEMS
-- ============================================
INSERT INTO inventory_item (name, unit_price, active) VALUES
('Bed Sheets Premium', 25.00, 1),
('Pillowcase Set', 15.00, 1),
('Towels Bath', 12.00, 1),
('Towels Hand', 8.00, 1),
('Towels Face Cloth', 5.00, 1),
('Shampoo Bottle', 3.50, 1),
('Conditioner Bottle', 3.50, 1),
('Soap Bar', 2.00, 1),
('Lotion Bottle', 4.00, 1),
('Toilet Paper Roll', 1.50, 1),
('Cleaning Supplies Spray', 5.00, 1),
('Vacuum Bag', 8.00, 1),
('Light Bulb LED', 10.00, 1),
('WiFi Router', 150.00, 1),
('Mattress Protector', 40.00, 1);

-- ============================================
-- INSERT GUESTS (100+)
-- ============================================
INSERT INTO guest (first_name, last_name, email, phone, credit_card_last4) VALUES
('John', 'Doe', 'john.doe@example.com', '555-1001', '1234'),
('Jane', 'Smith', 'jane.smith@example.com', '555-1002', '5678'),
('Bob', 'Johnson', 'bob.johnson@example.com', '555-1003', '9012'),
('Alice', 'Williams', 'alice.williams@example.com', '555-1004', '3456'),
('Charlie', 'Brown', 'charlie.brown@example.com', '555-1005', '7890'),
('Diana', 'Davis', 'diana.davis@example.com', '555-1006', '2345'),
('Edward', 'Miller', 'edward.miller@example.com', '555-1007', '6789'),
('Fiona', 'Wilson', 'fiona.wilson@example.com', '555-1008', '0123'),
('George', 'Moore', 'george.moore@example.com', '555-1009', '4567'),
('Hannah', 'Taylor', 'hannah.taylor@example.com', '555-1010', '8901'),
('Ian', 'Anderson', 'ian.anderson@example.com', '555-1011', '2345'),
('Jessica', 'Thomas', 'jessica.thomas@example.com', '555-1012', '6789'),
('Kevin', 'Jackson', 'kevin.jackson@example.com', '555-1013', '0123'),
('Laura', 'White', 'laura.white@example.com', '555-1014', '4567'),
('Michael', 'Harris', 'michael.harris@example.com', '555-1015', '8901'),
('Nicole', 'Martin', 'nicole.martin@example.com', '555-1016', '2345'),
('Oscar', 'Thompson', 'oscar.thompson@example.com', '555-1017', '6789'),
('Patricia', 'Garcia', 'patricia.garcia@example.com', '555-1018', '0123'),
('Quincy', 'Martinez', 'quincy.martinez@example.com', '555-1019', '4567'),
('Rachel', 'Robinson', 'rachel.robinson@example.com', '555-1020', '8901'),
('Samuel', 'Clark', 'samuel.clark@example.com', '555-1021', '2345'),
('Tanya', 'Rodriguez', 'tanya.rodriguez@example.com', '555-1022', '6789'),
('Ulysses', 'Lewis', 'ulysses.lewis@example.com', '555-1023', '0123'),
('Vanessa', 'Lee', 'vanessa.lee@example.com', '555-1024', '4567'),
('William', 'Walker', 'william.walker@example.com', '555-1025', '8901'),
('Xena', 'Hall', 'xena.hall@example.com', '555-1026', '2345'),
('Yuri', 'Allen', 'yuri.allen@example.com', '555-1027', '6789'),
('Zara', 'Young', 'zara.young@example.com', '555-1028', '0123'),
('Aaron', 'King', 'aaron.king@example.com', '555-1029', '4567'),
('Bella', 'Wright', 'bella.wright@example.com', '555-1030', '8901'),
('Calvin', 'Lopez', 'calvin.lopez@example.com', '555-1031', '2345'),
('Daisy', 'Hill', 'daisy.hill@example.com', '555-1032', '6789'),
('Ethan', 'Scott', 'ethan.scott@example.com', '555-1033', '0123'),
('Faye', 'Green', 'faye.green@example.com', '555-1034', '4567'),
('Gregory', 'Adams', 'gregory.adams@example.com', '555-1035', '8901'),
('Harper', 'Nelson', 'harper.nelson@example.com', '555-1036', '2345'),
('Isaac', 'Carter', 'isaac.carter@example.com', '555-1037', '6789'),
('Jasmine', 'Mitchell', 'jasmine.mitchell@example.com', '555-1038', '0123'),
('Kevin', 'Perez', 'kevin.perez@example.com', '555-1039', '4567'),
('Luna', 'Roberts', 'luna.roberts@example.com', '555-1040', '8901'),
('Marcus', 'Phillips', 'marcus.phillips@example.com', '555-1041', '2345'),
('Natalie', 'Campbell', 'natalie.campbell@example.com', '555-1042', '6789'),
('Oliver', 'Parker', 'oliver.parker@example.com', '555-1043', '0123'),
('Piper', 'Evans', 'piper.evans@example.com', '555-1044', '4567'),
('Quinton', 'Edwards', 'quinton.edwards@example.com', '555-1045', '8901'),
('Ruby', 'Collins', 'ruby.collins@example.com', '555-1046', '2345'),
('Sebastian', 'Reeves', 'sebastian.reeves@example.com', '555-1047', '6789'),
('Sophia', 'Morris', 'sophia.morris@example.com', '555-1048', '0123'),
('Tristan', 'Murphy', 'tristan.murphy@example.com', '555-1049', '4567'),
('Unity', 'Cook', 'unity.cook@example.com', '555-1050', '8901'),
('Victor', 'Morgan', 'victor.morgan@example.com', '555-1051', '2345'),
('Willa', 'Peterson', 'willa.peterson@example.com', '555-1052', '6789'),
('Xavier', 'Gray', 'xavier.gray@example.com', '555-1053', '0123'),
('Yolanda', 'Ramirez', 'yolanda.ramirez@example.com', '555-1054', '4567'),
('Zachary', 'James', 'zachary.james@example.com', '555-1055', '8901'),
('Amelia', 'Watson', 'amelia.watson@example.com', '555-1056', '2345'),
('Benjamin', 'Brooks', 'benjamin.brooks@example.com', '555-1057', '6789'),
('Charlotte', 'Chavez', 'charlotte.chavez@example.com', '555-1058', '0123'),
('Daniel', 'Wood', 'daniel.wood@example.com', '555-1059', '4567'),
('Emma', 'Mendoza', 'emma.mendoza@example.com', '555-1060', '8901'),
('Fredrick', 'Parks', 'fredrick.parks@example.com', '555-1061', '2345'),
('Grace', 'Bennett', 'grace.bennett@example.com', '555-1062', '6789'),
('Henry', 'Cruz', 'henry.cruz@example.com', '555-1063', '0123'),
('Iris', 'Porter', 'iris.porter@example.com', '555-1064', '4567'),
('Jacob', 'Howell', 'jacob.howell@example.com', '555-1065', '8901'),
('Katherine', 'Curry', 'katherine.curry@example.com', '555-1066', '2345'),
('Liam', 'Stokes', 'liam.stokes@example.com', '555-1067', '6789'),
('Mia', 'Ng', 'mia.ng@example.com', '555-1068', '0123'),
('Nathan', 'Tate', 'nathan.tate@example.com', '555-1069', '4567'),
('Olivia', 'Lamb', 'olivia.lamb@example.com', '555-1070', '8901');

-- ============================================
-- INSERT ROOM TYPES
-- ============================================
INSERT INTO room_type (name, max_occupancy) VALUES
('Single', 1),
('Double', 2),
('Twin', 2),
('Suite', 4),
('Deluxe Suite', 4),
('Penthouse', 6);

-- ============================================
-- INSERT SEASON RATES
-- ============================================
INSERT INTO season_rate (room_type_id, season, price_per_night, valid_from, valid_to) VALUES
(1, 'Low', 80.00, '2026-01-01', '2026-05-31'),
(1, 'High', 120.00, '2026-06-01', '2026-12-31'),
(2, 'Low', 120.00, '2026-01-01', '2026-05-31'),
(2, 'High', 180.00, '2026-06-01', '2026-12-31'),
(3, 'Low', 110.00, '2026-01-01', '2026-05-31'),
(3, 'High', 170.00, '2026-06-01', '2026-12-31'),
(4, 'Low', 200.00, '2026-01-01', '2026-05-31'),
(4, 'High', 300.00, '2026-06-01', '2026-12-31'),
(5, 'Low', 280.00, '2026-01-01', '2026-05-31'),
(5, 'High', 420.00, '2026-06-01', '2026-12-31'),
(6, 'Low', 500.00, '2026-01-01', '2026-05-31'),
(6, 'High', 750.00, '2026-06-01', '2026-12-31');

-- ============================================
-- INSERT ROOMS (50 rooms)
-- ============================================
INSERT INTO room (room_number, room_type_id, room_status, clean_status, occupied, type) VALUES
('101', 1, 'AVAILABLE', 'CLEAN', 0, 'Single'),
('102', 1, 'AVAILABLE', 'CLEAN', 0, 'Single'),
('103', 1, 'AVAILABLE', 'CLEAN', 0, 'Single'),
('104', 1, 'OCCUPIED', 'CLEAN', 1, 'Single'),
('105', 1, 'MAINTENANCE', 'DIRTY', 0, 'Single'),
('201', 2, 'AVAILABLE', 'CLEAN', 0, 'Double'),
('202', 2, 'AVAILABLE', 'CLEAN', 0, 'Double'),
('203', 2, 'OCCUPIED', 'CLEAN', 1, 'Double'),
('204', 2, 'AVAILABLE', 'CLEAN', 0, 'Double'),
('205', 2, 'AVAILABLE', 'CLEAN', 0, 'Double'),
('301', 3, 'AVAILABLE', 'CLEAN', 0, 'Twin'),
('302', 3, 'OCCUPIED', 'CLEAN', 1, 'Twin'),
('303', 3, 'AVAILABLE', 'CLEAN', 0, 'Twin'),
('304', 3, 'AVAILABLE', 'CLEAN', 0, 'Twin'),
('305', 3, 'AVAILABLE', 'CLEAN', 0, 'Twin'),
('401', 4, 'AVAILABLE', 'CLEAN', 0, 'Suite'),
('402', 4, 'OCCUPIED', 'CLEAN', 1, 'Suite'),
('403', 4, 'AVAILABLE', 'CLEAN', 0, 'Suite'),
('404', 4, 'AVAILABLE', 'CLEAN', 0, 'Suite'),
('405', 4, 'AVAILABLE', 'CLEAN', 0, 'Suite'),
('501', 5, 'AVAILABLE', 'CLEAN', 0, 'Deluxe Suite'),
('502', 5, 'OCCUPIED', 'CLEAN', 1, 'Deluxe Suite'),
('503', 5, 'AVAILABLE', 'CLEAN', 0, 'Deluxe Suite'),
('504', 5, 'AVAILABLE', 'CLEAN', 0, 'Deluxe Suite'),
('505', 5, 'AVAILABLE', 'CLEAN', 0, 'Deluxe Suite'),
('601', 6, 'AVAILABLE', 'CLEAN', 0, 'Penthouse'),
('602', 6, 'OCCUPIED', 'CLEAN', 1, 'Penthouse'),
('603', 6, 'AVAILABLE', 'CLEAN', 0, 'Penthouse'),
('604', 6, 'AVAILABLE', 'CLEAN', 0, 'Penthouse'),
('605', 6, 'AVAILABLE', 'CLEAN', 0, 'Penthouse');

-- ============================================
-- INSERT RESERVATIONS (50+)
-- ============================================
INSERT INTO reservation (reference_no, check_in_date, check_out_date, nights, num_guests, room_type_id, assigned_room_id, booked_rate_id, booked_nightly_price, status, created_at, guest_id, room_id) VALUES
('RES001', '2026-05-05', '2026-05-07', 2, 1, 1, 1, 1, 80.00, 'CONFIRMED', '2026-05-04 10:00:00', 1, 1),
('RES002', '2026-05-06', '2026-05-09', 3, 2, 2, 6, 3, 120.00, 'CONFIRMED', '2026-05-04 11:00:00', 2, 6),
('RES003', '2026-05-07', '2026-05-10', 3, 2, 3, 11, 5, 110.00, 'CONFIRMED', '2026-05-04 12:00:00', 3, 11),
('RES004', '2026-05-08', '2026-05-12', 4, 4, 4, 16, 7, 200.00, 'CONFIRMED', '2026-05-04 13:00:00', 4, 16),
('RES005', '2026-05-09', '2026-05-13', 4, 4, 5, 21, 9, 280.00, 'CONFIRMED', '2026-05-04 14:00:00', 5, 21),
('RES006', '2026-05-10', '2026-05-15', 5, 1, 1, 2, 1, 80.00, 'CONFIRMED', '2026-05-04 15:00:00', 6, 2),
('RES007', '2026-05-11', '2026-05-14', 3, 2, 2, 7, 3, 120.00, 'CONFIRMED', '2026-05-04 16:00:00', 7, 7),
('RES008', '2026-05-12', '2026-05-16', 4, 2, 3, 12, 5, 110.00, 'CONFIRMED', '2026-05-04 17:00:00', 8, 12),
('RES009', '2026-05-13', '2026-05-17', 4, 4, 4, 17, 7, 200.00, 'CONFIRMED', '2026-05-04 18:00:00', 9, 17),
('RES010', '2026-05-14', '2026-05-18', 4, 4, 5, 22, 9, 280.00, 'CONFIRMED', '2026-05-04 19:00:00', 10, 22),
('RES011', '2026-05-15', '2026-05-19', 4, 1, 1, 3, 1, 80.00, 'CONFIRMED', '2026-05-04 20:00:00', 11, 3),
('RES012', '2026-05-16', '2026-05-19', 3, 2, 2, 8, 3, 120.00, 'CONFIRMED', '2026-05-04 21:00:00', 12, 8),
('RES013', '2026-05-17', '2026-05-20', 3, 2, 3, 13, 5, 110.00, 'CONFIRMED', '2026-05-04 22:00:00', 13, 13),
('RES014', '2026-05-18', '2026-05-22', 4, 4, 4, 18, 7, 200.00, 'CONFIRMED', '2026-05-05 08:00:00', 14, 18),
('RES015', '2026-05-19', '2026-05-23', 4, 4, 5, 23, 9, 280.00, 'CONFIRMED', '2026-05-05 09:00:00', 15, 23),
('RES016', '2026-05-20', '2026-05-24', 4, 1, 1, 4, 1, 80.00, 'CANCELLED', '2026-05-05 10:00:00', 16, 4),
('RES017', '2026-05-21', '2026-05-24', 3, 2, 2, 9, 3, 120.00, 'CONFIRMED', '2026-05-05 11:00:00', 17, 9),
('RES018', '2026-05-22', '2026-05-25', 3, 2, 3, 14, 5, 110.00, 'CONFIRMED', '2026-05-05 12:00:00', 18, 14),
('RES019', '2026-05-23', '2026-05-27', 4, 4, 4, 19, 7, 200.00, 'CONFIRMED', '2026-05-05 13:00:00', 19, 19),
('RES020', '2026-05-24', '2026-05-28', 4, 4, 5, 24, 9, 280.00, 'CONFIRMED', '2026-05-05 14:00:00', 20, 24),
('RES021', '2026-05-25', '2026-05-29', 4, 1, 1, 5, 1, 80.00, 'CONFIRMED', '2026-05-05 15:00:00', 21, 5),
('RES022', '2026-05-26', '2026-05-29', 3, 2, 2, 10, 3, 120.00, 'CONFIRMED', '2026-05-05 16:00:00', 22, 10),
('RES023', '2026-05-27', '2026-05-30', 3, 2, 3, 15, 5, 110.00, 'CONFIRMED', '2026-05-05 17:00:00', 23, 15),
('RES024', '2026-05-28', '2026-06-01', 4, 4, 4, 20, 8, 300.00, 'CONFIRMED', '2026-05-05 18:00:00', 24, 20),
('RES025', '2026-05-29', '2026-06-02', 4, 4, 5, 25, 10, 420.00, 'CONFIRMED', '2026-05-05 19:00:00', 25, 25),
('RES026', '2026-05-30', '2026-06-03', 4, 1, 1, 1, 2, 120.00, 'CONFIRMED', '2026-05-05 20:00:00', 26, 1),
('RES027', '2026-05-31', '2026-06-03', 3, 2, 2, 11, 4, 180.00, 'CONFIRMED', '2026-05-05 21:00:00', 27, 11),
('RES028', '2026-06-01', '2026-06-04', 3, 2, 3, 12, 6, 170.00, 'CONFIRMED', '2026-05-05 22:00:00', 28, 12),
('RES029', '2026-06-02', '2026-06-06', 4, 4, 4, 21, 8, 300.00, 'CONFIRMED', '2026-05-06 08:00:00', 29, 21),
('RES030', '2026-06-03', '2026-06-07', 4, 4, 5, 26, 10, 420.00, 'CONFIRMED', '2026-05-06 09:00:00', 30, 26),
('RES031', '2026-06-04', '2026-06-08', 4, 1, 1, 2, 2, 120.00, 'CONFIRMED', '2026-05-06 10:00:00', 31, 2),
('RES032', '2026-06-05', '2026-06-08', 3, 2, 2, 6, 4, 180.00, 'CONFIRMED', '2026-05-06 11:00:00', 32, 6),
('RES033', '2026-06-06', '2026-06-09', 3, 2, 3, 13, 6, 170.00, 'CONFIRMED', '2026-05-06 12:00:00', 33, 13),
('RES034', '2026-06-07', '2026-06-11', 4, 4, 4, 16, 8, 300.00, 'CONFIRMED', '2026-05-06 13:00:00', 34, 16),
('RES035', '2026-06-08', '2026-06-12', 4, 4, 5, 21, 10, 420.00, 'CONFIRMED', '2026-05-06 14:00:00', 35, 21),
('RES036', '2026-06-09', '2026-06-13', 4, 1, 1, 3, 2, 120.00, 'CONFIRMED', '2026-05-06 15:00:00', 36, 3),
('RES037', '2026-06-10', '2026-06-13', 3, 2, 2, 7, 4, 180.00, 'CONFIRMED', '2026-05-06 16:00:00', 37, 7),
('RES038', '2026-06-11', '2026-06-14', 3, 2, 3, 14, 6, 170.00, 'CONFIRMED', '2026-05-06 17:00:00', 38, 14),
('RES039', '2026-06-12', '2026-06-16', 4, 4, 4, 17, 8, 300.00, 'CONFIRMED', '2026-05-06 18:00:00', 39, 17),
('RES040', '2026-06-13', '2026-06-17', 4, 4, 5, 22, 10, 420.00, 'CONFIRMED', '2026-05-06 19:00:00', 40, 22),
('RES041', '2026-06-14', '2026-06-18', 4, 1, 1, 4, 2, 120.00, 'CONFIRMED', '2026-05-06 20:00:00', 41, 4),
('RES042', '2026-06-15', '2026-06-18', 3, 2, 2, 8, 4, 180.00, 'CONFIRMED', '2026-05-06 21:00:00', 42, 8),
('RES043', '2026-06-16', '2026-06-19', 3, 2, 3, 15, 6, 170.00, 'CONFIRMED', '2026-05-06 22:00:00', 43, 15),
('RES044', '2026-06-17', '2026-06-21', 4, 4, 4, 18, 8, 300.00, 'CONFIRMED', '2026-05-07 08:00:00', 44, 18),
('RES045', '2026-06-18', '2026-06-22', 4, 4, 5, 23, 10, 420.00, 'CONFIRMED', '2026-05-07 09:00:00', 45, 23),
('RES046', '2026-06-19', '2026-06-23', 4, 1, 1, 5, 2, 120.00, 'CONFIRMED', '2026-05-07 10:00:00', 46, 5),
('RES047', '2026-06-20', '2026-06-23', 3, 2, 2, 9, 4, 180.00, 'CONFIRMED', '2026-05-07 11:00:00', 47, 9),
('RES048', '2026-06-21', '2026-06-24', 3, 2, 3, 16, 6, 170.00, 'CONFIRMED', '2026-05-07 12:00:00', 48, 16),
('RES049', '2026-06-22', '2026-06-26', 4, 4, 4, 19, 8, 300.00, 'CONFIRMED', '2026-05-07 13:00:00', 49, 19),
('RES050', '2026-06-23', '2026-06-27', 4, 4, 5, 24, 10, 420.00, 'CONFIRMED', '2026-05-07 14:00:00', 50, 24),
('RES051', '2026-06-24', '2026-06-28', 4, 1, 1, 1, 2, 120.00, 'CHECKED_OUT', '2026-05-07 15:00:00', 51, 1),
('RES052', '2026-06-25', '2026-06-28', 3, 2, 2, 10, 4, 180.00, 'CONFIRMED', '2026-05-07 16:00:00', 52, 10),
('RES053', '2026-06-26', '2026-06-29', 3, 2, 3, 11, 6, 170.00, 'CONFIRMED', '2026-05-07 17:00:00', 53, 11),
('RES054', '2026-06-27', '2026-07-01', 4, 4, 4, 20, 8, 300.00, 'CONFIRMED', '2026-05-07 18:00:00', 54, 20),
('RES055', '2026-06-28', '2026-07-02', 4, 4, 5, 25, 10, 420.00, 'CONFIRMED', '2026-05-07 19:00:00', 55, 25);

-- ============================================
-- INSERT RESERVATION GUESTS
-- ============================================
INSERT INTO reservation_guest (reservation_id, guest_id, is_primary) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 6, 1),
(7, 7, 1),
(8, 8, 1),
(9, 9, 1),
(10, 10, 1),
(11, 11, 1),
(12, 12, 1),
(13, 13, 1),
(14, 14, 1),
(15, 15, 1),
(16, 16, 1),
(17, 17, 1),
(18, 18, 1),
(19, 19, 1),
(20, 20, 1),
(21, 21, 1),
(22, 22, 1),
(23, 23, 1),
(24, 24, 1),
(25, 25, 1),
(26, 26, 1),
(27, 27, 1),
(28, 28, 1),
(29, 29, 1),
(30, 30, 1),
(31, 31, 1),
(32, 32, 1),
(33, 33, 1),
(34, 34, 1),
(35, 35, 1),
(36, 36, 1),
(37, 37, 1),
(38, 38, 1),
(39, 39, 1),
(40, 40, 1),
(41, 41, 1),
(42, 42, 1),
(43, 43, 1),
(44, 44, 1),
(45, 45, 1),
(46, 46, 1),
(47, 47, 1),
(48, 48, 1),
(49, 49, 1),
(50, 50, 1),
(51, 51, 1),
(52, 52, 1),
(53, 53, 1),
(54, 54, 1),
(55, 55, 1);

-- ============================================
-- INSERT BILLS
-- ============================================
INSERT INTO bill (reservation_id, opened_at, closed_at, total_amount) VALUES
(1, '2026-05-05 10:30:00', '2026-05-07 11:00:00', 160.00),
(2, '2026-05-06 11:00:00', '2026-05-09 10:00:00', 360.00),
(3, '2026-05-07 12:00:00', '2026-05-10 10:00:00', 330.00),
(4, '2026-05-08 13:00:00', '2026-05-12 10:00:00', 800.00),
(5, '2026-05-09 14:00:00', '2026-05-13 10:00:00', 1120.00),
(6, '2026-05-10 15:00:00', NULL, 400.00),
(7, '2026-05-11 16:00:00', '2026-05-14 10:00:00', 360.00),
(8, '2026-05-12 17:00:00', '2026-05-16 10:00:00', 440.00),
(9, '2026-05-13 18:00:00', '2026-05-17 10:00:00', 800.00),
(10, '2026-05-14 19:00:00', '2026-05-18 10:00:00', 1120.00),
(11, '2026-05-15 20:00:00', '2026-05-19 10:00:00', 320.00),
(12, '2026-05-16 21:00:00', '2026-05-19 10:00:00', 360.00),
(13, '2026-05-17 22:00:00', '2026-05-20 10:00:00', 330.00),
(14, '2026-05-18 08:00:00', '2026-05-22 10:00:00', 800.00),
(15, '2026-05-19 09:00:00', '2026-05-23 10:00:00', 1120.00),
(16, '2026-05-20 10:00:00', NULL, 320.00),
(17, '2026-05-21 11:00:00', '2026-05-24 10:00:00', 360.00),
(18, '2026-05-22 12:00:00', '2026-05-25 10:00:00', 330.00),
(19, '2026-05-23 13:00:00', '2026-05-27 10:00:00', 800.00),
(20, '2026-05-24 14:00:00', '2026-05-28 10:00:00', 1120.00),
(21, '2026-05-25 15:00:00', '2026-05-29 10:00:00', 320.00),
(22, '2026-05-26 16:00:00', '2026-05-29 10:00:00', 360.00),
(23, '2026-05-27 17:00:00', '2026-05-30 10:00:00', 330.00),
(24, '2026-05-28 18:00:00', '2026-06-01 10:00:00', 1200.00),
(25, '2026-05-29 19:00:00', '2026-06-02 10:00:00', 1680.00),
(26, '2026-05-30 20:00:00', '2026-06-03 10:00:00', 480.00),
(27, '2026-05-31 21:00:00', '2026-06-03 10:00:00', 540.00),
(28, '2026-06-01 22:00:00', '2026-06-04 10:00:00', 510.00),
(29, '2026-06-02 08:00:00', '2026-06-06 10:00:00', 1200.00),
(30, '2026-06-03 09:00:00', '2026-06-07 10:00:00', 1680.00),
(31, '2026-06-04 10:00:00', '2026-06-08 10:00:00', 480.00),
(32, '2026-06-05 11:00:00', '2026-06-08 10:00:00', 540.00),
(33, '2026-06-06 12:00:00', '2026-06-09 10:00:00', 510.00),
(34, '2026-06-07 13:00:00', '2026-06-11 10:00:00', 1200.00),
(35, '2026-06-08 14:00:00', '2026-06-12 10:00:00', 1680.00),
(36, '2026-06-09 15:00:00', '2026-06-13 10:00:00', 480.00),
(37, '2026-06-10 16:00:00', '2026-06-13 10:00:00', 540.00),
(38, '2026-06-11 17:00:00', '2026-06-14 10:00:00', 510.00),
(39, '2026-06-12 18:00:00', '2026-06-16 10:00:00', 1200.00),
(40, '2026-06-13 19:00:00', '2026-06-17 10:00:00', 1680.00),
(41, '2026-06-14 20:00:00', '2026-06-18 10:00:00', 480.00),
(42, '2026-06-15 21:00:00', '2026-06-18 10:00:00', 540.00),
(43, '2026-06-16 22:00:00', '2026-06-19 10:00:00', 510.00),
(44, '2026-06-17 08:00:00', '2026-06-21 10:00:00', 1200.00),
(45, '2026-06-18 09:00:00', '2026-06-22 10:00:00', 1680.00),
(46, '2026-06-19 10:00:00', '2026-06-23 10:00:00', 480.00),
(47, '2026-06-20 11:00:00', '2026-06-23 10:00:00', 540.00),
(48, '2026-06-21 12:00:00', '2026-06-24 10:00:00', 510.00),
(49, '2026-06-22 13:00:00', '2026-06-26 10:00:00', 1200.00),
(50, '2026-06-23 14:00:00', '2026-06-27 10:00:00', 1680.00),
(51, '2026-06-24 15:00:00', '2026-06-28 10:00:00', 480.00),
(52, '2026-06-25 16:00:00', '2026-06-28 10:00:00', 540.00),
(53, '2026-06-26 17:00:00', '2026-06-29 10:00:00', 510.00),
(54, '2026-06-27 18:00:00', '2026-07-01 10:00:00', 1200.00),
(55, '2026-06-28 19:00:00', '2026-07-02 10:00:00', 1680.00);

-- ============================================
-- INSERT BILL ITEMS
-- ============================================
INSERT INTO bill_item (bill_id, item_type, description, quantity, unit_price, line_total, posted_at) VALUES
(1, 'ROOM_CHARGE', 'Single Room - 2 nights', 2, 80.00, 160.00, '2026-05-07 11:00:00'),
(2, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 120.00, 360.00, '2026-05-09 10:00:00'),
(3, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 110.00, 330.00, '2026-05-10 10:00:00'),
(4, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 200.00, 800.00, '2026-05-12 10:00:00'),
(5, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 280.00, 1120.00, '2026-05-13 10:00:00'),
(6, 'ROOM_CHARGE', 'Single Room - 4 nights', 4, 100.00, 400.00, '2026-05-29 10:00:00'),
(7, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 120.00, 360.00, '2026-05-14 10:00:00'),
(8, 'ROOM_CHARGE', 'Twin Room - 4 nights', 4, 110.00, 440.00, '2026-05-16 10:00:00'),
(9, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 200.00, 800.00, '2026-05-17 10:00:00'),
(10, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 280.00, 1120.00, '2026-05-18 10:00:00'),
(11, 'EXTRA_SERVICE', 'Room Service Breakfast', 4, 25.00, 100.00, '2026-05-15 12:00:00'),
(12, 'EXTRA_SERVICE', 'Spa Treatment - Massage', 1, 100.00, 100.00, '2026-05-17 18:00:00'),
(13, 'EXTRA_SERVICE', 'Airport Transfer', 2, 50.00, 100.00, '2026-05-18 08:00:00'),
(14, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 200.00, 800.00, '2026-05-22 10:00:00'),
(15, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 280.00, 1120.00, '2026-05-23 10:00:00'),
(16, 'ROOM_CHARGE', 'Single Room - 4 nights', 4, 80.00, 320.00, '2026-05-24 10:00:00'),
(17, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 120.00, 360.00, '2026-05-24 10:00:00'),
(18, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 110.00, 330.00, '2026-05-25 10:00:00'),
(19, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 200.00, 800.00, '2026-05-27 10:00:00'),
(20, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 280.00, 1120.00, '2026-05-28 10:00:00'),
(21, 'EXTRA_SERVICE', 'City Tour', 1, 120.00, 120.00, '2026-05-26 14:00:00'),
(22, 'EXTRA_SERVICE', 'Wine Tasting', 2, 75.00, 150.00, '2026-05-27 19:00:00'),
(23, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 110.00, 330.00, '2026-05-30 10:00:00'),
(24, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 300.00, 1200.00, '2026-06-01 10:00:00'),
(25, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 420.00, 1680.00, '2026-06-02 10:00:00'),
(26, 'ROOM_CHARGE', 'Single Room - 4 nights', 4, 120.00, 480.00, '2026-06-03 10:00:00'),
(27, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 180.00, 540.00, '2026-06-03 10:00:00'),
(28, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 170.00, 510.00, '2026-06-04 10:00:00'),
(29, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 300.00, 1200.00, '2026-06-06 10:00:00'),
(30, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 420.00, 1680.00, '2026-06-07 10:00:00'),
(31, 'EXTRA_SERVICE', 'Early Breakfast', 4, 20.00, 80.00, '2026-06-05 07:00:00'),
(32, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 180.00, 540.00, '2026-06-08 10:00:00'),
(33, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 170.00, 510.00, '2026-06-09 10:00:00'),
(34, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 300.00, 1200.00, '2026-06-11 10:00:00'),
(35, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 420.00, 1680.00, '2026-06-12 10:00:00'),
(36, 'ROOM_CHARGE', 'Single Room - 4 nights', 4, 120.00, 480.00, '2026-06-13 10:00:00'),
(37, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 180.00, 540.00, '2026-06-13 10:00:00'),
(38, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 170.00, 510.00, '2026-06-14 10:00:00'),
(39, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 300.00, 1200.00, '2026-06-16 10:00:00'),
(40, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 420.00, 1680.00, '2026-06-17 10:00:00'),
(41, 'ROOM_CHARGE', 'Single Room - 4 nights', 4, 120.00, 480.00, '2026-06-18 10:00:00'),
(42, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 180.00, 540.00, '2026-06-18 10:00:00'),
(43, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 170.00, 510.00, '2026-06-19 10:00:00'),
(44, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 300.00, 1200.00, '2026-06-21 10:00:00'),
(45, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 420.00, 1680.00, '2026-06-22 10:00:00'),
(46, 'ROOM_CHARGE', 'Single Room - 4 nights', 4, 120.00, 480.00, '2026-06-23 10:00:00'),
(47, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 180.00, 540.00, '2026-06-23 10:00:00'),
(48, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 170.00, 510.00, '2026-06-24 10:00:00'),
(49, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 300.00, 1200.00, '2026-06-26 10:00:00'),
(50, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 420.00, 1680.00, '2026-06-27 10:00:00'),
(51, 'ROOM_CHARGE', 'Single Room - 4 nights', 4, 120.00, 480.00, '2026-06-28 10:00:00'),
(52, 'ROOM_CHARGE', 'Double Room - 3 nights', 3, 180.00, 540.00, '2026-06-28 10:00:00'),
(53, 'ROOM_CHARGE', 'Twin Room - 3 nights', 3, 170.00, 510.00, '2026-06-29 10:00:00'),
(54, 'ROOM_CHARGE', 'Suite - 4 nights', 4, 300.00, 1200.00, '2026-07-01 10:00:00'),
(55, 'ROOM_CHARGE', 'Deluxe Suite - 4 nights', 4, 420.00, 1680.00, '2026-07-02 10:00:00');

-- ============================================
-- INSERT ROOM CLEANING TASKS (50+)
-- ============================================
INSERT INTO room_cleaning_task (room_id, created_at, task_status, note) VALUES
(1, '2026-05-07 11:30:00', 'COMPLETED', 'Room cleaned after checkout'),
(2, '2026-05-08 10:00:00', 'PENDING', 'Waiting for cleaner'),
(3, '2026-05-09 09:00:00', 'IN_PROGRESS', 'Currently being cleaned'),
(4, '2026-05-10 08:00:00', 'COMPLETED', 'Room cleaned and ready'),
(5, '2026-05-11 10:00:00', 'COMPLETED', 'Maintenance cleaning'),
(6, '2026-05-09 12:00:00', 'COMPLETED', 'Room cleaned after checkout'),
(7, '2026-05-10 11:00:00', 'COMPLETED', 'Room cleaned'),
(8, '2026-05-11 10:00:00', 'IN_PROGRESS', 'Currently being cleaned'),
(9, '2026-05-12 09:00:00', 'PENDING', 'Waiting for cleaner'),
(10, '2026-05-13 08:00:00', 'COMPLETED', 'Room cleaned'),
(11, '2026-05-10 13:00:00', 'COMPLETED', 'Room cleaned after checkout'),
(12, '2026-05-11 12:00:00', 'COMPLETED', 'Room cleaned'),
(13, '2026-05-12 11:00:00', 'IN_PROGRESS', 'Currently being cleaned'),
(14, '2026-05-13 10:00:00', 'PENDING', 'Waiting for cleaner'),
(15, '2026-05-14 09:00:00', 'COMPLETED', 'Room cleaned'),
(16, '2026-05-11 14:00:00', 'COMPLETED', 'Room cleaned after checkout'),
(17, '2026-05-12 13:00:00', 'COMPLETED', 'Room cleaned'),
(18, '2026-05-13 12:00:00', 'IN_PROGRESS', 'Currently being cleaned'),
(19, '2026-05-14 11:00:00', 'PENDING', 'Waiting for cleaner'),
(20, '2026-05-15 10:00:00', 'COMPLETED', 'Room cleaned'),
(21, '2026-05-12 15:00:00', 'COMPLETED', 'Room cleaned after checkout'),
(22, '2026-05-13 14:00:00', 'COMPLETED', 'Room cleaned'),
(23, '2026-05-14 13:00:00', 'IN_PROGRESS', 'Currently being cleaned'),
(24, '2026-05-15 12:00:00', 'PENDING', 'Waiting for cleaner'),
(25, '2026-05-16 11:00:00', 'COMPLETED', 'Room cleaned'),
(26, '2026-05-13 16:00:00', 'COMPLETED', 'Room cleaned after checkout'),
(27, '2026-05-14 15:00:00', 'COMPLETED', 'Room cleaned'),
(28, '2026-05-15 14:00:00', 'IN_PROGRESS', 'Currently being cleaned'),
(29, '2026-05-16 13:00:00', 'PENDING', 'Waiting for cleaner'),
(30, '2026-05-17 12:00:00', 'COMPLETED', 'Room cleaned');

-- ============================================
-- INSERT ROOM CLEANING ASSIGNMENTS
-- ============================================
INSERT INTO room_cleaning_assignment (task_id, cleaner_id, assigned_at) VALUES
(1, 1, '2026-05-07 11:30:00'),
(2, 2, '2026-05-08 10:00:00'),
(3, 3, '2026-05-09 09:00:00'),
(4, 4, '2026-05-10 08:00:00'),
(5, 5, '2026-05-11 10:00:00'),
(6, 1, '2026-05-09 12:00:00'),
(7, 2, '2026-05-10 11:00:00'),
(8, 3, '2026-05-11 10:00:00'),
(9, 4, '2026-05-12 09:00:00'),
(10, 5, '2026-05-13 08:00:00'),
(11, 1, '2026-05-10 13:00:00'),
(12, 2, '2026-05-11 12:00:00'),
(13, 3, '2026-05-12 11:00:00'),
(14, 4, '2026-05-13 10:00:00'),
(15, 5, '2026-05-14 09:00:00'),
(16, 1, '2026-05-11 14:00:00'),
(17, 2, '2026-05-12 13:00:00'),
(18, 3, '2026-05-13 12:00:00'),
(19, 4, '2026-05-14 11:00:00'),
(20, 5, '2026-05-15 10:00:00'),
(21, 1, '2026-05-12 15:00:00'),
(22, 2, '2026-05-13 14:00:00'),
(23, 3, '2026-05-14 13:00:00'),
(24, 4, '2026-05-15 12:00:00'),
(25, 5, '2026-05-16 11:00:00'),
(26, 1, '2026-05-13 16:00:00'),
(27, 2, '2026-05-14 15:00:00'),
(28, 3, '2026-05-15 14:00:00'),
(29, 4, '2026-05-16 13:00:00'),
(30, 5, '2026-05-17 12:00:00');

-- ============================================
-- RE-ENABLE FOREIGN KEY CHECKS
-- ============================================
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- VERIFICATION QUERIES (optional)
-- ============================================
-- SELECT COUNT(*) as user_count FROM user_account;
-- SELECT COUNT(*) as cleaner_count FROM cleaner;
-- SELECT COUNT(*) as guest_count FROM guest;
-- SELECT COUNT(*) as reservation_count FROM reservation;
-- SELECT COUNT(*) as bill_count FROM bill;
-- SELECT COUNT(*) as room_cleaning_task_count FROM room_cleaning_task;
-- SELECT COUNT(*) as room_cleaning_assignment_count FROM room_cleaning_assignment;
