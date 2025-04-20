-- Update remaining_days to be total_days - used_days for all existing records
UPDATE leave_balances
SET remaining_days = total_days - used_days
WHERE remaining_days = 0; 