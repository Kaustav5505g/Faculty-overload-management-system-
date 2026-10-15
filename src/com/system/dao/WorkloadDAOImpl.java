package com.system.dao;

import com.system.model.AssistantProfessor;
import com.system.model.Faculty;
import com.system.model.Professor;
import com.system.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkloadDAOImpl implements WorkloadDAO {
    private final Connection connection;

    public WorkloadDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Faculty findFacultyById(int facultyId) throws SQLException {
        String sql = "SELECT faculty_id, name, email, designation "
                + "FROM faculty WHERE faculty_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, facultyId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                String designation = resultSet.getString("designation");
                String normalizedDesignation = designation == null
                        ? ""
                        : designation.replace(" ", "");
                int id = resultSet.getInt("faculty_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                if ("Professor".equalsIgnoreCase(normalizedDesignation)) {
                    return new Professor(id, name, email);
                }
                if ("AssistantProfessor".equalsIgnoreCase(normalizedDesignation)) {
                    return new AssistantProfessor(id, name, email);
                }
                throw new SQLException("Unsupported faculty designation: " + designation);
            }
        }
    }

    @Override
    public Subject findSubjectById(int subjectId) throws SQLException {
        String sql = "SELECT subject_id, subject_code, title, credits, weekly_hours, subject_type "
                + "FROM subjects WHERE subject_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, subjectId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new Subject(
                        resultSet.getInt("subject_id"),
                        resultSet.getString("subject_code"),
                        resultSet.getString("title"),
                        resultSet.getInt("credits"),
                        resultSet.getInt("weekly_hours"),
                        resultSet.getString("subject_type"));
            }
        }
    }

    @Override
    public List<Subject> findAssignedSubjects(int facultyId, int semester, String academicYear)
            throws SQLException {
        String sql = "SELECT s.subject_id, s.subject_code, s.title, s.credits, "
                + "s.weekly_hours, s.subject_type "
                + "FROM workload_assignments wa "
                + "JOIN subjects s ON s.subject_id = wa.subject_id "
                + "WHERE wa.faculty_id = ? AND wa.semester = ? AND wa.academic_year = ?";
        List<Subject> subjects = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, facultyId);
            pstmt.setInt(2, semester);
            pstmt.setString(3, academicYear);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    subjects.add(new Subject(
                            resultSet.getInt("subject_id"),
                            resultSet.getString("subject_code"),
                            resultSet.getString("title"),
                            resultSet.getInt("credits"),
                            resultSet.getInt("weekly_hours"),
                            resultSet.getString("subject_type")));
                }
            }
        }
        return subjects;
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