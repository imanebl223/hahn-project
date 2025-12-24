package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.Task;

public record TaskDTO(
    Long id,
    String title,
    String description,
    boolean completed
) {
    public static TaskDTO from(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted()
        );
    }
}
