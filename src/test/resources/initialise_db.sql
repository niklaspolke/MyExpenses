DROP SCHEMA PUBLIC CASCADE;
DROP TABLE expense;
DROP TABLE category;
DROP TABLE account;
DROP TABLE sequence;

CREATE TABLE sequence (
    seq_name    VARCHAR(40) PRIMARY KEY,
    seq_number  INTEGER NOT NULL
);
INSERT INTO sequence(seq_name, seq_number) VALUES ('ID_GENERATOR', 1000);

CREATE TABLE account (
    id          INTEGER PRIMARY KEY,
    login       VARCHAR(40) NOT NULL UNIQUE,
    password    VARCHAR(40) NOT NULL
);
INSERT INTO account(id, login, password) VALUES (1, 'test', '5f4dcc3b5aa765d61d8327deb882cf99');

CREATE TABLE category (
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(40),
    account_id  INTEGER NOT NULL,
    CONSTRAINT fk_category_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);
INSERT INTO category(id, name, account_id) VALUES (11, 'food', 1);
INSERT INTO category(id, name, account_id) VALUES (12, 'luxury', 1);

CREATE TABLE expense (
    id          INTEGER PRIMARY KEY,
    day         DATE NOT NULL,
    amount      DOUBLE NOT NULL,
    reason      VARCHAR(40),
    category_id INTEGER NOT NULL,
    account_id  INTEGER NOT NULL,
    CONSTRAINT fk_expense_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE,
    CONSTRAINT fk_expense_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (101, '2015-05-01', 5.5, 'burger', 11, 1);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (102, '2015-06-10', 700, 'jewels', 12, 1);
INSERT INTO expense(id, day, amount, reason, category_id, account_id) VALUES (103, '2015-07-20', 3.55, 'french fries', 11, 1);

commit;
