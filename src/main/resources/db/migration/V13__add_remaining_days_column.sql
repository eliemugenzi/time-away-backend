-- Add remaining_days column to leave_balances table
ALTER TABLE leave_balances
    ADD COLUMN remaining_days INTEGER NOT NULL DEFAULT 0; 