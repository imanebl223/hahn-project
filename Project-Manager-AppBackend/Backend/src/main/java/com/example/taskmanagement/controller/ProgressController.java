package com.example.taskmanagement.controller;

import com.example.taskmanagement.service.ProjectProgressService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
@Validated
public class ProgressController {

    private final ProjectProgressService projectProgressService;

    @SuppressWarnings("null")
    @GetMapping("/{projectId}/progress")
    public ResponseEntity<Map<String, Object>> getProjectProgress(
            @PathVariable @NotNull Long projectId,
            @NonNull Authentication authentication) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }
        // authentication.getName() is guaranteed to be non-null by Spring Security
        String username = authentication.getName();
        Map<String, Object> progress = projectProgressService.calculateProjectProgress(
                projectId, username);
        return ResponseEntity.ok(progress);
    }
}
