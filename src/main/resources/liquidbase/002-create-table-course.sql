-- liquibase formatted sql
-- changeset author: SHEOMM description: create table course

CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NULL,
    allow_only_registered BOOLEAN NOT NULL,
    manager_id BIGINT NOT NULL,
);

CREATE INDEX idx_course_manager_id ON course(manager_id);
