package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
    Optional<Project> findByIdAndUser(Long id, User user);
    boolean existsByIdAndUser(Long id, User user);
}
