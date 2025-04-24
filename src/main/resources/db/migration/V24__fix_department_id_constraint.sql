-- First update any NULL department_ids with the user's department
UPDATE leave_requests lr
SET department_id = (
    SELECT department_id 
    FROM users u 
    WHERE u.id = lr.user_id
)
WHERE lr.department_id IS NULL;
