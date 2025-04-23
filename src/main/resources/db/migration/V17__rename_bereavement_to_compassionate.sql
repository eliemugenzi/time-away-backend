-- First add all missing leave types to the enum
ALTER TYPE leave_type ADD VALUE IF NOT EXISTS 'PATERNITY';
ALTER TYPE leave_type ADD VALUE IF NOT EXISTS 'BEREAVEMENT';
ALTER TYPE leave_type ADD VALUE IF NOT EXISTS 'UNPAID';

-- Then rename BEREAVEMENT to COMPASSIONATE
ALTER TYPE leave_type RENAME VALUE 'BEREAVEMENT' TO 'COMPASSIONATE';

-- Update existing leave balances
UPDATE leave_balances
SET type = 'COMPASSIONATE'
WHERE type = 'BEREAVEMENT';

-- Update existing leave requests
UPDATE leave_requests
SET type = 'COMPASSIONATE'
WHERE type = 'BEREAVEMENT';

-- Add COMPASSIONATE to leave_type enum
ALTER TYPE leave_type ADD VALUE IF NOT EXISTS 'COMPASSIONATE'; 