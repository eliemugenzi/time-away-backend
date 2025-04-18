INSERT INTO departments (id, name, description, created_at, updated_at, is_deleted)
VALUES
    -- Technology Department
    (gen_random_uuid(), 'Technology', 'Responsible for software development, IT infrastructure, and technical operations', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
    
    -- Human Resources Department
    (gen_random_uuid(), 'Human Resources', 'Manages employee relations, recruitment, training, and workplace policies', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
    
    -- Marketing Department
    (gen_random_uuid(), 'Marketing', 'Handles brand management, marketing campaigns, and market research', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
    
    -- Sales Department
    (gen_random_uuid(), 'Sales', 'Manages sales operations, client relationships, and revenue generation', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
    
    -- Finance Department
    (gen_random_uuid(), 'Finance', 'Oversees financial planning, accounting, and financial reporting', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
    
    -- Operations Department
    (gen_random_uuid(), 'Operations', 'Manages day-to-day business operations and organizational efficiency', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
    
    -- Customer Service Department
    (gen_random_uuid(), 'Customer Service', 'Provides customer support, handles inquiries, and ensures customer satisfaction', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false); 