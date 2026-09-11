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

    public boolean assignSubjectToFaculty(Faculty faculty, Subject subject, int semester, String year)
            throws WorkloadExceededException, SQLException {
        if (!faculty.canAssign(subject, calculator)) {
            throw new WorkloadExceededException("Error: Assigning " + subject.getSubjectType()
                    + " exceeds max hour capacity for " + faculty.getName());
        }

        return workloadDAO.assignSubject(faculty.getFacultyId(), subject.getSubjectId(), semester, year);
    }
}