-- Update department_id in leave_requests table based on user's department
UPDATE leave_requests lr
SET department_id = (
    SELECT department_id 
    FROM users u 
    WHERE u.id = lr.user_id
)
WHERE lr.department_id IS NULL; 