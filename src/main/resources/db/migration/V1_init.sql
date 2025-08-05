CREATE TABLE donor (
                       id SERIAL PRIMARY KEY,
                       name TEXT NOT NULL,
                       email TEXT NOT NULL
);

CREATE TABLE payment (
                         id SERIAL PRIMARY KEY,
                         date TIMESTAMP NOT NULL,
                         amount BIGINT NOT NULL,
                         method TEXT,
                         psp_payment_id TEXT,
                         psp_type TEXT,
                         payer_email TEXT,
                         status TEXT
);

CREATE TABLE donation (
                          id SERIAL PRIMARY KEY,
                          donor_id INT REFERENCES donor(id),
                          payment_id INT REFERENCES payment(id)
);

CREATE TABLE beneficiary (
                             id SERIAL PRIMARY KEY,
                             name TEXT NOT NULL,
                             email TEXT NOT NULL
);

CREATE TABLE help (
                      id SERIAL PRIMARY KEY,
                      beneficiary_id INT REFERENCES beneficiary(id),
                      payment_id INT REFERENCES payment(id),
                      description TEXT
);
