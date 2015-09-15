DROP TABLE sequences;
DROP TABLE expense;

CREATE TABLE sequences (
    seq_name    VARCHAR(40) PRIMARY KEY,
    seq_number  INTEGER NOT NULL
);
INSERT INTO sequences (seq_name, seq_number) values ('ID_GENERATOR', 1000);

CREATE TABLE expense (
    id          INTEGER PRIMARY KEY,
    date        TEXT NOT NULL,
    amount      DOUBLE NOT NULL,
    reason      TEXT
);
INSERT INTO expense(id, date, amount, reason) values (3, '2015.05.01', 0, 'nothing');
INSERT INTO expense(id, date, amount, reason) values (4, '2015.06.10', 9, 'Café Kraft');
INSERT INTO expense(id, date, amount, reason) values (5, '2015.07.20', 18.5, 'Kristall Palm Beach');
