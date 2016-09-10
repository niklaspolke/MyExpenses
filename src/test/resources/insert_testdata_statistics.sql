INSERT INTO account(id, login, password) VALUES (2, 'statistics', '5f4dcc3b5aa765d61d8327deb882cf99');

INSERT INTO category(id, name, account_id) VALUES (21, 'sports', 2);
INSERT INTO category(id, name, account_id) VALUES (22, 'food', 2);
INSERT INTO category(id, name, account_id) VALUES (23, 'luxury', 2);

INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (201, '2015-05-01', 11, 'climbing', 21, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (202, '2015-05-02', 12, 'supermarket', 22, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (203, '2015-05-03', 13, 'jewels', 23, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (204, '2015-05-04', 14, 'climbing', 23, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (205, '2015-06-01', 15, 'jewels', 23, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (206, '2015-06-02', 16, 'supermarket', 22, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (207, '2015-06-03', 17, 'climbing', 21, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (208, '2015-06-04', 18, 'climbing', 21, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (209, '2015-06-04', 19, 'supermarket', 22, 2);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (210, '2015-07-04', 20, 'climbing', 21, 2);

commit;
