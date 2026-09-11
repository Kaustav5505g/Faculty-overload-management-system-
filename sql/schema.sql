CREATE DATABASE IF NOT EXISTS faculty_workload_db;
USE faculty_workload_db;

-- Faculty Master Table
CREATE TABLE faculty (
    faculty_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    designation VARCHAR(50) NOT NULL,
    max_hours INT NOT NULL
);

-- Subjects Master Table
CREATE TABLE subjects (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    weekly_hours INT NOT NULL,
    subject_type VARCHAR(20) NOT NULL
);

-- Workload Assignments Junction Table (3NF)
CREATE TABLE workload_assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    faculty_id INT NOT NULL,
    subject_id INT NOT NULL,
    semester INT NOT NULL,
    academic_year VARCHAR(10) NOT NULL,
    CONSTRAINT fk_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE CASCADE,
    CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    CONSTRAINT unique_course_section UNIQUE (subject_id, semester, academic_year)
);