# Faculty Workload Management System

A Java/JDBC application that validates a faculty member's weekly workload before recording a subject assignment in MySQL.

## What Is Implemented

- `Faculty` subclasses define maximum weekly hours: `Professor` (14) and `AssistantProfessor` (18).
- `WorkloadService` checks an assignment through a `WorkloadCalculator` before calling the DAO.
- Lecture subjects count their weekly hours; lab subjects count twice their weekly hours in the sample calculator in `Main`.
- `WorkloadDAOImpl` inserts valid assignments into `workload_assignments`.
- `WorkloadExceededException` reports assignments that exceed the faculty member's limit.

The current application demonstrates one assignment from `Main`; it does not yet provide a web or desktop user interface, authentication, reporting, or automated tests.

## Technology

- Java
- JDBC
- MySQL
- DAO and service-layer patterns

There is currently no Maven/Gradle build file. A MySQL Connector/J driver JAR is required on the classpath when compiling and running the application.

## Project Structure

```text
src/com/system/
├── Main.java
├── dao/
│   ├── DatabaseConnection.java
│   ├── WorkloadDAO.java
│   └── WorkloadDAOImpl.java
├── model/
│   ├── AssistantProfessor.java
│   ├── Faculty.java
│   ├── Professor.java
│   ├── Subject.java
│   └── WorkloadCalculator.java
└── service/
    ├── WorkloadExceededException.java
    └── WorkloadService.java
sql/schema.sql
```

## Database Setup

1. Install and start MySQL.
2. Run [`sql/schema.sql`](sql/schema.sql). It creates the `faculty_workload_db` database and the `faculty`, `subjects`, and `workload_assignments` tables.
3. Add matching faculty and subject rows before running the sample assignment. The schema does not include seed data.
4. Update the connection values in [`DatabaseConnection.java`](src/com/system/dao/DatabaseConnection.java) if your MySQL host, port, username, or password differs. The current defaults are `localhost:3306`, database `faculty_workload_db`, user `root`, and password `password`.

The schema enforces foreign keys and permits only one assignment for a subject, semester, and academic year combination.

## Running

Compile and run with a Java compiler and the MySQL Connector/J JAR on the classpath. For example, from the project root on Windows:

```powershell
New-Item -ItemType Directory -Force out
javac -cp "path\to\mysql-connector-j.jar" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out;path\to\mysql-connector-j.jar" com.system.Main
```

On a successful insert, the sample prints a success message. Database connection or workload validation failures are printed to standard error.
