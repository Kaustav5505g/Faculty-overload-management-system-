package com.system;

import com.system.dao.DatabaseConnection;
import com.system.dao.WorkloadDAOImpl;
import com.system.model.AssistantProfessor;
import com.system.model.Faculty;
import com.system.model.Subject;
import com.system.model.WorkloadCalculator;
import com.system.service.WorkloadExceededException;
import com.system.service.WorkloadService;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            WorkloadDAOImpl dao = new WorkloadDAOImpl(connection);

            WorkloadCalculator calculator = subject -> {
                if ("LAB".equalsIgnoreCase(subject.getSubjectType())) {
                    return subject.getWeeklyHours() * 2.0;
                }
                return subject.getWeeklyHours() * 1.0;
            };

            WorkloadService service = new WorkloadService(dao, calculator);

            Faculty prof = new AssistantProfessor(1, "Dr. Smith", "smith@university.edu");
            Subject javaSub = new Subject(101, "CS101", "Java Programming", 3, 4, "LECTURE");

            boolean assigned = service.assignSubjectToFaculty(prof, javaSub, 1, "2025-2026");

            if (assigned) {
                System.out.println("Success: Subject assigned safely and recorded in the database!");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        } catch (WorkloadExceededException e) {
            System.err.println("Validation Error: " + e.getMessage());
        }
    }
}