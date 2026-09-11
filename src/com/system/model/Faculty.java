package com.system.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Faculty {
    private int facultyId;
    private String name;
    private String email;
    private int maxHours;
    private List<Subject> assignments;

    public Faculty(int facultyId, String name, String email, int maxHours) {
        this.facultyId = facultyId;
        this.name = name;
        this.email = email;
        this.maxHours = maxHours;
        this.assignments = new ArrayList<>();
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

    public int getFacultyId() { return facultyId; }
    public String getName() { return name; }
    public int getMaxHours() { return maxHours; }
    public List<Subject> getAssignments() { return assignments; }
}