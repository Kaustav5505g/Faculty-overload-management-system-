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
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class WorkloadDAOImpl implements WorkloadDAO {
    private final Connection connection;

    public WorkloadDAOImpl(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "connection must not be null");
    }

    @Override
    public Optional<Faculty> findFacultyById(int facultyId) throws SQLException {
        String sql = "SELECT faculty_id, name, email, designation "
                + "FROM faculty WHERE faculty_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, facultyId);
            try (ResultSet results = statement.executeQuery()) {
                if (!results.next()) {
                    return Optional.empty();
                }

                String designation = results.getString("designation").trim().toLowerCase(Locale.ROOT);
                int id = results.getInt("faculty_id");
                String name = results.getString("name");
                String email = results.getString("email");
                if ("professor".equals(designation)) {
                    return Optional.of(new Professor(id, name, email));
                }
                if ("assistant professor".equals(designation)) {
                    return Optional.of(new AssistantProfessor(id, name, email));
                }
                throw new SQLException("Unsupported faculty designation: " + designation);
            }
        }
    }

    @Override
    public Optional<Subject> findSubjectById(int subjectId) throws SQLException {
        String sql = "SELECT subject_id, subject_code, title, credits, weekly_hours, subject_type "
                + "FROM subjects WHERE subject_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, subjectId);
            try (ResultSet results = statement.executeQuery()) {
                if (!results.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Subject(
                        results.getInt("subject_id"),
                        results.getString("subject_code"),
                        results.getString("title"),
                        results.getInt("credits"),
                        results.getInt("weekly_hours"),
                        results.getString("subject_type")));
            }
        }
    }

    @Override
    public List<Subject> findAssignedSubjects(int facultyId, int semester, String academicYear) throws SQLException {
        String sql = "SELECT s.subject_id, s.subject_code, s.title, s.credits, "
                + "s.weekly_hours, s.subject_type "
                + "FROM subjects s "
                + "JOIN workload_assignments wa ON wa.subject_id = s.subject_id "
                + "WHERE wa.faculty_id = ? AND wa.semester = ? AND wa.academic_year = ?";
        List<Subject> subjects = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, facultyId);
            statement.setInt(2, semester);
            statement.setString(3, academicYear);
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    subjects.add(new Subject(
                            results.getInt("subject_id"),
                            results.getString("subject_code"),
                            results.getString("title"),
                            results.getInt("credits"),
                            results.getInt("weekly_hours"),
                            results.getString("subject_type")));
                }
            }
        }
        return subjects;
    }

    @Override
    public boolean assignSubject(int facultyId, int subjectId, int semester, String year) throws SQLException {
        String sql = "INSERT INTO workload_assignments (faculty_id, subject_id, semester, academic_year) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, facultyId);
            statement.setInt(2, subjectId);
            statement.setInt(3, semester);
            statement.setString(4, year);
            return statement.executeUpdate() > 0;
        }
    }
}
