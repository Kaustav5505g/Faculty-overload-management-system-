package com.system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Faculty {
    private final int facultyId;
    private final String name;
    private final String email;
    private final int maxHours;
    private final List<Subject> assignments;

    public Faculty(int facultyId, String name, String email, int maxHours) {
        if (facultyId <= 0) {
            throw new IllegalArgumentException("Faculty ID must be positive");
        }
        if (maxHours <= 0) {
            throw new IllegalArgumentException("Maximum weekly hours must be positive");
        }
        this.facultyId = facultyId;
        this.name = requireText(name, "name");
        this.email = requireText(email, "email");
        this.maxHours = maxHours;
        this.assignments = new ArrayList<>();
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    public double calculateTotalHours(WorkloadCalculator calculator) {
        return assignments.stream()
                .mapToDouble(calculator::calculate)
                .sum();
    }

    public boolean canAssign(Subject subject, WorkloadCalculator calculator) {
        double newSubjectHours = calculator.calculate(subject);
        return (calculateTotalHours(calculator) + newSubjectHours) <= maxHours;
    }

    public void replaceAssignments(List<Subject> subjects) {
        assignments.clear();
        assignments.addAll(Objects.requireNonNull(subjects, "subjects must not be null"));
    }

    public void addAssignment(Subject subject) {
        assignments.add(Objects.requireNonNull(subject, "subject must not be null"));
    }

    public int getFacultyId() {
        return facultyId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public List<Subject> getAssignments() {
        return Collections.unmodifiableList(assignments);
    }
}