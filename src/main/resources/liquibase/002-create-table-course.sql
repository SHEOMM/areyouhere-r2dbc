-- liquibase formatted sql
-- changeset SHEOMM:002

CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NULL DEFAULT NULL,
    allow_only_registered BOOLEAN NOT NULL,
    manager_id BIGINT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

CREATE INDEX idx_course_manager_id ON course(manager_id, created_at);
