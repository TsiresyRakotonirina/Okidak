-- insertion données

-- role admin
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- user admin
INSERT INTO users (name, email, password) VALUES ('RAKOTONIRINA Andriatsiresy', 'tsiresy@gmail.com', '123');

-- relation
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);


