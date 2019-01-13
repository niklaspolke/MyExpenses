CREATE TABLE sequence (
    seq_name    VARCHAR(40) PRIMARY KEY,
    seq_number  INTEGER NOT NULL
);
INSERT INTO sequence(seq_name, seq_number) VALUES ('ID_GENERATOR', 1000);

CREATE TABLE account (
    id          INTEGER PRIMARY KEY,
    login       VARCHAR(40) NOT NULL UNIQUE,
    password    VARCHAR(40) NOT NULL,
    budget      DOUBLE
);

CREATE TABLE category (
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(40),
    account_id  INTEGER NOT NULL,
    CONSTRAINT fk_category_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);

CREATE TABLE expense (
    id          INTEGER PRIMARY KEY,
    day         DATE NOT NULL,
    amount      DOUBLE NOT NULL,
    reason      VARCHAR(40),
    monthly     BOOLEAN NOT NULL,
    income      BOOLEAN NOT NULL,
    category_id INTEGER NOT NULL,
    account_id  INTEGER NOT NULL,
    CONSTRAINT fk_expense_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE,
    CONSTRAINT fk_expense_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);

commit;
