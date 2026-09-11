package com.system;

import com.system.model.AssistantProfessor;
import com.system.model.Faculty;
import com.system.model.Subject;
import com.system.model.WorkloadCalculator;

public class Main {
    public static void main(String[] args) {
        WorkloadCalculator calculator = subject -> {
            if ("LAB".equalsIgnoreCase(subject.getSubjectType())) {
                return subject.getWeeklyHours() * 2.0;
            }
            return subject.getWeeklyHours() * 1.0;
        };

        Faculty prof = new AssistantProfessor(1, "Dr. Smith", "smith@university.edu");
        Subject javaSub = new Subject(101, "CS101", "Java Programming", 3, 4, "LECTURE");

        if (prof.canAssign(javaSub, calculator)) {
            System.out.println("Success: Subject can be assigned!");
        } else {
            System.out.println("Error: Faculty workload exceeded max limit!");
        }
    }
}