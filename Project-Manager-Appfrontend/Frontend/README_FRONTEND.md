# Project Task Manager - Frontend

A modern, professional React frontend for the Project Task Management application, designed to connect with a Spring Boot backend API.

## Features

- **Authentication**: Login and registration with JWT token-based authentication
- **Project Management**: Create and view projects
- **Task Management**: Create, complete, and delete tasks within projects
- **Progress Tracking**: Visual progress bars showing task completion percentage
- **Responsive Design**: Modern SaaS-style UI with Tailwind CSS

## Prerequisites

- Node.js (v18 or higher)
- Spring Boot backend running on `http://localhost:8080`

## Installation

```bash
# Install dependencies
npm install
```

## Running the Application

```bash
# Start the development server
npm run dev
```

The application will be available at `http://localhost:5173`

## Backend API Requirements

The frontend expects the following endpoints from your Spring Boot backend:

### Authentication Endpoints

- `POST /auth/register` - Register a new user
  - Body: `{ "email": string, "password": string }`
  
- `POST /auth/login` - Login user
  - Body: `{ "email": string, "password": string }`
  - Response: `{ "accessToken": string, "tokenType": "Bearer" }`

### Project Endpoints

- `GET /api/projects` - Get all user projects
  - Response: Array of `{ "id": number, "title": string, "description": string }`

- `GET /api/projects/{projectId}` - Get project by ID
  - Response: `{ "id": number, "title": string, "description": string }`

- `POST /api/projects` - Create new project
  - Body: `{ "title": string, "description": string }`

- `GET /api/projects/{projectId}/progress` - Get project progress
  - Response: `{ "projectId": number, "totalTasks": number, "completedTasks": number, "progressPercentage": number }`

### Task Endpoints

- `GET /api/projects/{projectId}/tasks` - Get all tasks for a project
  - Response: Array of `{ "id": number, "title": string, "description": string, "dueDate": string, "completed": boolean }`

- `POST /api/projects/{projectId}/tasks` - Create new task
  - Body: `{ "title": string, "description": string, "dueDate": string }`

- `PUT /api/projects/{projectId}/tasks/{taskId}/complete` - Toggle task completion

- `DELETE /api/projects/{projectId}/tasks/{taskId}` - Delete task

## Authentication Flow

1. User registers or logs in via the authentication page
2. On successful login, the JWT token is stored in localStorage
3. All subsequent API requests include the token in the Authorization header: `Bearer {token}`
4. If a request returns 401 or 403, the user is automatically logged out and redirected to the login page

## Project Structure

```
src/
├── app/
│   ├── components/
│   │   ├── auth-page.tsx              # Login/Register page
│   │   ├── dashboard-layout.tsx       # Main dashboard layout
│   │   ├── projects-overview.tsx      # Projects list view
│   │   ├── project-detail.tsx         # Project details with tasks
│   │   ├── create-project-form.tsx    # Project creation form
│   │   └── ui/                        # Reusable UI components
│   ├── services/
│   │   └── api.ts                     # Axios API client and interceptors
│   └── App.tsx                        # Main application component
└── styles/
    ├── index.css                      # Global styles
    ├── tailwind.css                   # Tailwind imports
    ├── theme.css                      # Theme variables
    └── fonts.css                      # Font imports
```

## Key Technologies

- **React 18** - UI library
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling
- **Axios** - HTTP client
- **Lucide React** - Icons
- **Radix UI** - Accessible UI components
- **Vite** - Build tool

## Configuration

The backend API URL is configured in `/src/app/services/api.ts`:

```typescript
const API_BASE_URL = "http://localhost:8080";
```

Update this if your backend runs on a different port or domain.

## Error Handling

- All API errors are caught and displayed to the user
- Network errors show user-friendly error messages
- 401/403 responses automatically log out the user
- Form validation prevents invalid submissions

## Browser Support

- Modern browsers (Chrome, Firefox, Safari, Edge)
- Requires ES6+ support
