package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<Project> getUserProjects(String userEmail) {
        return userService.findByEmail(userEmail)
                .map(user -> projectRepository.findByUser(user))
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
    }

    @Transactional(readOnly = true)
    public Optional<Project> getProject(Long projectId, String userEmail) {
        return userService.findByEmail(userEmail)
                .flatMap(user -> projectRepository.findByIdAndUser(projectId, user));
    }

    @Transactional
    public Project createProject(Project project, String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        project.setUser(user);
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public boolean isProjectOwnedByUser(Long projectId, String userEmail) {
        return userService.findByEmail(userEmail)
                .map(user -> projectRepository.existsByIdAndUser(projectId, user))
                .orElse(false);
    }
}
