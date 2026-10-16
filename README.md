# Faculty Overload Management System

A robust Java-based desktop application designed to track faculty teaching hours, enforce department workload policies, and prevent academic overload using Object-Oriented Programming (OOP) and MySQL database integration.

---

## 🚀 Project Overview

Managing faculty teaching assignments manually often leads to miscalculated hours and accidental teaching overloads. This system provides a systematic approach to model faculty members and subjects, calculate workloads dynamically based on course types (Lectures vs. Labs), enforce strict department limits, and securely store records using JDBC.

---

## 🛠️ Tech Stack & Architecture

* **Programming Language:** Java
* **Core Paradigms:** Object-Oriented Programming (OOP) (Inheritance, Polymorphism, Abstraction, Encapsulation)[cite: 4, 5]
* **Database & Connectivity:** MySQL, JDBC (`PreparedStatement`, DAO Design Pattern)[cite: 4, 8]
* **Frontend / UI:** Java Swing (Desktop GUI) / HTML & CSS (Web Presentation Mockup)[cite: 4, 6]

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