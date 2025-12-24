import { useState, useEffect } from "react";
import { AuthPage } from "./components/auth-page";
import { DashboardLayout } from "./components/dashboard-layout";
import { ProjectsOverview } from "./components/projects-overview";
import { ProjectDetail } from "./components/project-detail";

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [selectedProjectId, setSelectedProjectId] = useState<number | null>(null);
  const [currentPage, setCurrentPage] = useState<"projects" | "project-detail">("projects");

  // Check for existing token on mount
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      setIsAuthenticated(true);
    }
  }, []);

  // Auth handlers
  const handleLogin = () => {
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    setIsAuthenticated(false);
    setSelectedProjectId(null);
    setCurrentPage("projects");
  };

  // Navigation handlers
  const handleNavigate = (page: string) => {
    if (page === "dashboard" || page === "projects") {
      setCurrentPage("projects");
      setSelectedProjectId(null);
    }
  };

  const handleSelectProject = (projectId: number) => {
    setSelectedProjectId(projectId);
    setCurrentPage("project-detail");
  };

  const handleBackToProjects = () => {
    setSelectedProjectId(null);
    setCurrentPage("projects");
  };

  // Render auth page if not authenticated
  if (!isAuthenticated) {
    return <AuthPage onLogin={handleLogin} />;
  }

  // Render dashboard with content
  return (
    <DashboardLayout
      currentPage={currentPage === "project-detail" ? "projects" : currentPage}
      onNavigate={handleNavigate}
      onLogout={handleLogout}
    >
      {currentPage === "project-detail" && selectedProjectId ? (
        <ProjectDetail
          projectId={selectedProjectId}
          onBack={handleBackToProjects}
        />
      ) : (
        <ProjectsOverview onSelectProject={handleSelectProject} />
      )}
    </DashboardLayout>
  );
}
