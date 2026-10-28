/**
 * data.js — static demo data for the Faculty Workload Check UI.
 * Must stay in sync with sql/sample_data.sql exactly.
 *
 * Designation limits mirror Professor (14) and AssistantProfessor (18)
 * from the Java model classes.
 */

const FACULTY = [
  { id: 1, name: "Dr. Sharma",             designation: "Professor",           maxHours: 14 },
  { id: 2, name: "Prof. Abhishek Dwivedi", designation: "Professor",           maxHours: 14 },
  { id: 3, name: "Prof. Pankaj Pandey",    designation: "Professor",           maxHours: 14 },
  { id: 4, name: "Dr. Meera Iyer",         designation: "Assistant Professor", maxHours: 18 },
  { id: 5, name: "Dr. Rohan Singh",        designation: "Assistant Professor", maxHours: 18 },
  { id: 6, name: "Dr. Anita Nair",         designation: "Professor",           maxHours: 14 },
];

const SUBJECTS = [
  { id: 1, code: "CS101", title: "Java Programming",    type: "LECTURE", weeklyHours: 4, credits: 4 },
  { id: 2, code: "CS102", title: "Database Systems",    type: "LECTURE", weeklyHours: 3, credits: 3 },
  { id: 3, code: "CS103", title: "Database Lab",        type: "LAB",     weeklyHours: 4, credits: 2 },
  { id: 4, code: "CS104", title: "Operating Systems",   type: "LECTURE", weeklyHours: 3, credits: 3 },
  { id: 5, code: "CS105", title: "Computer Networks",   type: "LECTURE", weeklyHours: 3, credits: 3 },
  { id: 6, code: "CS106", title: "Programming Lab",     type: "LAB",     weeklyHours: 3, credits: 2 },
];

/**
 * Existing assignments.
 * Each entry: { facultyId, subjectId, semester, academicYear }
 * Semester 3, 2026-2027 only per the spec.
 */
const ASSIGNMENTS = [
  { facultyId: 1, subjectId: 1, semester: 3, academicYear: "2026-2027" }, // Dr. Sharma — CS101
  { facultyId: 1, subjectId: 3, semester: 3, academicYear: "2026-2027" }, // Dr. Sharma — CS103 (LAB)
  { facultyId: 2, subjectId: 2, semester: 3, academicYear: "2026-2027" }, // Prof. Abhishek Dwivedi — CS102
  { facultyId: 4, subjectId: 6, semester: 3, academicYear: "2026-2027" }, // Dr. Meera Iyer — CS106 (LAB)
];
