-- liquibase formatted sql
-- changeset SHEOMM:003

CREATE TABLE section (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     is_deactivated BOOLEAN NOT NULL,
     course_id BIGINT NOT NULL,
     created_at TIMESTAMP(6) NOT NULL,
     updated_at TIMESTAMP(6) NOT NULL
);

CREATE INDEX idx_section_course_id ON section(course_id, created_at);
