-- Make department_id column nullable in leave_requests table
ALTER TABLE leave_requests ALTER COLUMN department_id DROP NOT NULL; 