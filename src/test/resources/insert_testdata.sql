INSERT INTO account(id, login, password, budget) VALUES (1, 'test1', '5f4dcc3b5aa765d61d8327deb882cf99', null);
INSERT INTO account(id, login, password, budget) VALUES (10, 'test2', '5f4dcc3b5aa765d61d8327deb882cf99', 400);

INSERT INTO category(id, name, account_id) VALUES (11, 'food', 1);
INSERT INTO category(id, name, account_id) VALUES (12, 'luxury', 1);
INSERT INTO category(id, name, account_id) VALUES (13, 'sports', 1);
INSERT INTO category(id, name, account_id) VALUES (14, 'income', 1);

INSERT INTO expense(id, day, amount, reason, monthly, income, budget, category_id, account_id) VALUES (101, '2015-05-01', 5.5, 'burger', false, false, true, 11, 1);
INSERT INTO expense(id, day, amount, reason, monthly, income, budget, category_id, account_id) VALUES (102, '2015-05-10', 700, 'jewels', false, false, false, 12, 1);
INSERT INTO expense(id, day, amount, reason, monthly, income, budget, category_id, account_id) VALUES (103, '2015-05-21', 3.55, 'french fries', false, false, false, 11, 1);
INSERT INTO expense(id, day, amount, reason, monthly, income, budget, category_id, account_id) VALUES (104, '2015-06-03', 80, 'watch', false, false, false, 12, 1);
INSERT INTO expense(id, day, amount, reason, monthly, income, budget, category_id, account_id) VALUES (105, '2015-06-04', 800, 'flat', true, false, false, 12, 1);
INSERT INTO expense(id, day, amount, reason, monthly, income, budget, category_id, account_id) VALUES (106, '2015-06-01', 2000, 'work', true, true, false, 14, 1);

commit;
