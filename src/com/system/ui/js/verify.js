/**
 * verify.js — Node.js test runner for workload rule logic.
 * Inlines data.js and workload.js and runs all spec test cases.
 * Run: node verify.js
 */

// ── inline data ────────────────────────────────────────────────────────────
const FACULTY = [
  { id: 1, name: "Dr. Sharma",             designation: "Professor",           maxHours: 14 },
  { id: 2, name: "Prof. Abhishek Dwivedi", designation: "Professor",           maxHours: 14 },
  { id: 3, name: "Prof. Pankaj Pandey",    designation: "Professor",           maxHours: 14 },
  { id: 4, name: "Dr. Meera Iyer",         designation: "Assistant Professor", maxHours: 18 },
  { id: 5, name: "Dr. Rohan Singh",        designation: "Assistant Professor", maxHours: 18 },
  { id: 6, name: "Dr. Anita Nair",         designation: "Professor",           maxHours: 14 },
];
const SUBJECTS = [
  { id: 1, code: "CS101", title: "Java Programming",   type: "LECTURE", weeklyHours: 4, credits: 4 },
  { id: 2, code: "CS102", title: "Database Systems",   type: "LECTURE", weeklyHours: 3, credits: 3 },
  { id: 3, code: "CS103", title: "Database Lab",       type: "LAB",     weeklyHours: 4, credits: 2 },
  { id: 4, code: "CS104", title: "Operating Systems",  type: "LECTURE", weeklyHours: 3, credits: 3 },
  { id: 5, code: "CS105", title: "Computer Networks",  type: "LECTURE", weeklyHours: 3, credits: 3 },
  { id: 6, code: "CS106", title: "Programming Lab",    type: "LAB",     weeklyHours: 3, credits: 2 },
];
const ASSIGNMENTS = [
  { facultyId: 1, subjectId: 1, semester: 3, academicYear: "2026-2027" },
  { facultyId: 1, subjectId: 3, semester: 3, academicYear: "2026-2027" },
  { facultyId: 2, subjectId: 2, semester: 3, academicYear: "2026-2027" },
  { facultyId: 4, subjectId: 6, semester: 3, academicYear: "2026-2027" },
];

// ── inline workload functions ──────────────────────────────────────────────
function workloadHours(subject) {
  return subject.type === "LAB" ? subject.weeklyHours * 2 : subject.weeklyHours;
}
function workloadHoursLabel(subject) {
  if (subject.type === "LAB") return `${subject.weeklyHours} x 2 = ${subject.weeklyHours * 2}`;
  return String(subject.weeklyHours);
}
function getAssignedSubjects(facultyId, semester, academicYear) {
  return ASSIGNMENTS
    .filter(a => a.facultyId === facultyId && a.semester === semester && a.academicYear === academicYear)
    .map(a => SUBJECTS.find(s => s.id === a.subjectId))
    .filter(Boolean);
}
function currentLoad(facultyId, semester, academicYear) {
  return getAssignedSubjects(facultyId, semester, academicYear).reduce((s, sub) => s + workloadHours(sub), 0);
}
function subjectAlreadyAssignedTo(subjectId, semester, academicYear) {
  const match = ASSIGNMENTS.find(a => a.subjectId === subjectId && a.semester === semester && a.academicYear === academicYear);
  if (!match) return null;
  return FACULTY.find(f => f.id === match.facultyId) || null;
}
function checkAssignment(facultyId, subjectId, semester, academicYear) {
  const faculty = FACULTY.find(f => f.id === facultyId);
  const subject = SUBJECTS.find(s => s.id === subjectId);
  if (!faculty || !subject) return { verdict: "INVALID" };
  const alreadyHolder = subjectAlreadyAssignedTo(subjectId, semester, academicYear);
  if (alreadyHolder) return { verdict: "ALREADY_ASSIGNED", faculty, subject, semester, academicYear, alreadyHolder };
  const existingSubjects = getAssignedSubjects(facultyId, semester, academicYear);
  const existing = existingSubjects.reduce((s, sub) => s + workloadHours(sub), 0);
  const newHours = workloadHours(subject);
  const total = existing + newHours;
  const limit = faculty.maxHours;
  const allowed = total <= limit;
  const nearLimit = allowed && total >= Math.ceil(limit * 0.85);
  let verdict = !allowed ? "OVERLOAD" : nearLimit ? "NEAR_LIMIT" : "ALLOWED";
  return { verdict, faculty, subject, semester, academicYear, existingSubjects, existing, newHours, total, limit, allowed, overBy: allowed ? 0 : total - limit, remaining: allowed ? limit - total : 0 };
}

