package com.system.model;

public class Subject {
    private int subjectId;
    private String subjectCode;
    private String title;
    private int credits;
    private int weeklyHours;
    private String subjectType; // "LECTURE" or "LAB"

    public Subject(int subjectId, String subjectCode, String title, int credits, int weeklyHours, String subjectType) {
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.title = title;
        this.credits = credits;
        this.weeklyHours = weeklyHours;
        this.subjectType = subjectType;
    }

    public int getSubjectId() { return subjectId; }
    public int getWeeklyHours() { return weeklyHours; }
    public String getSubjectType() { return subjectType; }
}