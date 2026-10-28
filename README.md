# Faculty Overload Management System

A Java + MySQL + browser application that checks whether a teaching assignment is allowed before recording it. Given a faculty member, subject, semester and academic year, the system applies the department workload rules, records the assignment only when it passes, and explains the decision in plain language.


# Workload Rules
| Rule | Detail |
|---|---|
| **Workload hours** | LECTURE: weekly hours counted once. LAB: weekly hours counted **twice** (e.g. 4 weekly hours = 8 workload hours). |
| **Limits** | Professor: **14 h**. Assistant Professor: **18 h**. |
| **Scope** | Load is totalled per faculty member, per semester and academic year. |
| **Acceptance** | Allowed when `current load + new subject hours ≤ limit`. Equal to the limit is allowed. |
| **Single owner** | A subject can have only **one** faculty member per semester and academic year. |
These five rules are enforced in three independent layers: the MySQL database (trigger + stored procedure), the Java service layer (`Faculty.canAssign()`), and the browser UI (`workload.js`).
---
---

## Tech Stack
| Layer | Technology |
|---|---|
| **Database** | MySQL 8.x (also tested on MariaDB 10.11) |
| **DB connectivity** | JDBC with MySQL Connector/J, `PreparedStatement` |
| **Design pattern** | Data Access Object (DAO) — `WorkloadDAO` interface + `WorkloadDAOImpl` |
| **Java OOP** | Abstract `Faculty` → `Professor` / `AssistantProfessor`; `WorkloadCalculator` functional interface |
| **Web UI** | Plain HTML, CSS, vanilla JavaScript — no framework, no CDN, no build step |
| **SQL features** | View, stored functions, stored procedure, `BEFORE INSERT` trigger, `NOT EXISTS` subquery |
---
---

## 📌 Key Features & Business Logic

1. **OOP Hierarchy:** Abstract `Faculty` class extended by specialized subclasses (`Professor` with a max limit of 14 hours, and `Assistant Professor` with a max limit of 18 hours)[cite: 4, 5].
2. **Workload Calculation:** Automatically weighs teaching hours according to course structure (e.g., standard lectures vs. structured lab hours)[cite: 4, 6].
3. **Overload Validation (`canAssign()`):** Enforces department constraints by verifying whether `Current Load + New Assignment Load ≤ Maximum Allowed Limit` before saving[cite: 4, 6].
4. **Data Persistence:** Separates database operations using the Data Access Object (DAO) pattern with parameterized SQL queries via JDBC to prevent SQL injection[cite: 4, 8].

---

## 📂 Project Structure

```text
Faculty-overload-management-system/
│
├── sql/
│   └── schema.sql              # MySQL database schema and table definitions
├── src/
│   └── com/system/
│       ├── model/              # Faculty, Professor, AssistantProfessor, Subject classes
│       ├── dao/                # WorkloadDAO and WorkloadDAOImpl database handlers
│       ├── service/            # Workload calculation and business logic
│       └── ui/                 # Java Swing UI & Web-based interactive frontend
│
├── index.html                  # Web frontend dashboard presentation view
├── README.md                   # Project documentation
└── .gitignore                  # Git ignore rules