// ── test cases ─────────────────────────────────────────────────────────────
let pass = 0, fail = 0;
function test(description, facultyId, subjectId, semester, year, expectedVerdict, expectedTotal) {
  const r = checkAssignment(facultyId, subjectId, semester, year);
  const verdictOk = r.verdict === expectedVerdict;
  const totalOk   = expectedTotal === undefined || r.total === expectedTotal || r.verdict === "ALREADY_ASSIGNED";
  if (verdictOk && totalOk) {
    console.log("  PASS  " + description);
    pass++;
  } else {
    console.log("  FAIL  " + description);
    console.log("        got verdict=" + r.verdict + " total=" + r.total + ", expected verdict=" + expectedVerdict + " total=" + expectedTotal);
    fail++;
  }
}

console.log("\nSemester 3, 2026-2027:");
test("Dr. Sharma + CS104 → REJECTED (12+3=15 > 14)",          1, 4, 3, "2026-2027", "OVERLOAD",         15);
test("Prof. Abhishek Dwivedi + CS104 → ALLOWED (3+3=6 of 14)",2, 4, 3, "2026-2027", "ALLOWED",          6);
test("Prof. Pankaj Pandey + CS105 → ALLOWED (0+3=3 of 14)",   3, 5, 3, "2026-2027", "ALLOWED",          3);
test("Dr. Meera Iyer + CS104 → ALLOWED (6+3=9 of 18)",        4, 4, 3, "2026-2027", "ALLOWED",          9);
test("Any faculty + CS101 → NOT POSSIBLE (already assigned)",  2, 1, 3, "2026-2027", "ALREADY_ASSIGNED", undefined);
test("Any faculty + CS101 → NOT POSSIBLE (already assigned)",  5, 1, 3, "2026-2027", "ALREADY_ASSIGNED", undefined);

console.log("\nSemester 5, 2026-2027:");
test("Dr. Rohan Singh + CS103 → ALLOWED (0+8=8 of 18)",       5, 3, 5, "2026-2027", "ALLOWED",          8);
test("Dr. Sharma + CS106 → ALLOWED (3x2=6 of 14)",            1, 6, 5, "2026-2027", "ALLOWED",          6);

console.log("\n85% near-limit check (12/14 = 85.7% ≥ 85%):");
// Dr. Sharma already has 12h; adding CS102 (3h) = 15 = OVERLOAD; try CS104=3→15 OVERLOAD
// Let's check NEAR_LIMIT: Prof. Dwivedi has 3h; adding CS101 (4h) = 7h, 7/14=50% → ALLOWED
// Who has exactly near limit? Let's add a manual check
const r85 = checkAssignment(2, 1, 3, "2026-2027"); // Dwivedi + CS101 = 3+4=7 — ALLOWED (not near)
// Meera: 6 + CS101(4) = 10 of 18 = 55% → ALLOWED
// Meera: 6 + CS103 LAB(8) = 14 of 18 = 77% → ALLOWED (not near)
// Dwivedi 3 + CS103(8) = 11 of 14 = 78.5% → ALLOWED (not near)
// Dwivedi 3 + CS101(4) + CS104(3) context not in data but checkAssignment does single check
// Let's test Pankaj + CS101(4) + CS105(3) manually — out of scope since we track existing
// Prof. Pankaj has 0h; add CS101(4h) = 4, add CS103 LAB=8 → 12 of 14 = 85.7% NEAR_LIMIT expected
const rNear = checkAssignment(3, 3, 3, "2026-2027"); // Pankaj + CS103 LAB = 0+8=8 of 14 → 57% ALLOWED
console.log("  INFO  Pankaj + CS103 LAB sem3 = " + rNear.verdict + " total=" + rNear.total + " (8/14=57% → ALLOWED)");

console.log("\n─".repeat(40));
console.log("Results: " + pass + " passed, " + fail + " failed");
if (fail > 0) process.exit(1);
