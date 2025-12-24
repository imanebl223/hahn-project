package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    @SuppressWarnings("null")
    @GetMapping
    public List<TaskDTO> getProjectTasks(
            @PathVariable @NotNull Long projectId, 
            @NonNull Authentication authentication) {
        return taskService.getProjectTasks(projectId, authentication.getName())
                .stream()
                .map(TaskDTO::from)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @PathVariable @NotNull Long projectId, 
            @RequestBody @NotNull Task task, 
            @NonNull Authentication authentication) {
        Task createdTask = taskService.createTask(projectId, task, authentication.getName());
        TaskDTO taskDTO = TaskDTO.from(createdTask);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskDTO.id())
                .toUri();
                
        return ResponseEntity.created(location).body(taskDTO);
    }

    @SuppressWarnings("null")
    @PutMapping("/{taskId}/complete")
    public ResponseEntity<Void> markTaskAsCompleted(
            @PathVariable @NotNull Long projectId, 
            @PathVariable @NotNull Long taskId,
            @NonNull Authentication authentication) {
        taskService.markTaskAsCompleted(taskId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("null")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable @NotNull Long projectId,
            @PathVariable @NotNull Long taskId,
            @NonNull Authentication authentication) {
        taskService.deleteTask(taskId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
