package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.Task;

import java.util.List;

public record ProjectProgressDTO(
    Long projectId,
    String projectName,
    int totalTasks,
    int completedTasks,
    double progress
) {
    public static ProjectProgressDTO from(Project project) {
        List<Task> tasks = project.getTasks();
        int total = tasks != null ? tasks.size() : 0;
        int completed = tasks != null ? (int) tasks.stream().filter(Task::isCompleted).count() : 0;
        double progress = total > 0 ? (completed * 100.0) / total : 0;
        
        return new ProjectProgressDTO(
            project.getId(),
            project.getTitle(),
            total,
            completed,
            Math.round(progress * 100.0) / 100.0
        );
    }
}
