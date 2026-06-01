-- ============================================
-- 03_logic.sql
-- Hotel Management Database - Business Logic
-- Stored procedures, functions, views, triggers
-- Group: Asger, Magnus, Sophus, Joel
-- ============================================

USE hotel_db;

-- ============================================
-- STORED FUNCTION: fn_GetRoomRate
-- Purpose: Find the correct DKK price based on Room Type and Season
-- Parameters: p_room_type_id INT, p_season (Spring, Summer, Autumn, Winter)
-- Returns: DECIMAL(10,2) - Price per night
-- Usage: SELECT fn_GetRoomRate(1, 'Spring') AS daily_rate;
-- ============================================
DELIMITER //
CREATE FUNCTION fn_GetRoomRate(
    p_room_type_id INT,
    p_season VARCHAR(20)
)
RETURNS DECIMAL(10,2)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE r_price DECIMAL(10,2);

    SELECT price_per_night INTO r_price
    FROM season_rate
    WHERE room_type_id = p_room_type_id
      AND season = p_season
      AND CURDATE() BETWEEN valid_from AND valid_to
    LIMIT 1;

    RETURN IFNULL(r_price, 0.00);
END //
DELIMITER ;

-- ============================================
-- STORED PROCEDURE: sp_CalculateFinalBill
-- Purpose: Calculate final bill amount for a reservation
--          Sums room charges and all extra service items
-- Parameters: p_reservation_id INT
-- Process:
--   1. Calculate total room cost (nights * rate)
--   2. Sum all extra services/items
--   3. Update BILL table with total_amount
--   4. Return the final amount
-- Usage: CALL sp_CalculateFinalBill(1);
-- ============================================
DELIMITER //
CREATE PROCEDURE sp_CalculateFinalBill(
    IN p_reservation_id INT
)
READS SQL DATA
MODIFIES SQL DATA
BEGIN
    DECLARE v_room_total DECIMAL(10,2) DEFAULT 0;
    DECLARE v_extras_total DECIMAL(10,2) DEFAULT 0;
    DECLARE v_nights INT DEFAULT 0;
    DECLARE v_nightly_rate DECIMAL(10,2) DEFAULT 0;
    DECLARE v_error_message VARCHAR(255);
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        BEGIN
            SET v_error_message = 'Error calculating bill';
        END;

    -- Get reservation nights and booked rate
    SELECT DATEDIFF(check_out_date, check_in_date),
           booked_nightly_price
    INTO v_nights, v_nightly_rate
    FROM reservation
    WHERE reservation_id = p_reservation_id;

    -- Calculate room total
    SET v_room_total = v_nights * v_nightly_rate;

    -- Sum all items in BILL_ITEM for this reservation
    SELECT IFNULL(SUM(COALESCE(amount, 0) * quantity), 0)
    INTO v_extras_total
    FROM bill_item bi
    JOIN bill b ON bi.bill_id = b.bill_id
    WHERE b.reservation_id = p_reservation_id;

    -- Update the main BILL table with calculated total
    UPDATE bill
    SET total_amount = v_room_total + v_extras_total
    WHERE reservation_id = p_reservation_id;

    -- Select the result for the caller
    SELECT total_amount AS final_bill_dkk
    FROM bill
    WHERE reservation_id = p_reservation_id;
END //
DELIMITER ;

-- ============================================
-- TRIGGER: tr_AfterCheckout
-- Purpose: Automatically set room status when guest checks out
-- Action: When reservation status changes to 'Checked Out'
--         Set room to 'Vacant' and 'Dirty'
-- ============================================
DELIMITER //
CREATE TRIGGER tr_AfterCheckout
    AFTER UPDATE ON reservation
    FOR EACH ROW
BEGIN
    IF NEW.status = 'Checked Out' AND OLD.status != 'Checked Out' THEN
        UPDATE room
        SET clean_status = 'Dirty', room_status = 'Vacant'
        WHERE room_id = NEW.room_id;
    END IF;
END //
DELIMITER ;

-- ============================================
-- TRIGGER: tr_RoomStatusUpdate
-- Purpose: Update room occupancy when reservation is confirmed/cancelled
-- ============================================
DELIMITER //
CREATE TRIGGER tr_RoomStatusUpdate
    AFTER UPDATE ON reservation
    FOR EACH ROW
BEGIN
    IF NEW.status = 'CONFIRMED' AND OLD.status != 'CONFIRMED' THEN
        UPDATE room
        SET room_status = 'OCCUPIED', occupied = 1
        WHERE room_id = NEW.room_id;
    ELSEIF NEW.status = 'CANCELLED' THEN
        UPDATE room
        SET room_status = 'AVAILABLE', occupied = 0
        WHERE room_id = NEW.room_id;
    END IF;
END //
DELIMITER ;

-- ============================================
-- VIEW: vw_HousekeepingList
-- Purpose: Provide clear list for staff of rooms needing cleaning
-- Shows: Room number, type, clean status, occupancy
-- Used by: Housekeeping department to see what needs cleaning
-- ============================================
CREATE OR REPLACE VIEW vw_HousekeepingList AS
SELECT
    r.room_id,
    r.room_number,
    rt.name AS room_type,
    r.clean_status,
    r.room_status AS occupancy,
    r.room_type_id
FROM room r
JOIN room_type rt ON r.room_type_id = rt.room_type_id
WHERE r.clean_status IN ('Dirty', 'Inspected')
ORDER BY r.clean_status DESC, r.room_number ASC;

-- ============================================
-- VIEW: vw_ReservationDetails
-- Purpose: Show detailed reservation information with guest and room details
-- ============================================
CREATE OR REPLACE VIEW vw_ReservationDetails AS
SELECT
    res.reservation_id,
    res.reference_no,
    g.first_name,
    g.last_name,
    g.email,
    g.phone,
    rt.name AS room_type,
    r.room_number,
    res.check_in_date,
    res.check_out_date,
    res.nights,
    res.status,
    sr.price_per_night,
    (res.nights * sr.price_per_night) AS estimated_room_charge
FROM reservation res
JOIN guest g ON res.guest_id = g.guest_id
JOIN room_type rt ON res.room_type_id = rt.room_type_id
LEFT JOIN room r ON res.room_id = r.room_id
LEFT JOIN season_rate sr ON res.booked_rate_id = sr.rate_id;

-- ============================================
-- VIEW: vw_BillDetails
-- Purpose: Show complete bill information with line items
-- ============================================
CREATE OR REPLACE VIEW vw_BillDetails AS
SELECT
    b.bill_id,
    b.reservation_id,
    res.reference_no,
    g.first_name,
    g.last_name,
    b.opened_at,
    b.closed_at,
    b.total_amount,
    b.is_paid,
    COUNT(bi.bill_item_id) AS line_item_count
FROM bill b
JOIN reservation res ON b.reservation_id = res.reservation_id
JOIN guest g ON res.guest_id = g.guest_id
LEFT JOIN bill_item bi ON b.bill_id = bi.bill_id
GROUP BY b.bill_id;

-- ============================================
-- End of Business Logic
-- ============================================
-- NOTE: Indexes can be added separately after schema validation
-- This avoids SQL syntax issues in CI/CD environments