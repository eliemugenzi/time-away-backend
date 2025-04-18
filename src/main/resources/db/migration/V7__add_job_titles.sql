CREATE TABLE job_titles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

ALTER TABLE users
ADD COLUMN job_title_id UUID REFERENCES job_titles(id);

-- Add default job titles
INSERT INTO job_titles (name, description, created_at, updated_at)
VALUES
    -- Technology
    ('Software Engineer', 'Develops and maintains software applications', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Senior Software Engineer', 'Leads software development projects and mentors junior developers', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('DevOps Engineer', 'Manages deployment, automation, and infrastructure', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('QA Engineer', 'Ensures software quality through testing and validation', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('System Administrator', 'Maintains and manages IT systems and infrastructure', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Management
    ('Project Manager', 'Manages project planning, execution, and delivery', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Product Manager', 'Oversees product development and strategy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Team Lead', 'Leads and manages team operations and performance', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Department Director', 'Provides strategic direction and oversight for department', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Human Resources
    ('HR Manager', 'Manages human resources operations and policies', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('HR Specialist', 'Handles specific HR functions like recruitment or benefits', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Training Coordinator', 'Organizes and manages employee training programs', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Marketing & Sales
    ('Marketing Manager', 'Develops and oversees marketing strategies', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Marketing Specialist', 'Implements marketing campaigns and initiatives', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Sales Manager', 'Leads sales team and develops sales strategies', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Sales Representative', 'Generates leads and closes sales deals', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Finance & Administration
    ('Financial Analyst', 'Analyzes financial data and prepares reports', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Accountant', 'Manages financial records and transactions', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Administrative Assistant', 'Provides administrative support to team or department', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Operations
    ('Operations Manager', 'Oversees daily business operations', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Business Analyst', 'Analyzes business processes and requirements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Quality Manager', 'Ensures compliance with quality standards', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Customer Service
    ('Customer Service Manager', 'Manages customer service operations', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Customer Service Representative', 'Handles customer inquiries and support', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 