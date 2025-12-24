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
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public Optional<Task> getTask(@NotNull Long taskId, @NonNull String userEmail) {
        if (taskId == null || userEmail == null) {
            throw new IllegalArgumentException("Task ID and user email must not be null");
        }
        
        try {
            Task task = getTaskByIdAndUserEmail(taskId, userEmail);
            return Optional.of(task);
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Task> updateTask(@NotNull Long taskId, @NonNull Task taskDetails, @NonNull String userEmail) {
        if (taskId == null || taskDetails == null || userEmail == null) {
            throw new IllegalArgumentException("Parameters must not be null");
        }

        return taskRepository.findById(taskId)
                .map(existingTask -> {
                    // Verify the task belongs to the user's project
                    if (existingTask.getProject() == null || 
                        !projectService.isProjectOwnedByUser(existingTask.getProject().getId(), userEmail)) {
                        throw new EntityNotFoundException("Task not found");
                    }

                    // Update task properties
                    if (taskDetails.getTitle() != null) {
                        existingTask.setTitle(taskDetails.getTitle());
                    }
                    if (taskDetails.getDescription() != null) {
                        existingTask.setDescription(taskDetails.getDescription());
                    }
                    if (taskDetails.getDueDate() != null) {
                        existingTask.setDueDate(taskDetails.getDueDate());
                    }
                    if (taskDetails.getPriority() != null) {
                        existingTask.setPriority(taskDetails.getPriority());
                    }
                    if (taskDetails.getStatus() != null) {
                        existingTask.setStatus(taskDetails.getStatus());
                    }
                    existingTask.setCompleted(taskDetails.isCompleted());
                    
                    return taskRepository.save(existingTask);
                });
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
