-- Add default NOW() for created_at and updated_at columns in all tables

-- Users table
ALTER TABLE users 
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Leave requests table
ALTER TABLE leave_requests 
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Departments table
ALTER TABLE departments 
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Job titles table
ALTER TABLE job_titles 
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Leave balances table
ALTER TABLE leave_balances 
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at SET DEFAULT NOW();


-- Update any existing NULL timestamps
UPDATE users SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL OR updated_at IS NULL;
UPDATE leave_requests SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL OR updated_at IS NULL;
UPDATE departments SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL OR updated_at IS NULL;
UPDATE job_titles SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL OR updated_at IS NULL;
UPDATE leave_balances SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL OR updated_at IS NULL;
