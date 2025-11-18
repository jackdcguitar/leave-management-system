INSERT IGNORE INTO roles(name) VALUES ('ROLE_USER'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

INSERT IGNORE INTO users(id, username, password, display_name, enabled) VALUES
  (1, 'admin', '{noop}admin123', '系統管理員', 1),
  (2, 'manager', '{noop}manager123', '部門主管', 1),
  (3, 'user', '{noop}user123', '一般員工', 1);

INSERT IGNORE INTO users_roles(user_id, role_id) SELECT 1, id FROM roles WHERE name='ROLE_ADMIN';
INSERT IGNORE INTO users_roles(user_id, role_id) SELECT 1, id FROM roles WHERE name='ROLE_USER';
INSERT IGNORE INTO users_roles(user_id, role_id) SELECT 2, id FROM roles WHERE name='ROLE_MANAGER';
INSERT IGNORE INTO users_roles(user_id, role_id) SELECT 2, id FROM roles WHERE name='ROLE_USER';
INSERT IGNORE INTO users_roles(user_id, role_id) SELECT 3, id FROM roles WHERE name='ROLE_USER';
