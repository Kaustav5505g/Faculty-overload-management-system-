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
        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DatabaseConnection.getConnection()) {
            WorkloadDAOImpl dao = new WorkloadDAOImpl(connection);
            WorkloadCalculator calculator = subject -> {
                if ("LAB".equalsIgnoreCase(subject.getSubjectType())) {
                    return subject.getWeeklyHours() * 2.0;
                }
                return subject.getWeeklyHours();
            };
            WorkloadService service = new WorkloadService(dao, calculator);

<<<<<<< HEAD
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
=======
            int facultyId = readInteger(scanner, "Enter faculty ID: ");
            int subjectId = readInteger(scanner, "Enter subject ID: ");
            int semester = readInteger(scanner, "Enter semester: ");
            System.out.print("Enter academic year (for example, 2025-2026): ");
            String academicYear = scanner.nextLine().trim();

            boolean assigned = service.assignSubjectToFaculty(
                    facultyId, subjectId, semester, academicYear);

            if (assigned) {
                System.out.println("Subject assigned successfully.");
            } else {
                System.err.println("The assignment was not saved.");
>>>>>>> 71a7eb1c06214c124db413bd1da69f0d6bfa1cef
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (WorkloadExceededException e) {
<<<<<<< HEAD
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
=======
            System.err.println("Validation Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Input Error: " + e.getMessage());
        }
    }

    private static int readInteger(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
>>>>>>> 71a7eb1c06214c124db413bd1da69f0d6bfa1cef
        }
    }
}