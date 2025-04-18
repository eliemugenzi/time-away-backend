INSERT INTO departments (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid(), 'Super Administration', 'Default super administration department', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING; 