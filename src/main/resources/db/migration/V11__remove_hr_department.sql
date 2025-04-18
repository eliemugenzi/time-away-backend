-- Update any job titles from HR department to Human Resources department
UPDATE job_titles 
SET department_id = (SELECT id FROM departments WHERE name = 'Human Resources' AND is_deleted = false)
WHERE department_id = (SELECT id FROM departments WHERE name = 'HR' AND is_deleted = false);

-- Update any users from HR department to Human Resources department
UPDATE users 
SET department_id = (SELECT id FROM departments WHERE name = 'Human Resources' AND is_deleted = false)
WHERE department_id = (SELECT id FROM departments WHERE name = 'HR' AND is_deleted = false);

-- Soft delete the HR department
UPDATE departments 
SET is_deleted = true, 
    updated_at = NOW()
WHERE name = 'HR'; 