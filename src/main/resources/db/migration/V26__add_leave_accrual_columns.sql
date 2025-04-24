-- Add columns for tracking monthly accrual and carried forward days
ALTER TABLE leave_balances
    ADD COLUMN monthly_accrual_rate DECIMAL(5,2),
    ADD COLUMN carried_forward_days INTEGER DEFAULT 0,
    ADD COLUMN last_accrual_date DATE;

-- Set default monthly accrual rate for ANNUAL leave type
UPDATE leave_balances
SET monthly_accrual_rate = 1.66,
    last_accrual_date = NOW()
WHERE type = 'ANNUAL';

-- Initialize carried_forward_days to 0 for all records
UPDATE leave_balances
SET carried_forward_days = 0
WHERE carried_forward_days IS NULL; 