package com.system.model;

public class Subject {
    private final int subjectId;
    private final String subjectCode;
    private final String title;
    private final int credits;
    private final int weeklyHours;
    private final String subjectType;

    public Subject(int subjectId, String subjectCode, String title, int credits, int weeklyHours, String subjectType) {
        if (subjectId <= 0) {
            throw new IllegalArgumentException("Subject ID must be positive");
        }
        if (credits <= 0) {
            throw new IllegalArgumentException("Subject credits must be positive");
        }
        if (weeklyHours <= 0) {
            throw new IllegalArgumentException("Subject weekly hours must be positive");
        }
        this.subjectId = subjectId;
        this.subjectCode = requireText(subjectCode, "subject code");
        this.title = requireText(title, "title");
        this.credits = credits;
        this.weeklyHours = weeklyHours;
        this.subjectType = requireText(subjectType, "subject type").toUpperCase(java.util.Locale.ROOT);
        if (!"LECTURE".equals(this.subjectType) && !"LAB".equals(this.subjectType)) {
            throw new IllegalArgumentException("Subject type must be LECTURE or LAB");
        }
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public String getSubjectType() {
        return subjectType;
    }
}