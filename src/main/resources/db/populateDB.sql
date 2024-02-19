DELETE FROM meals;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, description, calories, date_time)
VALUES (100000, 'завтрак', 300, '2004-10-19 10:23:54+02'),
       (100000, 'обед', 500, '2004-10-19 15:20:01+02'),
       (100000, 'ужин', 1000, '2004-10-19 21:20:01+02'),
       (100000, 'завтрак', 500, '2004-10-20 11:23:54+02'),
       (100000, 'обед', 1000, '2004-10-20 14:21:01+02'),
       (100000, 'ужин', 700, '2004-10-20 19:20:01+02'),
       (100001, 'завтрак', 500, '2004-11-20 11:23:54+02'),
       (100001, 'обед', 1000, '2004-11-20 14:21:01+02'),
       (100001, 'ужин', 700, '2004-11-20 19:20:01+02');
