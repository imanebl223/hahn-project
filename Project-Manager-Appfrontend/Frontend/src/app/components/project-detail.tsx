import { useState, useEffect } from "react";
import { ArrowLeft, Calendar, Circle, CircleCheck, Trash2, Plus, Loader2 } from "lucide-react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Textarea } from "./ui/textarea";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Badge } from "./ui/badge";
import { Progress } from "./ui/progress";
import { projectsAPI, tasksAPI } from "../services/api";

export interface Task {
  id: number;
  title: string;
  description: string;
  dueDate: string;
  completed: boolean;
}

export interface ProjectData {
  id: number;
  title: string;
  description: string;
}

export interface ProgressData {
  projectId: number;
  totalTasks: number;
  completedTasks: number;
  progressPercentage: number;
}

interface ProjectDetailProps {
  projectId: number;
  onBack: () => void;
}

export function ProjectDetail({ projectId, onBack }: ProjectDetailProps) {
  const [project, setProject] = useState<ProjectData | null>(null);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [progress, setProgress] = useState<ProgressData | null>(null);
  const [isAddingTask, setIsAddingTask] = useState(false);
  const [taskTitle, setTaskTitle] = useState("");
  const [taskDescription, setTaskDescription] = useState("");
  const [taskDueDate, setTaskDueDate] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadProjectData();
  }, [projectId]);

  const loadProjectData = async () => {
    try {
      setLoading(true);
      const [projectData, tasksData, progressData] = await Promise.all([
        projectsAPI.getById(projectId),
        tasksAPI.getByProject(projectId),
        projectsAPI.getProgress(projectId),
      ]);
      setProject(projectData);
      setTasks(tasksData);
      setProgress(progressData);
      setError("");
    } catch (err: any) {
      console.error("Error loading project data:", err);
      setError("Failed to load project data. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleAddTask = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!taskTitle.trim()) return;

    try {
      await tasksAPI.create(projectId, taskTitle, taskDescription, taskDueDate);
      setTaskTitle("");
      setTaskDescription("");
      setTaskDueDate("");
      setIsAddingTask(false);
      await loadProjectData();
    } catch (err: any) {
      console.error("Error adding task:", err);
      alert("Failed to add task. Please try again.");
    }
  };

  const handleToggleTask = async (taskId: number) => {
    try {
      await tasksAPI.toggleComplete(projectId, taskId);
      await loadProjectData();
    } catch (err: any) {
      console.error("Error toggling task:", err);
      alert("Failed to update task. Please try again.");
    }
  };

  const handleDeleteTask = async (taskId: number) => {
    if (!confirm("Are you sure you want to delete this task?")) return;

    try {
      await tasksAPI.delete(projectId, taskId);
      await loadProjectData();
    } catch (err: any) {
      console.error("Error deleting task:", err);
      alert("Failed to delete task. Please try again.");
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-16">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    );
  }

  if (error || !project) {
    return (
      <div className="space-y-4">
        <Button variant="ghost" onClick={onBack} className="text-slate-600 hover:text-slate-900">
          <ArrowLeft className="w-4 h-4 mr-2" />
          Back to Projects
        </Button>
        <div className="p-4 rounded-lg bg-red-50 border border-red-200">
          <p className="text-sm text-red-600">{error || "Project not found"}</p>
        </div>
      </div>
    );
  }

  const progressPercentage = progress?.progressPercentage || 0;
  const completedCount = progress?.completedTasks || 0;
  const totalCount = progress?.totalTasks || 0;

  return (
    <div className="space-y-6">
      {/* Back Button */}
      <Button
        variant="ghost"
        onClick={onBack}
        className="text-slate-600 hover:text-slate-900"
      >
        <ArrowLeft className="w-4 h-4 mr-2" />
        Back to Projects
      </Button>

      {/* Project Header */}
      <Card className="border-slate-200">
        <CardHeader>
          <CardTitle className="text-3xl">{project.title}</CardTitle>
          <p className="text-slate-600 mt-2">{project.description}</p>
        </CardHeader>
        <CardContent>
          {/* Progress Section */}
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium text-slate-700">
                Task Progress
              </span>
              <span className="text-sm font-semibold text-blue-600">
                {Math.round(progressPercentage)}%
              </span>
            </div>
            <Progress value={progressPercentage} className="h-3" />
            <p className="text-sm text-slate-600">
              {completedCount} of {totalCount} tasks completed
            </p>
          </div>
        </CardContent>
      </Card>

      {/* Tasks Section */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-semibold text-slate-900">Tasks</h2>
          {!isAddingTask && (
            <Button
              onClick={() => setIsAddingTask(true)}
              className="bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 shadow-md"
            >
              <Plus className="w-4 h-4 mr-2" />
              Add Task
            </Button>
          )}
        </div>

        {/* Add Task Form */}
        {isAddingTask && (
          <Card className="border-blue-200 shadow-md">
            <CardHeader>
              <CardTitle>Add New Task</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleAddTask} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="task-title">Task Title</Label>
                  <Input
                    id="task-title"
                    value={taskTitle}
                    onChange={(e) => setTaskTitle(e.target.value)}
                    placeholder="Enter task title"
                    className="h-11"
                    autoFocus
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="task-description">Task Description</Label>
                  <Textarea
                    id="task-description"
                    value={taskDescription}
                    onChange={(e) => setTaskDescription(e.target.value)}
                    placeholder="Enter task description"
                    rows={2}
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="task-due-date">Due Date</Label>
                  <Input
                    id="task-due-date"
                    type="date"
                    value={taskDueDate}
                    onChange={(e) => setTaskDueDate(e.target.value)}
                    className="h-11"
                  />
                </div>

                <div className="flex gap-3 pt-2">
                  <Button
                    type="submit"
                    disabled={!taskTitle.trim()}
                    className="flex-1 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700"
                  >
                    Add Task
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => setIsAddingTask(false)}
                    className="flex-1"
                  >
                    Cancel
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        )}

        {/* Task List */}
        {tasks.length === 0 ? (
          <Card className="border-dashed border-2 border-slate-300">
            <CardContent className="flex flex-col items-center justify-center py-12">
              <div className="w-16 h-16 bg-slate-100 rounded-full flex items-center justify-center mb-4">
                <Circle className="w-8 h-8 text-slate-400" />
              </div>
              <h3 className="text-lg font-medium text-slate-900 mb-2">
                No tasks yet
              </h3>
              <p className="text-slate-600 text-center mb-6 max-w-sm">
                Start adding tasks to track your project progress
              </p>
            </CardContent>
          </Card>
        ) : (
          <div className="space-y-3">
            {tasks.map((task) => (
              <Card
                key={task.id}
                className={`transition-all hover:shadow-md ${
                  task.completed ? "bg-slate-50 border-slate-300" : "border-slate-200"
                }`}
              >
                <CardContent className="p-5">
                  <div className="flex items-start gap-4">
                    {/* Checkbox */}
                    <button
                      onClick={() => handleToggleTask(task.id)}
                      className="mt-1 flex-shrink-0 transition-colors"
                    >
                      {task.completed ? (
                        <CircleCheck className="w-6 h-6 text-green-600" />
                      ) : (
                        <Circle className="w-6 h-6 text-slate-400 hover:text-blue-600" />
                      )}
                    </button>

                    {/* Task Content */}
                    <div className="flex-1 min-w-0">
                      <h3
                        className={`font-medium mb-1 ${
                          task.completed
                            ? "text-slate-500 line-through"
                            : "text-slate-900"
                        }`}
                      >
                        {task.title}
                      </h3>
                      {task.description && (
                        <p
                          className={`text-sm mb-3 ${
                            task.completed ? "text-slate-400" : "text-slate-600"
                          }`}
                        >
                          {task.description}
                        </p>
                      )}
                      {task.dueDate && (
                        <Badge
                          variant="outline"
                          className={`${
                            task.completed
                              ? "border-slate-300 text-slate-500"
                              : "border-blue-200 text-blue-700 bg-blue-50"
                          }`}
                        >
                          <Calendar className="w-3 h-3 mr-1" />
                          {new Date(task.dueDate).toLocaleDateString("en-US", {
                            month: "short",
                            day: "numeric",
                            year: "numeric",
                          })}
                        </Badge>
                      )}
                    </div>

                    {/* Delete Button */}
                    <button
                      onClick={() => handleDeleteTask(task.id)}
                      className="flex-shrink-0 p-2 rounded-lg text-slate-400 hover:text-red-600 hover:bg-red-50 transition-all"
                    >
                      <Trash2 className="w-5 h-5" />
                    </button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
