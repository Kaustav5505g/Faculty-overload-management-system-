package com.system.dao;

import java.sql.SQLException;

public interface WorkloadDAO {
    boolean assignSubject(int facultyId, int subjectId, int semester, String year) throws SQLException;
}