package com.system.dao;

import com.system.model.Faculty;
import com.system.model.Subject;

import java.sql.SQLException;
import java.util.List;

public interface WorkloadDAO {
    Faculty findFacultyById(int facultyId) throws SQLException;
    Subject findSubjectById(int subjectId) throws SQLException;
    List<Subject> findAssignedSubjects(int facultyId, int semester, String academicYear) throws SQLException;
    boolean assignSubject(int facultyId, int subjectId, int semester, String year) throws SQLException;
}