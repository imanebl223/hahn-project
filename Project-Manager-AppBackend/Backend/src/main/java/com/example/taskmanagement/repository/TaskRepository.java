package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    boolean existsByIdAndProject(Long id, Project project);
}
