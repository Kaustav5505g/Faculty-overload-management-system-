package com.system.service;

import com.system.dao.WorkloadDAO;
import com.system.model.Faculty;
import com.system.model.Subject;
import com.system.model.WorkloadCalculator;

import java.sql.SQLException;

public class WorkloadService {
    private WorkloadDAO workloadDAO;
    private WorkloadCalculator calculator;

    public WorkloadService(WorkloadDAO workloadDAO, WorkloadCalculator calculator) {
        this.workloadDAO = workloadDAO;
        this.calculator = calculator;
    }

    public boolean assignSubjectToFaculty(Faculty faculty, Subject subject, int semester, String academicYear)
            throws WorkloadExceededException, SQLException {
        if (faculty == null || subject == null) {
            throw new IllegalArgumentException("Faculty and subject are required.");
        }

        faculty.getAssignments().clear();
        faculty.getAssignments().addAll(
                workloadDAO.findAssignedSubjects(faculty.getFacultyId(), semester, academicYear));

        if (!faculty.canAssign(subject, calculator)) {
            throw new WorkloadExceededException(
                    "Assignment exceeds the weekly workload limit for " + faculty.getName());
        }

        return workloadDAO.assignSubject(faculty.getFacultyId(), subject.getSubjectId(), semester, academicYear);
    }

    public boolean assignSubjectToFaculty(int facultyId, int subjectId, int semester, String academicYear)
            throws WorkloadExceededException, SQLException {
        Faculty faculty = workloadDAO.findFacultyById(facultyId);
        if (faculty == null) {
            throw new IllegalArgumentException("No faculty member found with ID " + facultyId + ".");
        }

        Subject subject = workloadDAO.findSubjectById(subjectId);
        if (subject == null) {
            throw new IllegalArgumentException("No subject found with ID " + subjectId + ".");
        }

        return assignSubjectToFaculty(faculty, subject, semester, academicYear);
    }
}