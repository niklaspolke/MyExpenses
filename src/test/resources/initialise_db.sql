DROP TABLE sequences;
DROP TABLE categories;
DROP TABLE expenses;

CREATE TABLE sequences (
    seq_name    VARCHAR(40) PRIMARY KEY,
    seq_number  INTEGER NOT NULL
);
INSERT INTO sequences(seq_name, seq_number) VALUES ('ID_GENERATOR', 1000);

CREATE TABLE categories (
    id          INTEGER PRIMARY KEY,
    name        TEXT
);
INSERT INTO categories(id, name) VALUES (11, 'food');
INSERT INTO categories(id, name) VALUES (12, 'luxury');

CREATE TABLE expenses (
    id          INTEGER PRIMARY KEY,
    date        TEXT NOT NULL,
    amount      DOUBLE NOT NULL,
    reason      TEXT,
    category_id INTEGER NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

INSERT INTO expenses(id, date, amount, reason, category_id) VALUES (3, '2015.05.01', 0, 'nothing', 11);
INSERT INTO expenses(id, date, amount, reason, category_id) VALUES (4, '2015.06.10', 9, 'Café Kraft', 12);
INSERT INTO expenses(id, date, amount, reason, category_id) VALUES (5, '2015.07.20', 18.5, 'Kristall Palm Beach', 12);
commit;
