package com.system.ui;

import com.system.model.AssistantProfessor;
import com.system.model.Faculty;
import com.system.model.Professor;
import com.system.model.Subject;
import com.system.model.WorkloadCalculator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

public class FacultyWorkloadUI extends JFrame {
    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color TEXT = new Color(38, 50, 56);
    private static final Color MUTED_TEXT = new Color(96, 110, 120);
    private static final Color ACCENT = new Color(38, 90, 130);

    private final WorkloadCalculator calculator = subject ->
            "LAB".equalsIgnoreCase(subject.getSubjectType())
                    ? subject.getWeeklyHours() * 2.0
                    : subject.getWeeklyHours();

    private final JComboBox<FacultyOption> facultyCombo;
    private final JComboBox<SubjectOption> subjectCombo;
    private final JComboBox<Integer> semesterCombo;
    private final JTextField academicYearField;
    private final JLabel currentWorkloadValue = new JLabel("—");
    private final JLabel subjectWorkloadValue = new JLabel("—");
    private final JLabel maximumWorkloadValue = new JLabel("—");
    private final JLabel resultValue = new JLabel("Select a faculty member and subject.", SwingConstants.CENTER);

    public FacultyWorkloadUI() {
        super("Faculty Workload Management System");

        List<Faculty> facultyMembers = createSampleFaculty();
        List<SubjectOption> subjects = createSampleSubjects();

        facultyCombo = new JComboBox<>(facultyMembers.stream()
                .map(FacultyOption::new)
                .toArray(FacultyOption[]::new));
        subjectCombo = new JComboBox<>(subjects.toArray(new SubjectOption[0]));
        semesterCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        academicYearField = new JTextField("2025-2026", 12);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(createContent());
        setSize(720, 560);
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(28, 36, 32, 36));

