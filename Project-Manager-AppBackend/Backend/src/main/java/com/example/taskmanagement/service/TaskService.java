package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public List<Task> getProjectTasks(@NotNull Long projectId, @NonNull String userEmail) {
        if (projectId == null || userEmail == null) {
            throw new IllegalArgumentException("Project ID and user email must not be null");
        }
        if (!projectService.isProjectOwnedByUser(projectId, userEmail)) {
            throw new EntityNotFoundException("Project not found");
        }
        Project project = projectRepository.getReferenceById(projectId);
        return taskRepository.findByProject(project);
    }

    @Transactional
    public Task createTask(@NotNull Long projectId, @NonNull Task task, @NonNull String userEmail) {
        if (projectId == null || task == null || userEmail == null) {
            throw new IllegalArgumentException("Parameters must not be null");
        }
        Project project = projectService.getProject(projectId, userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        task.setProject(project);
        return taskRepository.save(task);
    }

    @SuppressWarnings("null")
    @Transactional
    public void markTaskAsCompleted(@NotNull Long taskId, @NonNull String userEmail) {
        Task task = getTaskByIdAndUserEmail(taskId, userEmail);
        task.setCompleted(true);
        taskRepository.save(task);
    }

    @SuppressWarnings("null")
    @Transactional
    public void deleteTask(@NotNull Long taskId, @NonNull String userEmail) {
        Task task = getTaskByIdAndUserEmail(taskId, userEmail);
        taskRepository.delete(task);
    }

    private Task getTaskByIdAndUserEmail(@NotNull Long taskId, @NonNull String userEmail) {
        if (taskId == null || userEmail == null) {
            throw new IllegalArgumentException("Task ID and user email must not be null");
        }
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        
        if (task == null) {
            throw new EntityNotFoundException("Task not found");
        }
        
        Project project = task.getProject();
        if (project == null || project.getId() == null || !projectService.isProjectOwnedByUser(project.getId(), userEmail)) {
            throw new EntityNotFoundException("Task not found");
        }
        
        return task;
    }
}
