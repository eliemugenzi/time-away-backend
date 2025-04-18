-- Add is_deleted column to users table
ALTER TABLE users
    ADD COLUMN is_deleted BOOLEAN DEFAULT false; 