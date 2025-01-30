-- liquibase formatted sql
-- changeset author: SHEOMM description: create table attendee

CREATE TABLE attendee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    note TEXT NULL,
    course_id BIGINT NOT NULL,
);

CREATE INDEX idx_attendee_course_id ON attendee(course_id, name);
