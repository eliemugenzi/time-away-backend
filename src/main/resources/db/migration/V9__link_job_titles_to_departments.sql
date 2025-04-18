-- Add department_id column as nullable first
ALTER TABLE job_titles
ADD COLUMN department_id UUID REFERENCES departments(id);

-- Link existing job titles to appropriate departments
UPDATE job_titles SET department_id = (
    SELECT id FROM departments WHERE name = 'Technology'
)
WHERE name IN (
    'Software Engineer',
    'Senior Software Engineer',
    'DevOps Engineer',
    'QA Engineer',
    'System Administrator'
);

UPDATE job_titles SET department_id = (
    SELECT id FROM departments WHERE name = 'Human Resources'
)
WHERE name IN (
    'HR Manager',
    'HR Specialist',
    'Training Coordinator'
);

UPDATE job_titles SET department_id = (
    SELECT id FROM departments WHERE name = 'Marketing'
)
WHERE name IN (
    'Marketing Manager',
    'Marketing Specialist',
    'Content Writer',
    'Digital Marketing Specialist'
);

UPDATE job_titles SET department_id = (
    SELECT id FROM departments WHERE name = 'Sales'
)
WHERE name IN (
    'Sales Manager',
    'Sales Representative',
    'Account Executive',
    'Business Development Manager'
);

UPDATE job_titles SET department_id = (
    SELECT id FROM departments WHERE name = 'Finance'
)
WHERE name IN (
    'Financial Analyst',
    'Accountant',
    'Finance Manager',
    'Financial Controller'
);

UPDATE job_titles SET department_id = (
    SELECT id FROM departments WHERE name = 'Operations'
)
WHERE name IN (
    'Operations Manager',
    'Business Analyst',
    'Quality Manager',
    'Administrative Assistant',
    'Project Manager'
);

UPDATE job_titles SET department_id = (
    SELECT id FROM departments WHERE name = 'Customer Service'
)
WHERE name IN (
    'Customer Service Manager',
    'Customer Service Representative',
    'Support Specialist'
);

-- Assign any remaining unlinked job titles to Super Administration department
UPDATE job_titles 
SET department_id = (SELECT id FROM departments WHERE name = 'Super Administration')
WHERE department_id IS NULL;

-- Now we can safely set the column as NOT NULL
ALTER TABLE job_titles
ALTER COLUMN department_id SET NOT NULL; 