-- Insert test user (password is 'password' encrypted with BCrypt)
INSERT INTO users (email, password) VALUES 
('test@example.com', '$2a$10$XURPShQNCsLjp1ESc7la..Oq5EF6BWey0RuLYkyXeylu0QgJBRAhK')
ON CONFLICT (email) DO NOTHING;
