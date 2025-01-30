-- liquibase formatted sql
-- changeset author: SHEOMM description: create table manager

CREATE TABLE manager (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE INDEX idx_email ON manager(email);
