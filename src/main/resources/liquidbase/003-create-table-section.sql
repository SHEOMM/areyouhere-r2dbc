-- liquibase formatted sql
-- changeset author: SHEOMM description: create table section

CREATE TABLE section (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     is_deactivated BOOLEAN NOT NULL,
     course_id BIGINT NOT NULL,
);

CREATE INDEX idx_section_course_id ON section(course_id);
