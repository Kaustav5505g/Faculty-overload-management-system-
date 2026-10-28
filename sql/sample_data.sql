/*
 * sample_data.sql — demo data for Faculty Overload Management System.
 * Run after sql/schema.sql (which creates the database and tables).
 * Must match data.js exactly: same IDs, names, types, hours, and assignments.
 */
USE faculty_workload_db;

-- ─── Faculty ───────────────────────────────────────────────────────────────
-- Designation: 'Professor' (max 14 h) or 'Assistant Professor' (max 18 h)
-- Limits are enforced by the Java model (Professor.java, AssistantProfessor.java)
INSERT INTO faculty (faculty_id, name, email, designation, max_hours) VALUES
  (1, 'Dr. Sharma',             'sharma@example.edu',           'Professor',           14),
  (2, 'Prof. Abhishek Dwivedi', 'abhishek.dwivedi@example.edu', 'Professor',           14),
  (3, 'Prof. Pankaj Pandey',    'pankaj.pandey@example.edu',    'Professor',           14),
  (4, 'Dr. Meera Iyer',         'meera.iyer@example.edu',       'Assistant Professor', 18),
  (5, 'Dr. Rohan Singh',        'rohan.singh@example.edu',      'Assistant Professor', 18),
  (6, 'Dr. Anita Nair',         'anita.nair@example.edu',       'Professor',           14);

-- ─── Subjects ──────────────────────────────────────────────────────────────
-- subject_type must be 'LECTURE' or 'LAB' (checked by Subject.java constructor)
-- LAB subjects count as weekly_hours * 2 in workload calculations (Main.java lambda)
INSERT INTO subjects (subject_id, subject_code, title, credits, weekly_hours, subject_type) VALUES
  (1, 'CS101', 'Java Programming',   4, 4, 'LECTURE'),
  (2, 'CS102', 'Database Systems',   3, 3, 'LECTURE'),
  (3, 'CS103', 'Database Lab',       2, 4, 'LAB'),
  (4, 'CS104', 'Operating Systems',  3, 3, 'LECTURE'),
  (5, 'CS105', 'Computer Networks',  3, 3, 'LECTURE'),
  (6, 'CS106', 'Programming Lab',    2, 3, 'LAB');

-- ─── Existing assignments (semester 3, academic year 2026-2027) ────────────
-- Dr. Sharma:             CS101 (4 h) + CS103 LAB (4×2=8 h) = 12 h  of 14 h limit
-- Prof. Abhishek Dwivedi: CS102 (3 h)                        = 3 h  of 14 h limit
-- Dr. Meera Iyer:         CS106 LAB (3×2=6 h)                = 6 h  of 18 h limit
-- All other faculty have 0 h assigned in semester 3, 2026-2027
INSERT INTO workload_assignments (faculty_id, subject_id, semester, academic_year) VALUES
  (1, 1, 3, '2026-2027'),  -- Dr. Sharma       → CS101 Java Programming
  (1, 3, 3, '2026-2027'),  -- Dr. Sharma       → CS103 Database Lab (LAB)
  (2, 2, 3, '2026-2027'),  -- Prof. A. Dwivedi → CS102 Database Systems
  (4, 6, 3, '2026-2027');  -- Dr. Meera Iyer   → CS106 Programming Lab (LAB)