        JLabel title = new JLabel("Faculty Workload Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 23));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Review teaching capacity before assigning a subject");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED_TEXT);

        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(BACKGROUND);
        GridBagConstraints headerConstraints = new GridBagConstraints();
        headerConstraints.gridx = 0;
        headerConstraints.gridy = 0;
        headerConstraints.anchor = GridBagConstraints.WEST;
        headerConstraints.insets = new Insets(0, 0, 7, 0);
        header.add(title, headerConstraints);
        headerConstraints.gridy = 1;
        headerConstraints.insets = Insets.EMPTY;
        header.add(subtitle, headerConstraints);
        content.add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setOpaque(false);
        mainPanel.add(createInputPanel(), BorderLayout.NORTH);
        mainPanel.add(createStatusPanel(), BorderLayout.CENTER);
        content.add(mainPanel, BorderLayout.CENTER);

        return content;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 233)),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));

        addInputRow(panel, 0, "Faculty", facultyCombo);
        addInputRow(panel, 1, "Subject", subjectCombo);
        addInputRow(panel, 2, "Semester", semesterCombo);
        addInputRow(panel, 3, "Academic Year", academicYearField);

        JButton checkButton = new JButton("Check Workload");
        checkButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkButton.setForeground(Color.WHITE);
        checkButton.setBackground(ACCENT);
        checkButton.setFocusPainted(false);
        checkButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        checkButton.addActionListener(event -> checkWorkload());

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 4;
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.insets = new Insets(12, 12, 0, 0);
        panel.add(checkButton, buttonConstraints);
        return panel;
    }

    private void addInputRow(JPanel panel, int row, String labelText, java.awt.Component input) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(0, 0, 12, 18);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TEXT);
        panel.add(label, labelConstraints);

        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.gridx = 1;
        inputConstraints.gridy = row;
        inputConstraints.weightx = 1;
        inputConstraints.fill = GridBagConstraints.HORIZONTAL;
        inputConstraints.insets = new Insets(0, 0, 12, 0);
        input.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(input, inputConstraints);
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 233)),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)));

        JLabel heading = new JLabel("Workload Status");
        heading.setFont(new Font("SansSerif", Font.BOLD, 17));
        heading.setForeground(TEXT);
        GridBagConstraints headingConstraints = new GridBagConstraints();
        headingConstraints.gridx = 0;
        headingConstraints.gridy = 0;
        headingConstraints.gridwidth = 2;
        headingConstraints.anchor = GridBagConstraints.WEST;
        headingConstraints.insets = new Insets(0, 0, 14, 0);
        panel.add(heading, headingConstraints);

        addStatusRow(panel, 1, "Current Workload", currentWorkloadValue);
        addStatusRow(panel, 2, "Subject Workload", subjectWorkloadValue);
        addStatusRow(panel, 3, "Maximum Workload", maximumWorkloadValue);

        resultValue.setFont(new Font("SansSerif", Font.BOLD, 14));
        resultValue.setForeground(MUTED_TEXT);
        resultValue.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        GridBagConstraints resultConstraints = new GridBagConstraints();
        resultConstraints.gridx = 0;
        resultConstraints.gridy = 4;
        resultConstraints.gridwidth = 2;
        resultConstraints.fill = GridBagConstraints.HORIZONTAL;
        resultConstraints.insets = new Insets(0, 0, 0, 0);
        panel.add(resultValue, resultConstraints);
        return panel;
    }

    private void addStatusRow(JPanel panel, int row, String labelText, JLabel value) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(MUTED_TEXT);
        value.setFont(new Font("SansSerif", Font.BOLD, 14));
        value.setForeground(TEXT);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.weightx = 1;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.insets = new Insets(0, 0, 10, 20);
        panel.add(label, labelConstraints);

        GridBagConstraints valueConstraints = new GridBagConstraints();
        valueConstraints.gridx = 1;
        valueConstraints.gridy = row;
        valueConstraints.anchor = GridBagConstraints.EAST;
        valueConstraints.insets = new Insets(0, 0, 10, 0);
        panel.add(value, valueConstraints);
    }

    private void checkWorkload() {
        Faculty faculty = ((FacultyOption) facultyCombo.getSelectedItem()).faculty;
        Subject subject = ((SubjectOption) subjectCombo.getSelectedItem()).subject;
        double currentHours = faculty.calculateTotalHours(calculator);
        double subjectHours = calculator.calculate(subject);
        boolean canAssign = faculty.canAssign(subject, calculator);

        currentWorkloadValue.setText(formatHours(currentHours));
        subjectWorkloadValue.setText(formatHours(subjectHours));
        maximumWorkloadValue.setText(formatHours(faculty.getMaxHours()));
        resultValue.setText(canAssign
                ? "Within limit — this subject can be assigned."
                : "Over limit — this subject cannot be assigned.");
        resultValue.setForeground(canAssign ? new Color(35, 115, 76) : new Color(175, 55, 55));
    }

    private String formatHours(double hours) {
        return (hours == (int) hours ? Integer.toString((int) hours) : Double.toString(hours)) + " hours";
    }

    private List<Faculty> createSampleFaculty() {
        Faculty assistantProfessor = new AssistantProfessor(1, "Dr. Smith", "smith@university.edu");
        Faculty professor = new Professor(2, "Dr. John", "john@university.edu");

        assistantProfessor.getAssignments().add(
                new Subject(201, "CS201", "Algorithms", 3, 4, "LECTURE"));
        professor.getAssignments().add(
                new Subject(202, "CS202", "Computer Networks", 3, 6, "LECTURE"));
        professor.getAssignments().add(
                new Subject(203, "CS203", "Networks Tutorial", 1, 2, "LECTURE"));

        List<Faculty> facultyMembers = new ArrayList<>();
        facultyMembers.add(assistantProfessor);
        facultyMembers.add(professor);
        return facultyMembers;
    }

    private List<Subject> createSampleSubjects() {
        List<SubjectOption> subjects = new ArrayList<>();
        subjects.add(new SubjectOption(
                new Subject(101, "CS101", "Java Programming", 3, 4, "LECTURE"),
                "CS101 - Java Programming"));
        subjects.add(new SubjectOption(
                new Subject(102, "CS102", "Database Systems", 3, 3, "LECTURE"),
                "CS102 - Database Systems"));
        subjects.add(new SubjectOption(
                new Subject(103, "CS103", "Database Lab", 2, 4, "LAB"),
                "CS103 - Database Lab"));
        return subjects;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
                System.err.println("Could not apply the system look and feel: " + exception.getMessage());
            }
            new FacultyWorkloadUI().setVisible(true);
        });
    }

    private static class FacultyOption {
        private final Faculty faculty;

        private FacultyOption(Faculty faculty) {
            this.faculty = faculty;
        }

        @Override
        public String toString() {
            String designation = faculty instanceof AssistantProfessor
                    ? "Assistant Professor"
                    : "Professor";
            return faculty.getName() + " - " + designation;
        }
    }

    private static class SubjectOption {
        private final Subject subject;
        private final String description;

        private SubjectOption(Subject subject, String description) {
            this.subject = subject;
            this.description = description;
        }

        @Override
        public String toString() {
            return description + " - " + subject.getWeeklyHours() + " hours - " + subject.getSubjectType();
        }
    }
}
