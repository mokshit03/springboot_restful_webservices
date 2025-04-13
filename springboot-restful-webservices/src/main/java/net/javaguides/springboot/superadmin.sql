INSERT INTO roles (role_id, role_name, description, displayname)
SELECT 1, 'ROLE_SERVICE_ADMIN', 'Service account with admin permissions', 'SERVICE ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role_name = 'Service_Admin');
 
INSERT INTO users (user_id, username, email, firstname, lastname, status, version, password)
SELECT 1,'serviceadmin', 'sadmin@gmail.com', 'Service', 'Admin', 'active', 1, '$2a$12$K7UMOmGfVHX7MSKN8NAN8eleFZBHdOKYaDP1RhBQeSFf./V3d0j02'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'serviceadmin');
 
INSERT INTO user_roles (user_id, role_id)
SELECT 1, 1
WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = 1 AND role_id = 1);