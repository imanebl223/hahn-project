package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.lang.NonNull;

@Service
@RequiredArgsConstructor
public class ProjectProgressService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public Map<String, Object> calculateProjectProgress(@NonNull Long projectId, @NonNull String userEmail) {
        Objects.requireNonNull(projectId, "Project ID cannot be null");
        Objects.requireNonNull(userEmail, "User email cannot be null");

        // Verify project ownership
        if (!projectService.isProjectOwnedByUser(projectId, userEmail)) {
            throw new EntityNotFoundException("Project not found");
        }

        Project project = projectRepository.getReferenceById(projectId);
        List<Task> tasks = taskRepository.findByProject(project);
        
        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        int progressPercentage = totalTasks > 0 ? (int) ((completedTasks * 100) / totalTasks) : 0;

        Map<String, Object> progress = new HashMap<>();
        progress.put("projectId", projectId);
        progress.put("projectTitle", project.getTitle());
        progress.put("totalTasks", totalTasks);
        progress.put("completedTasks", completedTasks);
        progress.put("progressPercentage", progressPercentage);
        
        return progress;
    }
}
