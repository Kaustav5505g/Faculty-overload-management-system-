# Faculty Workload Management System

A Java/JDBC application that checks faculty workload before saving subject assignments in MySQL.

## Prerequisites

- Java Development Kit (JDK) 8 or newer
- MySQL Server
- MySQL Connector/J
- A PowerShell terminal on Windows, or equivalent shell commands on another platform

## Overview

The application separates workload rules from database persistence. `WorkloadService` receives a faculty member, subject, semester, and academic year; it calculates whether the assignment is allowed and then delegates the insert to the DAO.

## Features

- `Professor` and `AssistantProfessor` have weekly limits of 14 and 18 hours.
- Lecture hours count normally; lab hours count twice in the sample calculator.
- `WorkloadService` validates assignments, and the DAO saves valid records.
- `WorkloadExceededException` reports assignments that exceed a faculty member's limit.
- The sample entry point is [`Main.java`](src/com/system/Main.java).

This is currently a command-line demonstration. It has no user interface, authentication, reports, or automated tests.

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
2. Insert matching rows into `faculty` and `subjects`; the schema does not include seed data.
3. Check the connection settings in [`DatabaseConnection.java`](src/com/system/dao/DatabaseConnection.java). Defaults are `localhost:3306`, database `faculty_workload_db`, user `root`, and password `password`.
4. Add MySQL Connector/J to the classpath. There is no Maven or Gradle build file.

The schema uses foreign keys with cascade deletion and allows only one assignment for a subject, semester, and academic year combination.

Before running the Java program, make sure the database contains at least one matching faculty row and subject row. The sample entry point uses an assistant professor and a lecture subject; their IDs must exist in the database or the insert will fail.

## Compile and Run on Windows

Run these commands from the project root. Replace the connector path with the location of your MySQL Connector/J JAR.

```powershell
New-Item -ItemType Directory -Force out
javac -cp "path\to\mysql-connector-j.jar" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out;path\to\mysql-connector-j.jar" com.system.Main
```

The sample creates an assistant professor and a lecture subject, validates the assignment, and attempts to store it. A successful insert prints a success message; connection and validation failures are printed to standard error.
