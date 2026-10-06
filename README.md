# Faculty Workload Management System

A Java application that models faculty and subjects, checks teaching workloads, and provides a JDBC DAO for storing assignments in MySQL.

## Features

- `Professor` and `AssistantProfessor` inherit from the abstract `Faculty` class, with workload limits of 14 and 18 hours.
- A `WorkloadCalculator` lambda weights lab hours twice and other subject hours once.
- `WorkloadService` checks workload limits before saving assignments through the DAO.
- The MySQL schema stores faculty, subjects, and their assignments with primary and foreign keys.

## Project layout

```text
src/com/system/
├── Main.java
├── dao/       # JDBC connection and assignment DAO
├── model/     # Faculty, subjects, and workload calculation
└── service/   # Workload validation and exception
sql/
└── schema.sql
```

## Requirements

- JDK 17 or later
- MySQL and MySQL Connector/J (for database access)

## Setup and run

1. Run `sql/schema.sql` in MySQL to create the `faculty_workload_db` database and tables.
2. Set the URL, username, and password in `src/com/system/dao/DatabaseConnection.java` for your MySQL instance.
3. Compile and run the console demo from the repository root in PowerShell:

   ```powershell
   javac -d bin (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object FullName)
   java -cp bin com.system.Main
   ```

The demo checks an assistant professor's eligibility to teach a sample subject. It does not connect to the database. To use the persistence layer, include MySQL Connector/J on the classpath and call `WorkloadService` with a `WorkloadDAO` and a workload calculator.

## Database tables

- `faculty`: faculty details, designation, and maximum hours.
- `subjects`: subject details and weekly hours.
- `workload_assignments`: faculty-subject assignments, semester, and academic year.
