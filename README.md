# 📝 Role-Based To-Do List Web Application (Spring Boot)

This is a **Spring Boot-based To-Do List Web Application** with **role-based access control** (Admin and User), **in-memory authentication** for Admins, and **database-backed authentication** for dynamically registered users. It uses **Thymeleaf** for frontend templating and follows a clean, modular design.

## 🔧 Features

- 🧑‍💼 **Admin Role (In-Memory)**
  - Add/Edit/Delete Users
  - Create, Assign, and Delete Tasks
  - View all tasks and users

- 👤 **User Role (From DB)**
  - View assigned tasks
  - Mark tasks as completed

- 🔒 **Spring Security Authentication**
  - In-memory admin credentials
  - Database-backed regular users
  - Role-based access control

- 📋 **To-Do Functionality**
  - Task creation, assignment, and completion
  - Task status updates
  - User-specific task views

- 🌐 **Frontend**
  - Thymeleaf templates with Bootstrap styling
  - Login page with validation
  - Dashboard views based on user role

## 🛠️ Tech Stack

| Layer       | Technology         |
|-------------|--------------------|
| Backend     | Spring Boot        |
| Security    | Spring Security    |
| Database    | PostgreSQL (JPA)   |
| Templating  | Thymeleaf          |
| Frontend    | HTML + Bootstrap   |
| Build Tool  | Maven              |

---

🔒 This project is private and not intended for reuse, redistribution, or public contribution.

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/todowebapp.git
cd todowebapp
