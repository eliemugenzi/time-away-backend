-- Drop foreign key constraints first
ALTER TABLE leave_requests DROP CONSTRAINT IF EXISTS leave_requests_user_id_fkey;
ALTER TABLE leave_requests DROP CONSTRAINT IF EXISTS leave_requests_approver_id_fkey;
ALTER TABLE leave_balances DROP CONSTRAINT IF EXISTS leave_balances_user_id_fkey;
ALTER TABLE refresh_tokens DROP CONSTRAINT IF EXISTS refresh_tokens_user_id_fkey;
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_department_id_fkey;

-- Convert primary key columns to UUID
ALTER TABLE departments 
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id TYPE UUID USING (gen_random_uuid()),
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE users 
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id TYPE UUID USING (gen_random_uuid()),
    ALTER COLUMN id SET DEFAULT gen_random_uuid(),
    ALTER COLUMN department_id TYPE UUID USING (gen_random_uuid());

ALTER TABLE leave_requests 
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id TYPE UUID USING (gen_random_uuid()),
    ALTER COLUMN id SET DEFAULT gen_random_uuid(),
    ALTER COLUMN user_id TYPE UUID USING (gen_random_uuid()),
    ALTER COLUMN approver_id TYPE UUID USING CASE WHEN approver_id IS NOT NULL THEN gen_random_uuid() ELSE NULL END;

ALTER TABLE leave_balances 
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id TYPE UUID USING (gen_random_uuid()),
    ALTER COLUMN id SET DEFAULT gen_random_uuid(),
    ALTER COLUMN user_id TYPE UUID USING (gen_random_uuid());

ALTER TABLE refresh_tokens 
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id TYPE UUID USING (gen_random_uuid()),
    ALTER COLUMN id SET DEFAULT gen_random_uuid(),
    ALTER COLUMN user_id TYPE UUID USING (gen_random_uuid());

-- Add back foreign key constraints
ALTER TABLE users 
    ADD CONSTRAINT users_department_id_fkey 
    FOREIGN KEY (department_id) REFERENCES departments(id);

ALTER TABLE leave_requests 
    ADD CONSTRAINT leave_requests_user_id_fkey 
    FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE leave_requests 
    ADD CONSTRAINT leave_requests_approver_id_fkey 
    FOREIGN KEY (approver_id) REFERENCES users(id);

ALTER TABLE leave_balances 
    ADD CONSTRAINT leave_balances_user_id_fkey 
    FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE refresh_tokens 
    ADD CONSTRAINT refresh_tokens_user_id_fkey 
    FOREIGN KEY (user_id) REFERENCES users(id); 