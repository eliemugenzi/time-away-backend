ALTER TABLE leave_requests
ADD COLUMN department_id UUID;

-- Update existing records with department_id from users table
UPDATE leave_requests lr
SET department_id = u.department_id
FROM users u
WHERE lr.user_id = u.id;

-- Add foreign key constraint
ALTER TABLE leave_requests
ADD CONSTRAINT fk_leave_requests_department
FOREIGN KEY (department_id) REFERENCES departments(id);
