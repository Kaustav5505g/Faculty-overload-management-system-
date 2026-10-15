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
                return subject.getWeeklyHours() * 1.0;
            };

            WorkloadService service = new WorkloadService(dao, calculator);

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
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        } catch (WorkloadExceededException e) {
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
        }
    }
}