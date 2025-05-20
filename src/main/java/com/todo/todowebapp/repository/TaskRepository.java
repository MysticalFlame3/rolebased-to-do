package com.todo.todowebapp.repository;

import com.todo.todowebapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(String username);
}
