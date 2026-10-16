# Faculty Workload Management System

A Java/JDBC application that checks faculty workload before saving subject assignments in MySQL.

## Prerequisites

- Java Development Kit (JDK) 8 or newer
- MySQL Server
- MySQL Connector/J
- A PowerShell terminal on Windows, or equivalent shell commands on another platform

## Overview

The application separates workload rules from database persistence. `WorkloadService` loads faculty, subject, and existing assignments through the DAO, checks the faculty member's workload for the selected semester and academic year, and then delegates the insert to the DAO.

## Features

- `Professor` and `AssistantProfessor` have weekly limits of 14 and 18 hours, respectively, preserving the existing workload rules.
- Lecture hours count normally; lab hours count twice in the workload calculator.
- `WorkloadService` validates assignments, and the DAO saves valid records using prepared statements.
- `WorkloadExceededException` reports assignments that exceed a faculty member's limit.
- The command-line entry point is [`Main.java`](src/com/system/Main.java). It asks for IDs and term details and uses records already in the database.

This is currently a command-line application. It has no graphical user interface, authentication, reports, or automated tests.

## Project Structure

```text
src/com/system/
├── Main.java
├── dao/       Database connection and assignment persistence
├── model/     Faculty, subjects, and workload calculation
└── service/  Assignment validation and exceptions
sql/schema.sql
```

## Database Setup

1. Install and start MySQL, then run [`sql/schema.sql`](sql/schema.sql). It creates the `faculty_workload_db` database and its three tables: `faculty`, `subjects`, and `workload_assignments`.
2. Add faculty and subject records to the database before using the application. The schema intentionally contains no hardcoded seed data.
3. Set the connection environment variables in the terminal where the application will run. `DB_URL` is optional and defaults to `jdbc:mysql://localhost:3306/faculty_workload_db`; `DB_USER` defaults to `root`; `DB_PASSWORD` defaults to an empty password. For PowerShell:

   ```powershell
   $env:DB_USER = "your-mysql-user"
   $env:DB_PASSWORD = "your-mysql-password"
   ```

   These values are read at runtime and are not stored in source control.
4. Add MySQL Connector/J to the classpath. There is no Maven or Gradle build file.

The schema uses foreign keys with cascade deletion and allows only one assignment for a subject, semester, and academic year combination. Workload is checked against assignments for the selected faculty member in that same semester and academic year.

Faculty designations must be `Professor` or `Assistant Professor` (case-insensitive) for the application to map a database row to the corresponding Java subclass. The schema's `max_hours` column is not used by the current Java workload policy; the Java subclasses retain the existing limits. Subject types must be `LECTURE` or `LAB`.

## Compile and Run on Windows

Run these commands from the project root. Replace the connector path with the location of your MySQL Connector/J JAR. Compile and run using the same configured environment variables from the database setup instructions.

```powershell
New-Item -ItemType Directory -Force out
javac -cp "path\to\mysql-connector-j.jar" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out;path\to\mysql-connector-j.jar" com.system.Main
```

The application prompts for a faculty ID, subject ID, semester, and academic year, then loads the selected records and existing assignments from MySQL. A successful insert prints a confirmation; connection and validation failures are printed to standard error.

## Troubleshooting

- `ClassNotFoundException` usually means the Connector/J JAR is missing from the compile or runtime classpath.
- A connection failure usually means MySQL is stopped or the settings in `DatabaseConnection.java` do not match the local server.
- A foreign-key or duplicate-key error means the referenced records are missing or the same subject and term already have an assignment.
- `Unsupported faculty designation` means the selected row does not use one of the supported designation names.
- The Java workload limits are fixed by faculty type, and assignments from other semesters or academic years do not count toward the selected term.
