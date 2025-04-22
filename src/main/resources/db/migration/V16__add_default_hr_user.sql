-- Create leave_type enum if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'leave_type') THEN
        CREATE TYPE leave_type AS ENUM ('ANNUAL', 'SICK', 'MATERNITY');
    END IF;
END $$;

-- Get HR Department ID
DO $$
DECLARE
    hr_department_id UUID;
    hr_job_title_id UUID;
BEGIN
    -- Get the HR Department ID
    SELECT id INTO hr_department_id
    FROM departments
    WHERE name = 'Human Resources' AND is_deleted = false;

    -- Get the HR Manager Job Title ID
    SELECT id INTO hr_job_title_id
    FROM job_titles
    WHERE name = 'HR Manager' AND is_deleted = false;

    -- Insert default HR user if not exists
    -- Password is 'Hr@Manager123' hashed with BCrypt
    INSERT INTO users (
        id,
        first_name,
        last_name,
        email,
        password,
        department_id,
        job_title_id,
        role,
        joining_date,
        created_at,
        updated_at,
        is_deleted
    )
    SELECT
        gen_random_uuid(),
        'HR',
        'Manager',
        'hr.manager@timeaway.com',
        '$2a$10$3.Z6NB5pDrHPXgkiTlGsUOyAm7gffrPY6VSGLBbQeiPZEIZX0XRjG', -- Hashed password for 'Hr@Manager123'
        hr_department_id,
        hr_job_title_id,
        'ROLE_HR',
        NOW(),
        NOW(),
        NOW(),
        false
    WHERE NOT EXISTS (
        SELECT 1 FROM users WHERE email = 'hr.manager@timeaway.com'
    );

    -- Initialize leave balances for the HR user
    INSERT INTO leave_balances (
        id,
        user_id,
        type,
        total_days,
        used_days,
        remaining_days,
        year,
        created_at,
        updated_at,
        is_deleted
    )
    SELECT
        gen_random_uuid(),
        u.id,
        leave_type,
        CASE 
            WHEN leave_type = 'ANNUAL' THEN 21
            WHEN leave_type = 'SICK' THEN 10
            WHEN leave_type = 'MATERNITY' THEN 90
        END,
        0,
        CASE 
            WHEN leave_type = 'ANNUAL' THEN 21
            WHEN leave_type = 'SICK' THEN 10
            WHEN leave_type = 'MATERNITY' THEN 90
        END,
        EXTRACT(YEAR FROM NOW()),
        NOW(),
        NOW(),
        false
    FROM users u
    CROSS JOIN (
        SELECT unnest(ARRAY['ANNUAL', 'SICK', 'MATERNITY']) as leave_type
    ) types
    WHERE u.email = 'hr.manager@timeaway.com'
    AND NOT EXISTS (
        SELECT 1
        FROM leave_balances lb
        WHERE lb.user_id = u.id
        AND lb.type = types.leave_type
        AND lb.year = EXTRACT(YEAR FROM NOW())
    );
END $$; 