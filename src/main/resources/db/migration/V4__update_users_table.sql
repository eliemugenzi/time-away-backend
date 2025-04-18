-- Add new columns
ALTER TABLE users
    ADD COLUMN joining_date TIMESTAMP,
    ADD COLUMN active BOOLEAN DEFAULT true,
    ALTER COLUMN department_id DROP NOT NULL;

-- Set joining_date for existing users
UPDATE users
SET joining_date = created_at
WHERE joining_date IS NULL;

-- Make joining_date not null after setting default values
ALTER TABLE users
    ALTER COLUMN joining_date SET NOT NULL; 