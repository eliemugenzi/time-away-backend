-- Delete leave balances with unused leave types
DELETE FROM leave_balances
WHERE type NOT IN ('ANNUAL', 'SICK', 'MATERNITY', 'COMPASSIONATE');

-- Delete leave requests with unused leave types
DELETE FROM leave_requests
WHERE type NOT IN ('ANNUAL', 'SICK', 'MATERNITY', 'COMPASSIONATE'); 