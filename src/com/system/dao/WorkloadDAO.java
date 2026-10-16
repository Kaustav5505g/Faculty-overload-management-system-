package com.system.dao;

import com.system.model.Faculty;
import com.system.model.Subject;

import java.sql.SQLException;
import java.util.List;
<<<<<<< HEAD
import java.util.Optional;

public interface WorkloadDAO {
    Optional<Faculty> findFacultyById(int facultyId) throws SQLException;

    Optional<Subject> findSubjectById(int subjectId) throws SQLException;

    List<Subject> findAssignedSubjects(int facultyId, int semester, String academicYear) throws SQLException;

=======

public interface WorkloadDAO {
    Faculty findFacultyById(int facultyId) throws SQLException;
    Subject findSubjectById(int subjectId) throws SQLException;
    List<Subject> findAssignedSubjects(int facultyId, int semester, String academicYear) throws SQLException;
>>>>>>> 71a7eb1c06214c124db413bd1da69f0d6bfa1cef
    boolean assignSubject(int facultyId, int subjectId, int semester, String year) throws SQLException;
}