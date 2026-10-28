/**
 * workload.js — pure rule functions for faculty workload calculation.
 *
 * Mirrors Faculty.canAssign() and the LAB-twice lambda in Main.java:
 *   - LAB subjects count as weeklyHours * 2
 *   - LECTURE and all other types count as weeklyHours * 1
 *   - Allowed when: currentLoad + newSubjectHours <= maxHours  (equal is allowed)
 *   - Load is per (facultyId, semester, academicYear) combination
 *   - A subject can only be assigned to one faculty per (subjectId, semester, academicYear)
 */

/**
 * Returns the workload hours for a subject object.
 * @param {object} subject - must have { type, weeklyHours }
 * @returns {number}
 */
function workloadHours(subject) {
  return subject.type === "LAB" ? subject.weeklyHours * 2 : subject.weeklyHours;
}

/**
 * Returns a human-readable hours-counted string for a subject.
 * LAB:     "4 x 2 = 8"
 * LECTURE: "4"
 * @param {object} subject
 * @returns {string}
 */
function workloadHoursLabel(subject) {
  if (subject.type === "LAB") {
    const total = subject.weeklyHours * 2;
    return `${subject.weeklyHours} x 2 = ${total}`;
  }
  return String(subject.weeklyHours);
}

/**
 * Returns all assigned subjects for a faculty member in a given semester/year.
 * @param {number} facultyId
 * @param {number} semester
 * @param {string} academicYear
 * @returns {object[]} array of subject objects
 */
function getAssignedSubjects(facultyId, semester, academicYear) {
  return ASSIGNMENTS
    .filter(a =>
      a.facultyId === facultyId &&
      a.semester === semester &&
      a.academicYear === academicYear
    )
    .map(a => SUBJECTS.find(s => s.id === a.subjectId))
    .filter(Boolean);
}

/**
 * Returns total workload hours for a faculty member in a given semester/year.
 * @param {number} facultyId
 * @param {number} semester
 * @param {string} academicYear
 * @returns {number}
 */
function currentLoad(facultyId, semester, academicYear) {
  return getAssignedSubjects(facultyId, semester, academicYear)
    .reduce((sum, s) => sum + workloadHours(s), 0);
}

/**
 * Returns the faculty member who already has the subject assigned in the same
 * semester/year, or null if not assigned.
 * @param {number} subjectId
 * @param {number} semester
 * @param {string} academicYear
 * @returns {object|null} faculty object or null
 */
function subjectAlreadyAssignedTo(subjectId, semester, academicYear) {
  const match = ASSIGNMENTS.find(a =>
    a.subjectId === subjectId &&
    a.semester === semester &&
    a.academicYear === academicYear
  );
  if (!match) return null;
  return FACULTY.find(f => f.id === match.facultyId) || null;
}

/**
 * Core check: returns a result object describing whether an assignment is allowed.
 *
 * Verdict strings:
 *   "ALLOWED"          — ok and under 85% of limit
 *   "NEAR_LIMIT"       — ok but at or above 85% of limit
 *   "OVERLOAD"         — current + new > limit
 *   "ALREADY_ASSIGNED" — subject already has a faculty this semester/year
 *
 * @param {number} facultyId
 * @param {number} subjectId
 * @param {number} semester
 * @param {string} academicYear
 * @returns {object}
 */
function checkAssignment(facultyId, subjectId, semester, academicYear) {
  const faculty  = FACULTY.find(f => f.id === facultyId);
  const subject  = SUBJECTS.find(s => s.id === subjectId);

  if (!faculty || !subject) {
    return { verdict: "INVALID", faculty, subject };
  }

  // Check if subject is already assigned (DB unique constraint mirror)
  const alreadyHolder = subjectAlreadyAssignedTo(subjectId, semester, academicYear);
  if (alreadyHolder) {
    return {
      verdict: "ALREADY_ASSIGNED",
      faculty,
      subject,
      semester,
      academicYear,
      alreadyHolder,
      existingSubjects: getAssignedSubjects(facultyId, semester, academicYear),
      newHours: workloadHours(subject),
    };
  }

  const existingSubjects = getAssignedSubjects(facultyId, semester, academicYear);
  const existing = existingSubjects.reduce((sum, s) => sum + workloadHours(s), 0);
  const newHours = workloadHours(subject);
  const total    = existing + newHours;
  const limit    = faculty.maxHours;
  const allowed  = total <= limit;
  const nearLimit = allowed && total >= Math.ceil(limit * 0.85);

  let verdict;
  if (!allowed)       verdict = "OVERLOAD";
  else if (nearLimit) verdict = "NEAR_LIMIT";
  else                verdict = "ALLOWED";

  return {
    verdict,
    faculty,
    subject,
    semester,
    academicYear,
    existingSubjects,
    existing,
    newHours,
    total,
    limit,
    allowed,
    overBy: allowed ? 0 : total - limit,
    remaining: allowed ? limit - total : 0,
  };
}

/**
 * For the "what can be done" section on result.html.
 * Returns other faculty who CAN take the subject this semester/year.
 * @param {number} subjectId
 * @param {number} semester
 * @param {string} academicYear
 * @param {number} excludeFacultyId  — skip this faculty (they're already shown)
 * @returns {object[]} array of { faculty, currentLoad, newHours, totalAfter, remaining }
 */
function whoCanTake(subjectId, semester, academicYear, excludeFacultyId) {
  const subject = SUBJECTS.find(s => s.id === subjectId);
  if (!subject) return [];
  const newHours = workloadHours(subject);

  return FACULTY
    .filter(f => f.id !== excludeFacultyId)
    .map(f => {
      const load  = currentLoad(f.id, semester, academicYear);
      const total = load + newHours;
      return {
        faculty: f,
        currentLoad: load,
        newHours,
        totalAfter: total,
        remaining: f.maxHours - total,
        canTake: total <= f.maxHours,
      };
    })
    .filter(r => r.canTake);
}

/**
 * Returns which existing assignments of a faculty member could be removed
 * to make the new subject fit.
 * @param {object} result — return value of checkAssignment()
 * @returns {object[]} array of subject objects
 */
function removalsThatWouldFit(result) {
  if (result.verdict !== "OVERLOAD") return [];
  const { existingSubjects, newHours, limit } = result;
  // Find single subjects whose removal brings total to <= limit
  return existingSubjects.filter(s => {
    const withoutThis = result.existing - workloadHours(s);
    return withoutThis + newHours <= limit;
  });
}

/**
 * Returns subjects that are unassigned in the given semester/year —
 * used for the ALREADY_ASSIGNED "what can be done" suggestion.
 * @param {number} semester
 * @param {string} academicYear
 * @returns {object[]}
 */
function freeSubjects(semester, academicYear) {
  const takenIds = ASSIGNMENTS
    .filter(a => a.semester === semester && a.academicYear === academicYear)
    .map(a => a.subjectId);
  return SUBJECTS.filter(s => !takenIds.includes(s.id));
}
