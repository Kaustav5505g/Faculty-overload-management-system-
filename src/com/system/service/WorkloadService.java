package com.system.service;

import com.system.dao.WorkloadDAO;
import com.system.model.Faculty;
import com.system.model.Subject;
import com.system.model.WorkloadCalculator;

import java.sql.SQLException;
import java.util.Objects;

public class WorkloadService {
    private final WorkloadDAO workloadDAO;
    private final WorkloadCalculator calculator;

    public WorkloadService(WorkloadDAO workloadDAO, WorkloadCalculator calculator) {
        this.workloadDAO = Objects.requireNonNull(workloadDAO, "workloadDAO must not be null");
        this.calculator = Objects.requireNonNull(calculator, "calculator must not be null");
    }

    public boolean assignSubjectToFaculty(int facultyId, int subjectId, int semester, String academicYear)
            throws WorkloadExceededException, SQLException {
        validateTerm(semester, academicYear);
        Faculty faculty = workloadDAO.findFacultyById(facultyId)
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + facultyId));
        Subject subject = workloadDAO.findSubjectById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + subjectId));
        return assignSubjectToFaculty(faculty, subject, semester, academicYear);
    }

    public boolean assignSubjectToFaculty(Faculty faculty, Subject subject, int semester, String academicYear)
            throws WorkloadExceededException, SQLException {
        Objects.requireNonNull(faculty, "faculty must not be null");
        Objects.requireNonNull(subject, "subject must not be null");
        validateTerm(semester, academicYear);

        faculty.replaceAssignments(workloadDAO.findAssignedSubjects(
                faculty.getFacultyId(), semester, academicYear.trim()));
        if (!faculty.canAssign(subject, calculator)) {
            throw new WorkloadExceededException(
                    "Assignment exceeds the weekly limit of " + faculty.getMaxHours()
                            + " hours for " + faculty.getName());
        }

        boolean assigned = workloadDAO.assignSubject(
                faculty.getFacultyId(), subject.getSubjectId(), semester, academicYear.trim());
        if (assigned) {
            faculty.addAssignment(subject);
        }
        return assigned;
    }

    private void validateTerm(int semester, String academicYear) {
        if (semester <= 0) {
            throw new IllegalArgumentException("Semester must be positive");
        }
        if (academicYear == null || academicYear.trim().isEmpty() || academicYear.trim().length() > 10) {
            throw new IllegalArgumentException("Academic year must contain 1 to 10 characters");
        }
    }
}