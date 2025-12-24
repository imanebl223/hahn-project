import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

// Create axios instance with default config
export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      // Clear token and redirect to login
      localStorage.removeItem("accessToken");
      window.location.href = "/";
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: async (email: string, password: string) => {
    const response = await api.post("/auth/login", { email, password });
    return response.data;
  },

  register: async (email: string, password: string) => {
    const response = await api.post("/auth/register", { email, password });
    return response.data;
  },
};

// Projects API
export const projectsAPI = {
  getAll: async () => {
    const response = await api.get("/api/projects");
    return response.data;
  },

  getById: async (projectId: number) => {
    const response = await api.get(`/api/projects/${projectId}`);
    return response.data;
  },

  create: async (title: string, description: string) => {
    const response = await api.post("/api/projects", { title, description });
    return response.data;
  },

  getProgress: async (projectId: number) => {
    const response = await api.get(`/api/projects/${projectId}/progress`);
    return response.data;
  },
};

// Tasks API
export const tasksAPI = {
  getByProject: async (projectId: number) => {
    const response = await api.get(`/api/projects/${projectId}/tasks`);
    return response.data;
  },

  create: async (
    projectId: number,
    title: string,
    description: string,
    dueDate: string
  ) => {
    const response = await api.post(`/api/projects/${projectId}/tasks`, {
      title,
      description,
      dueDate,
    });
    return response.data;
  },

  toggleComplete: async (projectId: number, taskId: number) => {
    const response = await api.put(
      `/api/projects/${projectId}/tasks/${taskId}/complete`
    );
    return response.data;
  },

  delete: async (projectId: number, taskId: number) => {
    await api.delete(`/api/projects/${projectId}/tasks/${taskId}`);
  },
};
