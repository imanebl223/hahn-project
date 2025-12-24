package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.ProjectDTO;
import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<ProjectDTO> getUserProjects(Authentication authentication) {
        return projectService.getUserProjects(authentication.getName())
                .stream()
                .map(ProjectDTO::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id, Authentication authentication) {
        return projectService.getProject(id, authentication.getName())
                .map(ProjectDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody Project project, Authentication authentication) {
        Project createdProject = projectService.createProject(project, authentication.getName());
        return ResponseEntity.ok(ProjectDTO.from(createdProject));
    }
}
