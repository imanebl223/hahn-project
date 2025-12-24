# Project Task Manager – Fullstack Application

A full-stack web application that allows users to manage projects and tasks with authentication, progress tracking, and a clean UI.


---

## 🚀 Features

### 🔐 Authentication
- User login with email & password
- JWT-based authentication
- Protected API routes

### 📁 Project Management
- Create projects (title + description)
- List user projects
- View project details
- Track project progress (completed tasks / total tasks)

### ✅ Task Management
- Add tasks to a project
- Mark tasks as completed
- Delete tasks
- List tasks by project

---

## 🧱 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Maven

### Frontend
- React
- TypeScript
- Vite
- Axios
- Tailwind CSS

---

## 📂 Project Structure
Project-Task-Manager
├── Backend
│ ├── src/main/java
│ ├── src/main/resources
│ ├── pom.xml
│ └── ...
├── Frontend
│ ├── src
│ ├── package.json
│ ├── vite.config.ts
│ └── ...
└── README.md

---
🎥 Demo

A demo video is provided showing: https://drive.google.com/file/d/1pxQa4tkkqjCKJ3xC2w57BsTA2fWyvQXq/view?usp=sharing

## ⚙️ Backend AND Frontend Setup

###  Configure PostgreSQL
Create a PostgreSQL database:

```sql
CREATE DATABASE task_manager_db;

Update application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/task_manager_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_jwt_secret
jwt.expiration=86400000

###  Run Backend
cd Backend
mvn clean install
mvn spring-boot:run


Backend runs on:

http://localhost:8080

### Frontend Setup
cd Frontend
npm install
npm run dev


### Frontend runs on:

http://localhost:5173

### Authentication

You can register a new account directly from the application.

🔌 API Endpoints (Summary)
Auth

POST /api/auth/register

POST /api/auth/login

Projects

POST /api/projects

GET /api/projects

GET /api/projects/{id}

Tasks

POST /api/projects/{projectId}/tasks

GET /api/projects/{projectId}/tasks

PUT /api/tasks/{taskId}/complete

DELETE /api/tasks/{taskId}

All routes (except login/register) require a JWT token.

📊 Project Progress

For each project:

Total tasks

Completed tasks

Progress percentage

Displayed in the UI with a progress bar.

🧪 Testing

Backend APIs tested using Postman

Frontend tested manually through UI



Authentication

Project creation

Task management

Progress tracking

📝 Notes

Passwords are securely hashed

No sensitive data is exposed

node_modules and target are excluded from GitHub

Clean JSON responses using DTOs (no infinite recursion)


