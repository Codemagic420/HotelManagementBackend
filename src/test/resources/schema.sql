-- Minimal test schema to ensure H2 has required tables
-- Guest
CREATE TABLE IF NOT EXISTS guest (
  guest_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  credit_card_last4 VARCHAR(10),
  email VARCHAR(255),
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  phone VARCHAR(50)
);

-- Reservation
CREATE TABLE IF NOT EXISTS reservation (
  reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  booked_nightly_price DECIMAL(10,2),
  guest_id BIGINT,
  reference_no VARCHAR(50),
  status VARCHAR(50),
  check_in_date DATE,
  check_out_date DATE,
  created_at TIMESTAMP,
  nights INT,
  num_guests INT,
  room_type_id BIGINT,
  room_id BIGINT
);

-- Bill
CREATE TABLE IF NOT EXISTS bill (
  bill_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  reservation_id BIGINT,
  opened_at TIMESTAMP,
  closed_at TIMESTAMP,
  total_amount DECIMAL(12,2)
);

-- BillItem
CREATE TABLE IF NOT EXISTS bill_item (
  bill_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  bill_id BIGINT,
  item_type VARCHAR(50),
  description VARCHAR(255),
  quantity INT,
  unit_price DECIMAL(10,2),
  line_total DECIMAL(12,2)
);

-- Room type
CREATE TABLE IF NOT EXISTS room_type (
  room_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  max_occupancy INT
);

-- Room
CREATE TABLE IF NOT EXISTS room (
  room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  room_number VARCHAR(50),
  room_type_id BIGINT,
  room_status VARCHAR(50)
);

-- User account for authentication tests
CREATE TABLE IF NOT EXISTS user_account (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);
