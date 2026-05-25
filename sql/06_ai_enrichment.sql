-- ============================================
-- 06_ai_enrichment.sql
-- AI Data Enrichment Columns
-- Adds AI-generated summaries and metadata
-- ============================================

USE hotel_db;

-- ============================================
-- Guest Table - AI Enrichment Columns
-- ============================================
ALTER TABLE guest ADD COLUMN ai_profile_summary LONGTEXT NULL COMMENT 'AI-generated guest profile summary';
ALTER TABLE guest ADD COLUMN ai_fields_updated_at TIMESTAMP NULL COMMENT 'Last time AI fields were updated';

-- ============================================
-- Reservation Table - AI Enrichment Columns
-- ============================================
ALTER TABLE reservation ADD COLUMN ai_notes_summary LONGTEXT NULL COMMENT 'AI-generated reservation notes and insights';
ALTER TABLE reservation ADD COLUMN ai_fields_updated_at TIMESTAMP NULL COMMENT 'Last time AI fields were updated';

-- ============================================
-- Room Table - AI Enrichment Columns
-- ============================================
ALTER TABLE rooms ADD COLUMN ai_assessment_summary LONGTEXT NULL COMMENT 'AI-generated room condition and maintenance assessment';
ALTER TABLE rooms ADD COLUMN ai_fields_updated_at TIMESTAMP NULL COMMENT 'Last time AI fields were updated';

-- ============================================
-- Summary
-- ============================================
-- Guest: +2 columns (ai_profile_summary, ai_fields_updated_at)
-- Reservation: +2 columns (ai_notes_summary, ai_fields_updated_at)
-- Room: +2 columns (ai_assessment_summary, ai_fields_updated_at)
-- Total: +6 columns
-- ============================================
