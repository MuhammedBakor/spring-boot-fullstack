/*CREATE SEQUENCE customer_id_sequence;
   id BIGINT DEFAULT nextval('customer_id_sequence') PRIMARY KEY,*/

CREATE TABLE customer(
                         id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL ,
                         email TEXT NOT NULL UNIQUE,
                         password TEXT NOT NULL,
                         gender TEXT NOT NULL,
                         age INT NOT NULL
);