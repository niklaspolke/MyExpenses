INSERT INTO account(id, login, password) VALUES (2, 'statistics', '5f4dcc3b5aa765d61d8327deb882cf99');

INSERT INTO category(id, name, account_id) VALUES (21, 'sports', 2);
INSERT INTO category(id, name, account_id) VALUES (22, 'food', 2);
INSERT INTO category(id, name, account_id) VALUES (23, 'luxury', 2);
INSERT INTO category(id, name, account_id) VALUES (24, 'income', 2);

INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (201, '2015-05-01', 11, 'climbing', 21, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (202, '2015-05-02', 12, 'supermarket', 22, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (203, '2015-05-03', 13, 'jewels', 23, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (204, '2015-05-04', 14, 'climbing', 23, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (205, '2015-06-01', 15, 'jewels', 23, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (206, '2015-06-02', 16, 'supermarket', 22, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (207, '2015-06-03', 17, 'climbing', 21, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (208, '2015-06-04', 18, 'climbing', 21, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (209, '2015-06-04', 19, 'supermarket', 22, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (210, '2015-07-04', 20, 'climbing', 21, 2, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (211, '2015-05-01', 2000, 'work', 24, 2, true, true, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (212, '2015-05-01', 21, 'sports club membership', 21, 2, true, false, false);

INSERT INTO account(id, login, password) VALUES (3, 'statistics2', '5f4dcc3b5aa765d61d8327deb882cf99');

INSERT INTO category(id, name, account_id) VALUES (31, 'sports', 3);
INSERT INTO category(id, name, account_id) VALUES (32, 'food', 3);
INSERT INTO category(id, name, account_id) VALUES (33, 'luxury', 3);

INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (320, '2016-01-31', 45, 'aaaa', 31, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (321, '2016-01-30', 22, 'bbbb', 31, 3, false, false, true);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (322, '2016-01-29', 33, 'cccc', 31, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (323, '2016-01-28', 44, 'dddd', 31, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (324, '2016-01-27', 11, 'eeee', 31, 3, false, false, true);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (325, '2016-01-26', 1, 'ffff', 31, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (326, '2016-01-25', 2, 'gggg', 31, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (327, '2016-01-24', 3, 'hhhh', 31, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (328, '2016-01-23', 4, 'iiii', 31, 3, false, false, true);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (329, '2016-01-22', 5, 'jjjj', 31, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (330, '2016-01-21', 6, 'kkkk', 31, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (331, '2016-01-20', 7, 'jjjj', 32, 3, false, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (332, '2016-01-19', 8, 'kkkk', 33, 3, false, false, true);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (333, '2016-01-15', 999, 'monthly expense', 31, 3, true, false, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (334, '2016-01-03', 999, 'monthly income', 31, 3, true, true, false);
INSERT INTO expense(id, day, amount, reason, category_id, account_id, monthly, income, budget) VALUES (335, '2016-01-05', 999, 'income', 31, 3, false, true, false);


commit;
