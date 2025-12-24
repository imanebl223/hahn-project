package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.ProjectRepository;
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
        User user = userService.getUserByEmail(userEmail);
        return projectRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Optional<Project> getProject(Long projectId, String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        return projectRepository.findByIdAndUser(projectId, user);
    }

    @Transactional
    public Project createProject(Project project, String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        project.setUser(user);
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public boolean isProjectOwnedByUser(Long projectId, String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        return projectRepository.existsByIdAndUser(projectId, user);
    }
}
