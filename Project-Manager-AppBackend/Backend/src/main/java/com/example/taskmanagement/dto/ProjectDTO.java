package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.Project;

public record ProjectDTO(
    Long id,
    String title,
    String description,
    UserDTO user
) {
    public static ProjectDTO from(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectDTO(
            project.getId(),
            project.getTitle(),
            project.getDescription(),
            UserDTO.from(project.getUser())
        );
    }
}
