-- liquibase formatted sql
-- changeset SHEOMM:004

CREATE TABLE attendee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    note TEXT NULL DEFAULT NULL,
    course_id BIGINT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

CREATE INDEX idx_attendee_course_id ON attendee(course_id, name);
CREATE INDEX idx_course_created_at ON attendee(created_at)
