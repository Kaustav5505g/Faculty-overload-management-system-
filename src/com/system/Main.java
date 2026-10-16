/*
 * Faculty Overload Management System: checks whether a faculty member can
 * take on a subject without exceeding their workload limit. The OOP model
 * represents faculty and subjects, while a lambda supplies the workload
 * policy: LAB hours count twice and other subject hours count once.
 */
package com.system;

import com.system.dao.DatabaseConnection;
import com.system.dao.WorkloadDAOImpl;
import com.system.model.WorkloadCalculator;
import com.system.service.WorkloadExceededException;
import com.system.service.WorkloadService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            WorkloadDAOImpl dao = new WorkloadDAOImpl(connection);
            WorkloadCalculator calculator = subject -> {
                if ("LAB".equalsIgnoreCase(subject.getSubjectType())) {
                    return subject.getWeeklyHours() * 2.0;
                }
                return subject.getWeeklyHours();
            };
            WorkloadService service = new WorkloadService(dao, calculator);

            try (Scanner scanner = new Scanner(System.in)) {
                int facultyId = readPositiveInteger(scanner, "Faculty ID: ");
                int subjectId = readPositiveInteger(scanner, "Subject ID: ");
                int semester = readPositiveInteger(scanner, "Semester: ");
                System.out.print("Academic year (up to 10 characters): ");
                String academicYear = scanner.nextLine().trim();

                boolean assigned = service.assignSubjectToFaculty(
                        facultyId, subjectId, semester, academicYear);
                if (assigned) {
                    System.out.println("Subject assigned and recorded in the database.");
                } else {
                    System.out.println("The assignment was not recorded.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (WorkloadExceededException e) {
            System.err.println("Workload validation error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Input error: " + e.getMessage());
        }
    }

    private static int readPositiveInteger(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            int value = Integer.parseInt(input);
            if (value <= 0) {
                throw new IllegalArgumentException("Value must be positive: " + input);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a whole number: " + input, e);
        }
    }
}