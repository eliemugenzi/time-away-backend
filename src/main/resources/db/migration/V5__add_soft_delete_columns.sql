-- Add is_deleted column to departments table if it doesn't exist
DO $$ 
BEGIN 
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                  WHERE table_name = 'departments' AND column_name = 'is_deleted') THEN
        ALTER TABLE departments
            ADD COLUMN is_deleted BOOLEAN DEFAULT false;
    END IF;
END $$;

-- Add is_deleted column to leave_requests table
ALTER TABLE leave_requests
    ADD COLUMN is_deleted BOOLEAN DEFAULT false;

-- Add is_deleted column to leave_balances table
ALTER TABLE leave_balances
    ADD COLUMN is_deleted BOOLEAN DEFAULT false;

-- Add is_deleted column to refresh_tokens table
ALTER TABLE refresh_tokens
    ADD COLUMN is_deleted BOOLEAN DEFAULT false; 