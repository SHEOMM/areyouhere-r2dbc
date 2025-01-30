-- liquibase formatted sql
-- changeset SHEOMM:005

CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attendee_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    status ENUM('ATTENDED', 'ABSENT', 'LATE') NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

CREATE INDEX idx_attendance_attendee_id ON attendance(attendee_id, status);
CREATE INDEX idx_course_created_at_1 ON attendance(section_id, status);
