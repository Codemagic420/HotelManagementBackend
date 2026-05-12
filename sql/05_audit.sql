-- ============================================
-- 05_audit.sql
-- Hotel Management Database - Audit Structure
-- Implements comprehensive audit logging with triggers
-- ============================================

USE hotel_db;

-- ============================================
-- TABLE: audit_log
-- Purpose: Store all changes to sensitive tables
-- Tracks: INSERT, UPDATE, DELETE operations
-- Fields: table_name, operation_type, old_values, new_values, changed_by, changed_at
-- ============================================
CREATE TABLE IF NOT EXISTS audit_log (
    audit_id INT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    operation_type ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    record_id INT,
    old_values JSON,
    new_values JSON,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_table (table_name),
    INDEX idx_audit_operation (operation_type),
    INDEX idx_audit_timestamp (changed_at),
    INDEX idx_audit_record (table_name, record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- AUDIT TRIGGERS FOR RESERVATION TABLE
-- Purpose: Track all reservation changes
-- ============================================

-- TRIGGER: Audit INSERT on RESERVATION
DELIMITER //
CREATE TRIGGER tr_audit_reservation_insert
AFTER INSERT ON reservation
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        new_values,
        changed_by,
        changed_at
    ) VALUES (
        'RESERVATION',
        'INSERT',
        NEW.reservation_id,
        JSON_OBJECT(
            'reference_no', NEW.reference_no,
            'guest_id', NEW.guest_id,
            'room_id', NEW.room_id,
            'check_in_date', NEW.check_in_date,
            'check_out_date', NEW.check_out_date,
            'status', NEW.status,
            'nights', NEW.nights,
            'num_guests', NEW.num_guests,
            'booked_nightly_price', NEW.booked_nightly_price
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- TRIGGER: Audit UPDATE on RESERVATION
DELIMITER //
CREATE TRIGGER tr_audit_reservation_update
AFTER UPDATE ON reservation
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        old_values,
        new_values,
        changed_by,
        changed_at
    ) VALUES (
        'RESERVATION',
        'UPDATE',
        NEW.reservation_id,
        JSON_OBJECT(
            'status', OLD.status,
            'check_in_date', OLD.check_in_date,
            'check_out_date', OLD.check_out_date,
            'room_id', OLD.room_id
        ),
        JSON_OBJECT(
            'status', NEW.status,
            'check_in_date', NEW.check_in_date,
            'check_out_date', NEW.check_out_date,
            'room_id', NEW.room_id
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- TRIGGER: Audit DELETE on RESERVATION
DELIMITER //
CREATE TRIGGER tr_audit_reservation_delete
AFTER DELETE ON reservation
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        old_values,
        changed_by,
        changed_at
    ) VALUES (
        'RESERVATION',
        'DELETE',
        OLD.reservation_id,
        JSON_OBJECT(
            'reference_no', OLD.reference_no,
            'guest_id', OLD.guest_id,
            'room_id', OLD.room_id,
            'status', OLD.status
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- ============================================
-- AUDIT TRIGGERS FOR BILL TABLE
-- Purpose: Track all billing changes
-- ============================================

-- TRIGGER: Audit INSERT on BILL
DELIMITER //
CREATE TRIGGER tr_audit_bill_insert
AFTER INSERT ON bill
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        new_values,
        changed_by,
        changed_at
    ) VALUES (
        'BILL',
        'INSERT',
        NEW.bill_id,
        JSON_OBJECT(
            'reservation_id', NEW.reservation_id,
            'total_amount', NEW.total_amount,
            'is_paid', NEW.is_paid,
            'opened_at', NEW.opened_at
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- TRIGGER: Audit UPDATE on BILL
DELIMITER //
CREATE TRIGGER tr_audit_bill_update
AFTER UPDATE ON bill
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        old_values,
        new_values,
        changed_by,
        changed_at
    ) VALUES (
        'BILL',
        'UPDATE',
        NEW.bill_id,
        JSON_OBJECT(
            'total_amount', OLD.total_amount,
            'is_paid', OLD.is_paid,
            'closed_at', OLD.closed_at
        ),
        JSON_OBJECT(
            'total_amount', NEW.total_amount,
            'is_paid', NEW.is_paid,
            'closed_at', NEW.closed_at
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- TRIGGER: Audit DELETE on BILL
DELIMITER //
CREATE TRIGGER tr_audit_bill_delete
AFTER DELETE ON bill
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        old_values,
        changed_by,
        changed_at
    ) VALUES (
        'BILL',
        'DELETE',
        OLD.bill_id,
        JSON_OBJECT(
            'reservation_id', OLD.reservation_id,
            'total_amount', OLD.total_amount,
            'is_paid', OLD.is_paid
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- ============================================
-- AUDIT TRIGGERS FOR BILL_ITEM TABLE
-- Purpose: Track all bill item changes
-- ============================================

-- TRIGGER: Audit INSERT on BILL_ITEM
DELIMITER //
CREATE TRIGGER tr_audit_bill_item_insert
AFTER INSERT ON bill_item
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        new_values,
        changed_by,
        changed_at
    ) VALUES (
        'BILL_ITEM',
        'INSERT',
        NEW.bill_item_id,
        JSON_OBJECT(
            'bill_id', NEW.bill_id,
            'item_type', NEW.item_type,
            'description', NEW.description,
            'quantity', NEW.quantity,
            'unit_price', NEW.unit_price,
            'line_total', NEW.line_total
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- TRIGGER: Audit DELETE on BILL_ITEM
DELIMITER //
CREATE TRIGGER tr_audit_bill_item_delete
AFTER DELETE ON bill_item
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        old_values,
        changed_by,
        changed_at
    ) VALUES (
        'BILL_ITEM',
        'DELETE',
        OLD.bill_item_id,
        JSON_OBJECT(
            'bill_id', OLD.bill_id,
            'item_type', OLD.item_type,
            'line_total', OLD.line_total
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- ============================================
-- AUDIT TRIGGERS FOR GUEST TABLE
-- Purpose: Track guest information changes
-- ============================================

-- TRIGGER: Audit INSERT on GUEST
DELIMITER //
CREATE TRIGGER tr_audit_guest_insert
AFTER INSERT ON guest
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        table_name,
        operation_type,
        record_id,
        new_values,
        changed_by,
        changed_at
    ) VALUES (
        'GUEST',
        'INSERT',
        NEW.guest_id,
        JSON_OBJECT(
            'first_name', NEW.first_name,
            'last_name', NEW.last_name,
            'email', NEW.email,
            'phone', NEW.phone
        ),
        CURRENT_USER(),
        NOW()
    );
END //
DELIMITER ;

-- TRIGGER: Audit UPDATE on GUEST
DELIMITER //
CREATE TRIGGER tr_audit_guest_update
AFTER UPDATE ON guest
FOR EACH ROW
BEGIN
    IF OLD.email != NEW.email OR OLD.phone != NEW.phone THEN
        INSERT INTO audit_log (
            table_name,
            operation_type,
            record_id,
            old_values,
            new_values,
            changed_by,
            changed_at
        ) VALUES (
            'GUEST',
            'UPDATE',
            NEW.guest_id,
            JSON_OBJECT('email', OLD.email, 'phone', OLD.phone),
            JSON_OBJECT('email', NEW.email, 'phone', NEW.phone),
            CURRENT_USER(),
            NOW()
        );
    END IF;
END //
DELIMITER ;

-- ============================================
-- AUDIT TRIGGERS FOR ROOM TABLE
-- Purpose: Track room status changes
-- ============================================

-- TRIGGER: Audit UPDATE on ROOM (status changes)
DELIMITER //
CREATE TRIGGER tr_audit_room_update
AFTER UPDATE ON room
FOR EACH ROW
BEGIN
    IF OLD.room_status != NEW.room_status OR OLD.clean_status != NEW.clean_status THEN
        INSERT INTO audit_log (
            table_name,
            operation_type,
            record_id,
            old_values,
            new_values,
            changed_by,
            changed_at
        ) VALUES (
            'ROOM',
            'UPDATE',
            NEW.room_id,
            JSON_OBJECT(
                'room_status', OLD.room_status,
                'clean_status', OLD.clean_status,
                'occupied', OLD.occupied
            ),
            JSON_OBJECT(
                'room_status', NEW.room_status,
                'clean_status', NEW.clean_status,
                'occupied', NEW.occupied
            ),
            CURRENT_USER(),
            NOW()
        );
    END IF;
END //
DELIMITER ;

-- ============================================
-- AUDIT VIEWS FOR QUERYING AUDIT DATA
-- ============================================

-- VIEW: vw_recent_audit_logs
-- Shows recent audit entries with human-readable timestamps
CREATE OR REPLACE VIEW vw_recent_audit_logs AS
SELECT
    audit_id,
    table_name,
    operation_type,
    record_id,
    changed_by,
    DATE_FORMAT(changed_at, '%Y-%m-%d %H:%i:%s') AS changed_at_formatted,
    changed_at
FROM audit_log
ORDER BY changed_at DESC
LIMIT 100;

-- VIEW: vw_audit_by_table
-- Groups audit entries by table
CREATE OR REPLACE VIEW vw_audit_by_table AS
SELECT
    table_name,
    operation_type,
    COUNT(*) AS change_count,
    MIN(changed_at) AS first_change,
    MAX(changed_at) AS last_change
FROM audit_log
GROUP BY table_name, operation_type;

-- VIEW: vw_audit_by_user
-- Groups audit entries by user
CREATE OR REPLACE VIEW vw_audit_by_user AS
SELECT
    changed_by,
    COUNT(*) AS total_changes,
    COUNT(CASE WHEN operation_type = 'INSERT' THEN 1 END) AS inserts,
    COUNT(CASE WHEN operation_type = 'UPDATE' THEN 1 END) AS updates,
    COUNT(CASE WHEN operation_type = 'DELETE' THEN 1 END) AS deletes,
    MIN(changed_at) AS first_action,
    MAX(changed_at) AS last_action
FROM audit_log
GROUP BY changed_by;

-- ============================================
-- AUDIT MAINTENANCE PROCEDURES
-- ============================================

-- PROCEDURE: Clean old audit logs (older than 90 days)
DELIMITER //
CREATE PROCEDURE sp_cleanup_old_audit_logs(
    IN p_days_old INT
)
MODIFIES SQL DATA
BEGIN
    DELETE FROM audit_log
    WHERE changed_at < DATE_SUB(NOW(), INTERVAL p_days_old DAY);
END //
DELIMITER ;

-- PROCEDURE: Get audit history for a specific record
DELIMITER //
CREATE PROCEDURE sp_get_audit_history(
    IN p_table_name VARCHAR(50),
    IN p_record_id INT
)
READS SQL DATA
BEGIN
    SELECT
        audit_id,
        operation_type,
        old_values,
        new_values,
        changed_by,
        changed_at
    FROM audit_log
    WHERE table_name = p_table_name
      AND record_id = p_record_id
    ORDER BY changed_at DESC;
END //
DELIMITER ;

-- ============================================
-- End of Audit Configuration
-- ============================================
