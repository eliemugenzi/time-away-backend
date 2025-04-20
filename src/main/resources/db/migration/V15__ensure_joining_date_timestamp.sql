-- Ensure joining_date is TIMESTAMP
ALTER TABLE users ALTER COLUMN joining_date TYPE TIMESTAMP USING joining_date::TIMESTAMP; 