-- Update roles for all users in the Human Resources department to ROLE_HR
UPDATE users
SET role = 'ROLE_HR'
WHERE department_id = (
    SELECT id 
    FROM departments 
    WHERE name = 'Human Resources' 
    AND is_deleted = false
); 