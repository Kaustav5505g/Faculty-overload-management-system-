# Faculty Workload Management System

A Java/JDBC application that checks faculty workload before saving subject assignments in MySQL.

## Features

- `Professor` and `AssistantProfessor` have weekly limits of 14 and 18 hours.
- Lecture hours count normally; lab hours count twice in the sample calculator.
- `WorkloadService` validates assignments, and the DAO saves valid records.
- The sample entry point is [`Main.java`](src/com/system/Main.java).

This is a command-line demonstration. It has no user interface, authentication, reports, or automated tests yet.

## Setup

1. Install MySQL and run [`sql/schema.sql`](sql/schema.sql).
2. Insert matching rows into `faculty` and `subjects`; the schema has no seed data.
3. Check the connection settings in [`DatabaseConnection.java`](src/com/system/dao/DatabaseConnection.java). Defaults: `localhost:3306`, `faculty_workload_db`, user `root`, password `password`.
4. Add MySQL Connector/J to the classpath. There is no Maven or Gradle build file.

The schema uses foreign keys and allows one assignment per subject, semester, and academic year.

## Run on Windows

```powershell
New-Item -ItemType Directory -Force out
javac -cp "path\to\mysql-connector-j.jar" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out;path\to\mysql-connector-j.jar" com.system.Main
```
