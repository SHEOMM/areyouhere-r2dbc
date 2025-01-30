-- liquibase formatted sql
-- changeset author: SHEOMM description: create table attendance

CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attendee_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    status ENUM('ATTENDED', 'ABSENT', 'LATE') NOT NULL,
);

CREATE INDEX idx_attendance_attendee_id ON attendance(attendee_id, status);
CREATE INDEX idx_attendance_section_id ON attendance(section_id, status);
