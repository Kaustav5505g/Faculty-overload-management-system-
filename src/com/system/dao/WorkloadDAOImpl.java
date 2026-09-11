package com.system.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WorkloadDAOImpl implements WorkloadDAO {
    private Connection connection;

    public WorkloadDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean assignSubject(int facultyId, int subjectId, int semester, String year) throws SQLException {
        String sql = "INSERT INTO workload_assignments (faculty_id, subject_id, semester, academic_year) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, facultyId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, semester);
            pstmt.setString(4, year);
            return pstmt.executeUpdate() > 0;
        }
    }
}